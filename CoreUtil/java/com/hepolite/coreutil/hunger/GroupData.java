package com.hepolite.coreutil.hunger;

import com.hepolite.api.config.CommonValues.TimeValue;
import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.IValue;
import com.hepolite.api.units.Time;

public class GroupData implements IValue
{
	public float hungerMax = 0.0f;

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

	@Override
	public void save(final IConfig config, final IProperty property)
	{}
	@Override
	public void load(final IConfig config, final IProperty property)
	{
		loadHunger(config, property.child("Hunger"));
		loadConsumption(config, property.child("Consumption"));
	}
	private void loadHunger(final IConfig config, final IProperty property)
	{
		hungerMax = config.getFloat(property.child("max"));
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

	private float convert(final Time time)
	{
		if (time.asSeconds() <= 0)
			return 0.0f;
		return 2.0f * hungerMax / time.asSeconds();
	}
}
