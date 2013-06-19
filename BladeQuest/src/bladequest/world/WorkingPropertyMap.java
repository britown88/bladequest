package bladequest.world;

import java.util.AbstractMap;
import java.util.HashMap;

import bladequest.serialize.Deserializer;
import bladequest.serialize.Serializable;
import bladequest.serialize.Serializer;

//A property map for data currently running in the game.

public class WorkingPropertyMap implements PropertyMap {

	private interface NodeData
	{
		Deserializer getData();
		byte[] getRaw();
		void eraseData();
	}
	
	private class BufferDeserializer implements Deserializer
	{
		int position = 0;
		byte[] buffer;
		BufferDeserializer(byte[] buffer)
		{
			this.buffer = buffer;
		}

		@Override
		public Object readObject() {
			return null;
		}

		public char readChar()
		{
			return (char) buffer[position++];
		}
		@Override
		public String readString() {
			StringBuilder builder = new StringBuilder();
			for (;;)
			{
				char c = readChar();
				if (c == 0) break;
				builder.append(c);
			}
			return builder.toString();
		}

		@Override
		public int readInt() {
			int out = 0;
			for (int i = 0; i < 4; ++i)
			{
				out += readChar() << (8 * i);
			}
			return out;
		}

		@Override
		public float readFloat() {
			return Float.intBitsToFloat(readInt());
		}
	}
	
	private class RAMNode implements NodeData
	{

		byte[] buffer;
		RAMNode(byte[] buffer)
		{
			this.buffer = buffer;
		}
		@Override
		public byte[] getRaw()
		{
			return buffer;
		}
		@Override
		public Deserializer getData() {
			return new BufferDeserializer(buffer);
		}

		@Override
		public void eraseData() {
			
		}
	}
//add "DiskNode" here.
//the jist of it is that we'll have to save a "working" file which bladequest cleans up all of on startup.
//post 2.1.	
	
	private class ByteBufferSerializer implements Serializer
	{
		int capacity = 0;
		int size = 0;
		byte[] buffer;
		
		void grow()
		{
			if (capacity == 0)
			{
				buffer = new byte[8];
				capacity = 8;
			}
			else
			{
				byte[] newBuf = new byte[capacity*=2];
				for (int i = 0; i < size; ++i) newBuf[i] = buffer[i];
				buffer = newBuf;
			}
		}
		void write(char data)
		{
			if (size + 1 >= capacity)
			{
				grow();
			}
			buffer[size++] = (byte)data;
		}
		
		@Override
		public void startObject(String tag) {
			write(tag);
		}

		@Override
		public void write(String data) {
			for (int i = 0; i < data.length(); ++i)
			{
				write(data.charAt(i));
			}
			write((char)0);
		}

		@Override
		public void write(int data) {
			if (size + 4 >= capacity)
			{
				grow();
			}
			for (int i = 0; i < 4; ++i)
			{
				buffer[size++] = (byte)(data&255);
				data >>= 8;
			}
		}

		@Override
		public void write(float data) {
			int bits = Float.floatToIntBits(data);
			write(bits);
		}

		@Override
		public void endObject() {
		}
		byte[] get()
		{
			if (buffer == null) return buffer;
			if (size != capacity)
			{
				byte[] newBuf = new byte[size];
				for (int i = 0; i < size; ++i) newBuf[i] = buffer[i];
				return newBuf;
			}
			return buffer;
		}
	}
	
	byte[] toByteBuffer(Serializable serializable)
	{
		ByteBufferSerializer serializer = new ByteBufferSerializer();
		serializable.onSerialize(serializer);
		return serializer.get();
	}
	
	void UnlinkNode(WorkingPropertyMap node)
	{
		if (node.prev != null)
		{
			if (node.prev.next != node)
			{
				node.prev.child = node.next;
			}
			else
			{
				node.prev.next = node.next;
			}
		}
		if (node.next != null)
		{
			node.next.prev = node.prev;
		}
		node.data.eraseData();
	}
	
	String nodekey;
	AbstractMap<String, WorkingPropertyMap> children;
	NodeData data;
	WorkingPropertyMap next,prev, child;
	
	public WorkingPropertyMap() {
		nodekey = "root";
		children = new HashMap<String, WorkingPropertyMap>();
		data = null;
		next = prev = child = null;
	}

	public String key() {
		return nodekey;
	}

	@Override
	public Deserializer get() {
		if (data == null) return null;
		return data.getData();
	}
	
	@Override
	public byte[] getRaw() {
		if (data == null) return null;
		return data.getRaw();
	}	

	@Override
	public void set(Serializable serializable) {
		if (data != null) data.eraseData();
		byte[] buffer = toByteBuffer(serializable);
//		if (buffer.length > 256) //store it to disk
//		{
//			data = new DiskNode(buffer);
//		}
		//else
		//{
		data = new RAMNode(buffer);
		//}
	}

	@Override
	public PropertyMap getChild(String child) {
		WorkingPropertyMap out = children.get(child);
		if (out == null)
		{
			out = new WorkingPropertyMap();
			children.put(child, out);
		}
		out.next = this.child;
		if (this.child != null)
		{
			this.child.prev = out;
		}
		out.prev = this;
		return out;
	}

	@Override
	public void remove(String child) {
		WorkingPropertyMap n = children.get(child);
		if (n != null)
		{
			children.remove(child);
			UnlinkNode(n);
		}
	}

	@Override
	public PropertyMap getNext() {
		return next;
	}

	@Override
	public PropertyMap getFirstChild() {
		return child;
	}

}
