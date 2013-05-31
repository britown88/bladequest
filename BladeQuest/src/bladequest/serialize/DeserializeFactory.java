package bladequest.serialize;

import bladequest.world.Global;

public abstract class DeserializeFactory {

	public DeserializeFactory(String tag) {
		Global.saveLoader.registerFactory(tag, this);
	}

	public abstract Object deserialize(Deserializer deserializer);
}
