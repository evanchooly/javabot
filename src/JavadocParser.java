import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class JavadocParser
{
	private HashMap map=new HashMap();

	/**
		@param file a file with lines referring to .java file names.
	*/
	public JavadocParser(File file,String baseUrl) throws IOException
	{
		this.baseUrl=baseUrl;
		
		FileReader fileReader=new FileReader(file);
		BufferedReader reader=new BufferedReader(fileReader);

		for(;;)
		{
			String name=reader.readLine();
		
			if (name==null)
				break;

			String fullName=name.substring
				(0,name.length() - 5);
				
			String shortName=fullName.substring
			(
				fullName.lastIndexOf("/")+1,
				fullName.length()
			).toLowerCase();
                
			ArrayList list=(ArrayList)map.get(shortName);
			
			if (list==null)
			{
				list=new ArrayList(1);
				map.put(shortName, list);
			}
                
			list.add(fullName);
		}
		
		reader.close();
		fileReader.close();
	}
    
	public String[] javadoc(String className)
	{
		ArrayList list=(ArrayList)map.get(className.toLowerCase());

		if (list == null)
		{
			return new String[0];
		}
		
		String[] results=new String[list.size()];
		
		for (int i=0;i<list.size();i++)
		{
			String entry=(String)list.get(i);
			String fullName=entry.replace('/','.');
			results[i]=fullName+": "+baseUrl+entry+".html";
        	}
		
		return results;
	}
    
	private final String baseUrl;
}
