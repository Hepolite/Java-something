package com.hepolite.race;

import com.hepolite.api.plugin.IPlugin;
import com.hepolite.api.plugin.PluginCore;
import com.hepolite.race.account.AccountHandler;
import com.hepolite.race.cmd.CmdRacialTraits;
import com.hepolite.race.ui.GuiHandler;

public final class RacialTraitsPlugin extends PluginCore implements IPlugin
{
	private static RacialTraitsPlugin INSTANCE;
	private AccountHandler accountHandler;
	private GuiHandler guiHandler;

	@Override
	public void onEnable()
	{
		INSTANCE = this;
		cmd = new CmdRacialTraits();

		accountHandler = handler.register(new AccountHandler(this));
		guiHandler = handler.register(new GuiHandler(this));
	}

	// ...

	public static RacialTraitsPlugin getInstance()
	{
		return INSTANCE;
	}
	public static AccountHandler getAccountHandler()
	{
		return INSTANCE.accountHandler;
	}
	public static GuiHandler getGuiHandler()
	{
		return INSTANCE.guiHandler;
	}

	public static final void INFO(final String msg)
	{
		INSTANCE.getLogger().info(msg);
	}
	public static final void WARN(final String msg)
	{
		INSTANCE.getLogger().warning(msg);
	}
	public static final void FATAL(final String msg)
	{
		INSTANCE.getLogger().severe("!!FATAL!! " + msg);
	}
}
