# Javabot — Improvement & Cleanup Checklist

Findings from a repo-wide review (2026-08-20). Kotlin/Dropwizard/Morphia IRC bot,
Maven build, ~178 Kotlin source files / 66 test files.

## Repository hygiene
- [x] Untracked working-tree clutter should be committed, gitignored, or removed:
      `mvn.out` (build log), `pom.xml.exclusions` (looks like a stale backup pom —
      older parent v34 vs current v69, javax vs jakarta swapped), `quarkus/` and
      `.quarkus/` (appear to be unrelated Quarkus scaffolding, not part of this
      project), `bin/dldump.sh`, `bin/importDumps.sh`.
- [ ] `lib/pircbotx-2.3-SNAPSHOT.jar` is a binary jar committed to git for a
      `system`-scoped Maven dependency on a `2.3-SNAPSHOT` build — no source,
      no provenance, can't be audited or rebuilt, and Maven `system` scope is
      deprecated/discouraged. Publish this to a repo (even a local/GitHub
      Packages one) or vendor the actual source.
- [ ] `downloads/` holds large javadoc/library jars (guava, jakarta EE, morphia —
      ~27MB total) fetched manually per the README; confirm these are meant to
      stay out of git (they're gitignored) but consider scripting the download
      instead of manual copy-paste steps.
- [x] README still references Travis CI badge/build (`travis-ci.org`), but CI
      has moved to GitHub Actions (`.github/workflows/*`) — badge is stale/dead.
- [ ] README build/setup instructions are manual and multi-step (install mongo,
      manually download JDK javadoc zip, copy sample properties) — a setup
      script or `docker-compose` based dev flow would reduce onboarding friction.

## Dependencies — outdated / abandoned libraries
- [ ] `org.twitter4j:twitter4j-core:4.1.2` — Twitter API v1.1 support is
      largely dead since X/Twitter's API changes; verify `TwitterService.kt`
      still works, or drop the feature.
- [ ] `com.rosaloves:bitlyj:2.0.0` — last released ~2013, unmaintained;
      `net.thauvin.erik:bitly-shorten` is already a dependency and looks like
      the intended replacement — check if `bitlyj` is now dead code.
- [ ] `net.htmlparser.jericho:jericho-html:3.4` — unmaintained since ~2015;
      `jsoup` is already a dependency and covers HTML parsing — evaluate
      consolidating onto jsoup only.
- [ ] `org.brickred:socialauth` / `socialauth-filter` — unmaintained since
      ~2015-2016, predates OAuth2 best practices in most providers; this is
      also security-sensitive code (`PublicOAuthResource.kt`, `User.kt`) so
      an unmaintained OAuth library is a real risk, not just tech debt.
- [ ] `ca.grimoire.maven:maven-utils:2.0` — tiny/obscure artifact, unclear
      what it's used for; confirm it's still needed.
- [ ] `commons-collections:commons-collections:3.2.2` — the legacy (non-`commons-collections4`)
      artifact; older releases of this line had a well-known deserialization
      RCE (CVE-2015-6420-family gadget chain) — confirm nothing here
      deserializes untrusted data via it, and prefer migrating to
      `commons-collections4` if only utility classes are needed.
- [ ] `org.reflections:reflections:0.10.2` — check for a newer release; classpath
      scanning libraries are also worth watching for perf/startup cost.
- [ ] `org.pircbotx:pircbotx:2.3-SNAPSHOT` — depending on an unreleased
      SNAPSHOT of the core IRC library long-term is risky (no reproducible
      builds, can disappear from any repo). Push for/track a real 2.3 release
      or pin to a known-good commit with source in-repo.

## Build / project structure
- [x] `pom.xml` disables the `default-compile`/`default-testCompile` Maven
      lifecycle bindings in favor of custom `java-compile`/`java-test-compile`
      IDs bound to the same phases — but there is no `src/main/java`, this is
      a pure-Kotlin project (`src/main/kotlin`, `src/test/kotlin`). Worth
      double-checking whether this compiler-plugin config is legacy cruft
      left over from a Java→Kotlin migration. Removing it dropped the
      project to the parent's default `kotlin.compiler.jvmTarget` (11) since
      there's no `src/main/java` for a Java-side `<release>` to matter —
      fixed by setting `kotlin.compiler.jvmTarget` to 17 explicitly.
- [x] `pom.xml.exclusions` (untracked) suggests an in-progress or abandoned
      experiment reverting `javax.xml.bind`/`javax.activation` back from
      `jakarta.*`, and downgrading kotlin/morphia/jackson versions — if this
      was a deliberate rollback branch, it should be a git branch, not a
      loose file in the working tree.
- [ ] No `Dockerfile`/container build for the bot itself, only
      `docker-compose-test.yml` for MongoDB in tests — if deployment is
      manual (`mvn exec:java` on a server), containerizing the bot would
      simplify ops.

## Framework migration — Dropwizard → Quarkus
- [~] Convert the web layer (`src/main/kotlin/javabot/web/**`, currently Dropwizard +
      FreeMarker views + `dropwizard-auth`/socialauth OAuth) to Quarkus. This
      is a large, well-scoped effort with prior art already in the repo to
      draw from rather than starting cold:
  - **In progress** — took the path this checklist recommended below:
    rebased `origin/copilot/convert-dropwizard-to-quarkus`'s web-layer
    conversion onto current master (merged clean, no conflicts) and fixed
    what didn't actually work end to end:
    - The FreeMarker `main.ftl` → `paged.ftl` → `<child>.ftl` nested
      `<#include>` chain wasn't reproduced in Qute at all — pages rendered
      an empty shell. Now uses Qute's `{#include _id=...}` dynamic include
      against `contentTemplate`/`pagedView` data keys instead.
    - `TemplateService`'s `@Location`-injected `Template` fields only work
      inside a full Quarkus/Arc container; the existing view test suite
      builds its object graph with plain Guice (`@Guice(modules =
      [JavabotTestModule])`), which has no notion of that build-time
      wiring. `TemplateService` now owns a standalone Qute `Engine`
      instead, so it renders identically either way.
    - Guice 5.1.0 only recognizes `javax.inject.Inject`; Quarkus/Arc
      requires `jakarta.inject.Inject`. Migrated the whole codebase to
      `jakarta.inject` and bumped Guice to 7.0.0 so one object graph
      satisfies both. Added a CDI producer (`GuiceInjectorProducer`)
      exposing the shared Guice `Injector` so web-layer CDI beans can pull
      DAOs from the still-Guice-managed domain layer instead of asking Arc
      to inject them directly (Arc has no bean definitions for them).
    - The committed session-cookie encryption key from the source branch's
      "fix ... encryption key" commit was still a real hardcoded default,
      just reworded — now env-var only (`SESSION_ENCRYPTION_KEY`), fails
      closed if unset outside `%dev`/`%test`.
    - Verified all 8 rendered pages (index, factoids/karma/changes paging,
      logs, all 4 admin pages) end to end with a standalone Qute-engine
      harness against the real templates, independent of Mongo/Docker.
    - Still open: the unmaintained `socialauth` OAuth library is untouched
      (separate, larger effort — see Security section); the web view tests
      still run under the Guice/TestNG harness rather than `@QuarkusTest`,
      so they need MongoDB via Docker to actually execute (unavailable in
      this environment, so only compiled, not run); `jacoco`/CI wiring not
      revisited.
  - `quarkus` branch (local + `origin/quarkus`, identical) — an older,
    substantial attempt: 286 commits, diverged from master back at
    `09fa0dd2` (Dec 2024), touches ~250 files (+5.9k/-4.1k lines) across
    tests, views, and models. It's now ~642 commits behind master, so it
    can't be merged as-is, but it's the deepest prior exploration and worth
    mining for approach/gotchas (e.g. how it handled Morphia/Guice wiring,
    test infra, and the admin views) before deciding whether to rebase it or
    start fresh.
  - `origin/copilot/convert-dropwizard-to-quarkus` — a small, recent (2026-02-03),
    Copilot-authored attempt, only 12 commits ahead of master. Converts
    `JavabotApplication`, health check, exception mapper, REST resources, and
    the FreeMarker views to Quarkus Qute templates (`TemplateService.kt`
    replacing `ViewFactory`/`PagedView`/`MainView`). Much closer to current
    master and easier to review/rebase, but shallower — worth a close read of
    its "Address code review feedback - fix template whitespace and
    encryption key" commit for issues it ran into (that commit message alone
    suggests a hardcoded/weak encryption key was flagged mid-review — verify
    it was actually fixed correctly, not just silenced).
  - `origin/copilot/convert-dropwizard-to-ktor` and
    `-ktor-again` — parallel explorations of Ktor instead of Quarkus. Not
    directly reusable for a Quarkus conversion, but useful as a second data
    point on what pain points come up when replacing Dropwizard here
    (routing, templating, auth) regardless of target framework.
  - The untracked `quarkus/code-with-quarkus` and `quarkus/gunnar-quarkus-qute`
    directories in the working tree look like reference/starter Quarkus
    projects (not part of this repo's history) kept alongside the `quarkus`
    branch work, presumably for inspiration on project layout/Qute usage —
    worth keeping as reference material while this conversion is active
    (revise the "repo hygiene" note above accordingly), but they should not
    ship as part of `javabot` itself.
  - Decide up front: rebase/finish the old `quarkus` branch, adopt the
    Copilot branch as the starting point, or do a clean conversion informed
    by both — given the 642-commit staleness of the former and the narrower
    scope of the latter, restarting from the Copilot branch's smaller diff
    (rebased onto current master) and pulling specific lessons from the old
    `quarkus` branch where it went deeper is likely the pragmatic path.
  - This migration would also resolve/replace several other checklist items
    above: it's a natural point to drop the unmaintained `socialauth` OAuth
    library, decide the `dropwizard.version` dependency's fate, and revisit
    `jacoco`/test wiring if Quarkus's test story differs.

## IRC event handling
- [ ] `IrcAdapter.kt` event handling is inconsistent and swallows failures:
      `onMessage`/`onPrivateMessage` (lines 51-90) offload to `bot.executors`
      (a bounded `ThreadPoolExecutor`, queue capacity 50, default
      `AbortPolicy`), but every other handler — `onJoin`, `onPart`, `onQuit`,
      `onKick`, `onAction`, `onNickChange`, `onNotice` — runs synchronously on
      PircBotX's listener-manager thread doing DB writes inline, so a slow
      write there stalls IRC dispatch for the whole bot. `executors.execute`
      has no try/catch, so a full queue throws `RejectedExecutionException`
      straight back into PircBotX's dispatch thread, unlogged; exceptions
      inside `processMessage` itself (as opposed to individual operation
      dispatch in `Javabot.getResponses`, which is caught and logged) aren't
      caught either and just die silently on the executor thread.
      `isChannel` (lines 216-225) also catches `Throwable` and does
      `e.printStackTrace()` instead of using `LOG` like the rest of the
      class. Route all handlers through the executor uniformly (or make
      synchronous handling deliberate and documented), wrap handler bodies
      in try/catch with `LOG.error`, and replace the stray
      `printStackTrace()`. The `onNotice` NickServ parser (lines 143-158)
      is also hand-rolled state accumulated into a shared `ArrayList`
      keyed off literal string matches (`"*** End of Info ***"`,
      `"Information on "`) — worth extracting into a small, separately
      tested parser.
- [ ] The abstraction meant to isolate javabot from PircBotX was never
      finished: `IrcAdapter` already uses `org.pircbotx.*` types directly
      throughout (it's essentially the only place besides `OfflineAdapter.kt`
      and `JavabotModule.kt` that imports them), so the isolation stalled at
      an inheritance seam rather than a real interface — `OfflineAdapter`
      subclasses `IrcAdapter` purely to override `action`/`joinChannel`/
      `leave`/`isOnCommonChannel`/etc. for offline/test mode, which buys
      nothing today since `IrcAdapter` still hard-depends on
      `Provider<PircBotX>` regardless. Finish removing it instead of
      completing it: fold `OfflineAdapter`'s behavior into `IrcAdapter` behind
      a config flag, drop `open` from the class and its members, simplify
      `JavabotModule.kt`'s `Provider<out IrcAdapter>`/`getBotListener()`
      indirection to bind `IrcAdapter` concretely, and update the IRC mocks
      under `src/test/kotlin/javabot/mocks/` to stub PircBotX types directly
      rather than the subclassing seam. Keep the `toJavabot()`/`toIrcUser()`/
      `toIrcChannel()` conversion helpers at the bottom of `IrcAdapter.kt` —
      that model-conversion layer works and is unrelated to the seam being
      removed.

## Code
- [ ] Unhandled `TODO()` calls that will throw `NotImplementedError` at
      runtime if hit: `JavadocClassVisitor.kt:132` (`JAVA6 -> TODO()`) and
      `:240` (`else -> TODO("... not mapped")`) — confirm these paths are
      genuinely unreachable for supported javadoc versions, or handle them
      gracefully instead of crashing.
- [ ] Open TODOs worth triaging into issues or resolving:
      `KarmaOperation.kt:68` (unhandled "tell" case), `AdminResource.kt:92`
      (missing redirect when channel is null), `TimezonesUS.kt:3` (US states
      with multiple timezones not handled — likely a real correctness bug for
      timezone lookups).
- [ ] `findbugs-exclude.xml` is essentially empty/commented-out sample text
      ("some samples left in to jog my memory") — confirm static analysis
      (SpotBugs/FindBugs) is actually wired into the build; if not, either
      remove the stale config or actually enable it in CI.

## CI / testing
- [ ] `upgrades.yml` workflow runs OpenRewrite against a `3.0.0-SNAPSHOT` of
      `dev.morphia.morphia:rewrite` weekly — depending on an external
      SNAPSHOT artifact for a scheduled CI job is fragile; confirm this is
      still relevant/passing or prune it if the Morphia 3.0 migration is done
      or abandoned.
- [ ] No visible dedicated "tests pass" gate independent of the shared
      `evanchooly/workflows` reusable workflows — can't verify from this repo
      alone what's actually asserted; worth confirming coverage of
      `src/test/kotlin` (66 files) is enforced (e.g., min coverage via
      `jacoco.version` property, which is declared but not obviously wired
      into a plugin in `pom.xml`).
- [ ] `jacoco.version` is declared as a property but no `jacoco-maven-plugin`
      execution is visible in `pom.xml` — dead property, or missing coverage
      reporting wiring.

## Security / secrets
- [ ] `javabot-sample.properties` includes empty placeholders for several
      secrets (`javabot.password`, `twitter.consumerSecret`,
      `javabot.chatgpt.token`, `javabot.bitly.token`, etc.) — good pattern
      already (`javabot.properties` itself is gitignored); just confirm no
      real credentials have ever been committed in history
      (`git log -p -- javabot.properties` / a secret-scan pass would confirm).
- [ ] OAuth flow (`PublicOAuthResource.kt`) relies on the unmaintained
      `socialauth` library noted above — review it specifically for known
      OAuth CSRF/state-handling issues given its age.
