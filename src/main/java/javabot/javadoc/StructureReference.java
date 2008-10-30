package javabot.javadoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import javabot.dao.ApiDao;
import javabot.dao.ClazzDao;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class StructureReference {
    @SpringBean
    private ApiDao apiDao;
    @SpringBean
    private ClazzDao clazzDao;

    public StructureReference() {
        InjectorHolder.getInjector().inject(this);
    }

    public void process(RootDoc doc, String apiName, String baseUrl) {
        apiDao.delete(apiName);
        Api api = new Api(apiName, baseUrl);
        apiDao.save(api);
        ClassDoc[] classDocs = doc.classes();
        for(ClassDoc cd : classDocs) {
            Clazz reference = clazzDao.getOrCreate(cd, api, cd.containingPackage().name(), cd.name());
        }
    }
}