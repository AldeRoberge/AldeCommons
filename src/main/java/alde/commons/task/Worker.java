package alde.commons.task;

public abstract class Worker<T extends Task> {

	public T task;

	private boolean isBusy;

	// Worker stats
	String name;
	int completedTasks = 0;
	int receivedTasks = 0;

	public Worker(String name) {
		this.name = name;
	}

	public boolean isBusy() {
		return isBusy;
	}

	public void receiveTask(T task) {
		isBusy = true;
		this.task = task;

		receivedTasks++;
	}

	public void completeTask() {
		isBusy = false;
		this.task.completed();

		completedTasks++;
	}

}
