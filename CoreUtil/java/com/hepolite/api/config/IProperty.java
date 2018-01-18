package com.hepolite.api.config;

/**
 * Specifies a property stored within a {@link IConfig} configuration instance
 */
public interface IProperty
{
	/**
	 * Converts the property to a string representing the location of the property
	 * 
	 * @return The path in the underlying configuration
	 */
	public String getPath();
	/**
	 * Returns the root of the path for this property, where the path is "root.name"
	 * 
	 * @return The root of the path
	 */
	public String getRoot();
	/**
	 * Returns the name of the path for this property, where the path is "root.name". If the root
	 * does not exist, the name is equal to the path
	 * 
	 * @return The name of the path
	 */
	public String getName();

	/**
	 * Constructs a new property which has the same path as the root of this property. If this
	 * property has no root, null is returned
	 * 
	 * @return A property which has a path equal to the root of this property
	 */
	public IProperty parent();
	/**
	 * Constructs a new property which has the child property appended to the path of the parent
	 * property.
	 * 
	 * @param child The child property to append
	 * @return A property with the path "parentPath.childName"
	 */
	public IProperty child(IProperty child);
	/**
	 * Constructs a new property which has the child property appended to the path of the parent
	 * property.
	 * 
	 * @param child The path of the child property to append
	 * @return A property with the path "parentPath.child"
	 */
	public IProperty child(String child);
}
