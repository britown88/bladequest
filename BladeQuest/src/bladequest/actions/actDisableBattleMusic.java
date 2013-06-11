package bladequest.actions;

import bladequest.combat.BattleMusicListener;
import bladequest.serialize.DeserializeFactory;
import bladequest.serialize.Deserializer;
import bladequest.serialize.Serializable;
import bladequest.serialize.Serializer;
import bladequest.world.Global;

public class actDisableBattleMusic extends Action {


	public actDisableBattleMusic() {
	}

	@Override
	public boolean isDone() {
		return true;
	}

	public static class DisabledBattleMusicListenerDeserializer extends DeserializeFactory
	{

		public DisabledBattleMusicListenerDeserializer() {
			super(DisabledBattleMusicListener.tag);
		}

		@Override
		public Object deserialize(Deserializer deserializer) {
			Global.battleMusicListener = new DisabledBattleMusicListener();
			return null;
		}
		
	}
	
	public static class DisabledBattleMusicListener extends Serializable implements BattleMusicListener 
	{
		public final static String tag = "DisabledBattleMusic";
				 
		DisabledBattleMusicListener()
		{
			 super(tag);
			 Global.saveLoader.register(this);
		}
		@Override
		public void onStart() {
			
		}

		@Override
		public void onVictory() {
			
		}

		@Override
		public void onBattleEnd() {
			
		}

		@Override
		public void onRemove() {
			Global.saveLoader.unregister(this);
		}
		@Override
		public void onSerialize(Serializer serializer) {
			//XD LOL OMG
		}
	}
	
	@Override
	public void run() {
		 Global.battleMusicListener = new DisabledBattleMusicListener();
	}

}
