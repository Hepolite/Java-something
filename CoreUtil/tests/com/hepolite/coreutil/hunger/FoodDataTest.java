package com.hepolite.coreutil.hunger;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.hepolite.api.config.Config;
import com.hepolite.api.config.Property;

class FoodDataTest
{
	@Test
	void testLoad()
	{
		final Config config = new Config();
		final Property property = new Property("name");
		config.set(property.child("food"), 12.0f);
		config.set(property.child("ratio"), 0.6f);
		config.set(property.child("categories"), "sweets meat vegetable");
		config.set(property.child("ingredients"), "sugar steak potato");
		final FoodData food = config.getValue(property, new FoodData());

		assertEquals("name", food.name);
		assertEquals(12.0f, food.food);
		assertEquals(0.6f, food.ratio);

		assertEquals(3, food.categories.size());
		assertTrue(food.categories.contains("sweets"));
		assertTrue(food.categories.contains("meat"));
		assertTrue(food.categories.contains("vegetable"));

		assertEquals(3, food.ingredients.size());
		assertTrue(food.ingredients.contains("sugar"));
		assertTrue(food.ingredients.contains("steak"));
		assertTrue(food.ingredients.contains("potato"));
	}
}
