package com.hepolite.race.capability;

import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.IValue;
import com.hepolite.race.config.Field;
import com.hepolite.race.config.FieldMap;

public abstract class CapabilityData implements IValue
{
	private final FieldMap fields = new FieldMap();
	public int level = 0;
	public int xp = 0;

	public final Field COOLDOWN = createField("cooldown");

	/**
	 * Creates a new field and tracks it. The field will take a default value of 0.0
	 * 
	 * @param name The identifier of the new field
	 */
	public final Field createField(final String name)
	{
		return createField(name, 0.0);
	}
	/**
	 * Creates a new field and tracks it. The field's value will be the specified value
	 * 
	 * @param name The identifier of the new field
	 * @param def The initial value of the field
	 */
	public final Field createField(final String name, final double def)
	{
		fields.put(name, new Field(def));
		return getField(name);
	}
	/**
	 * Attempts to retrieve the field under the given name. If the field does not exist, a new
	 * (untracked) field with value 0.0 is returned.
	 * 
	 * @param name The name of the field to retrieve
	 * @return The field if it exists, a new field otherwise
	 */
	public final Field getField(final String name)
	{
		return fields.containsKey(name) ? fields.get(name) : new Field();
	}

	// ...

	@Override
	public void save(final IConfig config, final IProperty property)
	{
		config.set(property.child("xp"), xp);
		config.set(property.child("level"), level);
		config.set(property.child("fields"), fields);
	}
	@Override
	public void load(final IConfig config, final IProperty property)
	{
		xp = config.getInt(property.child("xp"));
		level = config.getInt(property.child("level"));
		config.getValue(property.child("fields"), fields);
	}
}
