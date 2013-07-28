package bladequest.actions;

import java.util.List;

import bladequest.UI.MsgBox.Options;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class actSecretMessage extends Action
{


	
	public actSecretMessage()
	{
		super();
	}
	
	char toHexChar(int num)
	{
	  if (num < 10) 
		  return (char)('0'+num);
	  
	  return (char)('A' + (num-10));
	}

	String toHexString(int code)
	{
	   char [] out = { toHexChar((code) & 15), 
	                   toHexChar((code >> 4) & 15),
	                   toHexChar((code >> 8) & 15),
	                   toHexChar((code >> 12) & 15),
	                   toHexChar((code >> 16) & 15),
	                   toHexChar((code >> 20) & 15),
	                   toHexChar((code >> 24) & 15),
	                   toHexChar((code >> 28) & 15)};
	   
	   return new String(out);
	}

	String toHexString(int [] code)
	{
	   String out = new String();
	   for (int i : code)
	       out += (toHexString(i));

	   return out;
	}
	
	@Override
	public void run()
	{
		
		char[] levels = new char[3];
		
		List<PlayerCharacter> chars = Global.party.getPartyList(false);
		
		int i = 0;
		for(PlayerCharacter pc : chars)
			levels[i++] = (char)pc.getLevel();
		//will crash if you are cheating and have too many characters			
			
			
		int playTime = Global.playTimer.getSeconds();
		int UID = Global.rand.nextInt(2000000000);
		
		int[] secrethash = {0x0AB9E03F, 0x8A82B83E, 0x9F9CB39A};//SO RANDOM XD
		int[] out = secrethash.clone();
		
		out[0] ^= playTime;
		out[1] ^= UID;
		
		for(i = 0; i < 3; ++i)
			out[2] ^= levels[i] << (i*8);		
		
		Global.showMessage(toHexString(out).substring(0, 22),Options.None);

		
	}
	
	@Override
	public boolean isDone()
	{
		return Global.worldMsgBox.Closed();
	}
}
