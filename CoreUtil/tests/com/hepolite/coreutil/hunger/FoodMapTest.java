package com.hepolite.coreutil.hunger;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class FoodMapTest
{
	@Test
	void testGet()
	{
		final FoodData dataA = new FoodData();
		final FoodData dataB = new FoodData();
		dataA.name = "dataA";
		dataB.name = "dataB";
		final FoodMap map = new FoodMap();
		map.add(dataA, "Default");
		map.add(dataB, "Default");
		map.add(dataB, "Group");

		final Optional<FoodData> opDataA = map.get("dataA", "Default");
		final Optional<FoodData> opDataB = map.get("dataB", "Group");
		final Optional<FoodData> opDataC = map.get("dataB", "InvalidGroup");
		final Optional<FoodData> opDataD = map.get("invalid", "Whatever");

		assertTrue(opDataA.isPresent());
		assertTrue(opDataB.isPresent());
		assertTrue(opDataC.isPresent());
		assertFalse(opDataD.isPresent());
		assertSame(dataA, opDataA.get());
		assertSame(dataB, opDataB.get());
		assertSame(dataB, opDataC.get());
	}

	@Test
	void testResolveContents()
	{
		final FoodData dataA = new FoodData();
		final FoodData dataB = new FoodData();
		dataA.name = "dataA";
		dataA.categories.add("vegetable");
		dataB.name = "dataB";
		dataB.categories.add("meat");
		dataB.ingredients.add("dataA");
		final FoodMap map = new FoodMap();
		map.add(dataA, "Default");
		map.add(dataB, "Default");

		final Optional<FoodData> data = map.get("dataB", "Default");
		assertTrue(data.isPresent());
		assertEquals(0, data.get().ingredients.size());
		assertEquals(2, data.get().categories.size());
		assertTrue(data.get().categories.contains("meat"));
		assertTrue(data.get().categories.contains("vegetable"));
	}
}
