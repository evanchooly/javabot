package javabot;

import javabot.javadoc.StructureReference;
import org.jdom.JDOMException;

import java.io.File;
import java.io.IOException;

public class JavadocParser
{
	private StructureReference reference;

	public JavadocParser(File file,String baseUrl)
		throws IOException, JDOMException
	{
		this.baseUrl=baseUrl;
		reference=new StructureReference(file);
	}

	public String[] javadoc(String key)
	{
		// Use presence of an open parentheses to indicate we're
		// looking for a method
		
		int openIndex=key.indexOf('(');
		
		if (openIndex==-1)
			return reference.getClassDocUrls(key, baseUrl);
		
		int finalIndex=key.lastIndexOf('.');

		int closeIndex=key.indexOf(')');
		
		if (closeIndex == -1 || finalIndex == -1)
			return new String[0];

		String className=key.substring(0,finalIndex);
		
		String methodName=key.substring(finalIndex+1,openIndex);
		
		String signatureTypes=key.substring(openIndex+1,closeIndex);
		
		return reference.getMethodDocUrls
			(className,methodName,signatureTypes,baseUrl);
	}

	private final String baseUrl;
}
