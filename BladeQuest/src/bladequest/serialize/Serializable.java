package bladequest.serialize;


public abstract class Serializable {
	String tag;
	public Serializable(String tag)
	{
		this.tag = tag;
	}
	
	//implement me plz
	public abstract void onSerialize(Serializer serializer);
	
	
	public final void serialize(Serializer serializer)
	{
		serializer.startObject(tag);
		onSerialize(serializer);
		serializer.endObject();
	}
	
}
