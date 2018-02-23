package com.hepolite.api.util;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.commons.lang.NotImplementedException;
import org.junit.jupiter.api.Test;

import com.hepolite.api.util.Shapes.Box;
import com.hepolite.api.util.Shapes.Cone;
import com.hepolite.api.util.Shapes.Line;
import com.hepolite.api.util.Shapes.Point;
import com.hepolite.api.util.Shapes.Sphere;

class ShapesTest
{
	@Test
	void testPointPointIntersection()
	{
		final Point pointA = Point.fromCoord(3.0, 1.0, 4.0);
		final Point pointB = Point.fromCoord(0.0, 0.0, 0.0);
		final Point pointC = Point.fromCoord(3.0, 1.0, 4.0);

		assertTrue(pointA.intersects(pointC));
		assertFalse(pointB.intersects(pointC));
	}
	@Test
	void testPointSphereIntersection()
	{
		final Point pointA = Point.fromCoord(0.5, 0.0, 0.0);
		final Point pointB = Point.fromCoord(3.0, 1.0, 4.0);
		final Sphere sphere = Sphere.fromCoordAndRadius(0.0, 0.0, 0.0, 1.0);

		assertTrue(pointA.intersects(sphere));
		assertFalse(pointB.intersects(sphere));
	}
	@Test
	void testPointLineIntersection()
	{
		final Point pointA = Point.fromCoord(0.5, 0.0, 0.0);
		final Point pointB = Point.fromCoord(3.0, 0.0, 0.0);
		final Line line = Line.fromSpan(1.0, 0.0, 0.0, 0.0, 0.0, 0.0);

		assertTrue(pointA.intersects(line));
		assertFalse(pointB.intersects(line));
	}
	@Test
	void testPointBoxIntersection()
	{
		final Point pointA = Point.fromCoord(0.5, 0.0, 0.0);
		final Point pointB = Point.fromCoord(3.0, 1.0, 4.0);
		final Box box = Box.fromSize(0.0, 0.0, 0.0, 2.0, 2.0, 2.0);

		assertTrue(pointA.intersects(box));
		assertFalse(pointB.intersects(box));
	}
	@Test
	void testPointConeIntersection()
	{
		final Point pointA = Point.fromCoord(0.0, 0.0, 0.5);
		final Point pointB = Point.fromCoord(0.75, 0.0, 0.5);
		final Cone cone = Cone.fromDirection(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 90.0);

		assertTrue(pointA.intersects(cone));
		assertFalse(pointB.intersects(cone));
	}

	@Test
	void testSphereSphereIntersection()
	{
		final Sphere sphereA = Sphere.fromCoordAndRadius(0.0, 0.0, 0.0, 1.0);
		final Sphere sphereB = Sphere.fromCoordAndRadius(1.25, 0.0, 0.0, 1.0);
		final Sphere sphereC = Sphere.fromCoordAndRadius(2.5, 0.0, 0.0, 1.0);

		assertTrue(sphereA.intersects(sphereB));
		assertTrue(sphereB.intersects(sphereC));
		assertFalse(sphereC.intersects(sphereA));
	}
	@Test
	void testSphereLineIntersection()
	{
		final Sphere sphereA = Sphere.fromCoordAndRadius(0.0, 0.0, 0.0, 1.0);
		final Sphere sphereB = Sphere.fromCoordAndRadius(3.0, 0.0, 0.0, 1.0);
		final Line line = Line.fromSpan(0.5, 0.0, 0.0, 0.0, 0.5, 0.0);

		assertTrue(sphereA.intersects(line));
		assertFalse(sphereB.intersects(line));
	}
	@Test
	void testSphereBoxIntersection()
	{
		final Sphere sphereA = Sphere.fromCoordAndRadius(0.0, 0.0, 1.5, 1.0);
		final Sphere sphereB = Sphere.fromCoordAndRadius(3.0, 0.0, 0.0, 1.0);
		final Box box = Box.fromSize(0.0, 0.0, 0.0, 1.0, 4.0, 2.0);

		assertTrue(sphereA.intersects(box));
		assertFalse(sphereB.intersects(box));
	}
	@Test
	void testSphereConeIntersection()
	{
		final Sphere sphereA = Sphere.fromCoordAndRadius(0.0, 0.0, 0.0, 1.0);
		final Sphere sphereB = Sphere.fromCoordAndRadius(0.0, 0.0, 0.0, 1.0);
		final Cone cone = Cone.fromDirection(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 90.0);

		// TODO: Implement intersection
		assertThrows(NotImplementedException.class, () -> sphereA.intersects(cone));
		assertThrows(NotImplementedException.class, () -> sphereB.intersects(cone));
	}

	@Test
	void testLineLineIntersection()
	{
		final Line lineA = Line.fromSpan(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		final Line lineB = Line.fromSpan(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		final Line lineC = Line.fromSpan(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);

		// TODO: Implement intersection
		assertThrows(NotImplementedException.class, () -> lineA.intersects(lineB));
		assertThrows(NotImplementedException.class, () -> lineB.intersects(lineC));
		assertThrows(NotImplementedException.class, () -> lineC.intersects(lineA));
	}
	@Test
	void testLineBoxIntersection()
	{
		final Line lineA = Line.fromSpan(-1.0, 0.0, 0.0, 1.0, 0.0, 0.0);
		final Line lineB = Line.fromSpan(1.0, -2.0, 0.0, 1.0, 5.0, 0.0);
		final Box box = Box.fromSize(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);

		assertTrue(lineA.intersects(box));
		assertFalse(lineB.intersects(box));
	}
	@Test
	void testLineConeIntersection()
	{
		final Line lineA = Line.fromSpan(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		final Line lineB = Line.fromSpan(0.0, 0.0, 0.0, 0.0, 0.0, 0.0);
		final Cone cone = Cone.fromDirection(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 90.0);

		// TODO: Implement intersection
		assertThrows(NotImplementedException.class, () -> lineA.intersects(cone));
		assertThrows(NotImplementedException.class, () -> lineB.intersects(cone));
	}

	@Test
	void testBoxBoxIntersection()
	{
		final Box boxA = Box.fromSpan(0.0, 0.0, 0.0, 1.0, 1.0, 1.0);
		final Box boxB = Box.fromSpan(-0.75, 0.0, 0.5, 0.25, 1.0, 1.5);
		final Box boxC = Box.fromSpan(-1.5, 0.0, 0.0, -0.5, 1.0, 1.0);

		assertTrue(boxA.intersects(boxB));
		assertTrue(boxB.intersects(boxC));
		assertFalse(boxC.intersects(boxA));
	}
	@Test
	void testBoxConeIntersection()
	{
		final Box boxA = Box.fromSize(0.75, 0.75, 0.0, 1.0, 1.0, 1.0);
		final Box boxB = Box.fromSize(0.75, 0.0, 0.25, 0.1f, 0.1f, 0.1f);
		final Cone cone = Cone.fromDirection(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 90.0);

		assertTrue(boxA.intersects(cone));
		assertFalse(boxB.intersects(cone));
	}

	@Test
	void testConeConeIntersection()
	{
		final Cone coneA = Cone.fromDirection(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 90.0);
		final Cone coneB = Cone.fromDirection(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 90.0);
		final Cone coneC = Cone.fromDirection(0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 90.0);

		// TODO: Implement intersection
		assertThrows(NotImplementedException.class, () -> coneA.intersects(coneB));
		assertThrows(NotImplementedException.class, () -> coneB.intersects(coneC));
		assertThrows(NotImplementedException.class, () -> coneC.intersects(coneA));
	}
}
