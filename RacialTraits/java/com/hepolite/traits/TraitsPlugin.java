package com.hepolite.traits;

import com.hepolite.api.plugin.IPlugin;
import com.hepolite.api.plugin.PluginCore;
import com.hepolite.traits.cmd.CmdTraits;

public final class TraitsPlugin extends PluginCore implements IPlugin
{
	private static TraitsPlugin INSTANCE;

	@Override
	public void onEnable()
	{
		INSTANCE = this;
		cmd = new CmdTraits();
	}

	// ...

	public static TraitsPlugin getInstance()
	{
		return INSTANCE;
	}
}
