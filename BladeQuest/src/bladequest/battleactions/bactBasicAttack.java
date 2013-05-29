package bladequest.battleactions;

import java.util.List;

import bladequest.bladescript.ParserException;
import bladequest.bladescript.ScriptVar;
import bladequest.combat.BattleCalc.AccuracyType;
import bladequest.combat.BattleEventBuilder;
import bladequest.world.DamageTypes;
import bladequest.world.Global;
import bladequest.world.PlayerCharacter;

public class bactBasicAttack extends DelegatingAction {

	float power;
	DamageTypes type;
	float speedFactor;
	PlayerCharacter target;
	ScriptVar hitsIf;
	
	public bactBasicAttack(float power, DamageTypes type, float speedFactor) {
		this.power = power;
		this.type = type;
		this.speedFactor = speedFactor;	
	}
	public bactBasicAttack(float power, DamageTypes type, float speedFactor, ScriptVar hitsIf) {
		this.power = power;
		this.type = type;
		this.speedFactor = speedFactor;	
		this.hitsIf = hitsIf;
	}
	
	public void buildEvents(BattleEventBuilder builder)
	{
		PlayerCharacter attacker = builder.getSource();
		List<PlayerCharacter> targets = Global.battle.getTargetable(attacker, builder.getTargets());
		if (!targets.isEmpty())
		{
			target = targets.get(0);
			if (hitsIf != null)
			{
				try {
					if (hitsIf.apply(ScriptVar.toScriptVar(target)).getBoolean())
					{
						BattleActionPatterns.BuildSwordSlashWithAccuracy(builder, power, type, speedFactor, AccuracyType.NoMiss, 0.0f);
					}
					else
					{
						BattleActionPatterns.BuildSwordSlashWithAccuracy(builder, power, type, speedFactor, AccuracyType.ReplaceEvade, 100.0f);
					}
				} catch (ParserException e) {
					e.printStackTrace();
				}				
			}
			else
			{
				BattleActionPatterns.BuildSwordSlash(builder, power, type, speedFactor);
			}
			builder.addEventObject(new bactRunChildren(this).addDependency(builder.getLast()));
		}
	}
	protected BattleEventBuilder getAdaptedBuilder(BattleEventBuilder builder)
	{
		if (target == null)
		{
			return super.getAdaptedBuilder(builder);
		}
		else
		{
			return changeTargets(super.getAdaptedBuilder(builder), target);
		}
	}	
}
