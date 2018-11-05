package alde.commons.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jfree.util.Log;

/**
 * WorkerHandler delegates tasks to Workers
 * 
 * See alde.commons.network.proxy for a working example of Worker and Task
 * @author Alde
 */
public abstract class WorkerHandler<T extends Task> {

	volatile List<Worker> workers = new ArrayList<>();
	volatile List<T> queuedTasks = new ArrayList<T>();

	int amountOfTasks = 0;
	int amountOfSentTasks = 0;

	public void addTask(T task) {
		queuedTasks.add(task);
		amountOfTasks++;
	}

	public void addWorker(Worker<T> worker) {
		workers.add(worker);
	}

	public WorkerHandler() {
		start();
	}

	private void start() {
		Timer lookForFreeWorkers = new Timer();
		lookForFreeWorkers.schedule(new TimerTask() {
			public void run() {
				checkForFreeWorkers();
			}

		}, 0, 5000);
	}

	private void checkForFreeWorkers() {

		if (!workers.isEmpty() && !queuedTasks.isEmpty()) {
			for (final Worker<T> w : workers) {
				if (!w.isBusy()) {
					w.receiveTask(queuedTasks.remove(0));
					amountOfSentTasks++;
				}
			}
		}

		Log.info(queuedTasks.size() + " queued tasks. " + amountOfSentTasks + " completed tasks out of " + amountOfTasks + ".");

	}

}