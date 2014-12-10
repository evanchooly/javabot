package javabot.dao;

import javabot.javadoc.JavadocApi;
import javabot.javadoc.JavadocClass;
import javabot.javadoc.JavadocClassVisitor;
import javabot.javadoc.JavadocField;
import javabot.javadoc.JavadocMethod;
import javabot.javadoc.criteria.JavadocClassCriteria;
import javabot.javadoc.criteria.JavadocFieldCriteria;
import javabot.javadoc.criteria.JavadocMethodCriteria;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JavadocClassDao extends BaseDao<JavadocClass> {
    private static final Logger LOG = LoggerFactory.getLogger(JavadocClassDao.class);

    protected JavadocClassDao() {
        super(JavadocClass.class);
    }

    @SuppressWarnings({"unchecked"})
    public List<JavadocClass> getClass(final JavadocApi api, final String name) {
        final String[] strings = JavadocClassVisitor.calculateNameAndPackage(name);
        final String pkgName = strings[0];
        JavadocClassCriteria criteria = new JavadocClassCriteria(ds);
        criteria.upperName().equal(strings[1].toUpperCase());
        if (api != null) {
            criteria.apiId(api.getId());
        }
        if (pkgName != null) {
            criteria.upperPackageName().equal(pkgName.toUpperCase());
        }
        return criteria.query().asList();
    }

    @SuppressWarnings({"unchecked"})
    public JavadocClass[] getClass(final JavadocApi api, final String pkg, final String name) {
        final JavadocClassCriteria criteria;
        try {
            criteria = new JavadocClassCriteria(ds);
            criteria.apiId(api.getId());
            criteria.upperPackageName().equal(pkg.toUpperCase());
            criteria.upperName().equal(name.toUpperCase());
        } catch (NullPointerException e) {
            LOG.debug("pkg = " + pkg);
            LOG.debug("name = " + name);
            throw e;
        }
        return criteria.query().asList().toArray(new JavadocClass[0]);
    }

    @SuppressWarnings({"unchecked"})
    public List<JavadocField> getField(JavadocApi api, final String className, final String fieldName) {
        final JavadocFieldCriteria criteria = new JavadocFieldCriteria(ds);
        List<JavadocClass> classes = getClass(api, className);
        if (!classes.isEmpty()) {
            JavadocClass javadocClass = classes.get(0);
            criteria.javadocClassId(javadocClass.getId());
            criteria.upperName().equal(fieldName.toUpperCase());
            return criteria.query().asList();
        }
        return Collections.emptyList();
    }

    @SuppressWarnings({"unchecked"})
    public List<JavadocMethod> getMethods(JavadocApi api, final String className, final String methodName,
                                          final String signatureTypes) {
        final List<JavadocClass> classes = getClass(api, className);
        List<JavadocClass> list = new ArrayList<>(classes);
        final List<JavadocMethod> methods = new ArrayList<>();

        while (!list.isEmpty()) {
            JavadocClass javadocClass = list.remove(0);
            if (javadocClass != null) {
                methods.addAll(getMethods(methodName, signatureTypes, javadocClass));
                ObjectId superClassId = javadocClass.getSuperClassId();
                if (superClassId != null) {
                    list.add(find(superClassId));
                }
            }
        }
        return methods;
    }

    private List getMethods(final String name, final String signatureTypes, final JavadocClass javadocClass) {
        final JavadocMethodCriteria criteria = new JavadocMethodCriteria(ds);
        criteria.javadocClassId(javadocClass.getId());
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
        criteria.javadocClassId(javadocClass.getId());
        ds.delete(criteria.query());
    }

    private void deleteMethods(final JavadocClass javadocClass) {
        JavadocMethodCriteria criteria = new JavadocMethodCriteria(ds);
        criteria.javadocClassId(javadocClass.getId());
        ds.delete(criteria.query());
    }

    public long countMethods() {
        return ds.getCount(JavadocMethod.class);
    }

    public void deleteFor(final JavadocApi api) {
        if (api != null) {
            LOG.debug("Dropping fields from " + api.getName());
            JavadocFieldCriteria criteria = new JavadocFieldCriteria(ds);
            criteria.apiId(api.getId());
            ds.delete(criteria.query());

            LOG.debug("Dropping methods from " + api.getName());
            JavadocMethodCriteria method = new JavadocMethodCriteria(ds);
            method.apiId(api.getId());
            ds.delete(method.query());

            LOG.debug("Dropping classes from " + api.getName());
            JavadocClassCriteria klass = new JavadocClassCriteria(ds);
            klass.apiId(api.getId());
            ds.delete(klass.query());
        }
    }
}