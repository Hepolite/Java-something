package com.hepolite.mmob.abilities.passives;

import org.bukkit.entity.Creeper;

import com.hepolite.mmob.abilities.Passive;
import com.hepolite.mmob.mobs.MalevolentMob;
import com.hepolite.mmob.settings.Settings;

public class PassiveSupercharged extends Passive
{
	private int radius = 0;
	private int fuse = 0;
	
	public PassiveSupercharged(MalevolentMob mob, float scale)
	{
		super(mob, "Supercharged", Priority.NORMAL, scale);
	}

	@Override
	public void loadFromConfig(Settings settings, Settings alternative)
	{
		radius = (int) settings.getScaledValue(alternative, "Radius", scale, 1.0f);
		fuse = (int) settings.getScaledValue(alternative, "Fuse", scale, 1.0f);
	}

	@Override
	public void onSpawn()
	{
		if (!(mob.getEntity() instanceof Creeper))
			return;
		Creeper creeper = (Creeper) mob.getEntity();
		creeper.setPowered(true);
		creeper.setExplosionRadius(Math.max(1, radius));
		creeper.setMaxFuseTicks(Math.max(1, fuse));
	}
}
