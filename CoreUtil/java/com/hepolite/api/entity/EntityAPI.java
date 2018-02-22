package com.hepolite.api.entity;

import org.bukkit.entity.Entity;

import com.hepolite.api.util.Shapes.Box;
import com.hepolite.coreutil.util.reflection.reflected.RAxisAlignedBB;
import com.hepolite.coreutil.util.reflection.reflected.REntity;

public final class EntityAPI
{
	/**
	 * Attempts to retrieve the bounding box for the given entity
	 * 
	 * @param entity The entity to work with
	 * @return The bounding box or a zero-sized box if it could not be obtained
	 */
	public static Box getBBox(final Entity entity)
	{
		final Object entityHandle = REntity.cbGetHandle.invoke(entity);
		final Object aabb = REntity.nmsGetBoundingBox.invoke(entityHandle);

		final float x1 = (float) (double) RAxisAlignedBB.nmsMinX.get(aabb);
		final float y1 = (float) (double) RAxisAlignedBB.nmsMinY.get(aabb);
		final float z1 = (float) (double) RAxisAlignedBB.nmsMinZ.get(aabb);
		final float x2 = (float) (double) RAxisAlignedBB.nmsMaxX.get(aabb);
		final float y2 = (float) (double) RAxisAlignedBB.nmsMaxY.get(aabb);
		final float z2 = (float) (double) RAxisAlignedBB.nmsMaxZ.get(aabb);
		return Box.fromSpan(x1, y1, z1, x2, y2, z2);
	}
}
