package com.hepolite.api.util;

import org.bukkit.Location;

import com.hepolite.coreutil.util.reflection.reflected.RMovingObjectPosition;
import com.hepolite.coreutil.util.reflection.reflected.RVec3D;
import com.hepolite.coreutil.util.reflection.reflected.RWorld;

public final class Raytracer
{
	/**
	 * Performs a ray trace against the blocks in the world specified along the ray from start to
	 * end. If the raytrace finds any block, it will return the location of the impact point
	 * 
	 * @param start The starting point of the ray trace
	 * @param end The ending point of the ray trace
	 * @param unknownA Suspecting that this is related to whether fluids should count as hittable
	 *            blocks
	 * @param unknownB Suspecting that this is related to whether to return the last ray position
	 *            regardless if anything was hit or not
	 * @param hitNonsolidBlocks Whether blocks such as torches and other blocks with no bbox should
	 *            be ignored. If "unknownB" is true, this flag seems to not work.
	 * @return The position that was hit or null if no position were found
	 */
	public static Location rayTrace(final Location start, final Location end, final boolean unknownA,
			final boolean unknownB, final boolean hitNonsolidBlocks)
	{
		final Object vec3Start = RVec3D.nmsConstructor.instantiate(start.getX(), start.getY(), start.getZ());
		final Object vec3End = RVec3D.nmsConstructor.instantiate(end.getX(), end.getY(), end.getZ());
		final Object worldHandle = RWorld.cbGetHandle.invoke(start.getWorld());
		final Object movingObjPos = RWorld.nmsRayTraceTertiary.invoke(worldHandle, vec3Start, vec3End, unknownA,
				unknownB, !hitNonsolidBlocks);
		if (movingObjPos == null)
			return null;

		final Object vec3 = RMovingObjectPosition.nmsPos.get(movingObjPos);
		return new Location(start.getWorld(), (double) RVec3D.nmsX.get(vec3), (double) RVec3D.nmsY.get(vec3),
				(double) RVec3D.nmsZ.get(vec3));
	}
}
