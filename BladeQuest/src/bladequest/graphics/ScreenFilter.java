package bladequest.graphics;

import java.util.ArrayList;
import java.util.List;

import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import bladequest.serialize.DeserializeFactory;
import bladequest.serialize.Deserializer;
import bladequest.serialize.Serializable;
import bladequest.serialize.Serializer;
import bladequest.world.Global;

public class ScreenFilter extends Serializable {
	
	private class FilterSet
	{
		FilterSet()
		{
			filters = new float[16][];
			activeFilters = 0;			
		}
		FilterSet(FilterSet rhs)
		{
			activeFilters = rhs.activeFilters;
			filters = new float[16][];
			for (int i = 0; i < activeFilters; ++i)
			{
				filters[i] = rhs.filters[i].clone();
			}
		}
		
		float[][] filters;
		int activeFilters;	
		void serialize(Serializer serializer)
		{
			serializer.write(activeFilters);
			for (int i = 0; i < activeFilters; ++i)
			{
				for (int j = 0; j < 20; ++j)
				{
					serializer.write(filters[i][j]);
				}
			}
		}
	}
	
	
	private static final String tag = "ScreenFilter";
	
	private class ScreenFilterDeserializer extends DeserializeFactory
	{

		ScreenFilterDeserializer()
		{
			super(tag);
		}
		FilterSet deserializeFilterSet(Deserializer deserializer)
		{
			FilterSet out = new FilterSet();
			out.activeFilters = deserializer.readInt();
			for (int i = 0; i < out.activeFilters; ++i)
			{
				out.filters[i] = new float[20];
				for (int j = 0; j < 20; ++j)
				{
					out.filters[i][j] = deserializer.readFloat();	
				}
			}
			return out;
		}
		@Override
		public Object deserialize(Deserializer deserializer) {
			activeFilterSet = deserializeFilterSet(deserializer);
			
			int storedFilters = deserializer.readInt();
			
			storedFilterSets.clear();
			
			for (int i = 0; i < storedFilters; ++i)
			{
				storedFilterSets.add(deserializeFilterSet(deserializer));
			}
			current = calculateFilter();
			if (Global.map != null && Global.map.isLoaded())
			{
				Global.map.invalidateTiles();	
			}
			return null;
		}
		
	}
	
	FilterSet activeFilterSet;
	
	List<FilterSet> storedFilterSets;
	
	float[] current;
	
	Paint p;
	
	private ScreenFilter() {
		super(tag);
		activeFilterSet = new FilterSet();
		storedFilterSets = new ArrayList<FilterSet>();
		Global.saveLoader.registerFactory(tag, new ScreenFilterDeserializer()); 
	}
	
	private static ScreenFilter filter;
	public static ScreenFilter instance() 
	{
		if (filter == null)
		{
			filter = new ScreenFilter();
			Global.saveLoader.register(filter);
		}
		return filter;
	}
    public Paint defaultPaint()
    {
    	return p;
    }
    public void save()
    {
    	storedFilterSets.add(new FilterSet(activeFilterSet));
    }
    public void clear()
    {
    	activeFilterSet.activeFilters = 0;
    	p = null;
    }
    public void restore()
    {
    	activeFilterSet = storedFilterSets.get(storedFilterSets.size()-1);
    	storedFilterSets.remove(storedFilterSets.size()-1);
    	current = calculateFilter();
    }
	public void pushFilter(float[] filter ) //4x5 matrix
	{
		activeFilterSet.filters[activeFilterSet.activeFilters++] = filter.clone();
		current = calculateFilter();
	}
	public void popFilter()
	{
		--activeFilterSet.activeFilters;
		if (activeFilterSet.activeFilters < 0) activeFilterSet.activeFilters = 0;
		current = calculateFilter();
	}
	public boolean isFiltering()
	{
		return activeFilterSet.activeFilters > 0;
	}
	public float[] currentFilter()
	{
		return current;
	}
	public static float[] darknessFilter(float t)
	{
		final float rg = 0.6f;
		final float b = 0.97f;
		//L =  0.2126 R + 0.7152 G + 0.0722 B
		return new float[] {
				(t * rg) * 0.2126f + (1.0f-t), (t * rg) * 0.7152f,              (t * rg) * 0.0722f,            0.0f, 0.0f,
				(t * rg) * 0.2126f,            (t * rg) * 0.7152f  + (1.0f-t),  (t * rg) * 0.0722f,            0.0f, 0.0f,
				(t * b)  * 0.2126f,            (t * b)  * 0.7152f,              (t * b)  * 0.0722f + (1.0f-t), 0.0f, 0.0f,
				
				
				0.0f,    0.0f,    0.0f,    1.0f, 0.0f 
		};
	}
	
	private float[] calculateFilter()
	{
		if (activeFilterSet.activeFilters == 0)
		{
			p = new Paint();
			return null;
		}
		float [] out = new float[20];
		float [] next = new float[20];
		
		float [] temp;
		for (int i = 0; i < 20; ++i) out[i] = activeFilterSet.filters[0][i];
		
		
		//row * column... pretend homogenous 5x5, 0,0,0,0,1
		for (int j = 1; j < activeFilterSet.activeFilters; ++j)
		{
			for (int y = 0; y < 4; ++y)
			{
				for (int x = 0; x < 5; ++x)
				{
					next[x + y * 5] = 0.0f;
					float[] nextFilter = activeFilterSet.filters[j];
					for (int i = 0; i < 4; ++i)
					{
						next[x + y * 5] += out[i + y * 5] * nextFilter[x + i * 5];
					}
					//homogenous component
					if (x == 4)
					{
						next[x + y * 5] += out[4 + y * 5];
					}
				}
			}
			temp = out;
			out = next;
			next = temp;
		}
		
		p = new Paint();
		p.setColorFilter( new ColorMatrixColorFilter(out));
		//composited filters!
		
		return out;
	}
	@Override
	public void onSerialize(Serializer serializer) {
		activeFilterSet.serialize(serializer);
		
		serializer.write(storedFilterSets.size());
		for (int i = 0; i < storedFilterSets.size(); ++i)
		{
			storedFilterSets.get(i).serialize(serializer);
		}
		
	}
}
