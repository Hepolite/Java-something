package com.hepolite.api.attribute;

import com.hepolite.api.damage.DamageType;
import com.hepolite.api.damage.DamageVariant;

public final class AttributeType
{
	/// @formatter:off
	
	// Generic attributes
	public static final String DAMAGE_ATTACK_ALL = "damage_attack_all";
	public static final String DAMAGE_ATTACK(final DamageVariant v) { return "damage_attack_v" + v.toString(); }
	public static final String DAMAGE_ATTACK(final DamageType t) { return "damage_attack_t" + t.toString(); }
	public static final String DAMAGE_DEFENCE_ALL = "damage_defence_all";
	public static final String DAMAGE_DEFENCE(final DamageVariant v) { return "damage_defence_v" + v.toString(); }
	public static final String DAMAGE_DEFENCE(final DamageType t) { return "damage_defence_t" + t.toString(); }
	
	// Player attributes
	public static final String HEALTH = "health";
	public static final String HEALTH_MAX = "health_max";
	public static final String HUNGER_MAX = "hunger_max";
	public static final String SPEED_WALK = "speed_walk";
	public static final String SPEED_FLY = "speed_fly";
	
	/// @formatter:on
}
