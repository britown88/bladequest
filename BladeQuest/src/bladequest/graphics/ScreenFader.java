package bladequest.graphics;

import bladequest.serialize.DeserializeFactory;
import bladequest.serialize.Deserializer;
import bladequest.serialize.Serializable;
import bladequest.serialize.Serializer;
import bladequest.world.Global;

public class ScreenFader extends Serializable
{
	private static class ColorARGB  extends Serializable
	{
		private static final String ColorTag = "ColorARGB";
		ColorARGB() 
		{
			super(ColorTag);
			a = r = g = b = 0;
		}
		ColorARGB(ColorARGB rhs) 
		{
			super(ColorTag);
			this.a = rhs.a;
			this.r = rhs.r;
			this.g = rhs.g;
			this.b = rhs.b;			
		}
		ColorARGB(int a, int r, int g, int b) 
		{
			super(ColorTag);
			this.a = a;
			this.r = r;
			this.g = g;
			this.b = b;
		}
		void set (int a,int r,int g, int b)
		{
			this.a = a;
			this.r = r;
			this.g = g;
			this.b = b;
		}
		ColorARGB interpolate(ColorARGB rhs, float t)
		{
			return new ColorARGB(BattleAnim.linearInterpolation(a, rhs.a, t),
			BattleAnim.linearInterpolation(r, rhs.r, t),
			BattleAnim.linearInterpolation(g, rhs.g, t),
			BattleAnim.linearInterpolation(b, rhs.b, t));
		}

		static class ColorDeserialize extends DeserializeFactory{

			public  ColorDeserialize() {
				super(ColorTag);
			}

			@Override
			public Object deserialize(Deserializer deserializer) {
				int a = deserializer.readInt();
				int r = deserializer.readInt();
				int g = deserializer.readInt();
				int b = deserializer.readInt();
				
				return new ColorARGB(a,r,g,b);
			}
			
		}
		@Override
		public void onSerialize(Serializer serializer)
		{
			serializer.write(a);
			serializer.write(r);
			serializer.write(g);
			serializer.write(b);
		}
		
		int a,r,g,b;
	}
	
	ColorARGB from, to, current;
	private float fadeTime;
	private long startTime;
	private boolean fading, fadeIn, done, flashing;
	
	private static final String ScreenFaderTag = "screenfader";
	
	private class ScreenDeserialize extends DeserializeFactory{

		public ScreenDeserialize() {
			super(ScreenFaderTag);
		}

		@Override
		public Object deserialize(Deserializer deserializer) {
			ColorARGB to = (ColorARGB) new ColorARGB.ColorDeserialize().deserialize(deserializer);

			Global.screenFader.setFadeToColor(to.a, to.r, to.g, to.b);
			
			return Global.screenFader;
		}
		
	}
	
	public ScreenFader()
	{
		super(ScreenFaderTag);
		fading = false;
		fadeIn = true;
		done = true;
		new ScreenDeserialize();
		new ColorARGB.ColorDeserialize();
		from = new ColorARGB();
		to = new ColorARGB();
		current = new ColorARGB();
	}
	
	public void setFadeToColor(int a, int r, int g, int b)
	{
		to.set(a, r, g, b);
	}
	
	public void setFadeColor(int a, int r, int g, int b)
	{
		from.set(a, r, g, b);
	}
	
	public void fadeIn(float fadeTime)
	{
		fading = true;
		this.fadeTime = fadeTime;
		fadeIn = true;
		done = false;
		startTime = System.currentTimeMillis();
	}
	
	public void fadeOut(float fadeTime)
	{
		fading = true;
		this.fadeTime = fadeTime;
		fadeIn = false;
		done = false;
		startTime = System.currentTimeMillis();
	}
	
	public void flash(float flashLength)
	{
		flashing = true;
		fadeOut(flashLength/2);
	}
	
	public boolean isDone() { return done; }
	public boolean isFadedOut() { return done && !fadeIn; }
	public boolean isFadedIn() { return done && fadeIn; }
	public boolean fadingOut() { return fading && !fadeIn;}
	
	public void setFaded()
	{
		current = from;
		fading = false;
		done = true;
	}
	
	public void clear()
	{
		flashing = false;
		current = to;
		fading = false;
		done = true;
	}
	
	public void update()
	{
		if(fading)
		{
			float delta = (System.currentTimeMillis() - startTime)/(fadeTime*1000.0f);
						
			if(delta >= 1.0f)
			{
				if(flashing)
				{
					if(fadeIn)
					{
						flashing = false;
						fading = false;
						done = true;		
					}
					else
					{
						fadeIn(fadeTime);
						delta = 0.0f;
					}
				}
				else
				{
					fading = false;
					done = true;	
					current = fadeIn ? to : from;
				}
			}
			
			if(fading)
			{
				float t = delta;
				if(!fadeIn)
					t = 1.0f-delta;
				
				current = from.interpolate(to, t);
			}
			
			
		}
	}
	
	public void render()
	{
		if (current.a > 0)
		{
			Global.renderer.drawColor(current.a, current.r, current.g, current.b);			
		}
	}

	@Override
	public void onSerialize(Serializer serializer) {
		to.onSerialize(serializer);
	}

}
