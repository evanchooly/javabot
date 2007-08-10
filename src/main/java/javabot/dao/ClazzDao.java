package javabot.dao;

import java.util.List;

import com.sun.javadoc.ClassDoc;
import javabot.javadoc.Clazz;
import javabot.javadoc.Method;

/**
 * Created Jul 27, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
public interface ClazzDao extends BaseDao<Clazz> {
    String DELETE_ALL = "ClazzDao.deleteAll";
    String DELETE_ALL_METHODS = "ClazzDao.deleteAllMethods";
    String GET_BY_NAME = "ClazzDao.getByName";
    String GET_BY_PACKAGE_AND_NAME = "ClazzDao.getByPackageAndName";
    String GET_METHOD_NO_SIG = "ClazzDao.getMethodNoSig";
    String GET_METHOD = "ClazzDao.getMethod";

    void deleteAll();

    Clazz getOrCreate(ClassDoc classDoc);

    Clazz[] getClass(String name);

    List<Method> getMethods(String className, String methodName, String signatureTypes, String baseUrl);
}
