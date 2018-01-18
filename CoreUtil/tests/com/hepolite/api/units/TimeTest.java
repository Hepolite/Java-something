package com.hepolite.api.units;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class TimeTest
{
	@Test
	void testConstructor()
	{
		assertEquals(1, Time.fromTicks(1).asTicks());
		assertEquals(1, Time.fromSeconds(1).asSeconds());
		assertEquals(1, Time.fromMinutes(1).asMinutes());
		assertEquals(1, Time.fromHours(1).asHours());
		assertEquals(1, Time.fromDays(1).asDays());
		assertEquals(1, Time.fromWeeks(1).asWeeks());
		assertEquals(1, Time.fromMonths(1).asMonths());
		assertEquals(1, Time.fromYears(1).asYears());
	}

	@Test
	void testIncrement()
	{
		assertEquals(1, Time.fromInstant().increment().asTicks());
		assertEquals(5, Time.fromInstant().increment(Time.fromTicks(5)).asTicks());
	}
	@Test
	void testDecrement()
	{
		assertEquals(4, Time.fromTicks(5).decrement().asTicks());
		assertEquals(0, Time.fromInstant().decrement().asTicks());
		assertEquals(3, Time.fromTicks(5).decrement(Time.fromTicks(2)).asTicks());
		assertEquals(0, Time.fromTicks(5).decrement(Time.fromTicks(20)).asTicks());
	}
}
