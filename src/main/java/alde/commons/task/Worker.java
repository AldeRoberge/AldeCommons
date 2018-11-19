package alde.commons.task;

public abstract class Worker<T extends Task> {

	protected T task;

	private boolean isBusy;

	WorkerStats workerStats;

	protected Worker(String name) {
		this.workerStats = new WorkerStats(name);
	}

	public boolean isBusy() {
		return isBusy;
	}

	public void receiveTask(T task) {
		isBusy = true;
		this.task = task;

		workerStats.receivedTasks++;
	}

	protected void completeTask() {
		isBusy = false;
		this.task.completed();

		workerStats.completedTasks++;
	}

}
