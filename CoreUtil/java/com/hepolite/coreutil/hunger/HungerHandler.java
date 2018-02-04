package com.hepolite.coreutil.hunger;

import org.bukkit.plugin.java.JavaPlugin;

import com.hepolite.api.event.HandlerCore;

public class HungerHandler extends HandlerCore
{
	public final FoodRegistry foodRegistry;

	public HungerHandler(final JavaPlugin plugin)
	{
		super(plugin);
		foodRegistry = new FoodRegistry(plugin);
	}

	@Override
	public void onTick(final int tick)
	{}
	@Override
	public void onReload()
	{
		foodRegistry.loadFoods();
	}
	
	// ...
	
	
}
