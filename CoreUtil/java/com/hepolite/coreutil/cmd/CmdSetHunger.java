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

public class CmdSetHunger extends Cmd
{
	public CmdSetHunger()
	{
		/// @formatter:off
		super("hunger",
			GenericArgs.playerOrUser("player"),
			GenericArgs.floatNum("hunger"),
			GenericArgs.floatNum("saturation")
		);
		/// @formatter:on
	}

	@Override
	public boolean execute(final IUser user, final ICmdContext context) throws CommandException
	{
		final Player player = context.<Player> get("player").get();
		final float hunger = context.<Float> get("hunger").get();
		final float saturation = context.<Float> get("saturation").get();

		final HungerData data = CoreUtilPlugin.getHungerHandler().getHungerData(player);
		data.hunger = hunger;
		data.saturation = saturation;

		user.sendMessage(new Builder(String.format("&bChanged &9%s's&b hunger and saturation to &9%.1f/%.1f",
				player.getName(), data.hunger, data.saturation)).build());
		return true;
	}
}
