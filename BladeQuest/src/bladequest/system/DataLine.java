package bladequest.system;

import java.util.*;

public class DataLine 
{
	public String item;
	public List<String> values;
	
	private String line;
	private int index;
	
	public DataLine(String line)
	{
		index = 0;
		values = new ArrayList<String>();
		String s = "";
		this.line = line;
		item = readWord();
		
		index = item.length();
		do
		{
			s = readWord();
			values.add(s);

		}while(s.length() > 0);
		
	}
	
	private String readWord()
	{
		char c;
		boolean openString = false;
		String s = "";
		
		while(true)
		{
			if(index >= line.length())
				return s;
			
			c = line.charAt(index++);
			
			if(c == '\"')
			{
				if(openString)
					return s;
				else
					openString = true;

			}
			else if((c == '\r' || c == '\n' || c == ' ') && !openString)
			{
				if(s.length() > 0)
					return s;
			}
			else
				s += c;	
		}
	}

}
