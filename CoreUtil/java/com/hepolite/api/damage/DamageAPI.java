package com.hepolite.api.damage;

import org.bukkit.Location;
import org.bukkit.attribute.Attribute;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.hepolite.api.event.HandlerCore;
import com.hepolite.api.event.events.DamageEvent;
import com.hepolite.api.event.events.HealEvent;

public final class DamageAPI extends HandlerCore
{
	private static boolean cancelledDamage = false;

	private static DamageAPI instance;
	private final DamageAdapter adapter;
	private final DamageCalculator calculator;

	public DamageAPI(final JavaPlugin plugin)
	{
		super(plugin);
		DamageAPI.instance = this;
		this.adapter = new DamageAdapter();
		this.calculator = new DamageCalculator(plugin);
	}

	@Override
	public void onReload()
	{
		calculator.reloadConfigs();
	}

	// ...

	/**
	 * Performs an attack on the target
	 * 
	 * @param target The target that will receive the damage
	 * @param damage The damage to be dealt to the target
	 * @return True iff the damage was dealt to the target
	 */
	public static boolean damage(final LivingEntity target, final Damage damage)
	{
		return damage(target, null, damage);
	}
	/**
	 * Performs an attack on the target, from the attacker if the attacker is specified
	 * 
	 * @param target The target that will receive the damage
	 * @param attacker The attack which deals the damage, may be null
	 * @param damage The damage to be dealt to the target
	 * @return True iff the damage was dealt to the target
	 */
	public static boolean damage(final LivingEntity target, final LivingEntity attacker, final Damage damage)
	{
		if (!target.isValid() || damage.getAmount() <= 0.0)
			return false;
		instance.adapter.injectDamage(damage);
		if (attacker == null)
			target.damage(damage.getAmount());
		else
			target.damage(damage.getAmount(), attacker);
		instance.adapter.injectDamage(null);
		return !cancelledDamage;
	}
	/**
	 * Performs an attack on the target, using the provided item as a weapon
	 * 
	 * @param target The target that will receive the damage
	 * @param item The item that will deal the damage
	 * @return True iff the damage was dealt to the target
	 */
	public static boolean damage(final LivingEntity target, final ItemStack item)
	{
		return damage(target, null, item);
	}
	/**
	 * Performs an attack on the target, using the provided item as a weapon
	 * 
	 * @param target The target that will receive the damage
	 * @param attacker The attack which deals the damage, may be null
	 * @param item The item that will deal the damage
	 * @return True iff the damage was dealt to the target
	 */
	public static boolean damage(final LivingEntity target, final LivingEntity attacker, final ItemStack item)
	{
		// Calculate damage done by the item
		double damage = 0.0;
		damage += DamageAPI.getDamageFromItem(item);
		damage += DamageAPI.getDamageAgainstEntity(target.getType(), item);
		if (attacker != null)
			damage += DamageAPI.getDamageFromEntity(attacker);

		// Apply item effects if the attack was valid
		final int fireAspect = item.getEnchantmentLevel(Enchantment.FIRE_ASPECT);

		final boolean didDamage = damage(target, attacker, new Damage(DamageType.NORMAL, damage));
		if (didDamage && fireAspect != 0)
			target.setFireTicks(Math.max(80 * fireAspect, target.getFireTicks()));
		return didDamage;
	}

	/**
	 * Performs a heal on the target
	 * 
	 * @param target The entity to heal
	 * @param healing The healing to perform
	 * @return True if the entity was healed
	 */
	public static boolean heal(final LivingEntity target, final Heal healing)
	{
		return heal(target, null, healing);
	}
	/**
	 * Performs a heal on the target
	 * 
	 * @param target The entity to heal
	 * @param healer The entity performing the healing
	 * @param healing The healing to perform
	 * @return True if the entity was healed
	 */
	public static boolean heal(final LivingEntity target, final LivingEntity healer, final Heal healing)
	{
		if (!target.isValid() || healing.getAmount() <= 0.0)
			return false;

		// It's utterly ridiculous that Spigot does not provide some heal method on living
		// entities... Seriously, it would have been super-easy to add some LivingEntity.heal
		// method!
		instance.adapter.injectHeal(healing);
		final EntityRegainHealthEvent event = instance
				.post(new EntityRegainHealthEvent(target, healing.getAmount(), RegainReason.CUSTOM));
		instance.adapter.injectHeal(null);

		// Make sure the entity is not dead at this point, otherwise it ends up in limbo!
		if (event.isCancelled() || event.getAmount() < 0.0 || !target.isValid() || target.isDead())
			return false;
		final double maxHealth = target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
		if (target.getHealth() >= maxHealth)
			return false;
		target.setHealth(Math.min(target.getHealth() + event.getAmount(), maxHealth));
		return true;
	}

	/**
	 * Applies a force on the target, knocking them away from the origin location
	 * 
	 * @param target The target to knock to the side
	 * @param origin The source location of the force
	 * @param strength The strength of the force, measured in m/s
	 * @param lift Additional lift parameter, measured in m/s
	 */
	public static void knockback(final LivingEntity target, final Location origin, final double strength,
			final double lift)
	{
		final Vector delta = target.getLocation().subtract(origin).toVector();
		delta.setY(0.0);
		if (delta.lengthSquared() > 0.001)
			delta.normalize();
		delta.multiply(0.05 * strength);
		delta.setY(delta.getY() + 0.05 * lift);
		target.setVelocity(target.getVelocity().add(delta));
	}

	// ...

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onEntityReceiveDamage(final EntityDamageEvent event)
	{
		final DamageEvent damageEvent = adapter.adapt(event);
		if (damageEvent == null)
			return;
		calculator.calculateReduction(damageEvent.getTarget(), damageEvent);

		post(damageEvent);
		event.setDamage(damageEvent.getFinalDamage());
		event.setCancelled(damageEvent.isCancelled());
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void monitorEntityReceiveDamage(final EntityDamageEvent event)
	{
		cancelledDamage = event.isCancelled();
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onEntityReceiveHeal(final EntityRegainHealthEvent event)
	{
		final HealEvent healEvent = adapter.adapt(event);
		if (healEvent == null)
			return;

		post(healEvent);
		event.setAmount(healEvent.getHealing());
		event.setCancelled(healEvent.isCancelled());
	}

	// ...

	/**
	 * Returns the amount of damage the given item can inflict upon use
	 * 
	 * @param item The item to check
	 * @return The damage the item can do
	 */
	private static double getDamageFromItem(final ItemStack item)
	{
		switch (item.getType())
		{
		case WOOD_PICKAXE:
		case GOLD_PICKAXE:
			return 2.0;
		case WOOD_SPADE:
		case GOLD_SPADE:
			return 2.5;
		case STONE_PICKAXE:
			return 3.0;
		case STONE_SPADE:
			return 3.5;
		case WOOD_SWORD:
		case GOLD_SWORD:
		case IRON_PICKAXE:
			return 4.0;
		case IRON_SPADE:
			return 4.5;
		case STONE_SWORD:
		case DIAMOND_PICKAXE:
			return 5.9;
		case DIAMOND_SPADE:
			return 5.5;
		case IRON_SWORD:
			return 6.0;
		case WOOD_AXE:
		case GOLD_AXE:
		case DIAMOND_SWORD:
			return 7.0;
		case STONE_AXE:
		case IRON_AXE:
		case DIAMOND_AXE:
			return 9.0;
		default:
			return 1.0;
		}
	}
	/**
	 * Returns the bonus damage the item will deal against the given mob type; this will only
	 * consider the enchantments on the item
	 * 
	 * @param type The entity type to fight against
	 * @param item The item that is used
	 * @return The additional damage done by the item
	 */
	private static double getDamageAgainstEntity(final EntityType type, final ItemStack item)
	{
		final int sharpness = item.getEnchantmentLevel(Enchantment.DAMAGE_ALL);
		final int bane = item.getEnchantmentLevel(Enchantment.DAMAGE_ARTHROPODS);
		final int smite = item.getEnchantmentLevel(Enchantment.DAMAGE_UNDEAD);

		double damage = 0.5 * sharpness;
		if (type == EntityType.SKELETON || type == EntityType.ZOMBIE || type == EntityType.WITHER
				|| type == EntityType.WITHER_SKELETON || type == EntityType.PIG_ZOMBIE
				|| type == EntityType.SKELETON_HORSE || type == EntityType.ZOMBIE_HORSE || type == EntityType.STRAY
				|| type == EntityType.ZOMBIE_VILLAGER || type == EntityType.HUSK)
			damage += 2.5 * smite;
		if (type == EntityType.SPIDER || type == EntityType.CAVE_SPIDER || type == EntityType.SILVERFISH
				|| type == EntityType.ENDERMITE)
			damage += 2.5 * bane;
		return damage;
	}
	/**
	 * Returns the bonus damage the given entity can deal out when attacking; this will only
	 * consider potion effects
	 * 
	 * @param entity The entity to look at
	 * @return The additional damage done by the entity
	 */
	private static double getDamageFromEntity(final LivingEntity entity)
	{
		final PotionEffect effect = entity.getPotionEffect(PotionEffectType.INCREASE_DAMAGE);
		if (effect == null)
			return 0.0;
		return 3.0 * (effect.getAmplifier() + 1);
	}
}
