package com.hepolite.race.capability;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import com.hepolite.api.config.CommonValues.PotionEffectsValue;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.Property;
import com.hepolite.api.damage.Damage;
import com.hepolite.api.damage.DamageAPI;
import com.hepolite.api.damage.DamageType;
import com.hepolite.api.entity.EntityLocater;
import com.hepolite.api.util.Shapes.Cone;
import com.hepolite.race.race.Race;

/**
 * Quick and dirty example of a fully fleshed out skill
 */
public class SkillBite extends Capability
{
	private static final IProperty EFFECTS = new Property("effects");

	private static final String FIELD_DAMAGE = "damage";
	private static final String FIELD_RANGE = "range";
	private static final String FIELD_ANGLE = "angle";

	public SkillBite(final Race race)
	{
		super(race, CapabilityType.SKILL, "Bite", Data.class);
	}

	@Override
	public boolean onCast(final Player player, final CapabilityData data)
	{
		final PotionEffectsValue effects = config.getValue(EFFECTS, new PotionEffectsValue());
		final double damage = data.getField(FIELD_DAMAGE).value;
		final double range = data.getField(FIELD_RANGE).value;
		final double angle = data.getField(FIELD_ANGLE).value;

		final Cone cone = Cone.fromDirection(player.getEyeLocation(), range, angle);
		final EntityLocater<Cone> locater = new EntityLocater<>(player.getWorld(), cone);
		final LivingEntity target = locater.getNearestUnobstructed(LivingEntity.class, player.getEyeLocation(), false,
				player);
		if (DamageAPI.damage(target, player, new Damage(DamageType.PIERCING, damage)))
		{
			effects.apply(target);
			return true;
		}
		return false;
	}

	// ...

	public static final class Data extends CapabilityData
	{
		public Data()
		{
			createField(FIELD_DAMAGE);
			createField(FIELD_RANGE);
			createField(FIELD_ANGLE);
		}
	}
}
