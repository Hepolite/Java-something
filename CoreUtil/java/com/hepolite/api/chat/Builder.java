package com.hepolite.api.chat;

import java.util.HashMap;
import java.util.Map;

import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;

/**
 * Provides a simple and easy way to build new messages
 */
public class Builder
{
	private final BaseComponent base;
	private BaseComponent current;

	private final Map<String, TranslatableComponent> keys = new HashMap<>();

	/**
	 * Appends another text block to the finalized message.
	 * 
	 * @param data The parameters to add. Colors, formats and strings are accepted
	 * @return The same builder for convenience
	 */
	public Builder(final String text, final Object... format)
	{
		base = new TextComponent(text);
		current = base;
		applyFormat(base, format);
	}

	/**
	 * Appends another text block to the finalized message.
	 * 
	 * @param text The text to add to the message
	 * @param data The parameters to add. Colors, formats and strings are accepted
	 * @return The same builder for convenience
	 */
	public Builder addText(final String text, final Object... format)
	{
		current = new TextComponent(text);
		base.addExtra(current);
		applyFormat(current, format);
		return this;
	}
	/**
	 * Appends another translated text block to the finalized message. The translation values are
	 * specified similar to the way {@link java.lang.String#format}'s format string works. <br>
	 * <br>
	 * Example: <br>
	 * addTranslation("key", "float: %2$.2f, string: %1$s"); <br>
	 * translate("key", "This is my string", 3.14f);
	 * 
	 * @param key The key needed to include the translation values
	 * @param text The text message that is to be translated
	 * @param format The coloring and format of the text
	 * @return The same builder for convenience
	 */
	public Builder addTranslatedText(final String key, final String text, final Object... format)
	{
		current = new TranslatableComponent(text);
		base.addExtra(current);
		keys.put(key, (TranslatableComponent) current);
		applyFormat(current, format);
		return this;
	}
	/**
	 * Appends some hover text if the user hovers over the previously specified text block.
	 * 
	 * @param text The text to display while hovering the current text
	 * @param data The parameters to add. Colors, formats and strings are accepted
	 * @return The same builder for convenience
	 */
	public Builder addHover(final String text, final Object... format)
	{
		final BaseComponent component = new TextComponent(text);
		applyFormat(component, format);
		current.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new BaseComponent[] {
			component
		}));
		return this;
	}
	/**
	 * Appends a command to be executed if the user clicks the previously specified text block.
	 * 
	 * @param command The command that will be executed as the user
	 * @return The same builder for convenience
	 */
	public Builder addCommand(final String command)
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
		return new Message(base, keys);
	}

	// ...

	private final void applyFormat(final BaseComponent component, final Object... format)
	{
		for (final Object object : format)
		{
			if (object instanceof Color)
				component.setColor(((Color) object).get());
			else if (object instanceof Format)
			{
				switch ((Format) object)
				{
				case BOLD:
					component.setBold(true);
					break;
				case ITALIC:
					component.setItalic(true);
					break;
				case OBFUSCATE:
					component.setObfuscated(true);
					break;
				case STRIKETHROUGH:
					component.setStrikethrough(true);
					break;
				case UNDERLINE:
					component.setUnderlined(true);
					break;
				}
			}
		}
	}
}
