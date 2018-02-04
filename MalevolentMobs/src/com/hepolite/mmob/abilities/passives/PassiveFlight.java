package com.hepolite.mmob.abilities.passives;

import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.hepolite.mmob.abilities.Passive;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;

public class PassiveFlight extends Passive
{
	private int cooldown = 0;
	private int duration = 0;
	
	boolean flying = false;
	
	public PassiveFlight(MalevolentMob mob, float scale)
	{
		super(mob, "Flight", Priority.HIGH, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		cooldown = (int) settings.getScaledValue(alternative, "Cooldown", scale, 0.0f);
		duration = (int) settings.getScaledValue(alternative, "Duration", scale, 0.0f);
	}

	@Override
	public void onTick()
	{
		
	}

	public void onAttacked(EntityDamageEvent event)
	{
		if (event.getCause() == DamageCause.FALL)
			event.setCancelled(true);
	}
}
