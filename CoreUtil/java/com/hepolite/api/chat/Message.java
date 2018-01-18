package com.hepolite.api.chat;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;

/**
 * The message class is designed to allow easy use of text formatting, as well as various text
 * events such as hovering text, clicking text, and more
 */
public final class Message
{
	private final BaseComponent base;

	public Message(final BaseComponent base)
	{
		this.base = base;
	}

	/**
	 * Converts the message to a plaintext version with no color formatting.
	 * 
	 * @return The string version of the message
	 */
	public String toCleaned()
	{
		return ChatColor.stripColor(toPlain());
	}
	/**
	 * Converts the message to a plaintext version. It may contain formatting.
	 * 
	 * @return The string version of the message
	 */
	public final String toPlain()
	{
		return base.toPlainText();
	}



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
