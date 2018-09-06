package alde.commons.task;

public abstract class Task {

	private final float timeCreated = System.currentTimeMillis();
	private boolean isCompleted;
	private float timeCompleted;

	private float timeToComplete = -1;

	public boolean isCompleted() {
		return isCompleted;
	}

	/**
	 * Do not use this method directly. Instead use completeTask (Worker).
	 */
	protected void completed() {
		isCompleted = true;
		timeCompleted = System.currentTimeMillis();
		timeToComplete = timeCreated - timeCompleted;
	}

	public float getTimeToComplete() {
		if (!isCompleted) {
			return -1;
		}
		return timeToComplete;
	}

}
