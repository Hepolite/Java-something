package com.hepolite.api.user;

import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.hepolite.api.chat.Message;

import ru.tehkode.permissions.PermissionUser;
import ru.tehkode.permissions.bukkit.PermissionsEx;

public final class EntityUser implements IUser
{
	private final UUID uuid;

	public EntityUser()
	{
		this(UUID.randomUUID());
	}
	public EntityUser(final UUID uuid)
	{
		this.uuid = uuid;
	}

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
		throw new NotImplementedException();
	}

	@Override
	public boolean hasPermission(final String permission)
	{
		final Optional<Player> opPlayer = getPlayer();
		if (!opPlayer.isPresent())
			return false;
		final PermissionUser user = PermissionsEx.getUser(opPlayer.get());
		if (user == null)
			return false;
		return user.has(permission);
	}

	@Override
	public void sendMessage(final Message message)
	{
		throw new NotImplementedException();
	}

	@Override
	public boolean isOnline()
	{
		throw new NotImplementedException();
	}

	@Override
	public Optional<Entity> getEntity()
	{
		throw new NotImplementedException();
	}
	@Override
	public Optional<Player> getPlayer()
	{
		return Optional.ofNullable(Bukkit.getPlayer(uuid));
	}
	@Override
	public Optional<OfflinePlayer> getPlayerOffline()
	{
		throw new NotImplementedException();
	}
	@Override
	public Optional<Location> getLocation()
	{
		throw new NotImplementedException();
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
