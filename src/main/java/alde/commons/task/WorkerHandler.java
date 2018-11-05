package alde.commons.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alde.commons.network.proxy.ProxyLeecher;

/**
 * WorkerHandler delegates tasks to Workers
 * 
 * See alde.commons.network.proxy for a working example of Worker and Task
 * @author Alde
 */
public abstract class WorkerHandler<T extends Task> {

	private static Logger log = LoggerFactory.getLogger(WorkerHandler.class);

	public volatile List<Worker> workers = new ArrayList<>();
	public volatile List<T> queuedTasks = new ArrayList<T>();

	int amountOfTasks, amountOfSentTasks;

	public void addTask(T task) {
		queuedTasks.add(task);
		amountOfTasks++;
	}

	public void addWorker(Worker<T> worker) {
		workers.add(worker);
	}

	public void addWorkers(List<Worker<T>> workers) {
		workers.addAll(workers);
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

	synchronized private void checkForFreeWorkers() {

		for (final Worker<T> w : workers) {
			if (!w.isBusy()) {

				if (!workers.isEmpty() && !queuedTasks.isEmpty()) {
					w.receiveTask(queuedTasks.remove(0));
					amountOfSentTasks++;
				}
			}
		}

		log.info(workers.size() + " workers, " + queuedTasks.size() + " queued tasks. " + amountOfSentTasks + " completed tasks out of " + amountOfTasks + ".");

	}

}