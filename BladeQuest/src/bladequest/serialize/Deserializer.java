package bladequest.serialize;

public interface Deserializer {
	Object readObject();
	String readString();
	int readInt();
	float readFloat();
}
