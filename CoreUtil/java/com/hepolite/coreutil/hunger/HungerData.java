package com.hepolite.coreutil.hunger;

import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.IValue;

public class HungerData implements IValue
{
	public String group = "Default";
	public float hunger = 0.0f;
	public float saturation = 0.0f;
	public float consumption = 0.0f;

	@Override
	public void save(final IConfig config, final IProperty property)
	{
		config.set(property.child("group"), group);
		config.set(property.child("hunger"), hunger);
		config.set(property.child("saturation"), saturation);
		config.set(property.child("consumption"), consumption);
	}
	@Override
	public void load(final IConfig config, final IProperty property)
	{
		group = config.getString(property.child("group"));
		hunger = config.getFloat(property.child("hunger"));
		saturation = config.getFloat(property.child("saturation"));
		consumption = config.getFloat(property.child("consumption"));
	}
}
