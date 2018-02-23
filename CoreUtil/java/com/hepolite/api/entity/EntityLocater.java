package com.hepolite.api.entity;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.hepolite.api.util.Raytracer;
import com.hepolite.api.util.Shapes.Box;
import com.hepolite.api.util.Shapes.Shape;

public class EntityLocater<S extends Shape>
{
	private final World world;
	private final S shape;

	public EntityLocater(final World world, final S shape)
	{
		this.world = world;
		this.shape = shape;
	}

	/**
	 * @return The shape associated with this locater
	 */
	public S getShape()
	{
		return shape;
	}

	// ...

	/**
	 * Locates all entities of the given type that exists inside the shape
	 * 
	 * @param entityClass The entity class to search for
	 * @return A collection of all the found entities
	 */
	public final <T extends Entity> Collection<T> getAll(final Class<T> entityClass)
	{
		final Collection<T> entities = world.getEntitiesByClass(entityClass);
		final Collection<T> collection = new ArrayList<>();
		for (final T entity : entities)
		{
			if (shape.intersects(EntityAPI.getBBox(entity)))
				collection.add(entity);
		}
		return collection;
	}
	/**
	 * Locates all entities of the given type that exists inside the shape, that have a clear path
	 * to the specified location
	 * 
	 * @param entityClass The entity class to search for
	 * @param location The location to perform checks from
	 * @param hitFluids Whether fluids should be counted as solid or not. WARNING: Blocks without
	 *            collision boxes will ALSO beconsidered solid in this mode!
	 * @return A collection of all the found entities
	 */
	public final <T extends Entity> Collection<T> getAllUnobstructed(final Class<T> entityClass,
			final Location location, final boolean hitFluids)
	{
		final Collection<T> collection = new ArrayList<>();
		for (final T entity : getAll(entityClass))
		{
			if (validateRaytrace(location, entity.getLocation(), hitFluids))
				collection.add(entity);
			else if (entity instanceof LivingEntity)
			{
				final LivingEntity living = (LivingEntity) entity;
				if (validateRaytrace(location, living.getEyeLocation(), hitFluids))
					collection.add(entity);
			}
		}
		return collection;
	}

	/**
	 * Locates the nearest entity of the given type that exists inside the shape, optionally
	 * ignoring the specified entity
	 * 
	 * @param entityClass The entity class to search for
	 * @param location The location to measure distance from
	 * @param ignore The entity to ignore
	 * @return The nearest entity or null if none were found
	 */
	public final <T extends Entity> T getNearest(final Class<T> entityClass, final Location location,
			final Entity ignore)
	{
		return getNearest(getAll(entityClass), location, ignore);
	}
	/**
	 * Locates the nearest entity of the given type that exists inside the shape, optionally
	 * ignoring the specified entity, that has a clear path to the specified location
	 * 
	 * @param entityClass The entity class to search for
	 * @param location The location to measure distance from
	 * @param hitFluids Whether fluids should be counted as solid or not. WARNING: Blocks without
	 *            collision boxes will ALSO beconsidered solid in this mode!
	 * @param ignore The entity to ignore
	 * @return The nearest entity or null if none were found
	 */
	public final <T extends Entity> T getNearestUnobstructed(final Class<T> entityClass, final Location location,
			final boolean hitFluids, final Entity ignore)
	{
		return getNearest(getAllUnobstructed(entityClass, location, hitFluids), location, ignore);
	}

	// ...

	/**
	 * Checks if the path from the starting location to the target location is clear or not
	 * 
	 * @param origin The starting location
	 * @param entity The target location
	 * @param hitFluids Whether fluids should count as solid or not
	 * @return True iff the entity can see the target
	 */
	private final boolean validateRaytrace(final Location origin, final Location target, final boolean hitFluids)
	{
		return Raytracer.rayTrace(origin, target, hitFluids, !hitFluids, false).block == null;
	}

	/**
	 * Returns the nearest entitiy of the entities provided
	 * 
	 * @param entities The entities to look through
	 * @param location The location determining the origin
	 * @param ignore The entity to ignore, if any
	 * @return The nearest entity or null if none were provided
	 */
	private final <T extends Entity> T getNearest(final Collection<T> entities, final Location location,
			final Entity ignore)
	{
		T nearest = null;
		double distance = Double.MAX_VALUE;
		for (final T entity : entities)
		{
			if (entity == ignore)
				continue;

			final Box bbox = EntityAPI.getBBox(entity);
			final Vector bboxCenter = new Vector(0.5 * (bbox.minX() + bbox.maxX()), 0.5 * (bbox.minY() + bbox.maxY()),
					0.5 * (bbox.minZ() + bbox.maxZ()));

			final double currentDistance = location.toVector().distanceSquared(bboxCenter);
			if (currentDistance < distance)
			{
				distance = currentDistance;
				nearest = entity;
			}
		}
		return nearest;
	}
}
