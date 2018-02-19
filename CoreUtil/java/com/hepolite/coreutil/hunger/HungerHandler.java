package com.hepolite.coreutil.hunger;

import java.util.Optional;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.attribute.Attribute;
import com.hepolite.api.attribute.AttributeDatabase;
import com.hepolite.api.attribute.AttributeType;
import com.hepolite.api.damage.Damage;
import com.hepolite.api.damage.DamageAPI;
import com.hepolite.api.damage.DamageType;
import com.hepolite.api.damage.Heal;
import com.hepolite.api.damage.HealType;
import com.hepolite.api.event.HandlerCore;
import com.hepolite.api.event.events.PlayerHungerChange;
import com.hepolite.api.event.events.PlayerSaturationChange;
import com.hepolite.api.units.Time;
import com.hepolite.api.user.IUser;
import com.hepolite.api.user.UserFactory;
import com.hepolite.api.util.StringConverter;
import com.hepolite.coreutil.CoreUtilPlugin;

public final class HungerHandler extends HandlerCore
{
	private final FoodRegistry foodRegistry;
	private final HungerRegistry hungerRegistry;
	private final GroupRegistry groupRegistry;

	private boolean ignoreDamageEvent = false;
	private boolean ignoreHealingEvent = false;

	public HungerHandler(final JavaPlugin plugin)
	{
		super(plugin);
		foodRegistry = new FoodRegistry(plugin);
		hungerRegistry = new HungerRegistry(plugin);
		groupRegistry = new GroupRegistry(plugin);
	}

	@Override
	public void onTick(final int tick)
	{
		for (final Player player : Bukkit.getOnlinePlayers())
		{
			if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
				continue;

			final HungerData data = getHungerData(player);
			final GroupData group = groupRegistry.getGroupData(data.group);

			if (tick % Time.TICKS_PER_SECOND == 0)
				consumeHunger(player, data, group);
			if (group.healingEnable && tick % group.healingFrequency.asTicks() == 0)
				handleHealing(player, data, group);
			if (group.starvationEnable && tick % group.starvationFrequency.asTicks() == 0)
				handleStarvation(player, data, group);
		}

		if (tick % Time.TICKS_PER_HOUR == 0)
			hungerRegistry.saveData();
	}
	@Override
	public void onReload()
	{
		foodRegistry.loadFoods();
		groupRegistry.loadGroups();
	}
	@Override
	public void onDisable()
	{
		hungerRegistry.saveData();
	}

	// ...

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		if (!hungerRegistry.hasHungerData(player))
			resetHunger(player);
		setPlayerGroup(player, hungerRegistry.getHungerData(player).group);
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerRespawn(final PlayerRespawnEvent event)
	{
		resetHunger(event.getPlayer());
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerTakeDamage(final EntityDamageEvent event)
	{
		if (ignoreDamageEvent || event.getCause() != DamageCause.STARVATION)
			return;
		event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerHealDamage(final EntityRegainHealthEvent event)
	{
		if (ignoreHealingEvent || event.getRegainReason() != RegainReason.SATIATED)
			return;
		event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = false)
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		final Player player = event.getPlayer();
		final ItemStack item = player.getInventory().getItemInMainHand();

		player.sendMessage("Item " + StringConverter.fromItem(item) + ": " + canPlayerEat(player, item));
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void onPlayerConsumeItem(final PlayerItemConsumeEvent event)
	{

	}

	// ...

	private void resetHunger(final Player player)
	{
		final HungerData data = hungerRegistry.getHungerData(player);
		final GroupData group = groupRegistry.getGroupData(data.group);

		data.hunger = data.saturation = group.hungerMax;
		data.consumption = 0.0f;
	}
	private void updateHunger(final Player player)
	{
		final IUser user = UserFactory.fromPlayer(player);
		final Attribute maxHunger = AttributeDatabase.get(user, AttributeType.HUNGER_MAX);
		final HungerData data = hungerRegistry.getHungerData(player);

		player.setFoodLevel(Math.round(20.0f * data.hunger / maxHunger.getValue()));
		player.setSaturation(20.0f * data.saturation / maxHunger.getValue());
	}

	private void consumeHunger(final Player player, final HungerData data, final GroupData group)
	{
		float targetConsumption = 0.0f;
		switch (CoreUtilPlugin.getMovementHandler().getMovementType(player))
		{
		case FLOATING:
			targetConsumption = group.consumptionFloating;
			break;
		case FLYING:
			targetConsumption = group.consumptionFlying;
			break;
		case GLIDING:
			targetConsumption = group.consumptionGliding;
			break;
		case HOVERING:
			targetConsumption = group.consumptionHovering;
			break;
		case RUNNING:
			targetConsumption = group.consumptionRunning;
			break;
		case SNEAKING:
			targetConsumption = group.consumptionSneaking;
			break;
		case STANDING:
			targetConsumption = group.consumptionStanding;
			break;
		case SWIMMING:
			targetConsumption = group.consumptionSwimming;
			break;
		case WALKING:
			targetConsumption = group.consumptionWalking;
			break;
		default:
			CoreUtilPlugin.WARN("[HungerHandler] Illegal movement type");
		}

		data.consumption += group.consumptionChange * (targetConsumption - data.consumption);
		changeSaturation(player, -data.consumption);
	}
	private void handleStarvation(final Player player, final HungerData data, final GroupData group)
	{
		if (data.hunger > 0.0f)
			return;
		ignoreDamageEvent = true;
		DamageAPI.damage(player, new Damage(DamageType.HUNGER, group.starvationDamage));
		ignoreDamageEvent = false;
	}
	private void handleHealing(final Player player, final HungerData data, final GroupData group)
	{
		if (data.hunger <= group.healingStart)
			return;
		ignoreHealingEvent = true;
		if (DamageAPI.heal(player, player, new Heal(HealType.SATIATED_REGEN, group.healingAmount)))
			changeSaturation(player, -group.healingCost);
		ignoreHealingEvent = false;
	}

	// ...

	/**
	 * Returns true iff the player is allowed to eat the given item
	 * 
	 * @param player The player that want to eat the item
	 * @param item The item the player wants to eat
	 * @return True iff the player is allowed to devour that item
	 */
	public boolean canPlayerEat(final Player player, final ItemStack item)
	{
		final String group = getPlayerGroup(player);
		final Optional<FoodData> opFood = foodRegistry.getFoodData(item, group);
		if (!opFood.isPresent())
			return false;

		final Attribute maxHunger = AttributeDatabase.get(UserFactory.fromPlayer(player), AttributeType.HUNGER_MAX);
		final HungerData hunger = hungerRegistry.getHungerData(player);
		final FoodData food = opFood.get();
		final GroupData data = groupRegistry.getGroupData(group);

		// If the food contains no nourishment or the player is full, eating cannot commence
		if (!food.alwaysConsumable)
		{
			if (food.food == 0.0f || hunger.hunger >= maxHunger.getValue())
				return false;
		}

		// Ensure the player cannot eat forbidden fruit
		for (final String category : food.categories)
			if (data.forbiddenCategories.contains(category))
				return false;
		for (final String category : food.ingredients)
			if (data.forbiddenIngredients.contains(category))
				return false;
		return !data.forbiddenCategories.contains(food.name);
	}

	/**
	 * Retrieves the hunger group the given player is associated with
	 * 
	 * @param player The player to look up
	 * @return The hunger group ID the player is associated with
	 */
	public String getPlayerGroup(final Player player)
	{
		return hungerRegistry.getHungerData(player).group;
	}
	/**
	 * Assigns the hunger group the given player is associated with
	 * 
	 * @param player The player to look up
	 * @param group The group the player should be associated with
	 */
	public void setPlayerGroup(final Player player, final String group)
	{
		final IUser user = UserFactory.fromPlayer(player);
		final GroupData groupData = groupRegistry.getGroupData(group);
		final Attribute maxHunger = AttributeDatabase.get(user, AttributeType.HUNGER_MAX);

		hungerRegistry.getHungerData(player).group = group;
		maxHunger.setBaseValue(groupData.hungerMax).setMinValue(0.0f);
	}

	/**
	 * Changes the given player's hunger level by the given amount. This will not consume saturation
	 * first; if the final hunger value is less than the current saturation value, some saturation
	 * points will be lost.
	 * 
	 * @param player The player to change the hunger for
	 * @param amount The amount of hunger points to add to the player's account
	 */
	public void changeHunger(final Player player, final float amount)
	{
		final IUser user = UserFactory.fromPlayer(player);
		final Attribute maxHunger = AttributeDatabase.get(user, AttributeType.HUNGER_MAX);
		final HungerData data = hungerRegistry.getHungerData(player);

		final PlayerHungerChange event = post(new PlayerHungerChange(player, data.hunger, data.hunger + amount));
		if (event.isCancelled())
			return;

		data.hunger = Math.max(0.0f, Math.min(maxHunger.getValue(), event.getNewHunger()));
		data.saturation = Math.min(data.hunger, data.saturation);
		updateHunger(player);
	}
	/**
	 * Changes the given player's saturation level by the given amount. If the saturation change
	 * leads to a saturation value greater than the hunger value, the excess saturation points are
	 * lost. If the saturation is about to become negative, the hunger value will be drained
	 * instead.
	 * 
	 * @param player The player to change the saturation for
	 * @param amount The amount of saturation points to add to the player's account
	 */
	public void changeSaturation(final Player player, final float amount)
	{
		final HungerData data = hungerRegistry.getHungerData(player);

		final PlayerSaturationChange event = post(
				new PlayerSaturationChange(player, data.saturation, data.saturation + amount));
		if (event.isCancelled())
			return;

		// If the player has enough saturation to sustain a saturation loss, simply update.
		// Otherwise,
		// drain hunger
		if (event.getNewSaturation() >= 0.0f)
			data.saturation = Math.max(0.0f, Math.min(data.hunger, event.getNewSaturation()));
		else
		{
			data.saturation = 0.0f;
			changeHunger(player, event.getNewSaturation());
		}
		updateHunger(player);
	}

	// ...

	/**
	 * Retrieves the raw hunger data for the given player. Consider the returned object as
	 * immutable; use {@link HungerHandler} itself to modify any of the fields in the hunger data.
	 * 
	 * @param player The player to look up
	 * @return The hunger data associated with the given player
	 */
	public HungerData getHungerData(final Player player)
	{
		return hungerRegistry.getHungerData(player);
	}

	// ...

	/**
	 * Attempts to retrieve the food data associated with the given item, under the given group. The
	 * the food is not a valid food under the give group, the food will be checked against the
	 * default group. If the food has a custom name, the custom name will be prioritized, then the
	 * same search will be performed on the item type.
	 * 
	 * @param item The item to look up
	 * @param group The group to search under
	 * @return The food data associated with the item if any
	 */
	public Optional<FoodData> getFoodData(final ItemStack item, final String group)
	{
		return foodRegistry.getFoodData(item, group);
	}
	/**
	 * Attempts to retrieve the food data associated with the given item, under the given group. The
	 * the food is not a valid food under the give group, the food will be checked against the
	 * default group.
	 * 
	 * @param item The item to look up
	 * @param group The group to search under
	 * @return The food data associated with the item if any
	 */
	public Optional<FoodData> getFoodData(final String item, final String group)
	{
		return foodRegistry.getFoodData(item, group);
	}
}
