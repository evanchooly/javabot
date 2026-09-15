# Migrate the test suite to `@QuarkusTest` / JUnit5

## Context

The Dropwizard → Quarkus migration (see `IMPROVEMENTS.md`) converted the
production web layer (REST resources, Qute templates, DI wiring) but left
the entire test suite on TestNG + a plain Guice `@Guice(modules =
[JavabotTestModule::class])` harness (`BaseTest`, 43 subclasses). Nothing
in the suite runs under Quarkus's own test infrastructure — the web-layer
view tests render Qute templates directly via `TemplateService` method
calls, never through Arc's CDI container or RESTEasy Reactive routing. This
is the biggest completeness gap identified in the migration review.

## Goals

- All test classes run under `@QuarkusTest` (JUnit5), so the web layer's
  actual CDI wiring, `quarkus.arc.exclude-types` split, and REST resources
  get exercised by tests instead of bypassed.
- One test runner for the whole suite (JUnit5), replacing TestNG entirely.
- Domain-layer tests (operations, DAOs, IRC bot) keep their current Guice
  object graph (via `JavabotTestModule`) — this migration changes *how*
  that graph is reached from a test class, not what it contains.
- No loss of MongoDB/Testcontainers-backed integration coverage.

## Non-goals

- Not converting domain-layer classes (DAOs, PircBotX, operations) into
  CDI beans. `quarkus.arc.exclude-types` stays as-is; the domain layer
  stays Guice-managed by design (see `GuiceInjectorProducer`'s doc
  comment).
- Not adding new test coverage (e.g. RestAssured HTTP-level tests for
  `PublicOAuthResource`/OAuth flow) as part of this change. That's a
  separate, follow-on effort once the framework migration lands.
- Not changing CI group/tag filtering behavior beyond the mechanical
  `groups` → `@Tag` mapping (no visible surefire `groups`/`excludedGroups`
  config in this repo's `pom.xml` — CI's actual behavior here is inherited
  from `evanchooly/workflows` and out of scope to audit).

## Architecture

### DI wiring: `BaseTest` moves off direct `@Inject` for Guice types

Arc validates every `@Inject` injection point at build time, including on
test classes. A field like `@Inject lateinit var adminDao: AdminDao` would
fail the build under `@QuarkusTest`, because `AdminDao` (and the rest of
`javabot.*`/`javabot.dao.**`/etc.) is deliberately excluded from CDI bean
discovery.

`AdminResource` and `PublicOAuthResource` already establish the pattern for
this: inject the shared Guice `Injector` (a real CDI bean, produced by
`GuiceInjectorProducer`) and pull domain types off it lazily.

`BaseTest` adopts the same pattern:

```kotlin
@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
open class BaseTest {
    @Inject lateinit var injector: Injector

    protected val datastore: Datastore by lazy { injector.getInstance(Datastore::class.java) }
    protected val apiDao: ApiDao by lazy { injector.getInstance(ApiDao::class.java) }
    // ... same pattern for eventDao, channelDao, logsDao, adminDao, changeDao, messages, config
    protected val bot: Provider<TestJavabot> by lazy { injector.getProvider(TestJavabot::class.java) }
    protected val ircBot: Provider<PircBotX> by lazy { injector.getProvider(PircBotX::class.java) }
}
```

Every field that was `@Inject lateinit var` for a Guice-domain type becomes
a lazy delegate off `injector`. Subclasses that reference these fields
(`adminDao.save(...)`, `bot.get().start()`, etc.) need no changes — the
external shape (`val name: Type`) is preserved.

Fields for genuinely CDI-managed types (`TemplateService` and other
`javabot.web.**` beans, used only by the web-layer test subset) stay as
plain `@Inject lateinit var` — Arc resolves those normally.

### Guice module selection: prod module vs. test module

`GuiceInjectorProducer.INJECTOR` currently hardcodes `Guice.createInjector(JavabotModule())`.
Tests need `JavabotTestModule()` (Testcontainers `MongoDBContainer`, mock
`IrcAdapter`, `TestNickServDao`) instead. Since `src/main` can't reference
a `src/test` class directly, the module class becomes configurable:

`application.properties`:
```properties
javabot.guice.module=javabot.JavabotModule
%test.javabot.guice.module=javabot.JavabotTestModule
```

`GuiceInjectorProducer` reads the class name via `@ConfigProperty` and
instantiates it reflectively (`Class.forName(name).getDeclaredConstructor().newInstance()`),
replacing the current `companion object` hardcoded lazy val. The producer
stays `@ApplicationScoped`, so the injector (and its Testcontainers Mongo
instance) is built once per test JVM, matching today's effective behavior
under TestNG's Guice-module caching.

### Suite-level teardown

TestNG's `@AfterSuite` (`bot.shutdown()`, once for the whole run) has no
direct per-class JUnit5 equivalent — `@AfterAll` runs once per test class,
not once per suite. Register a JUnit5 `TestExecutionListener`
(`META-INF/services/org.junit.platform.launcher.TestExecutionListener`)
that calls shutdown once when the whole test plan finishes, preserving
today's semantics rather than shutting the bot down and restarting it
between every test class.

### Web-layer tests: real gain, not just a framework swap

The `src/test/kotlin/javabot/web/**` tests (views, `BotResourceTest`) are
the ones that actually benefit: under `@QuarkusTest` they run inside Arc,
so `TemplateService`, the `quarkus.arc.exclude-types` boundary, and (for
`BotResourceTest`, currently disabled/stubbed with `TODO()`) REST resource
routing become real, verifiable behavior instead of direct method calls
against a hand-built object graph. Re-enabling `BotResourceTest`'s stubbed
methods is in scope as part of converting that file, since leaving
`@Test(enabled = false)` + `TODO()` bodies as `@Disabled` + `TODO()` under
JUnit5 carries the gap forward unchanged.

## TestNG → JUnit5 mapping

| TestNG | JUnit5 | Notes |
|---|---|---|
| `@Test` | `org.junit.jupiter.api.Test` | |
| `@BeforeTest` / `@BeforeMethod` | `@BeforeEach` | |
| `@AfterMethod` | `@AfterEach` | |
| `@BeforeClass` / `@AfterClass` | `@BeforeAll` / `@AfterAll` | Instance methods via `@TestInstance(PER_CLASS)` on `BaseTest`, avoids Kotlin companion-object boilerplate per subclass |
| `@AfterSuite` | `TestExecutionListener` (see above) | Not expressible as a per-class annotation |
| `@DataProvider` / `@Test(dataProvider = "x")` | `@ParameterizedTest` + `@MethodSource("x")` | Method source returns `Stream<Arguments>`; one rewrite per provider, not a mechanical swap |
| `@Test(groups = [...])` | `@Tag(...)` | |
| `@Test(enabled = false)` | `@Disabled` | |
| `@Test(dependsOnMethods = [...])` | `@TestMethodOrder(OrderAnnotation::class)` + `@Order` | **Semantic loss**: JUnit5 ordering does not skip a dependent test when its prerequisite fails, TestNG's `dependsOnMethods` does. Each occurrence gets called out individually during conversion rather than silently reordered. |
| `org.testng.Assert.assertEquals(actual, expected)` | `org.junit.jupiter.api.Assertions.assertEquals(expected, actual)` | **Argument order reversed.** Mechanical import swap is not correct — each of the ~105 call sites needs the arguments swapped too, or failure messages will report expected/actual backwards. |
| `Assert.assertTrue/assertFalse/assertNotNull/assertNull/assertNotSame/assertNotEquals/fail` | JUnit5 equivalents | Same semantics, argument order unchanged for these (only `assertEquals`/`assertNotEquals` swap) |

`kotlin-test-testng` and `org.testng:testng` are removed from `pom.xml`;
`io.quarkus:quarkus-junit5` is added (test scope).

## Rollout

Convert bottom-up, in batches that each leave the suite green:

1. **Foundation** — add `quarkus-junit5`, remove TestNG deps, rework
   `BaseTest` (DI wiring above) and `GuiceInjectorProducer`
   (config-driven module selection), add the `TestExecutionListener`.
   Nothing compiles again until at least one subclass is converted, so
   this batch pairs with a small smoke-test subclass conversion (e.g.
   `JavabotConfigTest`) to prove the wiring end-to-end before the rest.
2. **Leaf tests** — the majority of the 43 `BaseTest` subclasses with no
   `@DataProvider` or `dependsOnMethods`: mechanical annotation/assertion
   conversion per the mapping table.
3. **Data-provider tests** — files using `@DataProvider` (e.g.
   `KarmaOperationTest`, `RFCOperationTest`, `URLTitleOperationTest`, and
   others found via the `@DataProvider` grep): rewritten to
   `@MethodSource`, one at a time, verified individually.
4. **`dependsOnMethods` tests** — the handful with method dependencies:
   each converted with an explicit decision on how to preserve or
   consciously drop the skip-on-failure behavior.
5. **Web-layer tests last** — `src/test/kotlin/javabot/web/**`, since
   these are where the framework change adds real coverage rather than
   just changing test runners; includes re-enabling `BotResourceTest`.

Each batch is verified with `mvn test` (MongoDB via Testcontainers/Docker,
same requirement as today) before starting the next.

## Testing / verification

- `mvn test` must pass after every batch, not just at the end.
- After batch 1, confirm the smoke-test subclass actually boots Arc,
  resolves `Injector` as a CDI bean, and reaches a Guice-managed DAO
  through it — this is the riskiest wiring point and worth an explicit
  check before converting the other 42 files on faith.
- After batch 5, confirm at least one web-layer test observably exercises
  something it couldn't before (e.g. a real CDI-resolved `TemplateService`
  bean, or `BotResourceTest` making an actual HTTP call) — the point of
  moving those files is new coverage, not just a syntax change.

## Risks / open questions

- **CI group filtering**: no `groups`/`excludedGroups` surefire config is
  visible in this repo's `pom.xml`; if `evanchooly/workflows`' shared build
  filters by TestNG group somewhere outside this repo, the `@Tag`
  mechanical mapping may not preserve that filtering. Not verifiable from
  this repo alone — flagged for the person merging this to check CI
  behavior post-migration, not blocking the code change itself.
- **`dependsOnMethods` semantics**: see mapping table above — each
  occurrence needs a judgment call, not a mechanical rule.
- **Parallelism**: TestNG's default execution model and JUnit5's may
  differ (JUnit5 is sequential by default unless parallel execution is
  explicitly enabled); if the current suite relies on TestNG's default
  parallel behavior for runtime, this migration may change total suite
  wall-clock time. Not treated as a blocking concern, but worth noting
  post-migration if CI times shift noticeably.
