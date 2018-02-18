package com.hepolite.api.damage;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.hepolite.api.config.CommonValues.DamageCauseSetValue;
import com.hepolite.api.config.ConfigFactory;
import com.hepolite.api.config.IConfig;
import com.hepolite.api.config.IProperty;
import com.hepolite.api.config.Property;
import com.hepolite.api.event.events.DamageEvent;

/**
 * Don't use this class yet. It is still in development and will not be needed before
 * {@link org.bukkit.event.entity.EntityDamageEvent.DamageModifier}s are gone.
 */
public final class DamageCalculator
{
	private final JavaPlugin plugin;
	private final IConfig configAR;		// Armor rating
	private final IConfig configEPF;	// Enchantment protection factor
	private final IConfig configPR;		// Potion rating

	private final Map<String, Set<DamageCause>> enchantmentCauses = new HashMap<>();
	private final Map<String, Double> enchantmentStrengths = new HashMap<>();

	public DamageCalculator(final JavaPlugin plugin)
	{
		this.plugin = plugin;
		configAR = ConfigFactory.create(plugin, "Damage/ArmorRating");
		configEPF = ConfigFactory.create(plugin, "Damage/EnchantmentRating");
		configPR = ConfigFactory.create(plugin, "Damage/PotionRating");
		reloadConfigs();
	}

	public void reloadConfigs()
	{
		configAR.loadFromDisk();
		configEPF.loadFromDisk();
		configPR.loadFromDisk();

		for (final IProperty property : configEPF.getProperties())
		{
			enchantmentCauses.put(property.getName().toLowerCase(),
					configEPF.getValue(property.child("blocks"), new DamageCauseSetValue()).types);
			enchantmentStrengths.put(property.getName(), configEPF.getDouble(property.child("strength")));
		}

		plugin.getLogger().info("Loaded damage calculation configs...");
	}
	// ...

	/**
	 * Calculates how the given entity reduces the input damage. The damage calculator will only
	 * factor in default effects such as armor, enchantments and potion effects. No non-vanilla
	 * effects are considered in these calculations.
	 * 
	 * @param target The target that reduces the damage
	 * @param damage The damage that is to be reduced
	 */
	public void calculateReduction(final LivingEntity target, final DamageEvent event)
	{
		event.clearModifiers();
		final ItemStack armor[] = target.getEquipment().getArmorContents();
		final Collection<PotionEffect> effects = target.getActivePotionEffects();

		calculateBlockReduction(target, event);
		calculateArmorReduction(armor, event);
		calculateResistanceReduction(effects, event);
		calculateEnchantmentReduction(armor, event);

		final DamageType type = event.getType();
		event.setDamage(DamageModifier.ARMOR, type.getArmorFactor() * event.getDamage(DamageModifier.ARMOR));
		event.setDamage(DamageModifier.BLOCKING, type.getBlockFactor() * event.getDamage(DamageModifier.BLOCKING));
		event.setDamage(DamageModifier.MAGIC, type.getMagicFactor() * event.getDamage(DamageModifier.MAGIC));
		event.setDamage(DamageModifier.POTION, type.getPotionFactor() * event.getDamage(DamageModifier.POTION));
	}
	public void calculateArmorReduction(final ItemStack[] armor, final DamageEvent event)
	{
		double rating = 0.0f;
		double toughness = 0.0f;
		for (final ItemStack item : armor)
		{
			if (item == null)
				continue;
			final IProperty property = new Property(item.getType().toString().toLowerCase());
			final String variant = event.getVariant().toString().toLowerCase();

			rating += configAR.getDouble(property.child(variant));
			toughness += configAR.getDouble(property.child("toughness"));
		}

		final double tot = event.getFinalDamage();
		final double dmg = tot
				* (1.0 - Math.min(20.0, Math.max(rating / 5.0, rating - tot / (toughness / 4.0 + 2.0))) / 25.0);
		event.setDamage(DamageModifier.ARMOR, dmg - tot);
	}
	public void calculateEnchantmentReduction(final ItemStack[] armor, final DamageEvent event)
	{
		double epf = 0.0;
		for (final ItemStack item : armor)
		{
			if (item == null)
				continue;
			for (final Entry<Enchantment, Integer> entry : item.getEnchantments().entrySet())
			{
				final String key = entry.getKey().getName().toLowerCase();
				if (!enchantmentCauses.containsKey(key) || !enchantmentStrengths.containsKey(key))
					continue;
				if (!enchantmentCauses.get(key).contains(event.getType().getUnderlyingCause()))
					continue;
				epf += enchantmentStrengths.get(key) * entry.getValue();
			}
		}

		final double tot = event.getFinalDamage();
		final double dmg = tot * (1.0 - Math.min(20.0, epf) / 25.0);
		event.setDamage(DamageModifier.MAGIC, dmg - tot);
	}
	public void calculateResistanceReduction(final Collection<PotionEffect> effects, final DamageEvent event)
	{
		for (final PotionEffect effect : effects)
		{
			if (!effect.getType().equals(PotionEffectType.DAMAGE_RESISTANCE))
				continue;
			final IProperty property = new Property("resistance", "strength");

			final double pr = 1.0f + effect.getAmplifier();
			final double tot = event.getFinalDamage();
			final double dmg = tot * (1.0 - Math.min(1.0, pr * configPR.getDouble(property)));
			event.setDamage(DamageModifier.POTION, dmg - tot);
			break;
		}
	}
	public void calculateBlockReduction(final LivingEntity target, final DamageEvent event)
	{
		if (!(target instanceof Player))
			return;
		final Player player = (Player) target;
		if (!player.isBlocking() || event.getSource() == null)
			return;

		final Vector direction = player.getEyeLocation().getDirection();
		final Vector delta = event.getSource().getLocation().subtract(player.getLocation()).toVector();
		if (direction.dot(delta) <= 0.0)
			return;

		event.setDamage(DamageModifier.BLOCKING, -event.getFinalDamage());
	}
}
