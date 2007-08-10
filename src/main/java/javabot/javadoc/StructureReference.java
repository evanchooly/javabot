package javabot.javadoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import javabot.dao.ClazzDao;
import org.apache.wicket.injection.web.InjectorHolder;
import org.apache.wicket.spring.injection.annot.SpringBean;

public class StructureReference {
    @SpringBean
    private ClazzDao clazzDao;

    public StructureReference() {
        InjectorHolder.getInjector().inject(this);
    }

    public void process(RootDoc doc) {
        clazzDao.deleteAll();
        ClassDoc[] classDocs = doc.classes();
        for(ClassDoc cd : classDocs) {
            Clazz reference = clazzDao.getOrCreate(cd);
            clazzDao.save(reference);
        }
    }
}