package com.hepolite.api.chat;

import net.md_5.bungee.api.ChatColor;

/**
 * Shorter names for all forms of colors
 */
public enum Color
{
	AQUA(ChatColor.AQUA),
	AQUA_DARK(ChatColor.DARK_AQUA),
	BLACK(ChatColor.BLACK),
	BLUE(ChatColor.BLUE),
	BLUE_DARK(ChatColor.DARK_BLUE),
	GRAY(ChatColor.GRAY),
	GRAY_DARK(ChatColor.DARK_GRAY),
	GREEN(ChatColor.GREEN),
	GREEN_DARK(ChatColor.DARK_GREEN),
	PURPLE(ChatColor.DARK_PURPLE),
	PURPLE_DARK(ChatColor.LIGHT_PURPLE),
	RED(ChatColor.RED),
	RED_DARK(ChatColor.DARK_RED),
	YELLOW(ChatColor.YELLOW),
	YELLOW_DARK(ChatColor.GOLD),
	WHITE(ChatColor.WHITE);

	private final ChatColor chatColor;

	Color(final ChatColor chatColor)
	{
		this.chatColor = chatColor;
	}

	/**
	 * Returns the underlying {@link ChatColor} color
	 */
	public final ChatColor get()
	{
		return chatColor;
	}
}
