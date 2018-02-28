package com.hepolite.api.util;

import org.apache.commons.lang.NotImplementedException;
import org.bukkit.Location;
import org.bukkit.util.Vector;

public final class Shapes
{
	public static abstract class Shape
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
		public static Point fromCoord(final double x, final double y, final double z)
		{
			return new Point(x, y, z);
		}

		// ...

		private double x, y, z;

		private Point(final double x, final double y, final double z)
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
		public void setCoord(final double x, final double y, final double z)
		{
			this.x = x;
			this.y = y;
			this.z = z;
		}

		/**
		 * @return x-coordinate
		 */
		public double getX()
		{
			return x;
		}
		/**
		 * @return y-coordinate
		 */
		public double getY()
		{
			return y;
		}
		/**
		 * @return z-coordinate
		 */
		public double getZ()
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
			final double dx = sphere.x - x;
			final double dy = sphere.y - y;
			final double dz = sphere.z - z;
			return dx * dx + dy * dy + dz * dz < sphere.r * sphere.r;
		}
		@Override
		public boolean intersects(final Line line)
		{
			// Point: p
			// Line: a + t * u
			// d = ||AP x u|| / ||u|| = 0 during intersection
			final double apx = x - line.x;
			final double apy = y - line.y;
			final double apz = z - line.z;
			final double cx = apy * line.dz - apz * line.dy;
			final double cy = apz * line.dx - apx * line.dz;
			final double cz = apx * line.dy - apy * line.dx;

			// P = a + t * u, solve for t
			final double tx = (line.dx == 0.0 ? 0.0 : apx / line.dx);
			final double ty = (line.dy == 0.0 ? 0.0 : apy / line.dy);
			final double tz = (line.dz == 0.0 ? 0.0 : apz / line.dz);

			return cx * cx + cy * cy + cz * cz == 0.0 && tx >= 0.0 && ty >= 0.0 && tz >= 0.0 && tx <= 1.0 && ty <= 1.0
					&& tz <= 1.0;
		}
		@Override
		public boolean intersects(final Box box)
		{
			return !(x > box.x2 || y > box.y2 || z > box.z2 || x < box.x1 || y < box.y1 || z < box.z1);
		}
		@Override
		public boolean intersects(final Cone cone)
		{
			// cos A = ||dp * d|| / (||dp|| * ||dr||)
			final double dx = x - cone.x;
			final double dy = y - cone.y;
			final double dz = z - cone.z;
			final double d1l = dx * dx + dy * dy + dz * dz;
			final double d2l = cone.dx * cone.dx + cone.dy * cone.dy + cone.dz * cone.dz;
			final double cos = (dx * cone.dx + dy * cone.dy + dz * cone.dz) / Math.sqrt(d1l * d2l);
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
		public static Sphere fromCoordAndRadius(final double x, final double y, final double z, final double r)
		{
			return new Sphere(x, y, z, r);
		}

		// ...

		private double x, y, z, r;

		private Sphere(final double x, final double y, final double z, final double r)
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
		public void setCoordAndRadius(final double x, final double y, final double z, final double r)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.r = r;
		}

		/**
		 * @return center x-coordinate
		 */
		public double getX()
		{
			return x;
		}
		/**
		 * @return center y-coordinate
		 */
		public double getY()
		{
			return y;
		}
		/**
		 * @return center z-coordinate
		 */
		public double getZ()
		{
			return z;
		}
		/**
		 * @return radius
		 */
		public double getRadius()
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
			final double dx = sphere.x - x;
			final double dy = sphere.y - y;
			final double dz = sphere.z - z;
			final double dr = sphere.r + r;
			return dx * dx + dy * dy + dz * dz < dr * dr;
		}
		@Override
		public boolean intersects(final Line line)
		{
			// Point: p
			// Line: a + t * u
			// d = ||AP x u|| / ||u|| = 0 during intersection
			final double apx = x - line.x;
			final double apy = y - line.y;
			final double apz = z - line.z;
			final double cx = apy * line.dz - apz * line.dy;
			final double cy = apz * line.dx - apx * line.dz;
			final double cz = apx * line.dy - apy * line.dx;

			// P = a + t * u, solve for t
			final double tx = (line.dx == 0.0 ? 0.0 : apx / line.dx);
			final double ty = (line.dy == 0.0 ? 0.0 : apy / line.dy);
			final double tz = (line.dz == 0.0 ? 0.0 : apz / line.dz);

			return cx * cx + cy * cy + cz * cz <= r * r && tx >= 0.0 && ty >= 0.0 && tz >= 0.0 && tx <= 1.0 && ty <= 1.0
					&& tz <= 1.0;
		}
		@Override
		public boolean intersects(final Box box)
		{
			// Source:
			// https://studiofreya.com/3d-math-and-physics/sphere-vs-aabb-collision-detection-test/
			final TriFunction<Double, Double, Double, Double> fun = (pn, bmin, bmax) -> {
				if (pn < bmin)
					return (bmin - pn) * (bmin - pn);
				if (pn > bmax)
					return (pn - bmax) * (pn - bmax);
				return 0.0;
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
		public static Line fromSpan(final double xs, final double ys, final double zs, final double xe, final double ye,
				final double ze)
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
		public static Line fromDirection(final double x, final double y, final double z, final double dx,
				final double dy, final double dz)
		{
			return new Line(x, y, z, dx, dy, dz);
		}

		// ...

		private double x, y, z;
		private double dx, dy, dz;

		private Line(final double x, final double y, final double z, final double xd, final double dy, final double dz)
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
		public void setSpan(final double xs, final double ys, final double zs, final double xe, final double ye,
				final double ze)
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
		public void setDirection(final double x, final double y, final double z, final double dx, final double dy,
				final double dz)
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
		public double getX()
		{
			return x;
		}
		/**
		 * @return y-coordinate
		 */
		public double getY()
		{
			return y;
		}
		/**
		 * @return z-coordinate
		 */
		public double getZ()
		{
			return z;
		}
		/**
		 * @return x-direction
		 */
		public double deltaX()
		{
			return dx;
		}
		/**
		 * @return y-direction
		 */
		public double deltaY()
		{
			return dy;
		}
		/**
		 * @return z-direction
		 */
		public double deltaZ()
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
			final double t1x = (box.x1 - x) / dx;
			final double t1y = (box.y1 - y) / dy;
			final double t1z = (box.z1 - z) / dz;
			final double t2x = (box.x2 - x) / dx;
			final double t2y = (box.y2 - y) / dy;
			final double t2z = (box.z2 - z) / dz;

			// Explanation missing
			final double min = Math.max(Math.max(Math.min(t1x, t2x), Math.min(t1y, t2y)), Math.min(t1z, t2z));
			final double max = Math.min(Math.min(Math.max(t1x, t2x), Math.max(t1y, t2y)), Math.max(t1z, t2z));
			return max >= min && max >= 0.0 && min <= 1.0;
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
		public static Box fromSpan(final double x1, final double y1, final double z1, final double x2, final double y2,
				final double z2)
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
		public static Box fromSize(final double cx, final double cy, final double cz, final double sx, final double sy,
				final double sz)
		{
			final double hx = 0.5 * sx;
			final double hy = 0.5 * sy;
			final double hz = 0.5 * sz;
			return fromSpan(cx - hx, cy - hy, cz - hz, cx + hx, cy + hy, cz + hz);
		}

		// ...

		private double x1, y1, z1;
		private double x2, y2, z2;

		private Box(final double x1, final double y1, final double z1, final double x2, final double y2,
				final double z2)
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
		public void setSpan(final double x1, final double y1, final double z1, final double x2, final double y2,
				final double z2)
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
		public void setSize(final double cx, final double cy, final double cz, final double sx, final double sy,
				final double sz)
		{
			final double hx = 0.5 * sx;
			final double hy = 0.5 * sy;
			final double hz = 0.5 * sz;
			setSpan(cx - hx, cy - hy, cz - hz, cx + hx, cy + hy, cz + hz);
		}

		/**
		 * @return Minimum x-coordinate
		 */
		public double minX()
		{
			return x1;
		}
		/**
		 * @return Minimum y-coordinate
		 */
		public double minY()
		{
			return y1;
		}
		/**
		 * @return Minimum z-coordinate
		 */
		public double minZ()
		{
			return z1;
		}
		/**
		 * @return Maximum x-coordinate
		 */
		public double maxX()
		{
			return x2;
		}
		/**
		 * @return Maximum y-coordinate
		 */
		public double maxY()
		{
			return y2;
		}
		/**
		 * @return Maximum z-coordinate
		 */
		public double maxZ()
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
				Point.fromCoord(0.5 * (x1 + x2), 0.5 * (y1 + y2), 0.5 * (z1 + z2))
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
		 * Creates a new cone from the given center coordinate and radius
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
		public static Cone fromDirection(final double x, final double y, final double z, final double dx,
				final double dy, final double dz, final double angle)
		{
			return new Cone(x, y, z, dx, dy, dz, angle);
		}
		/**
		 * Creates a new cone from the given location, extending in the direction of the location
		 * 
		 * @param origin The position and direction of the cone
		 * @param length The length of the cone
		 * @param angle The cone angle
		 * @return The cone spanning from the starting coordinate in the given direction
		 */
		public static Cone fromDirection(final Location origin, final double length, final double angle)
		{
			final Vector dir = origin.getDirection().multiply(length);
			return new Cone(origin.getX(), origin.getY(), origin.getZ(), dir.getX(), dir.getY(), dir.getZ(), angle);
		}

		// ...

		private double x, y, z;
		private double dx, dy, dz;
		private double angle;

		private Cone(final double x, final double y, final double z, final double dx, final double dy, final double dz,
				final double angle)
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
		public void setDirection(final double x, final double y, final double z, final double dx, final double dy,
				final double dz, final double angle)
		{
			this.x = x;
			this.y = y;
			this.z = z;
			this.dx = dx;
			this.dy = dy;
			this.dz = dz;
			this.angle = Math.cos(0.5 * angle * Math.PI / 180.0);
		}

		/**
		 * @return center x-coordinate
		 */
		public double getX()
		{
			return x;
		}
		/**
		 * @return center y-coordinate
		 */
		public double getY()
		{
			return y;
		}
		/**
		 * @return center z-coordinate
		 */
		public double gettZ()
		{
			return z;
		}
		/**
		 * @return x-direction
		 */
		public double deltaX()
		{
			return dx;
		}
		/**
		 * @return y-direction
		 */
		public double deltaY()
		{
			return dy;
		}
		/**
		 * @return z-direction
		 */
		public double deltaZ()
		{
			return dz;
		}
		/**
		 * @return angle
		 */
		public double getAngle()
		{
			return 2.0 * Math.acos(angle) * 180.0 / Math.PI;
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
