package javabot.javadoc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.RootDoc;
import org.jdom.DocType;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class StructureReference
{
	private ListMap shortNames=new ListMap();
	private Map longNames=new TreeMap();

	public StructureReference(RootDoc doc)
	{
		ClassDoc[] classDocs=doc.classes();
		
		for (int i=0;i<classDocs.length;i++)
		{
			ClassDoc cd=classDocs[i];
			ClassReference reference=new ClassReference(cd);
			addClassReference(reference);
		}
	}

	public StructureReference(File file) throws IOException, JDOMException
	{
		SAXBuilder sax=new SAXBuilder(false);
		Document doc=sax.build(new FileInputStream(file));

		List children=doc.getRootElement().getChildren
			("ClassReference");
		
		for (int i=0;i<children.size();i++)
		{
			Element child=(Element)children.get(i);
			ClassReference reference=new ClassReference(child);
			addClassReference(reference);
		}
	}

	private void addClassReference(ClassReference reference)
	{
		shortNames.put(reference.getClassName().toLowerCase(),reference);
		longNames.put(reference.getQualifiedName(),reference);
	}

	public String[] getClassDocUrls(String className,String baseURL)
	{
		ClassReference reference=
			(ClassReference)longNames.get(className);
		
		if (reference!=null)
			return new String[]{reference.getClassUrl(baseURL)};
		
		List list=(List)shortNames.get(className.toLowerCase());
		
		if (list==null)
			return new String[0];
		
		String[] urls = new String[list.size()];
		
		for (int i=0;i<list.size();i++)
		{
			ClassReference ref=(ClassReference)list.get(i);
			urls[i]=ref.getClassUrl(baseURL);
		}
		
		return urls;
	}

	public String[] getMethodDocUrls
	(
		String className,
		String methodName,
		String signatureTypes,
		String baseUrl
	)
	{
		ClassReference reference=
			(ClassReference)longNames.get(className);
		
		if (reference != null)
			{
			String url = getUrl
					(
						reference,
						methodName,
						signatureTypes,
						baseUrl
					);
			if (url == null)
				return new String[0];
			return new String[]{url};
		}
		
		List classes=(List)shortNames.get(className.toLowerCase());
		
		if (classes==null)
			return new String[0];
		
		List urls=new ArrayList(classes.size());
		
		for (int i=0;i<classes.size();i++)
		{
			ClassReference shortReference=
				(ClassReference)classes.get(i);
			
			String url=getUrl
			(
				shortReference,
				methodName,
				signatureTypes,
				baseUrl
			);
			
			if (url!=null)
				urls.add(url);
			
		}
		
		return (String[])urls.toArray(new String[urls.size()]);
	}

	private String getUrl
	(
		ClassReference reference,
		String methodName,
		String signatureTypes,String baseUrl
	)
	{
		String url=reference.getMethodUrl
			(methodName,signatureTypes,baseUrl);
		
		if (url!=null)
			return url;
				
		String sClass=reference.getSuperClass();
			
		if (sClass==null)
			return null;

		ClassReference superClass=
			(ClassReference)longNames.get(sClass);
				
		if (superClass!=null)
			return getUrl
			(
				superClass,
				methodName,
				signatureTypes,
				baseUrl
			);
		
		return null;
	}

	public void writeReference(File file) throws IOException
	{
		Element root=new Element("StructureReference");
		
		Document document=new Document
			(root,new DocType("StructureReference"));
		
		Iterator it=longNames.values().iterator();
		
		while (it.hasNext())
		{
			ClassReference reference=(ClassReference)it.next();
			root.addContent(reference.buildElement());
		}
		
		XMLOutputter output=new XMLOutputter(Format.getPrettyFormat());
		
		output.output(document, new FileOutputStream(file));
	}
}
