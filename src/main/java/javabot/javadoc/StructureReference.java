package javabot.javadoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import org.apache.wicket.injection.web.InjectorHolder;
import org.springframework.beans.factory.annotation.Autowired;

public class StructureReference {
    @Autowired
    private ApiDao apiDao;
    @Autowired
    private ClazzDao clazzDao;

    public StructureReference() {
        InjectorHolder.getInjector().inject(this);
    }

    public void process(final RootDoc doc, final String apiName, final String baseUrl) {
        apiDao.delete(apiName);
        final Api api = new Api(apiName, baseUrl);
        apiDao.save(api);
        final ClassDoc[] classDocs = doc.classes();
        for(final ClassDoc cd : classDocs) {
            final Clazz reference = clazzDao.getOrCreate(cd, api, cd.containingPackage().name(), cd.name());
        }
    }
}