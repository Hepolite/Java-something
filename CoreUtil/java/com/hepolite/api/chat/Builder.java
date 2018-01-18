package com.hepolite.api.chat;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.ComponentBuilder.FormatRetention;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

/**
 * Provides a simple and easy way to build new messages
 */
public class Builder
{
	private final BaseComponent base;
	private BaseComponent current;

	/**
	 * Appends another text block to the finalized message.
	 * 
	 * @param data The parameters to add. Colors, formats and strings are accepted
	 * @return The same builder for convenience
	 */
	public Builder(final Object... data)
	{
		base = new TextComponent();
		text(data);
	}

	/**
	 * Appends another text block to the finalized message.
	 * 
	 * @param data The parameters to add. Colors, formats and strings are accepted
	 * @return The same builder for convenience
	 */
	public Builder text(final Object... data)
	{
		current = new TextComponent();
		for (final BaseComponent component : construct(data))
			current.addExtra(component);
		base.addExtra(current);
		return this;
	}
	/**
	 * Appends some hover text if the user hovers over the previously specified text block.
	 * 
	 * @param data The parameters to add. Colors, formats and strings are accepted
	 * @return The same builder for convenience
	 */
	public Builder hover(final Object... data)
	{
		current.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, construct(data)));
		return this;
	}
	/**
	 * Appends a command to be executed if the user clicks the previously specified text block.
	 * 
	 * @param command The command that will be executed as the user
	 * @return The same builder for convenience
	 */
	public Builder cmd(final String command)
	{
		current.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, command));
		return this;
	}

	/**
	 * Finalizes the builder and constructs a message instance from it
	 * 
	 * @return The message that was built
	 */
	public final Message build()
	{
		return new Message(base);
	}

	// ...

	private final BaseComponent[] construct(final Object... data)
	{
		final ComponentBuilder b = new ComponentBuilder("");
		for (final Object object : data)
		{
			if (object instanceof String)
				b.append((String) object, FormatRetention.EVENTS);
			else if (object instanceof Color)
				b.color(((Color) object).get());
			else if (object instanceof Format)
			{
				switch ((Format) object)
				{
				case BOLD:
					b.bold(true);
					break;
				case ITALIC:
					b.italic(true);
					break;
				case OBFUSCATE:
					b.obfuscated(true);
					break;
				case STRIKETHROUGH:
					b.strikethrough(true);
					break;
				case UNDERLINE:
					b.underlined(true);
					break;
				}
			}
		}
		return b.create();
	}
}
