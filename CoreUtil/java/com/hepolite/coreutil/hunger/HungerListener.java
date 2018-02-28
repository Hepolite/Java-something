package com.hepolite.coreutil.hunger;

import java.util.AbstractMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent.RegainReason;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import com.hepolite.api.attribute.Attribute;
import com.hepolite.api.attribute.AttributeDatabase;
import com.hepolite.api.attribute.AttributeType;
import com.hepolite.api.chat.Builder;
import com.hepolite.api.damage.Damage;
import com.hepolite.api.damage.DamageAPI;
import com.hepolite.api.damage.DamageType;
import com.hepolite.api.damage.Heal;
import com.hepolite.api.damage.HealType;
import com.hepolite.api.event.IListener;
import com.hepolite.api.units.Time;
import com.hepolite.api.user.IUser;
import com.hepolite.api.user.UserFactory;
import com.hepolite.api.util.InventoryHelper;
import com.hepolite.coreutil.CoreUtilPlugin;

public final class HungerListener implements IListener
{
	private final HungerSystem system;

	private boolean ignoreDamageEvent = false;
	private boolean ignoreHealingEvent = false;
	private boolean ignoreConsumeEvent = false;

	private final Map<UUID, Entry<Long, ItemStack>> chewing = new HashMap<>();
	private long currentTime = 0;

	public HungerListener(final HungerSystem system)
	{
		this.system = system;
	}

	/**
	 * Updates the players' hunger values, and health if they are saturated enough or starving
	 * 
	 * @param tick The current tick
	 */
	public void onTick(final int tick)
	{
		currentTime = tick;

		for (final Player player : Bukkit.getOnlinePlayers())
		{
			if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
				continue;
			final HungerData hunger = system.getHungerData(player);
			final GroupData group = system.getGroupData(player);

			if (tick % (2 * Time.TICKS_PER_SECOND / 3) == 0)
				performChewing(player);
			if (tick % Time.TICKS_PER_SECOND == 0)
				performHunger(player, hunger, group);
			if (group.healingEnable && tick % group.healingFrequency.asTicks() == 0)
				performHealing(player, hunger, group);
			if (group.starvationEnable && tick % group.starvationFrequency.asTicks() == 0)
				performStarving(player, hunger, group);
		}
	}

	/**
	 * Updates eating sounds whenever the player is eating something
	 * 
	 * @param player The player that is eating
	 */
	private void performChewing(final Player player)
	{
		final UUID uuid = player.getUniqueId();
		if (chewing.containsKey(uuid))
		{
			final Entry<Long, ItemStack> entry = chewing.get(uuid);
			if (entry.getKey() > currentTime)
				system.getGroupData(player).chewingSound.play(player.getEyeLocation());
			else
			{
				ignoreConsumeEvent = true;
				system.consumeItem(player, entry.getValue());
				system.getGroupData(player).eatingSound.play(player.getEyeLocation());
				ignoreConsumeEvent = false;
				chewing.remove(uuid);
			}
		}
	}
	/**
	 * Updates the player hunger values, and attempts to bring their consumption up to the level of
	 * whichever activity they are currently performing
	 * 
	 * @param player The player that is doing work
	 * @param hunger The player's hunger data
	 * @param group The player's group data
	 */
	private void performHunger(final Player player, final HungerData hunger, final GroupData group)
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
			CoreUtilPlugin.WARN("[HungerHandler] Illegal movement type detected");
		}

		hunger.consumption += group.consumptionChange * (targetConsumption - hunger.consumption);
		system.changeSaturation(player, -hunger.consumption);
	}
	/**
	 * Handles healing players that have enough food to naturally regenerate their health
	 * 
	 * @param player The player that is healing up
	 * @param hunger The player's hunger data
	 * @param group The player's group data
	 */
	private void performHealing(final Player player, final HungerData hunger, final GroupData group)
	{
		if (hunger.hunger <= group.healingStart)
			return;
		ignoreHealingEvent = true;
		if (DamageAPI.heal(player, player, new Heal(HealType.SATIATED_REGEN, group.healingAmount)))
			system.changeSaturation(player, -group.healingCost);
		ignoreHealingEvent = false;
	}
	/**
	 * Handles players with an empty hunger bar, dealing starvation damage to them
	 * 
	 * @param player The player that is starving
	 * @param hunger The player's hunger data
	 * @param group The player's group data
	 */
	private void performStarving(final Player player, final HungerData hunger, final GroupData group)
	{
		if (hunger.hunger > 0.0f)
			return;
		ignoreDamageEvent = true;
		DamageAPI.damage(player, new Damage(DamageType.HUNGER, group.starvationDamage));
		ignoreDamageEvent = false;
	}

	// ...

	@EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
	public void onPlayerJoin(final PlayerJoinEvent event)
	{
		final Player player = event.getPlayer();
		final IUser user = UserFactory.fromPlayer(player);
		final Attribute maxHunger = AttributeDatabase.get(user, AttributeType.HUNGER_MAX);

		maxHunger.setMinValue(0.0f).setBaseValue(system.getGroupData(player).hungerMax);
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerRespawn(final PlayerRespawnEvent event)
	{
		final Player player = event.getPlayer();
		final IUser user = UserFactory.fromPlayer(player);
		final float maxHunger = AttributeDatabase.get(user, AttributeType.HUNGER_MAX).getValue();

		system.changeHunger(player, maxHunger);
		system.changeSaturation(player, maxHunger);
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
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerConsume(final PlayerItemConsumeEvent event)
	{
		if (ignoreConsumeEvent || InventoryHelper.isDrinkable(event.getItem()))
			return;
		event.setCancelled(true);
	}

	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public void onPlayerInteract(final PlayerInteractEvent event)
	{
		// Detect that cursed cake
		final boolean isCake = event.getClickedBlock() == null ? false
				: event.getClickedBlock().getType() == Material.CAKE_BLOCK;

		// Okay, calm down... Things will get worse from here on...
		if (event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK)
			return;
		final Player player = event.getPlayer();
		if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR)
			return;
		final ItemStack item = isCake ? new ItemStack(Material.CAKE_BLOCK) : event.getItem();
		if (item == null)
			return;

		final boolean allowedEating = system.canPlayerEat(player, item);
		if (allowedEating && !InventoryHelper.isDrinkable(item))
		{
			if (shouldCancelEatingBecauseOfInteraction(event))
				return;

			// Usually the event should always be cancelled from here on and out,
			// but that cursed CAKE RUINS IT ALL!
			event.setCancelled(!isCake);
			if (startChewing(player, item))
			{
				if (!isCake)
				{
					if (item.getAmount() <= 1)
						item.setType(Material.AIR);
					else
						item.setAmount(item.getAmount() - 1);

					if (event.getHand() == EquipmentSlot.HAND)
						player.getInventory().setItemInMainHand(item);
					else
						player.getInventory().setItemInOffHand(item);
				}
			}
			else if (isCake) // BLASTED CAKE MESSING UP GOOD CODE! RAAAA
				event.setCancelled(true);
		}
		else if (!allowedEating && InventoryHelper.isConsumable(item) && !system.isPlayerFull(player))
			displayDenyEatingMessage(player, item);
	}

	// ...

	/**
	 * Attempts to start chewing the given food item. Returns true if the chewing started
	 * 
	 * @param player The player to check
	 * @param item The item the player is trying to eat
	 * @return True iff the player is eating slowly enough
	 */
	private boolean startChewing(final Player player, final ItemStack item)
	{
		final Optional<FoodData> food = system.getFoodData(item, system.getPlayerGroup(player));
		if (!food.isPresent())
			return true;
		final UUID uuid = player.getUniqueId();
		if (chewing.containsKey(uuid))
			return false;
		final long chewTime = Time.TICKS_PER_SECOND * Math.min(60, food.get().time.asSeconds());
		chewing.put(uuid, new AbstractMap.SimpleEntry<>(currentTime + chewTime, item.clone()));
		return true;
	}
	/**
	 * Displays the reason why the player was unable to eat the item
	 * 
	 * @param player The player that should receive the message
	 * @param item The item the player attempted to eat
	 */
	private void displayDenyEatingMessage(final Player player, final ItemStack item)
	{
		final FoodData food = system.getFoodData(item, system.getPlayerGroup(player)).get();
		final GroupData group = system.getGroupData(player);
		final Set<String> inedibles = new TreeSet<>();
		final Set<String> forbidden = new HashSet<>();
		forbidden.addAll(group.forbiddenCategories);
		forbidden.addAll(group.forbiddenIngredients);
		inedibles.addAll(food.categories);
		inedibles.addAll(food.ingredients);
		inedibles.retainAll(forbidden);

		final Builder builder = new Builder("");
		builder.addText(String.format("&cYou are unable to eat &e'%s'&c!", food.name));
		builder.addHover(String.format("&cFood &e'%s'&c contains inedibles:\n&f%s", food.name,
				StringUtils.join(inedibles, ", ")));
		player.spigot().sendMessage(builder.build().spigot());
	}

	/**
	 * And the pain begins... Checks if the given event leads to some item interaction that could
	 * interfere with eating, if so, return true. If there are *still* issues related to that,
	 * canceling the whole event somewhere would allow the item to not be eaten.
	 *
	 * 
	 * @param event The event to handle
	 * @return True iff the event leads to some item intraction
	 */
	private boolean shouldCancelEatingBecauseOfInteraction(final PlayerInteractEvent event)
	{
		final Block block = event.getClickedBlock();
		final BlockFace face = event.getBlockFace();
		final ItemStack item = event.getItem();
		if (block == null || item == null || block.getType() == Material.AIR)
			return false;
		if (event.isCancelled())
			return false;

		// Blocks against blocks
		if (block.getType().isBlock() && event.isBlockInHand())
			return true;
		// Cocoa beans
		if (block.getType() == Material.LOG && item.getType() == Material.COCOA)
			return true;
		// That FREAKING cake again
		if (item.getType() == Material.CAKE)
			return true;

		// Generic plants the rest of the way down from here!
		if (face != BlockFace.UP)
			return false;
		// Mushrooms can be planted almost anywhere...
		switch (item.getType())
		{
		case RED_MUSHROOM:
		case BROWN_MUSHROOM:
			return true;
		default:
		}
		switch (block.getType())
		{
		case DIRT:
		case GRASS:
			switch (item.getType())
			{
			case DOUBLE_PLANT:
			case GRASS:
			case LONG_GRASS:
			case RED_ROSE:
			case SAPLING:
			case SUGAR_CANE:
			case YELLOW_FLOWER:
				return true;
			default:
				return false;
			}
		case ENDER_STONE:
			switch (item.getType())
			{
			case CHORUS_FLOWER:
				return true;
			default:
				return false;
			}
		case SOIL:
		{
			switch (item.getType())
			{
			case BEETROOT_SEEDS:
			case CARROT_ITEM:
			case MELON_SEEDS:
			case POTATO_ITEM:
			case PUMPKIN_SEEDS:
			case SEEDS:
				return true;
			default:
				return false;
			}
		}
		case SOUL_SAND:
			switch (item.getType())
			{
			case NETHER_WARTS:
				return true;
			default:
				return false;
			}
		default:
			return false;
		}
	}
}
