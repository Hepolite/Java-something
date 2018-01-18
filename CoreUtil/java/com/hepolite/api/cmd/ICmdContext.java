package com.hepolite.api.cmd;

import java.util.Collection;
import java.util.Optional;

public interface ICmdContext
{
	/** Assigns the command that is to be executed using this context */
	public void setCommand(ICmd command);
	/** Returns the command that is to be executed using this context */
	public ICmd getCommand();

	/**
	 * Adds the given value under the given key. Multiple values may be specified per key, the
	 * ordering of the values will be preserved.
	 * 
	 * @param key The key to store the value under
	 * @param value The value to store
	 */
	public void put(String key, Object value);

	/**
	 * Returns true iff at least one value has been specified under the key
	 * 
	 * @param key The key to look up
	 * @return True iff at least one value has been stored
	 */
	public boolean has(String key);

	/**
	 * Returns a single value (the first inserted) under the given key, if it exists.
	 * 
	 * @param key The key to look up
	 * @return The value if it exists
	 */
	public <T> Optional<T> get(String key);
	/**
	 * Returns all values stored under the given key. If no values are stored under the given key,
	 * and empty collection is returned.
	 * 
	 * @param key The key to look up
	 * @return The collection of values
	 */
	public <T> Collection<T> getAll(String key);
}
