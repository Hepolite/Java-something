package com.hepolite.coreutil.cmd;

import java.util.Optional;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import com.hepolite.api.chat.Builder;
import com.hepolite.api.cmd.Cmd;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.user.IUser;
import com.hepolite.coreutil.CoreUtilPlugin;
import com.hepolite.coreutil.hunger.FoodData;
import com.hepolite.coreutil.hunger.GroupRegistry;
import com.hepolite.coreutil.hunger.HungerHandler;

public class CmdDebugFood extends Cmd
{
	public CmdDebugFood()
	{
		/// @formatter:off
		super("food",
			GenericArgs.optional(GenericArgs.playerOrUser("player")),
			GenericArgs.optional(
				GenericArgs.string("item"),
				GenericArgs.optional(GenericArgs.string("group"))
			)
		);
		/// @formatter:on
	}

	@Override
	public boolean execute(final IUser user, final ICmdContext context) throws CommandException
	{
		final HungerHandler hungerHandler = CoreUtilPlugin.getHungerHandler();

		final Optional<Player> player = context.get("player");
		final Optional<String> item = context.get("item");
		String group = context.<String> get("group").orElse(GroupRegistry.DEFAULT_GROUP);

		Optional<FoodData> data = Optional.empty();
		if (item.isPresent())
			data = hungerHandler.getFoodData(item.get(), group);
		else if (player.isPresent())
		{
			group = hungerHandler.getPlayerGroup(player.get());
			data = hungerHandler.getFoodData(player.get().getInventory().getItemInMainHand(), group);
		}

		if (!data.isPresent())
			throw new CommandException("Could not deduce a food item from the provided parameters");
		displayGenericFoodInfo(user, data.get(), group);
		return true;
	}

	/**
	 * Displays generic information about the food to the specified user
	 * 
	 * @param user The user that should see the information
	 * @param data The food to provide information about
	 */
	private void displayGenericFoodInfo(final IUser user, final FoodData data, final String group)
	{
		final String categories = data.categories.isEmpty() ? "none" : StringUtils.join(data.categories, ", ");
		final String ingredients = data.ingredients.isEmpty() ? "none" : StringUtils.join(data.ingredients, ", ");
		final String effects = data.effects.isEmpty() ? "none" : StringUtils.join(data.effects, "\n");

		final Builder builder = new Builder("");
		builder.addText(String.format("&bFood &9'%s'&b contains &9'%.1f/%.1f (%.1f)'&b hunger/saturation (ratio)",
				data.name, data.food, data.ratio * data.food, data.ratio));
		builder.addHover(String.format("&bGroup: &9%s\n&bCategories: &f%s\n&bIngredients: &f%s\n&bEffects:\n&f%s",
				group, categories, ingredients, effects));
		user.sendMessage(builder.build());
	}
}
