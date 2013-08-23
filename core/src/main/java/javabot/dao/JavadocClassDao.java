package javabot.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javabot.javadoc.JavadocApi;
import javabot.javadoc.JavadocClass;
import javabot.javadoc.JavadocField;
import javabot.javadoc.JavadocMethod;
import javabot.javadoc.criteria.JavadocClassCriteria;
import javabot.javadoc.criteria.JavadocFieldCriteria;
import javabot.javadoc.criteria.JavadocMethodCriteria;

public class JavadocClassDao extends BaseDao<JavadocClass> {
  protected JavadocClassDao() {
    super(JavadocClass.class);
  }

  @SuppressWarnings({"unchecked"})
  public JavadocClass[] getClass(final JavadocApi api, final String name) {
    final String[] strings = calculateNameAndPackage(name);
    final String pkgName = strings[0];
    JavadocClassCriteria criteria = new JavadocClassCriteria(ds);
    criteria.upperName().equal(strings[1].toUpperCase());
    if (api != null) {
      criteria.api(api);
    }
    if (pkgName != null) {
      criteria.upperPackageName().equal(pkgName.toUpperCase());
    }
    return criteria.query().asList().toArray(new JavadocClass[0]);
  }

  @SuppressWarnings({"unchecked"})
  public JavadocClass[] getClass(final String pkg, final String name) {
    final JavadocClassCriteria criteria = new JavadocClassCriteria(ds);
    criteria.upperPackageName().equal(pkg.toUpperCase());
    criteria.upperName().equal(name.toUpperCase());
    return criteria.query().asList().toArray(new JavadocClass[0]);
  }

  @SuppressWarnings({"unchecked"})
  public List<JavadocField> getField(JavadocApi api, final String className, final String fieldName) {
    final JavadocFieldCriteria criteria = new JavadocFieldCriteria(ds);
    JavadocClass[] classes = getClass(api, className);
    if (classes.length != 0) {
      JavadocClass javadocClass = classes[0];
      criteria.javadocClass(javadocClass);
      criteria.upperName().equal(fieldName.toUpperCase());
      return criteria.query().asList();
    }
    return Collections.emptyList();
  }

  @SuppressWarnings({"unchecked"})
  public List<JavadocMethod> getMethods(JavadocApi api, final String className, final String methodName,
      final String signatureTypes) {
    final JavadocClass[] classes = getClass(api, className);
    List<JavadocClass> list = new ArrayList<>(Arrays.asList(classes));
    final List<JavadocMethod> methods = new ArrayList<>();
    while (!list.isEmpty()) {
      JavadocClass javadocClass = list.remove(0);
      if (javadocClass != null) {
        methods.addAll(getMethods(methodName, signatureTypes, javadocClass));
        JavadocClass superClass = javadocClass.getSuperClass();
        if(superClass != null) {
          list.add(find(superClass.getId()));
        }
      }
    }
    return methods;
  }

  private List getMethods(final String name, final String signatureTypes, final JavadocClass javadocClass) {
    final JavadocMethodCriteria criteria = new JavadocMethodCriteria(ds);
    criteria.javadocClass(javadocClass);
    criteria.upperName().equal(name.toUpperCase());
    if (!"*".equals(signatureTypes)) {
      criteria.or(
          criteria.shortSignatureTypes().equal(signatureTypes),
          criteria.longSignatureTypes().equal(signatureTypes)
      );
    }
    return criteria.query().asList();
  }

  public void delete(JavadocClass javadocClass) {
    deleteFields(javadocClass);
    deleteMethods(javadocClass);
    super.delete(javadocClass);
  }

  private void deleteFields(final JavadocClass javadocClass) {
    JavadocFieldCriteria criteria = new JavadocFieldCriteria(ds);
    criteria.javadocClass(javadocClass);
    ds.delete(criteria.query());
  }

  private void deleteMethods(final JavadocClass javadocClass) {
    JavadocMethodCriteria criteria = new JavadocMethodCriteria(ds);
    criteria.javadocClass(javadocClass);
    ds.delete(criteria.query());
  }

  public long countMethods() {
    return ds.getCount(JavadocMethod.class);
  }

  private String[] calculateNameAndPackage(final String name) {
    String clsName = name;
    while (clsName.contains(".") && Character.isLowerCase(clsName.charAt(0))) {
      clsName = clsName.substring(clsName.indexOf(".") + 1);
    }
    String pkgName = name.equals(clsName) ? null : name.substring(0, name.indexOf(clsName) - 1);
    return new String[]{pkgName, clsName};
  }

  public void deleteFor(final JavadocApi api) {
    JavadocFieldCriteria fields = new JavadocFieldCriteria(ds);
    fields.api(api);
    ds.delete(fields.query());

    JavadocMethodCriteria methods = new JavadocMethodCriteria(ds);
    methods.api(api);
    ds.delete(methods.query());

    JavadocClassCriteria classes = new JavadocClassCriteria(ds);
    classes.api(api);
    ds.delete(classes.query());
  }
}