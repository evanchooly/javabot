package javabot.javadoc;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Comparator;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import javabot.dao.ClazzDao;
import javabot.model.Persistent;
import org.cyberneko.html.parsers.DOMParser;
import org.jaxen.JaxenException;
import org.jaxen.dom.DOMXPath;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.html.HTMLElement;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@Entity
@Table(name = "classes")
@NamedQueries({
    @NamedQuery(name = ClazzDao.DELETE_ALL, query = "delete from Clazz c where c.api=:api"),
    @NamedQuery(name = ClazzDao.DELETE_ALL_METHODS, query = "delete from Method m where m.clazz.api=:api"),
    @NamedQuery(name = ClazzDao.GET_BY_NAME, query = "select c from Clazz c where "
        + "upper(c.className)=:name"),
    @NamedQuery(name = ClazzDao.GET_BY_API_PACKAGE_AND_NAME, query = "select c from Clazz c where "
        + "c.packageName=:package and c.api=:api and c.className=:name"),
    @NamedQuery(name = ClazzDao.GET_BY_PACKAGE_AND_NAME, query = "select c from Clazz c where "
        + "upper(c.packageName)=upper(:package) and upper(c.className)=upper(:name)"),
    @NamedQuery(name = ClazzDao.GET_METHOD_NO_SIG, query = "select m from Method m where "
        + "m.clazz.id=:classId and upper(m.methodName)=:name"),
    @NamedQuery(name = ClazzDao.GET_METHOD, query = "select m from Clazz c join c.methods m where "
        + "m.clazz.id=:classId and upper(m.methodName)=:name and (upper(m.shortSignatureTypes)=:params"
        + " or upper(m.shortSignatureStripped)=:params or upper(m.longSignatureTypes)=:params"
        + " or upper(m.longSignatureStripped)=:params)")
})
public class Clazz extends JavadocElement implements Persistent {
    private Long id;
    private Api api;
    private String packageName;
    private String className;
    private Clazz superClass;
    private List<Method> methods;

    public Clazz() {
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

    public void setId(final Long classId) {
        id = classId;
    }

    public Clazz(final Api apiName, final String pkg, final String name) {
        api = apiName;
        packageName = pkg;
        className = name;
    }

    @SuppressWarnings({"unchecked"})
    @Transactional
    public final void populate(final ClazzDao dao) throws IOException, SAXException, JaxenException {
        final Document document = getDocument(api.getBaseUrl() + "/" + packageName.replace('.', '/')
            + "/" + getClassName() + ".html");
        final List<HTMLElement> dts = (List<HTMLElement>) new DOMXPath("//DT/following-sibling::node()"
            + "[text()='extends ']").evaluate(document);
        if (!dts.isEmpty()) {
            final HTMLElement element = dts.get(0);
            final HTMLElement aNode = (HTMLElement) element.getChildNodes().item(1);
            String pkg = aNode.getAttribute("title");
            pkg = pkg.substring(pkg.indexOf(" in ") + 4);
            superClass = dao.getClass(pkg, aNode.getTextContent())[0];
        }
        final List<HTMLElement> result = (List<HTMLElement>) new DOMXPath("//HEAD/META[@name='keywords']")
            .evaluate(document);
        for (final HTMLElement element : result) {
            String content = element.getAttribute("content");
            if (content.endsWith("()")) {
                content = content.substring(0, content.length() - 2);
                final List<HTMLElement> methodList = (List<HTMLElement>) new DOMXPath(
                    "//A[starts-with(@name, '" + content + "(')]").evaluate(document);
                for (final HTMLElement htmlElement : methodList) {
                    final NamedNodeMap attributes = htmlElement.getAttributes();
                    final Method method = new Method(attributes.getNamedItem("name").getNodeValue(), this);
                    dao.save(method);
                    methods.add(method);
                }
            }
        }
        dao.save(this);
    }

    public static Document getDocument(final String specUrl) throws IOException, SAXException {
        final URL url = new URL(specUrl);
        final URLConnection connection = url.openConnection();
        connection.setConnectTimeout(1000);
        connection.setReadTimeout(1000);
        final DOMParser parser = new DOMParser();
        try {
            parser.parse(new InputSource(url.openStream()));
        } catch (SAXException e) {
            System.out.println("specUrl = " + specUrl);
            throw e;
        } catch (IOException e) {
            System.out.println("specUrl = " + specUrl);
            throw e;
        }
        return parser.getDocument();
    }

    @ManyToOne
    public Api getApi() {
        return api;
    }

    public void setApi(final Api api) {
        this.api = api;
    }

    @Column(nullable = false)
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(final String name) {
        packageName = name;
    }

    @Column(nullable = false)
    public String getClassName() {
        return className;
    }

    public void setClassName(final String name) {
        className = name;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Clazz getSuperClass() {
        return superClass;
    }

    public void setSuperClass(final Clazz aClass) {
        superClass = aClass;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "clazz", fetch = FetchType.EAGER)
    public List<Method> getMethods() {
        return methods;
    }

    public void setMethods(final List<Method> list) {
        methods = list;
    }

    @Override
    public String toString() {
        return packageName + "." + className;
    }
}
