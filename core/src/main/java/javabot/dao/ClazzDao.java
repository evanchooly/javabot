package javabot.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javabot.javadoc.Api;
import javabot.javadoc.Clazz;
import javabot.javadoc.Field;
import javabot.javadoc.Method;
import javabot.javadoc.criteria.ClazzCriteria;
import javabot.javadoc.criteria.FieldCriteria;
import javabot.javadoc.criteria.MethodCriteria;

public class ClazzDao extends BaseDao<Clazz> {
  protected ClazzDao() {
    super(Clazz.class);
  }

  @SuppressWarnings({"unchecked"})
  public Clazz[] getClass(final Api api, final String name) {
    final String[] strings = calculateNameAndPackage(name);
    final String pkgName = strings[0];
    ClazzCriteria criteria = new ClazzCriteria(ds);
    criteria.upperClassName().equal(strings[1].toUpperCase());
    if (api != null) {
      criteria.apiId().equal(api.getId());
    }
    if (pkgName != null) {
      criteria.upperPackage().equal(pkgName.toUpperCase());
    }
    return criteria.query().asList().toArray(new Clazz[0]);
  }

  @SuppressWarnings({"unchecked"})
  public Clazz[] getClass(final String pkg, final String name) {
    final ClazzCriteria criteria = new ClazzCriteria(ds);
    criteria.upperPackage().equal(pkg.toUpperCase());
    criteria.upperClassName().equal(name.toUpperCase());
    return criteria.query().asList().toArray(new Clazz[0]);
  }

  @SuppressWarnings({"unchecked"})
  public List<Field> getField(Api api, final String className, final String fieldName) {
    final FieldCriteria criteria = new FieldCriteria(ds);
    Clazz[] classes = getClass(api, className);
    if (classes.length != 0) {
      Clazz clazz = classes[0];
      criteria.classId().equal(clazz.getId());
      criteria.upperName().equal(fieldName.toUpperCase());
      return criteria.query().asList();
    }
    return Collections.emptyList();
  }

  @SuppressWarnings({"unchecked"})
  public List<Method> getMethods(Api api, final String className, final String methodName,
      final String signatureTypes) {
    final Clazz[] classes = getClass(api, className);
    List<Clazz> list = new ArrayList<>(Arrays.asList(classes));
    final List<Method> methods = new ArrayList<>();
    while (!list.isEmpty()) {
      Clazz clazz = list.remove(0);
      if (clazz != null) {
        methods.addAll(getMethods(methodName, signatureTypes, clazz));
        list.add(find(clazz.getSuperClassId()));
      }
    }
    return methods;
  }

  private List getMethods(final String name, final String signatureTypes, final Clazz clazz) {
    final MethodCriteria criteria = new MethodCriteria(ds);
    criteria.clazzId().equal(clazz.getId());
    criteria.upperMethodName().equal(name.toUpperCase());
    if (!"*".equals(signatureTypes)) {
      criteria.or(
          criteria.shortSignatureTypes().equal(signatureTypes),
          criteria.longSignatureTypes().equal(signatureTypes)
      );
    }
    return criteria.query().asList();
  }

  public void delete(Clazz clazz) {
    deleteFields(clazz);
    deleteMethods(clazz);
    super.delete(clazz);
  }

  private void deleteFields(final Clazz clazz) {
    FieldCriteria criteria = new FieldCriteria(ds);
    criteria.classId().equal(clazz.getId());
    ds.delete(criteria.query());
  }

  private void deleteMethods(final Clazz clazz) {
    MethodCriteria criteria = new MethodCriteria(ds);
    criteria.clazzId().equal(clazz.getId());
    ds.delete(criteria.query());
  }

  private String[] calculateNameAndPackage(final String name) {
    String clsName = name;
    while (clsName.contains(".") && Character.isLowerCase(clsName.charAt(0))) {
      clsName = clsName.substring(clsName.indexOf(".") + 1);
    }
    String pkgName = name.equals(clsName) ? null : name.substring(0, name.indexOf(clsName) - 1);
    return new String[]{pkgName, clsName};
  }
}