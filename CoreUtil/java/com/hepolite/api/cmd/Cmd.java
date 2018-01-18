package com.hepolite.api.cmd;

import java.util.Optional;

import com.hepolite.api.cmd.element.ICmdElement;

public abstract class Cmd implements ICmd
{
	private final String name;
	private final String permission;
	private final ICmdElement element;

	protected Cmd(final String name, final ICmdElement... elements)
	{
		this(name, null, elements);
	}
	protected Cmd(final String name, final String permission, final ICmdElement... elements)
	{
		if (name == null)
			throw new IllegalArgumentException("Name cannot be null");
		if (elements == null)
			throw new IllegalArgumentException("Element cannot be null");
		this.name = name;
		this.permission = permission;
		this.element = GenericArgs.sequence(elements);
	}

	@Override
	public final String getName()
	{
		return name;
	}
	@Override
	public final Optional<String> getPermission()
	{
		return Optional.ofNullable(permission);
	}
	@Override
	public final ICmdElement getCommandElement()
	{
		return element;
	}
}
