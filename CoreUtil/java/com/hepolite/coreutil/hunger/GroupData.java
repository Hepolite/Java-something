package com.hepolite.coreutil.hunger;

import java.util.HashSet;
import java.util.Set;

import com.hepolite.api.config.CommonValues.SoundValue;
import com.hepolite.api.config.CommonValues.TimeValue;
import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.IValue;
import com.hepolite.api.units.Time;

public class GroupData implements IValue
{
	public float hungerMax = 0.0f;
	public Set<String> forbiddenCategories = new HashSet<>();
	public Set<String> forbiddenIngredients = new HashSet<>();
	public final SoundValue chewingSound = new SoundValue();
	public final SoundValue eatingSound = new SoundValue();

	public float consumptionChange = 0.0f;
	public float consumptionFloating = 0.0f;
	public float consumptionFlying = 0.0f;
	public float consumptionGliding = 0.0f;
	public float consumptionHovering = 0.0f;
	public float consumptionRunning = 0.0f;
	public float consumptionSneaking = 0.0f;
	public float consumptionStanding = 0.0f;
	public float consumptionSwimming = 0.0f;
	public float consumptionWalking = 0.0f;

	public boolean healingEnable = true;
	public Time healingFrequency = Time.fromSeconds(1);
	public double healingAmount = 0.0;
	public float healingStart = 0.0f;
	public float healingCost = 0.0f;

	public boolean starvationEnable = true;
	public Time starvationFrequency = Time.fromSeconds(1);
	public double starvationDamage = 0.0;

	@Override
	public void save(final IConfig config, final IProperty property)
	{}
	@Override
	public void load(final IConfig config, final IProperty property)
	{
		loadHunger(config, property.child("Hunger"));
		loadConsumption(config, property.child("Consumption"));
		loadHealing(config, property.child("Healing"));
		loadStarvation(config, property.child("Starvation"));
		loadMisc(config, property);
	}
	private void loadHunger(final IConfig config, final IProperty property)
	{
		hungerMax = config.getFloat(property.child("max"));

		final IProperty forbidden = property.child("forbidden");
		for (final String category : config.getString(forbidden.child("categories")).split(" "))
			forbiddenCategories.add(category);
		for (final String ingredient : config.getString(forbidden.child("ingredients")).split(" "))
			forbiddenIngredients.add(ingredient);
	}
	private void loadConsumption(final IConfig config, final IProperty property)
	{
		final IProperty state = property.child("state");
		consumptionChange = config.getFloat(property.child("change"));
		consumptionFloating = convert(config.getValue(state.child("floating"), new TimeValue()).time);
		consumptionFlying = convert(config.getValue(state.child("flying"), new TimeValue()).time);
		consumptionGliding = convert(config.getValue(state.child("gliding"), new TimeValue()).time);
		consumptionHovering = convert(config.getValue(state.child("hovering"), new TimeValue()).time);
		consumptionRunning = convert(config.getValue(state.child("running"), new TimeValue()).time);
		consumptionSneaking = convert(config.getValue(state.child("sneaking"), new TimeValue()).time);
		consumptionStanding = convert(config.getValue(state.child("standing"), new TimeValue()).time);
		consumptionSwimming = convert(config.getValue(state.child("swimming"), new TimeValue()).time);
		consumptionWalking = convert(config.getValue(state.child("walking"), new TimeValue()).time);
	}
	private void loadHealing(final IConfig config, final IProperty property)
	{
		healingEnable = config.getBool(property.child("enable"));
		healingFrequency = config.getValue(property.child("frequency"), new TimeValue()).time;
		healingAmount = config.getDouble(property.child("heal"));
		healingStart = config.getFloat(property.child("start"));
		healingCost = config.getFloat(property.child("cost"));
	}
	private void loadStarvation(final IConfig config, final IProperty property)
	{
		starvationEnable = config.getBool(property.child("enable"));
		starvationFrequency = config.getValue(property.child("frequency"), new TimeValue()).time;
		starvationDamage = config.getDouble(property.child("damage"));
	}
	private void loadMisc(final IConfig config, final IProperty property)
	{
		final IProperty sound = property.child("Sound");
		config.getValue(sound.child("chew"), chewingSound);
		config.getValue(sound.child("eat"), eatingSound);
	}

	private float convert(final Time time)
	{
		if (time.asSeconds() <= 0)
			return 0.0f;
		return 2.0f * hungerMax / time.asSeconds();
	}
}
