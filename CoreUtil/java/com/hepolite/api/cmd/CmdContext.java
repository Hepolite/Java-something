package com.hepolite.api.cmd;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

public class CmdContext implements ICmdContext
{
	private final Multimap<String, Object> args = ArrayListMultimap.create();
	private ICmd command = null;

	public CmdContext()
	{}
	public CmdContext(final ICmd command)
	{
		setCommand(command);
	}

	@Override
	public void setCommand(final ICmd command)
	{
		this.command = command;
	}
	@Override
	public ICmd getCommand()
	{
		return command;
	}

	@Override
	public void put(final String key, final Object value)
	{
		if (value == null)
			throw new IllegalArgumentException("Value cannot be null");
		args.put(key, value);
	}

	@Override
	public boolean has(final String key)
	{
		return args.containsKey(key);
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> Collection<T> getAll(final String key)
	{
		return Collections.unmodifiableCollection((Collection<T>) args.get(key));
	}
	@Override
	public <T> Optional<T> get(final String key)
	{
		final Collection<T> all = getAll(key);
		if (all.isEmpty())
			return Optional.empty();
		return Optional.of(all.iterator().next());
	}
}
