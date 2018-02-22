package com.hepolite.api.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.hepolite.api.damage.DamageType;
import com.hepolite.api.nbt.NBTAPI;
import com.hepolite.api.units.Time;
import com.hepolite.coreutil.CoreUtilPlugin;

public final class CommonValues
{
	/**
	 * Allows a single item stack to be loaded and stored to a config. The item will be loaded with
	 * type, meta, amount, name, lore and NBT data.
	 */
	public static final class ItemStackValue implements IValue
	{
		public ItemStack item;

		@Override
		public void save(final IConfig config, final IProperty property)
		{
			if (item == null)
				return;

			config.set(property.child("type"), item.getType().toString().toLowerCase());
			if (item.getDurability() != 0)
				config.set(property.child("meta"), item.getDurability());
			if (item.getAmount() != 1)
				config.set(property.child("amount"), item.getAmount());
			if (item.hasItemMeta())
			{
				final ItemMeta meta = item.getItemMeta();
				if (meta.hasDisplayName())
					config.set(property.child("name"), meta.getDisplayName());
				if (meta.hasLore())
					config.set(property.child("lore"), meta.getLore());
			}
			// TODO: Save NBTAPI.getTag(item);
		}
		@Override
		public void load(final IConfig config, final IProperty property)
		{
			item = null;
			final String type = config.getString(property.child("type"));
			final short meta = config.getShort(property.child("meta"));
			final int amount = config.getInt(property.child("amount"), 1);
			final String name = config.getString(property.child("name"));
			final List<String> lore = config.getStrings(property.child("lore"));
			if (type.isEmpty())
				return;

			try
			{
				item = NBTAPI.getItemStack(Material.valueOf(type.toUpperCase()), amount, meta);

				if (!name.isEmpty())
					item.getItemMeta().setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
				if (!lore.isEmpty())
				{
					final List<String> processedLore = new ArrayList<>();
					for (final String string : lore)
						processedLore.add(ChatColor.translateAlternateColorCodes('&', string));
					item.getItemMeta().setLore(processedLore);
				}
				// TODO: NBTAPI.setTag(item, tag)
			}
			catch (final Exception e)
			{
				CoreUtilPlugin.WARN("Failed loading ItemStack: " + type + " under " + property.getPath());
			}
		}
	}

	/**
	 * Allows a single sound to be loaded and stored to a config. Provides a handy method for
	 * playing the sound either globally or locally at a specific location.
	 */
	public static final class SoundValue implements IValue
	{
		public Sound sound;
		public float volume = 1.0f;
		public float pitch = 0.0f;
		public boolean enabled = true;

		/**
		 * Plays the sound represented by this value, if possible. The sound will be played globally
		 * for all players to hear.
		 * 
		 * @return True iff the sound was played
		 */
		public boolean play()
		{
			if (!enabled)
				return false;
			for (final Player player : Bukkit.getOnlinePlayers())
				player.playSound(player.getLocation(), sound, volume, pitch);
			return true;
		}
		/**
		 * Plays the sound represented by this value, if possible. The sound will be played locally,
		 * only players nearby the provided location will hear the sound.
		 * 
		 * @return True iff the sound was played
		 */
		public boolean play(final Location location)
		{
			if (!enabled)
				return false;
			location.getWorld().playSound(location, sound, volume, pitch);
			return true;
		}

		// ...

		@Override
		public void save(final IConfig config, final IProperty property)
		{
			config.set(property.child("sound"), sound == null ? "invalid" : sound.toString().toLowerCase());
			config.set(property.child("volume"), volume);
			config.set(property.child("pitch"), pitch);
			config.set(property.child("enabled"), enabled);
		}
		@Override
		public void load(final IConfig config, final IProperty property)
		{
			final String sound = config.getString(property.child("sound"));
			volume = config.getFloat(property.child("volume"));
			pitch = config.getFloat(property.child("pitch"));
			enabled = config.getBool(property.child("enabled"));

			try
			{
				this.sound = Sound.valueOf(sound.toUpperCase());
			}
			catch (final Exception e)
			{
				CoreUtilPlugin.WARN("Failed loading Sound: " + sound + " under " + property.getPath());
			}
		}
	}

	/**
	 * Allows a single time to be loaded and stored to a config
	 */
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
			config.set(property, time == null ? "invalid" : time.toString());
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
		public void apply(final LivingEntity entity)
		{
			if (random.nextFloat() < chance)
			{
				entity.addPotionEffect(new PotionEffect(type, (int) duration.asTicks(),
						amplifier > 0 ? amplifier - 1 : amplifier, ambient, particles), true);
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
			return String.format("Type: %s, amplifier: %d, duration: %s, chance: %.0f%%", type.getName(), amplifier,
					duration.toString(), 100.0f * chance);
		}
	}

	public static final class DamageTypeSetValue implements IValue
	{
		public Set<DamageType> types = new HashSet<>();

		@Override
		public void save(final IConfig config, final IProperty property)
		{
			final Set<String> strings = new HashSet<>();
			for (final DamageType cause : types)
				strings.add(cause.toString().toLowerCase());
			config.set(property, strings);
		}
		@Override
		public void load(final IConfig config, final IProperty property)
		{
			types.clear();
			for (final String cause : config.getStrings(property))
			{
				try
				{
					types.add(DamageType.valueOf(cause.toUpperCase()));
				}
				catch (final Exception e)
				{
					CoreUtilPlugin.WARN("Failed loading DamageType: " + cause + " under " + property.getPath());
				}
			}
		}
	}
}
