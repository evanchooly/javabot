package javabot.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.List;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.Path;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.IllegalAddException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.rule.Action;
import org.dom4j.rule.Rule;
import org.dom4j.rule.Stylesheet;

/**
 * Created Jun 22, 2007
 *
 * @author <a href="mailto:jlee@antwerkz.com">Justin Lee</a>
 */
public class ProjectConfigTask extends Task {
    private String idea;
    private Path path;

    @Override
    @SuppressWarnings({"unchecked"})
    public void execute() throws BuildException {
        Document document;
        try {
            document = readProjectFile();
            removeOldLibraries(document);
            List<Element> list = document.selectNodes("//orderEntry[@type='module-library']");
            for(Element element : list) {
                Element element1 = (Element)element.selectSingleNode("library/CLASSES/root");
                log(element1.attribute("url").getValue());
            }
            Element component = (Element)document.selectSingleNode("//component[@name='NewModuleRootManager']");
            for(String element : path.list()) {
                addResource(component, element);
            }
            writeDocument(document);
        } catch(Exception e) {
            throw new BuildException(e);
        }
    }

    private void writeDocument(Document document) throws IOException {
        XMLWriter writer = new XMLWriter(new FileWriter(idea));
        writer.write(document);
        writer.close();
    }

    private Document readProjectFile() throws Exception {
        return new SAXReader().read(new File(idea));
    }

    private void removeOldLibraries(Document document) throws Exception {
        Rule removeOld = new Rule();
        removeOld.setPattern(DocumentHelper.createPattern("//orderEntry[@type='module-library']"));
        removeOld.setAction(new Action() {
            public void run(Node node) throws Exception {
                node.getParent().remove(node);
            }
        });
        Stylesheet style = new Stylesheet();
        style.addRule(removeOld);
        style.run(document);
    }

    private void addResource(Element component, String element) throws MalformedURLException {
        if(!element.endsWith("-javadoc.jar") && !element.endsWith("-sources.jar")) {
            try {
                Element orderEntry = component.addElement("orderEntry");
                orderEntry.addAttribute("type", "module-library");
                Element library = orderEntry.addElement("library");
                library.addElement("CLASSES").addElement("root").addAttribute("url", createURL(element));
                addOptionalResource(library, "JAVADOC", "javadoc", element);
                addOptionalResource(library, "SOURCES", "sources", element);
            } catch(IllegalAddException e) {
                e.printStackTrace();
                throw e;
            }
        }
    }

    private void addOptionalResource(Element library, String type, String extension, String file)
        throws MalformedURLException {
        Element element = library.addElement(type);
        if(file.endsWith(".jar")) {
            String optionalPath = file.replaceAll(".jar", "-" + extension + ".jar");
            File optional = new File(optionalPath);
            if(optional.exists()) {
                element.addElement("root").addAttribute("url", createURL(optionalPath));
            }
        }
    }

    private String createURL(String element) throws MalformedURLException {
        File file = new File(element);
        String url = file.toURI().toURL().toString();
        if(url.endsWith(".jar")) {
            url = url.replaceAll("file:", "jar://").replaceAll(".jar$", ".jar!/");
        }
        return url;
    }

    public String getIdea() {
        return idea;
    }

    public void setIdea(String idea) {
        this.idea = idea;
    }

    public Path getPath() {
        return path;
    }

    public void addPath(Path path) {
        this.path = path;
    }
}
