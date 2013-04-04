import com.google.inject.{Guice, Injector}
import play.GlobalSettings

class Global extends GuiceGlobalSettings {
  @Override
  def  createInjector: Injector = {
    Guice.createInjector(Stage.PRODUCTION, new PlayModule() {

      @Override
      protected void configurePlay () {
        // bind some stuff!
        bind(FooPresenter.class).in(RequestScoped.class);
      }
    });
  }
}
