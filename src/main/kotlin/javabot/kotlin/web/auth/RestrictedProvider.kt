package javabot.kotlin.web.auth

import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.ComponentContext
import com.sun.jersey.core.spi.component.ComponentScope
import com.sun.jersey.spi.inject.Injectable
import com.sun.jersey.spi.inject.InjectableProvider

public class RestrictedProvider : InjectableProvider<Restricted, Parameter> {

    override fun getScope(): ComponentScope {
        return ComponentScope.PerRequest
    }

    override fun getInjectable(ic: ComponentContext, a: Restricted, c: Parameter): Injectable<*> {
        return OpenIDRestrictedToInjectable(a.value)
    }
}
