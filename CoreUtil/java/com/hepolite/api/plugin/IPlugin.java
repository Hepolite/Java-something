package com.hepolite.api.plugin;

public interface IPlugin
{
	/**
	 * Invoked when the plugin is enabled; all resources should be loaded up here
	 */
	public void onEnable();
	/**
	 * Invoked when the plugin is disabled or shutting down; all persistent resources should be
	 * saved here
	 */
	public void onDisable();
	/**
	 * Invoked when the plugin is reloaded; make sure that persistent data is saved and that
	 * relevant data is reloaded
	 */
	public void onReload();

	/**
	 * Invoked each and every tick for as long as the server is running
	 * 
	 * @param tick The current tick number
	 */
	public void onTick(int tick);
}
