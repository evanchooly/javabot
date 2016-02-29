package javabot.web.auth

import com.sun.jersey.api.model.Parameter
import com.sun.jersey.core.spi.component.ComponentContext
import com.sun.jersey.core.spi.component.ComponentScope
import com.sun.jersey.core.spi.component.ComponentScope.PerRequest
import com.sun.jersey.spi.inject.Injectable
import com.sun.jersey.spi.inject.InjectableProvider

class RestrictedProvider : InjectableProvider<Restricted, Parameter> {

    override fun getScope(): ComponentScope {
        return PerRequest
    }

    override fun getInjectable(ic: ComponentContext, a: Restricted, c: Parameter): Injectable<*> {
        return OpenIDRestrictedToInjectable(a.value)
    }
}
