package alde.commons.task;

public abstract class Worker<T extends Task> {

	public T task;

	private boolean isBusy;

	public boolean isBusy() {
		return isBusy;
	}
	
	public void receiveTask(T task) {
		isBusy = true;
		this.task = task;
	}

	public void completeTask() {
		isBusy = false;
		this.task.completed();
	}

}
