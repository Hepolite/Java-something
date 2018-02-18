package com.hepolite.api.config;

import java.util.Random;

import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hepolite.api.units.Time;
import com.hepolite.coreutil.CoreUtilPlugin;

public final class CommonValues
{
	public static final class TimeValue implements IValue
	{
		public Time time;

		public TimeValue()
		{}
		public TimeValue(final Time time)
		{
			this.time = time;
		}

		@Override
		public void save(final IConfig config, final IProperty property)
		{
			config.set(property, time.toString());
		}
		@Override
		public void load(final IConfig config, final IProperty property)
		{
			time = Time.fromString(config.getString(property));
		}
	}

	public static final class PotionEffectTypeValue implements IValue
	{
		public PotionEffectType type;

		public PotionEffectTypeValue()
		{}
		public PotionEffectTypeValue(final PotionEffectType type)
		{
			this.type = type;
		}

		@Override
		public void save(final IConfig config, final IProperty property)
		{
			config.set(property, type.getName());
		}
		@Override
		public void load(final IConfig config, final IProperty property)
		{
			type = PotionEffectType.getByName(config.getString(property).toUpperCase());
			if (type == null)
				CoreUtilPlugin.WARN("Failed loading PotionEffectType: " + property.getPath());
		}

		@Override
		public String toString()
		{
			return type.getName();
		}
	}

	public static final class PotionEffectValue implements IValue
	{
		private static final Random random = new Random();

		public PotionEffectType type;
		public int amplifier;
		public Time duration;
		public boolean ambient = false;
		public boolean particles = true;
		public float chance = 1.0f;

		/**
		 * Applies the potion effect stored in this value to the specified entity
		 * 
		 * @param entity The entity to apply the effect to
		 */
		void apply(final LivingEntity entity)
		{
			if (random.nextFloat() < chance)
			{
				entity.addPotionEffect(new PotionEffect(type, amplifier > 0 ? amplifier - 1 : amplifier,
						(int) duration.asTicks(), ambient, particles));
			}
		}

		@Override
		public void save(final IConfig config, final IProperty property)
		{
			config.set(property.child("type"), type.getName());
			config.set(property.child("duration"), duration.toString());
			config.set(property.child("amplifier"), amplifier);
			config.set(property.child("ambient"), ambient);
			config.set(property.child("particles"), particles);
			config.set(property.child("chance"), chance);
		}
		@Override
		public void load(final IConfig config, final IProperty property)
		{
			type = config.getValue(property.child("type"), new PotionEffectTypeValue()).type;
			duration = config.getValue(property.child("duration"), new TimeValue()).time;
			amplifier = config.getInt(property.child("amplifier"));
			ambient = config.getBool(property.child("ambient"));
			particles = config.getBool(property.child("particles"));
			chance = config.getFloat(property.child("chance"));
		}

		@Override
		public String toString()
		{
			return String.format("Type: %s, amplifier: %d, duration: %s", type.getName(), amplifier,
					duration.toString());
		}
	}
}
