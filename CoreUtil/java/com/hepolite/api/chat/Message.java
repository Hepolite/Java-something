package com.hepolite.api.chat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.TranslatableComponent;

/**
 * The message class is designed to allow easy use of text formatting, as well as various text
 * events such as hovering text, clicking text, and more
 */
public final class Message
{
	private final BaseComponent base;
	private final Map<String, TranslatableComponent> keys;

	public Message(final BaseComponent base, final Map<String, TranslatableComponent> keys)
	{
		this.base = base;
		this.keys = keys;
	}

	/**
	 * Attempts to translate the translatable field stored under "key" if it exists
	 * 
	 * @param key The translatable component to translate
	 * @param values The values to use in the translation
	 * @return The same message for convenience
	 */
	public final Message translate(final String key, final Object... values)
	{
		if (key == null || key.isEmpty())
			throw new IllegalArgumentException("Key cannot be null or empty");
		if (!keys.containsKey(key))
			throw new IllegalArgumentException(String.format("Key %s does not exist in message", key));

		final List<BaseComponent> translations = new ArrayList<>();
		for (final Object value : values)
		{
			if (value instanceof BaseComponent)
				translations.add((BaseComponent) value);
			else
				translations.add(new TextComponent(value.toString()));
		}

		translate(keys.get(key), translations);
		return this;
	}
	private final void translate(final TranslatableComponent component, final List<BaseComponent> translations)
	{
		component.setWith(translations);
		if (component.getExtra() != null)
		{
			for (final BaseComponent extra : component.getExtra())
			{
				if (extra instanceof TranslatableComponent)
					translate((TranslatableComponent) extra, translations);
			}
		}
	}

	/**
	 * Converts the message to a plaintext version with no color formatting.
	 * 
	 * @return The string version of the message
	 */
	public String toPlain()
	{
		return ChatColor.stripColor(base.toPlainText());
	}
	/**
	 * Converts the message to a plaintext version. It may contain formatting.
	 * 
	 * @return The string version of the message
	 */
	@Override
	public final String toString()
	{
		return base.toLegacyText();
	}

	// ...

	/**
	 * Retrieves the base component of the message
	 * 
	 * @return The base component of the message
	 */
	public final BaseComponent spigot()
	{
		return base;
	}
}
