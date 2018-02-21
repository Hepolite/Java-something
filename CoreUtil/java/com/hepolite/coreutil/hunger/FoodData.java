package com.hepolite.coreutil.hunger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.bukkit.inventory.ItemStack;

import com.hepolite.api.config.CommonValues.ItemStackValue;
import com.hepolite.api.config.CommonValues.PotionEffectValue;
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
	public final Collection<PotionEffectValue> effects = new ArrayList<>();
	public float food = 0.0f;
	public float ratio = 0.0f;
	public boolean alwaysConsumable = false;
	public Time time;
	public ItemStack result;

	@Override
	public void save(final IConfig config, final IProperty property)
	{}
	@Override
	public void load(final IConfig config, final IProperty property)
	{
		name = property.getName();
		food = config.getFloat(property.child("food"));
		ratio = config.getFloat(property.child("ratio"));
		alwaysConsumable = config.getBool(property.child("alwaysConsumable"));
		time = config.getValue(property.child("time"), new TimeValue()).time;
		result = config.getValue(property.child("result"), new ItemStackValue()).item;

		for (final String category : config.getString(property.child("categories")).split(" "))
			categories.add(category);
		for (final String ingredient : config.getString(property.child("ingredients")).split(" "))
			ingredients.add(ingredient);
		for (final IProperty effectProperty : config.getProperties(property.child("effects")))
			effects.add(config.getValue(effectProperty, new PotionEffectValue()));
	}
}
