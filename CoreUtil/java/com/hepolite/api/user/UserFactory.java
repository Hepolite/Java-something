package com.hepolite.api.user;

import java.util.UUID;

import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public final class UserFactory
{
	/**
	 * Constructs a new user instance from the given uuid
	 * 
	 * @param uuid The uuid of the user
	 * @return The new user instance
	 */
	public static IUser fromUUID(final UUID uuid)
	{
		if (uuid == null)
			throw new IllegalArgumentException("UUID cannot be null");

		if (uuid.getLeastSignificantBits() == 0 && uuid.getMostSignificantBits() == 0)
			return new ConsoleUser();
		else
			return new EntityUser(uuid);
	}
	/**
	 * Constructs a new user instance from the given uuid in string form
	 * 
	 * @param uuid The uuid of the user
	 * @return The new user instance
	 */
	public static IUser fromUUID(final String uuid)
	{
		if (uuid == null || uuid.isEmpty())
			throw new IllegalArgumentException("String cannot be null or empty");
		return fromUUID(UUID.fromString(uuid));
	}
	/**
	 * Constructs a new user instance from the given player
	 * 
	 * @param player The player of the users
	 * @return The new user instance
	 */
	public static IUser fromPlayer(final Player player)
	{
		return fromUUID(player.getUniqueId());
	}
	/**
	 * Constructs a new user instance from the given entity
	 * 
	 * @param player The player of the users
	 * @return The new user instance
	 */
	public static IUser fromEntity(final LivingEntity entity)
	{
		return fromUUID(entity.getUniqueId());
	}
}
