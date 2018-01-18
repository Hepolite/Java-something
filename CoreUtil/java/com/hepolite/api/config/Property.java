package com.hepolite.api.config;

public final class Property implements IProperty
{
	private final String path, root, name;

	public Property()
	{
		this.path = this.root = this.name = "";
	}
	public Property(final String path)
	{
		this(getRootFromPath(path), getNameFromPath(path));
	}
	public Property(final String root, final String name)
	{
		if (root == null || name == null)
			throw new IllegalArgumentException("Root and/or name cannot be null");
		if (root.startsWith("."))
			throw new IllegalArgumentException("Root cannot start with character '.'");
		if (name.isEmpty())
			throw new IllegalArgumentException("Name cannot be empty");
		if (name.contains("."))
			throw new IllegalArgumentException("Name cannot contain character '.'");

		this.path = root.isEmpty() ? name : root + "." + name;
		this.root = root;
		this.name = name;
	}

	@Override
	public String getPath()
	{
		return path;
	}
	@Override
	public String getRoot()
	{
		return root;
	}
	@Override
	public String getName()
	{
		return name;
	}

	@Override
	public IProperty parent()
	{
		if (root.isEmpty())
			return new Property();
		return new Property(root);
	}
	@Override
	public IProperty child(final IProperty child)
	{
		if (child == null)
			throw new IllegalArgumentException("Child cannot be null");
		return new Property(path, child.getPath());
	}
	@Override
	public IProperty child(final String child)
	{
		return new Property(path, child);
	}

	// ...

	private static String getRootFromPath(final String path)
	{
		if (path == null)
			throw new IllegalArgumentException("Path cannot be null");

		final int index = path.lastIndexOf('.');
		if (index == -1)
			return "";
		return path.substring(0, index);
	}
	private static String getNameFromPath(final String path)
	{
		if (path == null)
			throw new IllegalArgumentException("Path cannot be null");

		final int index = path.lastIndexOf('.');
		if (index == -1)
			return path;
		return path.substring(index + 1);
	}

	// ...

	@Override
	public final String toString()
	{
		return getPath();
	}
	@Override
	public final boolean equals(final Object other)
	{
		if (other instanceof IProperty)
			return getPath().equals(((IProperty) other).getPath());
		return false;
	}
}
