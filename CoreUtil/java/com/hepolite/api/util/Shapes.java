package com.hepolite.api.util;

import org.apache.commons.lang.NotImplementedException;

public final class Shapes
{
	private static abstract class Shape
	{
		/**
		 * Returns true if the current shape intersects with the provided point
		 * 
		 * @param point The point to check for an intersection with
		 * @return True if the shape intersects the point
		 */
		public abstract boolean intersects(Point point);
		/**
		 * Returns true if the current shape intersects with the provided sphere
		 * 
		 * @param sphere The sphere to check for an intersection with
		 * @return True if the shape intersects the sphere
		 */
		public abstract boolean intersects(Sphere sphere);
		/**
		 * Returns true if the current shape intersects with the provided line
		 * 
		 * @param line The line to check for an intersection with
		 * @return True if the shape intersects the line
		 */
		public abstract boolean intersects(Line line);
		/**
		 * Returns true if the current shape intersects with the provided box
		 * 
		 * @param box The box to check for an intersection with
		 * @return True if the shape intersects the box
		 */
		public abstract boolean intersects(Box box);
		/**
		 * Returns true if the current shape intersects with the provided cone
		 * 
		 * @param cone The cone to check for an intersection with
		 * @return True if the shape intersects the cone
		 */
		public abstract boolean intersects(Cone cone);
	}

	// ...

	public static final class Point extends Shape
	{
		/**
		 * Creates a new point from the given coordinate
		 * 
		 * @param x The x-coordinate
		 * @param y The y-coordinate
		 * @param z The z-coordinate
		 * @return The point with the given coordinate
		 */
		public static Point fromCoord(final float x, final float y, final float z)
		{
			return new Point(x, y, z);
		}

		// ...

		private float x, y, z;

		private Point(final float x, final float y, final float z)
		{
			setCoord(x, y, z);
		}

		/**
		 * Assigns the point coordinate
		 * 
		 * @param x The x-coordinate
		 * @param y The y-coordinate
		 * @param z The z-coordinate
		 */
		public void setCoord(final float x, final float y, final float z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}

		/**
		 * @return x-coordinate
		 */
		public float getX()
		{
			return x;
		}
		/**
		 * @return y-coordinate
		 */
		public float getY()
		{
			return y;
		}
		/**
		 * @return z-coordinate
		 */
		public float getZ()
		{
			return z;
		}

		// ...

		@Override
		public boolean intersects(final Point point)
		{
			return x == point.x && y == point.y && z == point.z;
		}
		@Override
		public boolean intersects(final Sphere sphere)
		{
			final float dx = sphere.x - x;
			final float dy = sphere.y - y;
			final float dz = sphere.z - z;
			return dx * dx + dy * dy + dz * dz < sphere.r * sphere.r;
		}
		@Override
		public boolean intersects(final Line line)
		{
			// Point: p
			// Line: a + t * u
			// d = ||AP x u|| / ||u|| = 0 during intersection
			final float apx = x - line.x;
			final float apy = y - line.y;
			final float apz = z - line.z;
			final float cx = apy * line.dz - apz * line.dy;
			final float cy = apz * line.dx - apx * line.dz;
			final float cz = apx * line.dy - apy * line.dx;

			// P = a + t * u, solve for t
			final float tx = (line.dx == 0.0f ? 0.0f : apx / line.dx);
			final float ty = (line.dy == 0.0f ? 0.0f : apy / line.dy);
			final float tz = (line.dz == 0.0f ? 0.0f : apz / line.dz);

			return cx * cx + cy * cy + cz * cz == 0.0f && tx >= 0.0f && ty >= 0.0f && tz >= 0.0f && tx <= 1.0f
					&& ty <= 1.0f && tz <= 1.0f;
		}
		@Override
		public boolean intersects(final Box box)
		{
			return !(x > box.x2 || y > box.y2 || z > box.z2 || x < box.x1 || y < box.y1 || z < box.z1);
		}
		@Override
		public boolean intersects(final Cone cone)
		{
			final float dx = x - cone.x;
			final float dy = y - cone.y;
			final float dz = z - cone.z;
			final float d1l = dx * dx + dy * dy + dz * dz;
			final float d2l = cone.dx * cone.dx + cone.dy * cone.dy + cone.dz * cone.dz;
			final float cos = (dx * cone.dx + dy * cone.dy + dz * cone.dz) / (float) Math.sqrt(d1l * d2l);

			System.out.println(String.format("[%.2f, %.2f, %.2f] (%.2f, %.2f) {%.2f, %.2f}", dx, dy, dz, d1l, d2l, cos,
					cone.angle));

			return cos >= cone.angle && d1l <= d2l;
		}

		// ...

		@Override
		public String toString()
		{
			return String.format("(%.2f, %.2f, %.2f)", x, y, z);
		}
	}

	public static final class Sphere extends Shape
	{
		/**
		 * Creates a new sphere from the given center coordinate and radius
		 * 
		 * @param x The x-coordinate
		 * @param y The y-coordinate
		 * @param z The z-coordinate
		 * @param r The radius
		 * @return The sphere with the given center coordinate and radius
		 */
		public static Sphere fromCoordAndRadius(final float x, final float y, final float z, final float r)
		{
			return new Sphere(x, y, z, r);
		}

		// ...

		private float x, y, z, r;

		private Sphere(final float x, final float y, final float z, final float r)
		{
			setCoordAndRadius(x, y, z, r);
		}

		/**
		 * Assigns the sphere center coordinate
		 * 
		 * @param x The center x-coordinate
		 * @param y The center y-coordinate
		 * @param z The center z-coordinate
		 */
		public void setCoordAndRadius(final float x, final float y, final float z, final float r)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.r = r;
		}

		/**
		 * @return center x-coordinate
		 */
		public float getX()
		{
			return x;
		}
		/**
		 * @return center y-coordinate
		 */
		public float getY()
		{
			return y;
		}
		/**
		 * @return center z-coordinate
		 */
		public float getZ()
		{
			return z;
		}
		/**
		 * @return radius
		 */
		public float getRadius()
		{
			return r;
		}

		// ...

		@Override
		public boolean intersects(final Point point)
		{
			return point.intersects(this);
		}
		@Override
		public boolean intersects(final Sphere sphere)
		{
			final float dx = sphere.x - x;
			final float dy = sphere.y - y;
			final float dz = sphere.z - z;
			final float dr = sphere.r + r;
			return dx * dx + dy * dy + dz * dz < dr * dr;
		}
		@Override
		public boolean intersects(final Line line)
		{
			// Point: p
			// Line: a + t * u
			// d = ||AP x u|| / ||u|| = 0 during intersection
			final float apx = x - line.x;
			final float apy = y - line.y;
			final float apz = z - line.z;
			final float cx = apy * line.dz - apz * line.dy;
			final float cy = apz * line.dx - apx * line.dz;
			final float cz = apx * line.dy - apy * line.dx;

			// P = a + t * u, solve for t
			final float tx = (line.dx == 0.0f ? 0.0f : apx / line.dx);
			final float ty = (line.dy == 0.0f ? 0.0f : apy / line.dy);
			final float tz = (line.dz == 0.0f ? 0.0f : apz / line.dz);

			return cx * cx + cy * cy + cz * cz <= r * r && tx >= 0.0f && ty >= 0.0f && tz >= 0.0f && tx <= 1.0f
					&& ty <= 1.0f && tz <= 1.0f;
		}
		@Override
		public boolean intersects(final Box box)
		{
			// Source:
			// https://studiofreya.com/3d-math-and-physics/sphere-vs-aabb-collision-detection-test/
			final TriFunction<Float, Float, Float, Float> fun = (pn, bmin, bmax) -> {
				if (pn < bmin)
					return (bmin - pn) * (bmin - pn);
				if (pn > bmax)
					return (pn - bmax) * (pn - bmax);
				return 0.0f;
			};
			return fun.apply(x, box.x1, box.x2) + fun.apply(y, box.y1, box.y2) + fun.apply(z, box.z1, box.z2) <= r * r;
		}
		@Override
		public boolean intersects(final Cone cone)
		{
			throw new NotImplementedException();
		}

		// ...

		@Override
		public String toString()
		{
			return String.format("(%.2f, %.2f, %.2f; %.2f)", x, y, z, r);
		}
	}

	public static final class Line extends Shape
	{
		/**
		 * Creates a new line from the given span
		 * 
		 * @param xs The starting x-coordinate
		 * @param ys The starting y-coordinate
		 * @param zs The starting z-coordinate
		 * @param xe The ending x-coordinate
		 * @param ye The ending y-coordinate
		 * @param ze The ending z-coordinate
		 * @return The line spanning between the two coordinates
		 */
		public static Line fromSpan(final float xs, final float ys, final float zs, final float xe, final float ye,
				final float ze)
		{
			return new Line(xs, ys, zs, xe - xs, ye - ys, ze - zs);
		}
		/**
		 * Creates a new line from the given coordinate and direction
		 * 
		 * @param x The starting x-coordinate
		 * @param y The starting y-coordinate
		 * @param z The starting z-coordinate
		 * @param dx The direction x-coordinate
		 * @param dy The direction y-coordinate
		 * @param dz The direction z-coordinate
		 * @return The line spanning from the starting coordinate in the given direction
		 */
		public static Line fromDirection(final float x, final float y, final float z, final float dx, final float dy,
				final float dz)
		{
			return new Line(x, y, z, dx, dy, dz);
		}

		// ...

		private float x, y, z;
		private float dx, dy, dz;

		private Line(final float x, final float y, final float z, final float xd, final float dy, final float dz)
		{
			setSpan(x, y, z, xd, dy, dz);
		}

		/**
		 * Assigns the area the line spans over
		 * 
		 * @param xs The starting x-coordinate
		 * @param ys The starting y-coordinate
		 * @param zs The starting z-coordinate
		 * @param xe The ending x-coordinate
		 * @param ye The ending y-coordinate
		 * @param ze The ending z-coordinate
		 */
		public void setSpan(final float xs, final float ys, final float zs, final float xe, final float ye,
				final float ze)
		{
			setDirection(xs, ys, zs, xe - xs, ye - ys, ze - zs);
		}
		/**
		 * Assigns the area the line spans over from the given coordinate and direction
		 * 
		 * @param x The starting x-coordinate
		 * @param y The starting y-coordinate
		 * @param z The starting z-coordinate
		 * @param xe The ending x-coordinate
		 * @param ye The ending y-coordinate
		 * @param ze The ending z-coordinate
		 */
		public void setDirection(final float x, final float y, final float z, final float dx, final float dy,
				final float dz)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.dx = dx;
			this.dy = dy;
			this.dz = dz;
		}

		/**
		 * @return x-coordinate
		 */
		public float getX()
		{
			return x;
		}
		/**
		 * @return y-coordinate
		 */
		public float getY()
		{
			return y;
		}
		/**
		 * @return z-coordinate
		 */
		public float getZ()
		{
			return z;
		}
		/**
		 * @return x-direction
		 */
		public float deltaX()
		{
			return dx;
		}
		/**
		 * @return y-direction
		 */
		public float deltaY()
		{
			return dy;
		}
		/**
		 * @return z-direction
		 */
		public float deltaZ()
		{
			return dz;
		}

		// ...

		@Override
		public boolean intersects(final Point point)
		{
			return point.intersects(this);
		}
		@Override
		public boolean intersects(final Sphere sphere)
		{
			return sphere.intersects(this);
		}
		@Override
		public boolean intersects(final Line line)
		{
			throw new NotImplementedException();
		}
		@Override
		public boolean intersects(final Box box)
		{
			// Formula missing
			final float t1x = (box.x1 - x) / dx;
			final float t1y = (box.y1 - y) / dy;
			final float t1z = (box.z1 - z) / dz;
			final float t2x = (box.x2 - x) / dx;
			final float t2y = (box.y2 - y) / dy;
			final float t2z = (box.z2 - z) / dz;

			// Explanation missing
			final float min = Math.max(Math.max(Math.min(t1x, t2x), Math.min(t1y, t2y)), Math.min(t1z, t2z));
			final float max = Math.min(Math.min(Math.max(t1x, t2x), Math.max(t1y, t2y)), Math.max(t1z, t2z));
			return max >= min && max >= 0.0f && min <= 1.0f;
		}
		@Override
		public boolean intersects(final Cone cone)
		{
			throw new NotImplementedException();
		}

		// ...

		@Override
		public String toString()
		{
			return String.format("{(%.2f, %.2f, %.2f); [%.2f, %.2f, %.2f]}", x, y, z, dx, dy, dz);
		}
	}

	public static final class Box extends Shape
	{
		/**
		 * Creates a new box from the given span
		 * 
		 * @param x1 The first x-coordinate
		 * @param y1 The first y-coordinate
		 * @param z1 The first z-coordinate
		 * @param x2 The second x-coordinate
		 * @param y2 The second y-coordinate
		 * @param z2 The second z-coordinate
		 * @return The box spanning between the two coordinates
		 */
		public static Box fromSpan(final float x1, final float y1, final float z1, final float x2, final float y2,
				final float z2)
		{
			return new Box(x1, y1, z1, x2, y2, z2);
		}
		/**
		 * Creates a new box from the given size and center
		 * 
		 * @param x1 The center x-coordinate
		 * @param y1 The center y-coordinate
		 * @param z1 The center z-coordinate
		 * @param x2 The x size
		 * @param y2 The y size
		 * @param z2 The z size
		 * @return The box located at the center coordinate with the given size
		 */
		public static Box fromSize(final float cx, final float cy, final float cz, final float sx, final float sy,
				final float sz)
		{
			final float hx = 0.5f * sx;
			final float hy = 0.5f * sy;
			final float hz = 0.5f * sz;
			return fromSpan(cx - hx, cy - hy, cz - hz, cx + hx, cy + hy, cz + hz);
		}

		// ...

		private float x1, y1, z1;
		private float x2, y2, z2;

		private Box(final float x1, final float y1, final float z1, final float x2, final float y2, final float z2)
		{
			setSpan(x1, y1, z1, x2, y2, z2);
		}

		/**
		 * Assigns the area the box spans over
		 * 
		 * @param x1 The first x-coordinate
		 * @param y1 The first y-coordinate
		 * @param z1 The first z-coordinate
		 * @param x2 The second x-coordinate
		 * @param y2 The second y-coordinate
		 * @param z2 The second z-coordinate
		 */
		public void setSpan(final float x1, final float y1, final float z1, final float x2, final float y2,
				final float z2)
		{
			this.x1 = Math.min(x1, x2);
			this.x2 = Math.max(x1, x2);
			this.y1 = Math.min(y1, y2);
			this.y2 = Math.max(y1, y2);
			this.z1 = Math.min(z1, z2);
			this.z2 = Math.max(z1, z2);
		}
		/**
		 * Assigns the area the box spans over from the given size and center
		 * 
		 * @param x1 The center x-coordinate
		 * @param y1 The center y-coordinate
		 * @param z1 The center z-coordinate
		 * @param x2 The x size
		 * @param y2 The y size
		 * @param z2 The z size
		 */
		public void setSize(final float cx, final float cy, final float cz, final float sx, final float sy,
				final float sz)
		{
			final float hx = 0.5f * sx;
			final float hy = 0.5f * sy;
			final float hz = 0.5f * sz;
			setSpan(cx - hx, cy - hy, cz - hz, cx + hx, cy + hy, cz + hz);
		}

		/**
		 * @return Minimum x-coordinate
		 */
		public float minX()
		{
			return x1;
		}
		/**
		 * @return Minimum y-coordinate
		 */
		public float minY()
		{
			return y1;
		}
		/**
		 * @return Minimum z-coordinate
		 */
		public float minZ()
		{
			return z1;
		}
		/**
		 * @return Maximum x-coordinate
		 */
		public float maxX()
		{
			return x2;
		}
		/**
		 * @return Maximum y-coordinate
		 */
		public float maxY()
		{
			return y2;
		}
		/**
		 * @return Maximum z-coordinate
		 */
		public float maxZ()
		{
			return z2;
		}

		// ...

		@Override
		public boolean intersects(final Point point)
		{
			return point.intersects(this);
		}
		@Override
		public boolean intersects(final Sphere sphere)
		{
			return sphere.intersects(this);
		}
		@Override
		public boolean intersects(final Line line)
		{
			return line.intersects(this);
		}
		@Override
		public boolean intersects(final Box box)
		{
			return !(x1 > box.x2 || y1 > box.y2 || z1 > box.z2 || x2 < box.x1 || y2 < box.y1 || z2 < box.z1);
		}
		@Override
		public boolean intersects(final Cone cone)
		{
			// TODO: This approach is not completely accurate
			for (final Point point : new Point[] {
				Point.fromCoord(x1, y1, z1),
				Point.fromCoord(x1, y1, z2),
				Point.fromCoord(x1, y2, z1),
				Point.fromCoord(x1, y2, z2),
				Point.fromCoord(x2, y1, z1),
				Point.fromCoord(x2, y1, z2),
				Point.fromCoord(x2, y2, z1),
				Point.fromCoord(x2, y2, z2),
				Point.fromCoord(0.5f * (x1 + x2), 0.5f * (y1 + y2), 0.5f * (z1 + z2))
			})
				if (point.intersects(cone))
					return true;
			return false;
		}

		// ...

		@Override
		public String toString()
		{
			return String.format("{(%.2f, %.2f, %.2f); (%.2f, %.2f, %.2f)}", x1, y1, z1, x2, y2, z2);
		}
	}

	public static final class Cone extends Shape
	{
		/**
		 * Creates a new sphere from the given center coordinate and radius
		 * 
		 * @param x The x-coordinate
		 * @param y The y-coordinate
		 * @param z The z-coordinate
		 * @param dx The direction x-coordinate
		 * @param dy The direction y-coordinate
		 * @param dz The direction z-coordinate
		 * @param angle The cone angle
		 * @return The cone spanning from the starting coordinate in the given direction
		 */
		public static Cone fromDirection(final float x, final float y, final float z, final float dx, final float dy,
				final float dz, final float angle)
		{
			return new Cone(x, y, z, dx, dy, dz, angle);
		}

		// ...

		private float x, y, z;
		private float dx, dy, dz;
		private float angle;

		private Cone(final float x, final float y, final float z, final float dx, final float dy, final float dz,
				final float angle)
		{
			setDirection(x, y, z, dx, dy, dz, angle);
		}

		/**
		 * Assigns the cone center coordinate and radius
		 * 
		 * @param x The x-coordinate
		 * @param y The y-coordinate
		 * @param z The z-coordinate
		 * @param dx The direction x-coordinate
		 * @param dy The direction y-coordinate
		 * @param dz The direction z-coordinate
		 * @param angle The cone angle
		 */
		public void setDirection(final float x, final float y, final float z, final float dx, final float dy,
				final float dz, final float angle)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.dx = dx;
			this.dy = dy;
			this.dz = dz;
			this.angle = (float) Math.cos(0.5 * angle * Math.PI / 180.0);
		}

		/**
		 * @return center x-coordinate
		 */
		public float getX()
		{
			return x;
		}
		/**
		 * @return center y-coordinate
		 */
		public float getY()
		{
			return y;
		}
		/**
		 * @return center z-coordinate
		 */
		public float gettZ()
		{
			return z;
		}
		/**
		 * @return x-direction
		 */
		public float deltaX()
		{
			return dx;
		}
		/**
		 * @return y-direction
		 */
		public float deltaY()
		{
			return dy;
		}
		/**
		 * @return z-direction
		 */
		public float deltaZ()
		{
			return dz;
		}
		/**
		 * @return angle
		 */
		public float getAngle()
		{
			return (float) (2.0 * Math.acos(angle) * 180.0 / Math.PI);
		}

		// ...

		@Override
		public boolean intersects(final Point point)
		{
			return point.intersects(this);
		}
		@Override
		public boolean intersects(final Sphere sphere)
		{
			return sphere.intersects(this);
		}
		@Override
		public boolean intersects(final Line line)
		{
			return line.intersects(this);
		}
		@Override
		public boolean intersects(final Box box)
		{
			return box.intersects(this);
		}
		@Override
		public boolean intersects(final Cone cone)
		{
			throw new NotImplementedException();
		}

		// ...

		@Override
		public String toString()
		{
			return String.format("{(%.2f, %.2f, %.2f); [%.2f, %.2f, %.2f]; %.2f)", x, y, z, dx, dy, dz, angle);
		}

	}
}
