package bladequest.battleactions;

import bladequest.combat.BattleEventBuilder;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactShowDamagedFaces extends DelegatingAction {

	int time;
	public bactShowDamagedFaces(int time) {
		this.time = time;
	}

	@Override
	protected void buildEvents(BattleEventBuilder builder) {
		builder.addEventObject(new bactWait(time));
		
		for (PlayerCharacter t : builder.getTargets())
		{
			//randomly show a face every 500ms.
			int faceCount = time/500;
			
			for (int i = 0; i < faceCount; ++i)
			{
				int damagedTime = Global.rand.nextInt(time);
				
				builder.addEventObject(new bactWait(damagedTime));
				builder.addEventObject(new TargetedAction(t)
				{
					@Override
					protected void buildEvents(BattleEventBuilder builder) {
						// TODO Auto-generated method stub
						builder.addEventObject(new BattleAction(){
							public State run(BattleEventBuilder builder)
							{
								if (!BattleAction.getTarget(builder).isEnemy())
								{
									BattleAction.getTarget(builder).showDamaged();
								}
								return State.Finished;
							}
						});
					}
				}.addDependency(builder.getLast()));
				
				builder.addEventObject(new bactWait(damagedTime + 450));
				builder.addEventObject(new TargetedAction(t)
				{
					@Override
					protected void buildEvents(BattleEventBuilder builder) {
						// TODO Auto-generated method stub
						builder.addEventObject(new BattleAction(){
							public State run(BattleEventBuilder builder)
							{
								if (!BattleAction.getTarget(builder).isEnemy())
								{
									BattleAction.getTarget(builder).clearDamaged();
								}
								return State.Finished;
							}
						});
					}
				}.addDependency(builder.getLast()));				
			}	
		}
		
		
	}

}
