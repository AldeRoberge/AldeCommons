package alde.commons.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jfree.util.Log;

/**
 * See alde.commons.network.proxy for a working example of Worker and Task
 * @author Alde
 */
public abstract class WorkerHandler<T extends Task> {

	volatile List<Worker<T>> workers = new ArrayList<Worker<T>>();

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

				report();

				new Thread() {
					public void run() {
						if (!workers.isEmpty() && !queuedTasks.isEmpty()) {
							for (final Worker<T> w : workers) {
								if (!w.isBusy()) {
									w.receiveTask(queuedTasks.remove(0));
									amountOfSentTasks++;
								}
							}
						}
					}
				}.start();

			}

			// Prints debug data to the console (amount of workers and tasks)
			private void report() {

				try {

					int amountOfWorkers = 0;
					int amountOfFreeWorkers = 0;

					for (Worker<T> w : workers) {

						amountOfWorkers++;

						if (!w.isBusy()) {
							amountOfFreeWorkers++;
						}

					}

					Log.info(queuedTasks.size() + " queued tasks. " + amountOfSentTasks + " completed tasks out of " + amountOfTasks + ". " + amountOfFreeWorkers + " free workers out of "
							+ amountOfWorkers + ".");

				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}, 0, 5000);
	}

}