package bladequest.system;

import java.io.*;

public class FileReader 
{
	private InputStream file;
	
	public FileReader(InputStream file)
	{
		this.file = file;		
	}
	
	public String ReadLine()
	{
		char c;
		int i = 0;
		String s = "";
		while(true)
		{
			try {
				i = file.read();
			} catch (Exception e) {
				// TODO: handle exception
			}
			
			if(i == -1)
				return "";
			
			c = (char)i;				

			if(c == '\r' || c == '\n')
			{
				if(s.length() > 0)
					return s;
			}
			else
				s += c;				
			
		}
		
		
	}

}
