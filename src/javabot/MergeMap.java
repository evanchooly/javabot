package javabot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import java.util.Iterator;
import java.util.HashMap;

/**
	Merges two HashMaps from two files, prompting when there are conflicts.
*/
public class MergeMap
{
	public static void main(String[] args) throws Exception
	{
		if (args.length<3)
		{
			System.err.println
			(
				"Supply three arguments - two input files "+
				"and one output file"
			);
			return;
		}

		File input1=new File(args[0]);
		File input2=new File(args[1]);
		File output=new File(args[2]);

		if (!input1.exists())
		{
			System.err.println(input1+" does not exist");
			return;
		}

		if (!input2.exists())
		{
			System.err.println(input2+" does not exist");
			return;
		}

		if (output.exists())
		{
			System.err.println(output+" should not already exist");
			return;
		}

		FileInputStream fis=new FileInputStream(input1);
		ObjectInputStream ois=new ObjectInputStream(fis);
		HashMap map1=(HashMap)ois.readObject();
		ois.close();
		fis.close();
		
		fis=new FileInputStream(input2);
		ois=new ObjectInputStream(fis);
		HashMap map2=(HashMap)ois.readObject();
		ois.close();
		fis.close();

		Iterator iterator=map2.keySet().iterator();
	
		while (iterator.hasNext())
		{
			String next=(String)iterator.next();

			if
			(
				map1.containsKey(next) &&
				!map1.get(next).equals(map2.get(next))
			)
			{
				System.out.println
				(
					input1+" specifies "+next+"->"+
					map1.get(next)+", "+input2+
					" specifies "+map2.get(next)+"."
				);
			
				System.out.println
				(
					"Enter '1' if you want to use the "+
					"version from "+input1+", or enter "+
					"'2' if you want to use the version "+
					"from "+input2+"."
				);
				
				InputStreamReader isr=
					new InputStreamReader(System.in);
				
				BufferedReader br=new BufferedReader(isr);

				String input=br.readLine().trim();

				if (input.equals("2"))
					map1.put(next,map2.get(next));
				else
					if (!input.equals("1"))
					{
						System.err.println("Useless user, aborting.");
						return;
					}
			}
		}
		
		FileOutputStream fos=new FileOutputStream(output);
		ObjectOutputStream oos=new ObjectOutputStream(fos);
		oos.writeObject(map1);
		oos.close();
		fos.close();
	}
}
