package bladequest.combat;

public interface BattleMusicListener {

   void onStart();
   void onVictory();
   void onBattleEnd();
   void onBattleUnload();
	
   void onRemove();
}
