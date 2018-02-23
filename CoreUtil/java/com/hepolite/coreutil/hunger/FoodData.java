package com.hepolite.coreutil.hunger;

import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

import com.hepolite.api.config.CommonValues.ItemStacksValue;
import com.hepolite.api.config.CommonValues.PotionEffectsValue;
import com.hepolite.api.config.CommonValues.TimeValue;
import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.IValue;
import com.hepolite.api.units.Time;

public final class FoodData implements IValue
{
	public boolean resolved = false;
	public String name = "";
	public final Set<String> categories = new TreeSet<>();
	public final Set<String> ingredients = new TreeSet<>();
	public boolean alwaysConsumable = false;

	public float food = 0.0f;
	public float ratio = 0.0f;
	public Time time;
	public PotionEffectsValue effects = new PotionEffectsValue();
	public ItemStacksValue results = new ItemStacksValue();

	@Override
	public void save(final IConfig config, final IProperty property)
	{}
	@Override
	public void load(final IConfig config, final IProperty property)
	{
		name = property.getName();
		alwaysConsumable = config.getBool(property.child("alwaysConsumable"));
		categories.addAll(Arrays.asList(config.getString(property.child("categories")).split(" ")));
		ingredients.addAll(Arrays.asList(config.getString(property.child("ingredients")).split(" ")));

		food = config.getFloat(property.child("food"));
		ratio = config.getFloat(property.child("ratio"));
		time = config.getValue(property.child("time"), new TimeValue()).time;
		results = config.getValue(property.child("results"), new ItemStacksValue());
		effects = config.getValue(property.child("effects"), new PotionEffectsValue());
	}
}
