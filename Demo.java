import java.lang.*;
import java.util.*;
import java.io.*;		//For Directory Traversal
import java.io.FileInputStream;  //For File Reading
import java.security.MessageDigest; //For CheckSum

class Demo
{
	public static void main(String args[]) throws Exception
	{
		BufferedReader br=new BufferedReader(new InputStreamReader(System.in));

		System.out.println("Please Enter Directory Name :");
		String dir=br.readLine();

		Cleaner cobj=new Cleaner(dir);

		//To remove Empty Files
		cobj.cleanDirectoryEmptyFile();

		//To remove Duplicate Files
		cobj.cleanDirectoryDupFile();
	}
}


class Cleaner
{
	public File fdir=null;
	public Cleaner(String name)
	{
		fdir=new File(name);
		if(!fdir.exists())
		{
			System.out.println("Invalid Directory Name");
			System.exit(0);
		}
	}

	public void cleanDirectoryEmptyFile() throws Exception
	{
		File filelist[]=fdir.listFiles();
		int EmptyFile=0;

		for(File file:filelist)
		{
			//System.out.println(file.getName()+" "+file.length());
			if(file.length()==0)
			{
				System.out.println("Empty File Name :"+file.getName());
				if(!file.delete())
				{
					System.out.println("Unable to delete files");
				}
				else
				{
					EmptyFile++;
				}
			}
		}
		System.out.println("Total Empty Files deleted :"+EmptyFile);
	}

	public void cleanDirectoryDupFile() throws Exception
	{
		File filelist[]=fdir.listFiles();
		int DupFile=0;

		byte bytearr[]=new byte[1024];
		LinkedList<String> lobj=new LinkedList<String>();

		int Rcount=0;

		try
		{
			MessageDigest digest=MessageDigest.getInstance("MD5");
			if(digest==null)
			{
				System.out.println("Unable to get MD5");
				System.exit(0);
			}

			for(File file:filelist)
			{
				FileInputStream fis=new FileInputStream(file);

				if(file.length()!=0)
				{
					while((Rcount= fis.read(bytearr))!= -1)
					{
						digest.update(bytearr,0,Rcount);
					}
				}

				// to get the hash bytes
				byte bytes[]=digest.digest();

				StringBuilder sb = new StringBuilder();

				// Convert into hexadecimal format
				for(int i=0;i<bytes.length;i++)
				{
					sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
				}

				fis.close();
				// Add CheckSum to LinkedList
				if(lobj.contains(sb.toString())) //CheckSum is already there
				{
					if(!file.delete())
					{
						System.out.println("Unable to delete file :"+file.getName());
					}
					else
					{
						System.out.println("File gets deleted :"+file.getName());
						DupFile++;
					}
				}
				else
				{
					lobj.add(sb.toString());
				}

			}
		}
		catch(Exception obj)
		{
			System.out.println("Exception Occured :"+obj);
		}
		finally
		{

		}
		System.out.println("Total Duplicate Files deleted :"+DupFile);
	}
}