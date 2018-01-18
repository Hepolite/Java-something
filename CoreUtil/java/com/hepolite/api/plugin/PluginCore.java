package com.hepolite.api.plugin;

import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.event.HandlerCore;

public abstract class PluginCore extends JavaPlugin
{
	protected final HandlerCore handler = new HandlerCore(this);
}
