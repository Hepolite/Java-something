package com.hepolite.coreutil.cmd;

import java.util.Optional;

import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import com.hepolite.api.chat.Builder;
import com.hepolite.api.chat.Message;
import com.hepolite.api.cmd.Cmd;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.user.IUser;
import com.hepolite.coreutil.CoreUtilPlugin;
import com.hepolite.coreutil.hunger.FoodData;
import com.hepolite.coreutil.hunger.FoodRegistry;

public class CmdDebugFood extends Cmd
{
	protected CmdDebugFood()
	{
		/// @formatter:off
		super("food",
				GenericArgs.optional(GenericArgs.playerOrUser("player")),
				GenericArgs.optional(GenericArgs.string("item"))
		);
		/// @formatter:on
	}

	@Override
	public boolean execute(final IUser user, final ICmdContext context) throws CommandException
	{
		final FoodRegistry foodRegistry = CoreUtilPlugin.INSTANCE.hungerHandler.foodRegistry;
		final Optional<String> item = context.get("item");
		final Optional<Player> player = context.get("player");

		final String group = null;

		Optional<FoodData> opData = Optional.empty();
		if (item.isPresent())
			opData = foodRegistry.getFoodData(item.get(), group);
		else if (player.isPresent())
			opData = foodRegistry.getFoodData(player.get().getInventory().getItemInMainHand(), group);

		if (!opData.isPresent())
			throw new CommandException("Could not detect food to debug");
		final FoodData data = opData.get();

		final StringBuilder categories = new StringBuilder();
		for (final String category : data.categories)
		{
			if (categories.length() != 0)
				categories.append(", ");
			categories.append(category);
		}
		final Message message = new Builder(String.format("Food '%s' contains '%.1f/%.1f' points and categories '%s'",
				data.name, data.food, data.food * data.ratio, categories.toString())).build();
		user.sendMessage(message);
		return true;
	}
}
