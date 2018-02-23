package com.hepolite.api.util;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import com.hepolite.coreutil.util.reflection.reflected.RBaseBlockPosition;
import com.hepolite.coreutil.util.reflection.reflected.RMovingObjectPosition;
import com.hepolite.coreutil.util.reflection.reflected.RVec3D;
import com.hepolite.coreutil.util.reflection.reflected.RWorld;

public final class Raytracer
{
	public static final class RaytraceResult
	{
		/** The exact location the ray impacted */
		public Location location;
		/** The block the ray impacted with or null */
		public Block block;
		/** The side of the block the ray impacted with or null (Not implemented) */
		@Deprecated
		public BlockFace face;
	}

	// ...

	/**
	 * Performs a ray trace against the blocks in the world specified along the ray from start to
	 * end. If the raytracer finds any block along the ray, it will return the location of the
	 * impact point
	 * 
	 * @param start The starting point of the ray trace
	 * @param end The ending point of the ray trace
	 * @return The result of the raytrace
	 */
	public static RaytraceResult rayTrace(final Location start, final Location end)
	{
		final Object vec3Start = RVec3D.nmsConstructor.instantiate(start.getX(), start.getY(), start.getZ());
		final Object vec3End = RVec3D.nmsConstructor.instantiate(end.getX(), end.getY(), end.getZ());
		final Object worldHandle = RWorld.cbGetHandle.invoke(start.getWorld());
		final Object movingObjPos = RWorld.nmsRayTracePrimary.invoke(worldHandle, vec3Start, vec3End);
		return convert(start.getWorld(), movingObjPos);
	}

	/**
	 * Performs a ray trace against the blocks in the world specified along the ray from start to
	 * end. If the raytracer finds any block along the ray, it will return the location of the
	 * impact point
	 * 
	 * @param start The starting point of the ray trace
	 * @param end The ending point of the ray trace
	 * @param hitWater Whether water blocks should be hit by the ray or not
	 * @return The result of the raytrace
	 */
	public static RaytraceResult rayTrace(final Location start, final Location end, final boolean hitWater)
	{
		final Object vec3Start = RVec3D.nmsConstructor.instantiate(start.getX(), start.getY(), start.getZ());
		final Object vec3End = RVec3D.nmsConstructor.instantiate(end.getX(), end.getY(), end.getZ());
		final Object worldHandle = RWorld.cbGetHandle.invoke(start.getWorld());
		final Object movingObjPos = RWorld.nmsRayTraceSecondary.invoke(worldHandle, vec3Start, vec3End, hitWater);
		return convert(start.getWorld(), movingObjPos);
	}
	/**
	 * Performs a ray trace against the blocks in the world specified along the ray from start to
	 * end. If the raytracer finds any block along the ray, it will return the location of the
	 * impact point. How this function works with the parameter booleans if almost pure magic. Have
	 * a look here and cry: https://imgur.com/a/brutE <br>
	 * <br>
	 * Suggested uses:<br>
	 * false, true, false: Hit only solids<br>
	 * false, false, false: Hit solids and blocks without collision boxes<br>
	 * true, false, false: Hit solids, blocks without collision boxes and water<br>
	 * false, false, true: Hit anything, even the last air block<br>
	 * Trial and error show that there are no other combinations that work... Note that only
	 * stationary fluids can be picked up, flowing fluids will not register! One just *have* to love
	 * Minecraft code, eh...? *Sigh*
	 * 
	 * @param start The starting point of the ray trace
	 * @param end The ending point of the ray trace
	 * @param hitWater Whether stationary water blocks should count as solid
	 * @param skipNonsolidBlocks Whether blocks with no collision box should be skipped. If this is
	 *            true, all other boolean values appear to be completely ignored. Both water and air
	 *            are treated as blocks without no collision box.
	 * @param hitAir Whether the last block checked should be returned whether no other blocks where
	 *            found along the ray. If skipNonsolidBlocks is false, is hitAir is true, water
	 *            blocks will also be picked up.
	 * @return The result of the raytrace
	 */
	public static RaytraceResult rayTrace(final Location start, final Location end, final boolean hitWater,
			final boolean skipNonsolidBlocks, final boolean hitAir)
	{
		final Object vec3Start = RVec3D.nmsConstructor.instantiate(start.getX(), start.getY(), start.getZ());
		final Object vec3End = RVec3D.nmsConstructor.instantiate(end.getX(), end.getY(), end.getZ());
		final Object worldHandle = RWorld.cbGetHandle.invoke(start.getWorld());
		final Object movingObjPos = RWorld.nmsRayTraceTertiary.invoke(worldHandle, vec3Start, vec3End, hitWater,
				skipNonsolidBlocks, hitAir);
		return convert(start.getWorld(), movingObjPos);
	}

	// ...

	/**
	 * Converts the input MovingObjectPosition into a RaytraceResult
	 * 
	 * @param world The world the raytracer started in
	 * @param obj The MovingObjectPosition object to convert
	 * @return The resulting object
	 */
	private static RaytraceResult convert(final World world, final Object obj)
	{
		if (obj == null)
			return new RaytraceResult();
		final Object vec3 = RMovingObjectPosition.nmsPos.get(obj);
		final Object blockPos = RMovingObjectPosition.nmsGetBlockPos.invoke(obj);

		final RaytraceResult result = new RaytraceResult();
		if (vec3 != null)
			result.location = new Location(world, (double) RVec3D.nmsX.get(vec3), (double) RVec3D.nmsY.get(vec3),
					(double) RVec3D.nmsZ.get(vec3));
		if (blockPos != null)
			result.block = world.getBlockAt((int) RBaseBlockPosition.nmsGetX.invoke(blockPos),
					(int) RBaseBlockPosition.nmsGetY.invoke(blockPos),
					(int) RBaseBlockPosition.nmsGetZ.invoke(blockPos));
		return result;
	}
}
