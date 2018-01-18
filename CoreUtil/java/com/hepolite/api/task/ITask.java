package com.hepolite.api.task;

import com.hepolite.api.units.Time;

public interface ITask
{
	/**
	 * Starts up the task if it has not already been started. The task will be performed after
	 * {@code startup} time.
	 * 
	 * @param startup The time it takes before the task starts executing
	 * @return Whether the task could start running or not.
	 */
	public boolean start(Time startup);
	/**
	 * Starts up the task if it has not already been started. The task will start after
	 * {@code startup} time, and repeat every {@code repeat} time.
	 * 
	 * @param startup The time it takes before the task starts executing
	 * @param repeat The delay between each subsequent repeat execution of the task
	 * @return Whether the task could start running or not.
	 */
	public boolean start(Time startup, Time repeat);
	/**
	 * Stops the task if it is already running, preventing it from being begin performed.
	 * 
	 * @return Whether the task could stop running or not
	 */
	public boolean stop();
}
