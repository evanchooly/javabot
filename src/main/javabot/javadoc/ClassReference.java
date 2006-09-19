package javabot.javadoc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.ConstructorDoc;
import com.sun.javadoc.MethodDoc;
import org.jdom.Attribute;
import org.jdom.Element;

public class ClassReference {
    private String packageName;
    private String className;
    private String superClassQualified;
    private List<MethodReference> methods;

    public ClassReference(ClassDoc doc) {
        packageName = doc.containingPackage().name();
        className = doc.name();
        if(doc.superclass() != null) {
            superClassQualified = doc.superclass().qualifiedName();
        }
        this.methods = new ArrayList<MethodReference>(doc.methods().length + doc.constructors().length);
        for(MethodDoc methodDoc : doc.methods()) {
            methods.add(new MethodReference(methodDoc, this));
        }
        for(ConstructorDoc conDoc : doc.constructors()) {
            methods.add(new MethodReference(conDoc, this));
        }
        Collections.sort(methods, new MethodComparator());
    }

    public ClassReference(Element element) {
        className = element.getAttribute("className").getValue();
        packageName = element.getAttribute("packageName").getValue();
        Attribute superClassAttribute = element.getAttribute("superClassQualified");
        if(superClassAttribute != null) {
            superClassQualified = superClassAttribute.getValue();
        }
        methods = new ArrayList<MethodReference>(element.getChildren("Method").size());
        for(Object aChildren : element.getChildren("Method")) {
            Element child = (Element)aChildren;
            methods.add(new MethodReference(child, this));
        }
        Collections.sort(methods, new MethodComparator());
    }

    public String getMethodUrl(String methodName, String signatureTypes, String baseUrl) {
        if("*".equals(signatureTypes)) {
            return getWildcardMethodUrl(methodName, baseUrl);
        } else {
            String signature = methodName + "(" + signatureTypes + ")";
            for(MethodReference method : methods) {
                if(method.signatureMatches(signature)) {
                    return method.getMethodUrl(baseUrl);
                }
            }
            return null;
        }
    }

    private String getWildcardMethodUrl(String methodName, String baseUrl) {
        for(MethodReference method : methods) {
            if(method.getMethodName().equalsIgnoreCase(methodName)) {
                return method.getMethodUrl(baseUrl);
            }
        }
        return null;
    }

    public String getClassUrl(String baseUrl) {
        return getQualifiedName() + ": " + getClassHTMLPage(baseUrl);
    }

    public String getClassHTMLPage(String baseUrl) {
        String path = packageName == null ? "" : packageName.replaceAll("\\.", "/") + "/";
        return baseUrl + path + className + ".html";
    }

    public String getPackageName() {
        return packageName;
    }

    public String getClassName() {
        return className;
    }

    public String getQualifiedName() {
        return (packageName == null ? "" : packageName + ".") + className;
    }

    public String getSuperClass() {
        return superClassQualified;
    }

    public Element buildElement() {
        Element element = new Element("ClassReference");
        element.setAttribute("packageName", packageName);
        element.setAttribute("className", className);
        if(superClassQualified != null) {
            element.setAttribute("superClassQualified", superClassQualified);
        }
        for(MethodReference method : methods) {
            element.addContent(method.buildElement());
        }
        return element;
    }

    private static class MethodComparator implements Comparator<MethodReference> {
        public int compare(MethodReference o1, MethodReference o2) {
            if(o1.getArgumentCount() == o2.getArgumentCount()) {
                return 0;
            }
            if(o1.getArgumentCount() > o2.getArgumentCount()) {
                return 1;
            }
            return -1;
        }
    }
}
