package javabot.dao;

import java.util.List;

import javabot.javadoc.Clazz;
import javabot.javadoc.Method;
import javabot.javadoc.Field;

/**
 * Created Jul 27, 2007
 *
 * @author <a href="mailto:javabot@cheeseronline.org">cheeser</a>
 */
public interface ClazzDao extends BaseDao<Clazz> {
    String DELETE_ALL = "ClazzDao.deleteAll";
    String DELETE_ALL_METHODS = "ClazzDao.deleteAllMethods";
    String GET_BY_NAME = "ClazzDao.getByName";
    String GET_BY_API_PACKAGE_AND_NAME = "ClazzDao.getByAPIAndPackageAndName";
    String GET_BY_PACKAGE_AND_NAME = "ClazzDao.getByPackageAndName";
    String GET_METHOD_NO_SIG = "ClazzDao.getMethodNoSig";
    String GET_METHOD = "ClazzDao.getMethod";
    String GET_FIELD_WITH_CLS_AND_PKG = "ClazzDao.getFieldWithClassAndPackage";
    String GET_FIELD_WITH_CLS = "ClazzDao.getFieldWithClass";

    List<Clazz> findAll(String api);

    void deleteAll(String pkgName);

    Clazz[] getClass(String name);

    Clazz[] getClass(String pkg, String name);

    List<Method> getMethods(String className, String methodName, String signatureTypes);

    List<Field> getField(String className, String fieldName);
}