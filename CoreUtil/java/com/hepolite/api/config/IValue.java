package com.hepolite.api.config;

/**
 * Represents an arbitrary value which may be stored in an {@link IConfig}.
 */
public interface IValue
{
	/**
	 * Store the underlying value to the given configuration at the specified property
	 * 
	 * @param config The configuration where the value will be stored
	 * @param property The path in the underlying configuration
	 * @return True iff the value could be stored in the config
	 */
	public void save(IConfig config, IProperty property);
	/**
	 * Loads the underlying value from the given configuration at the specified property
	 * 
	 * @param config The configuration where the value will be stored
	 * @param property The path in the underlying configuration
	 * @return True iff the value could be stored in the config
	 */
	public void load(IConfig config, IProperty property);
}
