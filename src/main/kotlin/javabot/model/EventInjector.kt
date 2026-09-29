package javabot.model

import jakarta.enterprise.context.ApplicationScoped
import jakarta.enterprise.inject.UnsatisfiedResolutionException
import jakarta.enterprise.inject.spi.Bean
import jakarta.enterprise.inject.spi.BeanManager
import jakarta.inject.Inject

/**
 * Populates `@Inject`-annotated fields on an object CDI never constructed -- specifically
 * [AdminEvent]/[ApiEvent]/[ChannelEvent], which Morphia's reflective deserializer builds directly
 * from Mongo documents. Replaces Guice's one-line `Injector.injectMembers(event)`.
 *
 * CDI has no single-call equivalent, and `BeanManager.createInjectionTarget(...)` -- the standard
 * portable-extension primitive -- is not usable here: Quarkus's build-time Arc container throws
 * `UnsupportedOperationException` from `BeanManager.createAnnotatedType(Class)` at runtime, since
 * Arc does not support ad-hoc, reflection-based `AnnotatedType` creation outside of build-time bean
 * discovery (verified empirically against Arc 3.38.3; see task-3-report.md). Instead, this walks
 * the instance's class hierarchy for `@Inject`-annotated fields (matching Kotlin's default
 * `@Inject` placement on a `lateinit var`'s backing field) and resolves + sets each one directly
 * via the subset of `BeanManager` that Arc *does* support at runtime:
 * `getBeans`/`resolve`/`getReference`.
 *
 * The full set of types reachable through this reflection-based path -- `JavadocAsmParser` (via
 * `ApiEvent.asmParser`), `ApiDao`/`AdminDao` (via `ApiEvent`'s fields), and `ChannelDao` (via
 * `ChannelEvent.channelDao`) -- are invisible to Arc's build-time injection-point analysis, so each
 * is separately annotated `@Unremovable` to protect it from Arc's unused-bean removal even if their
 * other (currently incidental) injection points ever go away.
 */
@ApplicationScoped
class EventInjector @Inject constructor(private val beanManager: BeanManager) {

    fun <T : Any> inject(instance: T) {
        var type: Class<*>? = instance.javaClass
        while (type != null && type != Any::class.java) {
            for (field in type.declaredFields) {
                if (field.isAnnotationPresent(Inject::class.java)) {
                    field.isAccessible = true
                    @Suppress("UNCHECKED_CAST")
                    val beans = beanManager.getBeans(field.type) as Set<Bean<Any>>
                    val bean =
                        beanManager.resolve(beans)
                            ?: throw UnsatisfiedResolutionException(
                                "Unsatisfied dependency for type ${field.type.name} " +
                                    "at injection point ${type.name}.${field.name}"
                            )
                    val creationalContext = beanManager.createCreationalContext(bean)
                    val reference = beanManager.getReference(bean, field.type, creationalContext)
                    field.set(instance, reference)
                }
            }
            type = type.superclass
        }
    }
}
