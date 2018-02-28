package com.hepolite.race.ui;

import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.event.HandlerCore;

public final class GuiHandler extends HandlerCore
{
	private final GuiMap guis = new GuiMap();

	public GuiHandler(final JavaPlugin plugin)
	{
		super(plugin);
	}
}
