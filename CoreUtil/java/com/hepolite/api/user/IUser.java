package com.hepolite.api.user;

import java.util.Optional;
import java.util.UUID;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import com.hepolite.api.chat.Message;

/**
 * In order to unify access data related to players, entities, even the console, the data should be
 * stored under a specific user. Various utility functions are available for users as well.
 */
public interface IUser
{
	/**
	 * Retrieves the unique id that identifies this specific user
	 * 
	 * @return The UUID of the user
	 */
	public UUID getUUID();
	/**
	 * Retrieves the name that identifies this specific user. The name may be formatted if the
	 * underlying user has some special name. For players this will simply be their display name (or
	 * username if no display name has been specified), for other entities it will be the custom
	 * name (or entity type if not custom name has been specified).
	 * 
	 * @return The name of the user
	 */
	public String getName();
	/**
	 * Returns the name that identifies the specific user. The name will not contain any formatting.
	 * For players this will simply be their username, for other entities it will be the entity
	 * type.
	 * 
	 * @return The name of the user without formatting
	 */
	public String getNameUnformatted();

	/**
	 * Returns whether the user has the given permission node or not
	 * 
	 * @param permission The permission to check for
	 * @return True iff the player has the given permission node
	 */
	public boolean hasPermission(String permission);

	/**
	 * Sends the specified message to the user. If the user is not available, this does nothing.
	 * 
	 * @param message The message to send
	 */
	public void sendMessage(Message message);

	// ...

	/**
	 * Returns whether the user is online or not
	 * 
	 * @return True iff the user is on the server
	 */
	public boolean isOnline();

	/**
	 * Attempts to return the user as an entity, if possible
	 * 
	 * @return The underlying entity if it exists
	 */
	public Optional<Entity> getEntity();
	/**
	 * Attempts to return the user as a player, if possible
	 * 
	 * @return The underlying player if it exists
	 */
	public Optional<Player> getPlayer();
	/**
	 * Attempts to return the user as an offline player, if possible
	 * 
	 * @return The underlying offline player if it exists
	 */
	public Optional<OfflinePlayer> getPlayerOffline();

	/**
	 * Attempts to return the location of the user, if possible
	 * 
	 * @return The location of the underlying entity, if it exists
	 */
	public Optional<Location> getLocation();
}
