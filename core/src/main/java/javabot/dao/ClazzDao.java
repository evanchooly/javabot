package javabot.dao;

import java.util.List;

import javabot.javadoc.Api;
import javabot.javadoc.Clazz;
import javabot.javadoc.Field;
import javabot.javadoc.Method;

/**
 * Created Jul 27, 2007
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public interface ClazzDao extends BaseDao {
  String DELETE_ALL = "ClazzDao.deleteAll";
  String DELETE_ALL_METHODS = "ClazzDao.deleteAllMethods";
  String GET_BY_NAME = "ClazzDao.getByName";
  String GET_BY_NAME_API = "ClazzDao.getByNameApi";

  String GET_BY_API_PACKAGE_AND_NAME = "ClazzDao.getByAPIAndPackageAndName";
  String GET_BY_PACKAGE_AND_NAME = "ClazzDao.getByPackageAndName";

  String GET_METHOD_NO_SIG = "ClazzDao.getMethodNoSig";
  String GET_METHOD = "ClazzDao.getMethod";

  String GET_FIELD_WITH_CLS_AND_PKG = "ClazzDao.getFieldWithClassAndPackage";
  String GET_FIELD_WITH_CLS = "ClazzDao.getFieldWithClass";
  String GET_FIELD_WITH_CLS_PKG_API = "ClazzDao.getFieldWithClassPackageApi";
  String GET_FIELD_WITH_CLS_API = "ClazzDao.getFieldWithClassApi";

  List<Clazz> findAll(String api);

  void deleteAll(String pkgName);

  Clazz[] getClass(Api api, String name);

  Clazz[] getClass(String pkg, String name);

  List<Method> getMethods(Api api, String className, String methodName, String signatureTypes);

  List<Field> getField(Api api, String className, String fieldName);
}