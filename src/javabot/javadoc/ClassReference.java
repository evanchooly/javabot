package javabot.javadoc;

import com.sun.javadoc.ClassDoc;
import com.sun.javadoc.MethodDoc;
import com.sun.javadoc.ConstructorDoc;
import org.jdom.Attribute;
import org.jdom.Element;

import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Comparator;

public class ClassReference
{
	private String packageName;
	private String className;
	private String superClassQualified;
	private List methods;

	public ClassReference(ClassDoc doc)
	{
		packageName=doc.containingPackage().name();
		className=doc.name();
		
		if (doc.superclass()!=null)
			superClassQualified=doc.superclass().qualifiedName();
		
		MethodDoc[] methodDocs=doc.methods();
		ConstructorDoc[] conDocs = doc.constructors();
		
		this.methods=new ArrayList(methodDocs.length + conDocs.length);
		
		for (int i=0;i<methodDocs.length;i++)
		{
			MethodDoc methodDoc = methodDocs[i];
			methods.add(new MethodReference(methodDoc,this));
		}
		
		for (int i = 0; i < conDocs.length; i++)
		{
			ConstructorDoc conDoc = conDocs[i];
			methods.add(new MethodReference(conDoc, this));
		}
		
		Collections.sort(methods,new MethodComparator());
	}

	public ClassReference(Element element)
	{
		className=element.getAttribute("className").getValue();
		packageName=element.getAttribute("packageName").getValue();
		
		Attribute superClassAttribute=
			element.getAttribute("superClassQualified");
		
		if (superClassAttribute!=null)
			superClassQualified=superClassAttribute.getValue();

		List children=element.getChildren("Method");
		methods=new ArrayList(children.size());
		
		for (int i=0;i<children.size();i++)
		{
			Element child=(Element)children.get(i);
			methods.add(new MethodReference(child,this));
		}
		
		Collections.sort(methods,new MethodComparator());
	}

	public String getMethodUrl
		(String methodName,String signatureTypes,String baseUrl)
	{
		if ("*".equals(signatureTypes))
			return getWildcardMethodUrl(methodName,baseUrl);
		else
		{
			String signature=methodName+"("+signatureTypes+")";
			
			for (int i=0;i<methods.size();i++)
			{
				MethodReference reference=
					(MethodReference)methods.get(i);
				
				if (reference.signatureMatches(signature))
					return reference.getMethodUrl(baseUrl);
			}
			return null;
		}
	}

	private String getWildcardMethodUrl(String methodName,String baseUrl)
	{
		for (int i=0;i<methods.size();i++)
		{
			MethodReference reference=
				(MethodReference)methods.get(i);
			
			if (reference.getMethodName().equals(methodName))
				return reference.getMethodUrl(baseUrl);
		}
		return null;
	}

	public String getClassUrl(String baseUrl)
	{
		return getQualifiedName()+": "+getClassHTMLPage(baseUrl);
	}

	public String getClassHTMLPage(String baseUrl)
	{
		String path=getQualifiedName().replaceAll("\\.","/");
		return baseUrl+path+".html";
	}

	public String getPackageName()
	{
		return packageName;
	}

	public String getClassName()
	{
		return className;
	}

	public String getQualifiedName()
	{
		return
			(packageName==null ? "" : (packageName+"."))+
			className;
	}

	public String getSuperClass()
	{
		return superClassQualified;
	}

	public Element buildElement()
	{
		Element element=new Element("ClassReference");
		element.setAttribute("packageName",packageName);
		element.setAttribute("className",className);
		
		if (superClassQualified != null)
			element.setAttribute
				("superClassQualified",superClassQualified);
		
		for (int i=0;i<methods.size();i++)
		{
			MethodReference reference=
				(MethodReference)methods.get(i);
			
			element.addContent(reference.buildElement());
		}
		
		return element;
	}

	private static class MethodComparator implements Comparator
	{
		public int compare(Object o1,Object o2)
		{
			MethodReference one=(MethodReference)o1;
			MethodReference two=(MethodReference)o2;
			
			if (one.getArgumentCount()==two.getArgumentCount())
				return 0;
			
			if (one.getArgumentCount()>two.getArgumentCount())
				return 1;
			
			return -1;
		}
	}
}
