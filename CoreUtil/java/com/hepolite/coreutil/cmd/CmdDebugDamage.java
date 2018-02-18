package com.hepolite.coreutil.cmd;

import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import com.hepolite.api.chat.Builder;
import com.hepolite.api.cmd.Cmd;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.damage.Damage;
import com.hepolite.api.damage.DamageAPI;
import com.hepolite.api.damage.DamageType;
import com.hepolite.api.damage.DamageVariant;
import com.hepolite.api.user.IUser;

public class CmdDebugDamage extends Cmd
{
	public CmdDebugDamage()
	{
		/// @formatter:off
		super("damage",
			GenericArgs.playerOrUser("target"),
			GenericArgs.optional(GenericArgs.player("attacker")),
			GenericArgs.string("variant"),
			GenericArgs.string("type"),
			GenericArgs.doubleNum("amount")
		);
		/// @formatter:on
	}

	@Override
	public boolean execute(final IUser user, final ICmdContext context) throws CommandException
	{
		final Player target = context.<Player> get("target").get();
		final Player attacker = context.<Player> get("attacker").orElse(null);
		final String variant = context.<String> get("variant").orElse("invalid");
		final String type = context.<String> get("type").orElse("invalid");
		final double amount = context.<Double> get("amount").orElse(0.0);

		try
		{
			final DamageVariant damageVariant = DamageVariant.valueOf(variant.toUpperCase());
			final DamageType damageType = DamageType.valueOf(type.toUpperCase());

			if (DamageAPI.damage(target, attacker, new Damage(damageVariant, damageType, amount)))
			{
				final Builder builder = new Builder("");
				builder.addText(String.format("&bDealt &9'%.1f'&b damage to &9'%s'", amount, target.getName()));
				builder.addHover(String.format("&bDamage variant/type: &9'%s/%s'", variant, type));
				if (attacker != null)
					builder.addText(String.format("&b from &9'%s'", attacker.getName()));
				user.sendMessage(builder.build());
			}
			else
			{
				final Builder builder = new Builder("");
				builder.addText(String.format("&cFailed to damage &e'%s'", target.getName()));
				if (attacker != null)
					builder.addText(String.format("&c from &e'%s'", attacker.getName()));
				user.sendMessage(builder.build());
			}
		}
		catch (final Exception e)
		{
			final Builder builder = new Builder(
					String.format("&bInvalid variant &9'%s'&b or type &9'%s'", variant, type));
			user.sendMessage(builder.build());
		}
		return true;
	}
}
