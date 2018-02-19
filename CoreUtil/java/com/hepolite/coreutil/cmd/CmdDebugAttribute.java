package com.hepolite.coreutil.cmd;

import java.util.Collection;
import java.util.Optional;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import com.hepolite.api.attribute.Attribute;
import com.hepolite.api.attribute.AttributeDatabase;
import com.hepolite.api.attribute.Modifier;
import com.hepolite.api.chat.Builder;
import com.hepolite.api.cmd.Cmd;
import com.hepolite.api.cmd.GenericArgs;
import com.hepolite.api.cmd.ICmdContext;
import com.hepolite.api.user.IUser;
import com.hepolite.api.user.UserFactory;

public class CmdDebugAttribute extends Cmd
{
	public CmdDebugAttribute()
	{
		/// @formatter:off
		super("attribute",
			GenericArgs.playerOrUser("player"),
			GenericArgs.string("attribute"),
			GenericArgs.optional(
				GenericArgs.string("modifier"),
				GenericArgs.floatNum("multiplier"),
				GenericArgs.floatNum("scale"),
				GenericArgs.floatNum("constant")
			)
		);
		/// @formatter:on
	}

	@Override
	public boolean execute(final IUser user, final ICmdContext context) throws CommandException
	{
		final IUser player = UserFactory.fromPlayer(context.<Player> get("player").get());
		final String name = context.<String> get("attribute").get();
		final Attribute attribute = AttributeDatabase.get(player, name);

		final Optional<String> modifier = context.get("modifier");
		final Optional<Float> multiplier = context.get("multiplier");
		final Optional<Float> scale = context.get("scale");
		final Optional<Float> constant = context.get("constant");
		if (modifier.isPresent())
			attribute.put(modifier.get(), Modifier.from(multiplier.get(), scale.get(), constant.get()));

		final Builder builder = new Builder("");
		displayAttribute(builder, name, attribute);
		user.sendMessage(builder.build());
		return true;
	}

	/**
	 * Displays all information about the specified attribute
	 * 
	 * @param builder The builder to add data to
	 * @param name The name of the attribute
	 * @param attribute The attribute itself
	 */
	private void displayAttribute(final Builder builder, final String name, final Attribute attribute)
	{
		final Modifier total = attribute.getModifier();

		final StringBuilder data = new StringBuilder();
		data.append(String.format("&bAttribute &9'%s'&b", name));
		data.append(String.format("&b base/current: &9%.1f/%.1f", attribute.getBaseValue(), attribute.getValue()));
		if (attribute.getMinValue() != -Float.MAX_VALUE)
			data.append(String.format("&b min: &9%.1f", attribute.getMinValue()));
		if (attribute.getMaxValue() != Float.MAX_VALUE)
			data.append(String.format("&b max: &9%.1f", attribute.getMaxValue()));
		data.append(String.format("\n%s", buildModifier("total", total)));
		builder.addText(data.toString());

		final Collection<String> modifiers = new TreeSet<>();
		for (final String key : attribute.getKeys())
			modifiers.add(buildModifier(key, attribute.get(key).get()));
		if (!modifiers.isEmpty())
			builder.addHover(StringUtils.join(modifiers, "\n"));
	}
	/**
	 * Builds a string that represents the given modifier
	 * 
	 * @param key The key associated with the modifier
	 * @param modifier The modifier itself
	 * @return
	 */
	private String buildModifier(final String key, final Modifier modifier)
	{
		return String.format("&bModifier &9'%s'&b (multiplier/scale | constant): &9%.3f/%.3f | %.1f&b", key,
				modifier.multiplier, modifier.scale, modifier.constant);
	}
}
