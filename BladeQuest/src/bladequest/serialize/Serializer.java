package bladequest.serialize;

public interface Serializer {
	void startObject(String tag);
	
	void write(String data);
	void write(int data);
	void write(float data);
	
	void endObject();
}
