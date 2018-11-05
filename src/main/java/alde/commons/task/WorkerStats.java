package alde.commons.task;

public class WorkerStats {

	String name;
	int completedTasks = 0;
	int receivedTasks = 0;

	public WorkerStats(String name) {
		this.name = name;
	}

}
