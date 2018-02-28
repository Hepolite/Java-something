package com.hepolite.race.race;

import com.hepolite.race.capability.SkillBite;

/**
 * Quick and dirty example of a fully fleshed out race
 */
public class RaceWolf extends Race
{
	public RaceWolf()
	{
		super("Wolf");
		addCapability(new SkillBite(this));
	}
}
