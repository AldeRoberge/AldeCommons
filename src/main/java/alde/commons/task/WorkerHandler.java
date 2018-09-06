package alde.commons.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * See alde.commons.network.proxy for a working example of Worker and Task
 * 
 * @author Alde
 */
public abstract class WorkerHandler<T extends Task> {

	List<Worker<T>> workers = new ArrayList<Worker<T>>();
	List<T> queuedTasks = new ArrayList<T>();

	List<T> allTasks = new ArrayList<T>();

	public void addTask(T task) {
		queuedTasks.add(task);
		allTasks.add(task);
	}

	public void addWorker(Worker<T> worker) {
		workers.add(worker);
	}

	public WorkerHandler() {
		Timer lookForFreeWorkers = new Timer();
		lookForFreeWorkers.schedule(new TimerTask() {
			public void run() {

				report();

				if (workers.isEmpty()) {
					System.out.println("No workers found.");
				} else {
					if (queuedTasks.isEmpty()) {
						System.out.println("No tasks found.");
					} else {
						for (final Worker<T> w : workers) {
							if (!w.isBusy()) {
								new Thread() {
									public void run() {
										w.receiveTask(queuedTasks.remove(0));
									}
								}.start();
							}
						}
					}
				}
			}

			// Prints debug data to the console (amount of workers and tasks)
			private void report() {
				int amountOfWorkers = 0;
				int amountOfFreeWorkers = 0;

				for (Worker<T> w : workers) {
					amountOfWorkers++;

					if (!w.isBusy()) {
						amountOfFreeWorkers++;
					}
				}

				int amountOfTasks = 0;
				int amountOfCompletedTaks = 0;

				for (Task t : allTasks) {
					amountOfTasks++;

					if (t.isCompleted()) {
						amountOfCompletedTaks++;
					}
				}

				System.out.println(queuedTasks.size() + " queued tasks. " + amountOfCompletedTaks
						+ " completed tasks out of " + amountOfTasks + ". " + amountOfFreeWorkers
						+ " free workers out of " + amountOfWorkers + ".");

			}
		}, 0, 5000);
	}
}
