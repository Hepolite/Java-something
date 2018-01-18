package com.hepolite.api.units;

public final class Time
{
	public static final long TICKS_PER_SECOND = 20;
	public static final long SECONDS_PER_MINUTE = 60;
	public static final long MINUTES_PER_HOUR = 60;
	public static final long HOURS_PER_DAY = 24;
	public static final long DAYS_PER_WEEK = 7;
	public static final long DAYS_PER_MONTH = 30;
	public static final long DAYS_PER_YEAR = 365;

	/**
	 * Constructs a new time representing an instant
	 * 
	 * @return The new time instance
	 */
	public static Time fromInstant()
	{
		return new Time();
	}
	/**
	 * Constructs a new time representing the given amount of ticks
	 * 
	 * @param ticks The time measured in ticks
	 * @return The new time instance
	 */
	public static Time fromTicks(final long ticks)
	{
		return new Time(ticks);
	}
	/**
	 * Constructs a new time representing the given amount of seconds
	 * 
	 * @param seconds The time measured in seconds
	 * @return The new time instance
	 */
	public static Time fromSeconds(final long seconds)
	{
		return fromTicks(TICKS_PER_SECOND * seconds);
	}
	/**
	 * Constructs a new time representing the given amount of minutes
	 * 
	 * @param minutes The time measured in minutes
	 * @return The new time instance
	 */
	public static Time fromMinutes(final long minutes)
	{
		return fromSeconds(SECONDS_PER_MINUTE * minutes);
	}
	/**
	 * Constructs a new time representing the given amount of hours
	 * 
	 * @param hours The time measured in hours
	 * @return The new time instance
	 */
	public static Time fromHours(final long hours)
	{
		return fromMinutes(MINUTES_PER_HOUR * hours);
	}
	/**
	 * Constructs a new time representing the given amount of days
	 * 
	 * @param days The time measured in days
	 * @return The new time instance
	 */
	public static Time fromDays(final long days)
	{
		return fromHours(HOURS_PER_DAY * days);
	}
	/**
	 * Constructs a new time representing the given amount of weeks
	 * 
	 * @param weeks The time measured in weeks
	 * @return The new time instance
	 */
	public static Time fromWeeks(final long weeks)
	{
		return fromDays(DAYS_PER_WEEK * weeks);
	}
	/**
	 * Constructs a new time representing the given amount of months, assuming
	 * {@code DAYS_PER_MONTH} days per month
	 * 
	 * @param months The time measured in months
	 * @return The new time instance
	 */
	public static Time fromMonths(final long months)
	{
		return fromDays(DAYS_PER_MONTH * months);
	}
	/**
	 * Constructs a new time representing the given amount of years, assuming {@code DAYS_PER_YEAR}
	 * days per year
	 * 
	 * @param years The time measured in years
	 * @return The new time instance
	 */
	public static Time fromYears(final long years)
	{
		return fromDays(DAYS_PER_YEAR * years);
	}

	// ...

	private Long time;

	/**
	 * Constructs a time of zero duration
	 */
	private Time()
	{
		this(0L);
	}
	/**
	 * Creates a new time lasting for the given duration
	 * 
	 * @param time The time, measured in ticks
	 */
	private Time(final long time)
	{
		this.time = time;
	}

	/// ...

	/**
	 * Increases the time by a single tick and returns the same instance
	 * 
	 * @return Returns the same instance as this method is invoked on
	 */
	public Time increment()
	{
		this.time++;
		return this;
	}
	/**
	 * Increases the time by the specified amount and returns the same instance
	 * 
	 * @return Returns the same instance as this method is invoked on
	 */
	public Time increment(final Time time)
	{
		this.time += time.time;
		return this;
	}
	/**
	 * Decreases the time by a single tick to a minimum of 0 and returns the same instance
	 * 
	 * @return Returns the same instance as this method is invoked on
	 */
	public Time decrement()
	{
		this.time = Math.max(0L, this.time - 1L);
		return this;
	}
	/**
	 * Decreases the time by the specified amount to a minimum of 0 and returns the same instance
	 * 
	 * @return Returns the same instance as this method is invoked on
	 */
	public Time decrement(final Time time)
	{
		this.time = Math.max(0L, this.time - time.time);
		return this;
	}

	// ...

	/**
	 * @return The time represented measured in ticks
	 */
	public long asTicks()
	{
		return time;
	}
	/**
	 * @return The time represented measured in seconds
	 */
	public long asSeconds()
	{
		return asTicks() / TICKS_PER_SECOND;
	}
	/**
	 * @return The time represented measured in minutes
	 */
	public long asMinutes()
	{
		return asSeconds() / SECONDS_PER_MINUTE;
	}
	/**
	 * @return The time represented measured in hours
	 */
	public long asHours()
	{
		return asMinutes() / MINUTES_PER_HOUR;
	}
	/**
	 * @return The time represented measured in days
	 */
	public long asDays()
	{
		return asHours() / HOURS_PER_DAY;
	}
	/**
	 * @return The time represented measured in weeks
	 */
	public long asWeeks()
	{
		return asDays() / DAYS_PER_WEEK;
	}
	/**
	 * @return The time represented measured in months, assuming {@code DAYS_PER_MONTH} days per
	 *         month
	 */
	public long asMonths()
	{
		return asDays() / DAYS_PER_MONTH;
	}
	/**
	 * @return The time represented measured in months, assuming {@code DAYS_PER_YEAR} days per year
	 */
	public long asYears()
	{
		return asDays() / DAYS_PER_YEAR;
	}
}
