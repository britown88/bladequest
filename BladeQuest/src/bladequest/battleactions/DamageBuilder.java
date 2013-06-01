package bladequest.battleactions;

import java.util.List;

import bladequest.combat.BattleCalc;
import bladequest.combat.DamageComponent;
import bladequest.world.DamageTypes;
import bladequest.world.PlayerCharacter;

public interface DamageBuilder {

	int getDamage();
	void setDamage(int damage);
	
	DamageTypes getDamageType();

	PlayerCharacter getAttacker();
	PlayerCharacter getDefender();
	
	BattleCalc.DamageReturnType attackType();
	void setAttackType(BattleCalc.DamageReturnType type);
	List<DamageComponent> getDamageComponents();
}
