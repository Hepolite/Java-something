package com.hepolite.coreutil.cmd;

import org.bukkit.attribute.Attribute;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import com.hepolite.api.chat.Builder;
import com.hepolite.api.cmd.Cmd;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.damage.DamageAPI;
import com.hepolite.api.damage.Heal;
import com.hepolite.api.damage.HealType;
import com.hepolite.api.user.IUser;

public class CmdDebugHeal extends Cmd
{
	public CmdDebugHeal()
	{
		/// @formatter:off
		super("heal",
			GenericArgs.playerOrUser("target"),
			GenericArgs.optional(GenericArgs.player("healer")),
			GenericArgs.string("type"),
			GenericArgs.optional(GenericArgs.doubleNum("amount"))
		);
		/// @formatter:on
	}

	@Override
	public boolean execute(final IUser user, final ICmdContext context) throws CommandException
	{
		final Player target = context.<Player> get("target").get();
		final Player healer = context.<Player> get("healer").orElse(null);
		final String type = context.<String> get("type").orElse("invalid");
		final double amount = context.<Double> get("amount")
				.orElse(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());

		try
		{
			final HealType healType = HealType.valueOf(type.toUpperCase());

			if (DamageAPI.heal(target, healer, new Heal(healType, amount)))
			{
				final Builder builder = new Builder("");
				builder.addText(String.format("&bHealed &9'%s'&b for &9'%.1f'&b health", target.getName(), amount));
				builder.addHover(String.format("&bHeal type: &9'%s'", type));
				if (healer != null)
					builder.addText(String.format("&b from &9'%s'", healer.getName()));
				user.sendMessage(builder.build());
			}
			else
			{
				final Builder builder = new Builder("");
				builder.addText(String.format("&cFailed to heal &e'%s'", target.getName()));
				if (healer != null)
					builder.addText(String.format("&c from &e'%s'", healer.getName()));
				user.sendMessage(builder.build());
			}
		}
		catch (final Exception e)
		{
			final Builder builder = new Builder(String.format("&bInvalid type &9'%s'", type));
			user.sendMessage(builder.build());
		}
		return true;
	}
}
