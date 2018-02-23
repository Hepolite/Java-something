package com.hepolite.coreutil.cmd;

import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import com.hepolite.api.chat.Builder;
import com.hepolite.api.cmd.Cmd;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.user.IUser;
import com.hepolite.coreutil.CoreUtilPlugin;
import com.hepolite.coreutil.hunger.HungerData;
import com.hepolite.coreutil.hunger.HungerSystem;

public class CmdDebugHunger extends Cmd
{
	public CmdDebugHunger()
	{
		/// @formatter:off
		super("hunger",
			GenericArgs.firstParsing(
				GenericArgs.children(new CmdDebugHungerSet()),
				GenericArgs.playerOrUser("player")
			)
		);
		/// @formatter:on
	}

	@Override
	public boolean execute(final IUser user, final ICmdContext context) throws CommandException
	{
		final HungerSystem hungerHandler = CoreUtilPlugin.getHungerHandler();

		final Player player = context.<Player> get("player").get();
		final HungerData data = hungerHandler.getHungerData(player);
		displayGenericHungerInfo(user, player, data);
		return true;
	}

	/**
	 * Displays generic information about the hunger to the specified user
	 * 
	 * @param user The user that should see the information
	 * @param data The hunger to provide information about
	 */
	private void displayGenericHungerInfo(final IUser user, final Player player, final HungerData data)
	{
		final Builder builder = new Builder("");
		builder.addText(
				String.format("&bPlayer: &9%s&b - Group: &9%s\n&bHunger/Saturation (Consumption): &9%.1f/%.1f (%.3f)",
						player.getName(), data.group, data.hunger, data.saturation, data.consumption));
		user.sendMessage(builder.build());
	}
}
