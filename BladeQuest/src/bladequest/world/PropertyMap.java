package bladequest.world;

import bladequest.serialize.Deserializer;
import bladequest.serialize.Serializable;

public interface PropertyMap {

	//this entry
	String key();
	Deserializer get();
	byte[] getRaw();
	void set(Serializable serializable);
	
	//child entries
	PropertyMap getChild(String child);
	void remove(String child);
	
	//iteration
	PropertyMap getNext();
	PropertyMap getFirstChild();
}