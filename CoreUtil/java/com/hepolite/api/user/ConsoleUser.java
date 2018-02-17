package com.hepolite.api.user;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.hepolite.api.chat.Message;

public final class ConsoleUser implements IUser
{
	private final UUID uuid = new UUID(0, 0);

	@Override
	public UUID getUUID()
	{
		return uuid;
	}
	@Override
	public String getName()
	{
		return getNameUnformatted();
	}
	@Override
	public String getNameUnformatted()
	{
		return "CONSOLE";
	}

	@Override
	public boolean hasPermission(final String permission)
	{
		return true;
	}

	@Override
	public void sendMessage(final Message message)
	{
		Bukkit.getLogger().info(message.toPlain());
	}

	@Override
	public boolean isOnline()
	{
		return true;
	}

	@Override
	public Optional<Entity> getEntity()
	{
		return Optional.empty();
	}
	@Override
	public Optional<Player> getPlayer()
	{
		return Optional.empty();
	}
	@Override
	public Optional<OfflinePlayer> getPlayerOffline()
	{
		return Optional.empty();
	}
	@Override
	public Optional<Location> getLocation()
	{
		return Optional.empty();
	}



	@Override
	public boolean equals(final Object other)
	{
		if (other instanceof IUser)
			return uuid.equals(((IUser) other).getUUID());
		return false;
	}
	@Override
	public int hashCode()
	{
		return uuid.hashCode();
	}
	@Override
	public String toString()
	{
		return uuid.toString();
	}
}
