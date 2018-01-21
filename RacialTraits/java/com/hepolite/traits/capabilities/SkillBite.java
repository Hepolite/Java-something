package com.hepolite.traits.capabilities;

import org.bukkit.entity.Player;

import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.Property;

public class SkillBite extends Capability
{
	private static final IProperty MAX_TARGETS = new Property("max_targets");
	private static final IProperty EFFECTS = new Property("effects");

	private static final String FIELD_DAMAGE = "damage";
	private static final String FIELD_RANGE = "range";

	public SkillBite()
	{
		super("Bite");
	}

	@Override
	public boolean onCast(final Player player, final CapabilityData data)
	{
		final int maxTargets = config.getInt(MAX_TARGETS);
		// final IValue effects = config.getValue(EFFECTS, new IValue());
		final double damage = data.getField(FIELD_DAMAGE).value;
		final double range = data.getField(FIELD_RANGE).value;

		return false;
	}

	// ...

	public static final class Data extends CapabilityData
	{
		public Data()
		{
			createField(FIELD_DAMAGE);
			createField(FIELD_RANGE);
		}
	}
}
