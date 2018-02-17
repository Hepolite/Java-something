package com.hepolite.coreutil.hunger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.hepolite.api.config.CommonValues.PotionEffectValue;
import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.IValue;

public final class FoodData implements IValue
{
	public String name = "";
	public final Set<String> categories = new HashSet<>();
	public final Set<String> ingredients = new HashSet<>();
	public final Collection<PotionEffectValue> effects = new ArrayList<>();
	public float food = 0.0f;
	public float ratio = 0.0f;
	public boolean alwaysConsumable = false;

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

		for (final String category : config.getString(property.child("categories")).split(" "))
			categories.add(category);
		for (final String ingredient : config.getString(property.child("ingredients")).split(" "))
			ingredients.add(ingredient);
		for (final IProperty effectProperty : config.getProperties(property.child("effects")))
			effects.add(config.getValue(effectProperty, new PotionEffectValue()));
	}
}
