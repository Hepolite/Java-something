package com.hepolite.coreutil.hunger;

import java.util.Optional;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.attribute.Attribute;
import com.hepolite.api.attribute.AttributeDatabase;
import com.hepolite.api.attribute.AttributeType;
import com.hepolite.api.config.CommonValues.PotionEffectValue;
import com.hepolite.api.event.HandlerCore;
import com.hepolite.api.event.events.PlayerHungerChange;
import com.hepolite.api.event.events.PlayerSaturationChange;
import com.hepolite.api.units.Time;
import com.hepolite.api.user.IUser;
import com.hepolite.api.user.UserFactory;
import com.hepolite.api.util.InventoryHelper;

public final class HungerSystem extends HandlerCore
{
	private final FoodRegistry foodRegistry;
	private final HungerRegistry hungerRegistry;
	private final GroupRegistry groupRegistry;
	private final HungerListener hungerListener;

	public HungerSystem(final JavaPlugin plugin)
	{
		super(plugin);
		foodRegistry = new FoodRegistry(plugin);
		hungerRegistry = new HungerRegistry(plugin);
		groupRegistry = new GroupRegistry(plugin);
		hungerListener = register(new HungerListener(this));
	}

	@Override
	public void onTick(final int tick)
	{
		hungerListener.onTick(tick);

		// Be sure to make backups every now and then!
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

	/**
	 * Returns true iff the player has a full hunger bar
	 * 
	 * @param player The player to check
	 * @return True iff the player is full of food
	 */
	public boolean isPlayerFull(final Player player)
	{
		final Attribute maxHunger = AttributeDatabase.get(UserFactory.fromPlayer(player), AttributeType.HUNGER_MAX);
		final HungerData hunger = hungerRegistry.getHungerData(player);

		return hunger.hunger >= maxHunger.getValue();
	}
	/**
	 * Returns true iff the player is capable to eat the given item
	 * 
	 * @param player The player that want to eat the item
	 * @param item The item the player wants to eat
	 * @return True iff the player is allowed to devour that item
	 */
	public boolean canPlayerEat(final Player player, final ItemStack item)
	{
		if (item == null)
			return false;
		final String group = getPlayerGroup(player);
		final Optional<FoodData> opFood = foodRegistry.getFoodData(item, group);
		if (!opFood.isPresent())
			return false;

		final FoodData food = opFood.get();
		final GroupData data = groupRegistry.getGroupData(group);

		// If the food contains no nourishment or the player is full, eating cannot commence
		if (!food.alwaysConsumable)
		{
			if (food.food == 0.0f || isPlayerFull(player))
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
	 * Forces the player to consume the specified item. The itemstack that is passed in will be
	 * changed, if there are more than one item in the stack the amount is reduced by one, otherwise
	 * the type is set to air. If the item is not a food to the player, nothing happens.
	 * 
	 * @param player The player that is to eat the item
	 * @param item The item to be eaten
	 */
	public void consumeItem(final Player player, final ItemStack item)
	{
		// Just to be *somewhat* nice to other plugins; we don't care what they change items to,
		// however. The hunger system is ours and ours alone, they don't get to muddy up our system!
		final PlayerItemConsumeEvent event = post(new PlayerItemConsumeEvent(player, item));
		if (event.isCancelled())
			return;

		final String group = getPlayerGroup(player);
		final Optional<FoodData> opFood = foodRegistry.getFoodData(item, group);
		if (!opFood.isPresent())
			return;
		final FoodData food = opFood.get();

		if (food.food > 0.0f)
		{
			changeHunger(player, food.food);
			changeSaturation(player, food.food * food.ratio);
		}
		else
			changeSaturation(player, food.food * (1.0f + food.ratio));

		for (final PotionEffectValue effect : food.effects)
			effect.apply(player);

		if (food.result != null)
			InventoryHelper.add(player, food.result, player.getEyeLocation());
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

	/**
	 * Retrieves the group data for the given player. Consider the returned object as immutable, it
	 * is global for all players who belong to the same group as the given player. This method
	 * should never return null.
	 * 
	 * @param player The player to look up
	 * @return The group data associated with the given player
	 */
	public GroupData getGroupData(final Player player)
	{
		return groupRegistry.getGroupData(getPlayerGroup(player));
	}

	/**
	 * Retrieves the raw hunger data for the given player. Consider the returned object as
	 * immutable; use {@link HungerSystem} itself to modify any of the fields in the hunger data. If
	 * there is no hunger data associated with the player, the hunger data will be created.
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
	 * Updates the player's hunger bar, displaying how much hunger they have left
	 * 
	 * @param player The player to update
	 */
	private void updateHunger(final Player player)
	{
		final IUser user = UserFactory.fromPlayer(player);
		final Attribute maxHunger = AttributeDatabase.get(user, AttributeType.HUNGER_MAX);
		final HungerData data = hungerRegistry.getHungerData(player);

		player.setFoodLevel(Math.round(20.0f * data.hunger / maxHunger.getValue()));
		player.setSaturation(20.0f * data.saturation / maxHunger.getValue());
	}
}
