package com.hepolite.race.cmd;

import java.util.Optional;

import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import com.hepolite.api.chat.Builder;
import com.hepolite.api.chat.Format;
import com.hepolite.api.cmd.Cmd;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.user.IUser;
import com.hepolite.race.RacialTraitsPlugin;
import com.hepolite.race.account.Account;
import com.hepolite.race.account.Account.Data;

public class CmdShowInfo extends Cmd
{
	public CmdShowInfo()
	{
		/// @formatter:off
		super("info",
			GenericArgs.playerOrUser("player"),
			GenericArgs.optional(GenericArgs.intNum("id"))
		);
		/// @formatter:on
	}

	@Override
	public boolean execute(final IUser user, final ICmdContext context) throws CommandException
	{
		final Player player = context.<Player> get("player").get();
		final Account account = RacialTraitsPlugin.getAccountHandler().getAccount(player);
		final int id = context.<Integer> get("id").orElse(account.getActiveId());

		final Builder builder = new Builder("");

		final Optional<Data> opData = account.getData(id);
		if (opData.isPresent())
		{
			final Data data = opData.get();

			builder.addText(String.format("&9Player &b%s &9(&baccount %d&9):\n", player.getName(), id),
					Format.UNDERLINE);
			builder.addText(String.format("&9Race: &b%s\n", data.race == null ? "None" : data.race.getName()));
			builder.addText(String.format("&9Level: &b%d &9(xp: &b%d/%d&9)\n", data.level, data.xp, 0));
			builder.addText(String.format("&9Skill/attribute tokens: &b%d/%d", data.skillTokens, data.attributeTokens));
		}
		else
			builder.addText(String.format("&cCould not find data for &e%s&c (account: &e%d&c)", player.getName(), id));
		user.sendMessage(builder.build());
		return true;
	}
}
