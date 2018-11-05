package alde.commons.task;

public abstract class Worker<T extends Task> {

	public T task;

	private boolean isBusy;

	WorkerStats workerStats;

	public Worker(String name) {
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

	public void completeTask() {
		isBusy = false;
		this.task.completed();

		workerStats.completedTasks++;
	}

}
