package alde.commons.network.batch;

import alde.commons.util.as3.Vector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Handler {

	private Logger log = LoggerFactory.getLogger(Handler.class);

	private static final int DELAY_BETWEEN_FREE_WORKER_CHECK = 3;

	private List<Worker> workerList = new ArrayList<>();
	private Vector<GetWebsiteTaskAvoidAnswer> tasks = new Vector<>();

	private int maxWorkers;

	public Handler(int maxWorkers) {
		this.maxWorkers = maxWorkers;
		watchForTasks();
	}

	private Thread watchWorkers = new Thread() {
		public void run() {
			try {


				log.info("Looking for free workers...");

				boolean hasWorker = false;

				if (tasks.size() > 0) {
					for (Worker work : workerList) {
						if (!work.isBusy()) {
							work.receiveTask(tasks.remove(0));
							hasWorker = true;
						}
					}
				}

				if (!hasWorker && workerList.size() < maxWorkers) {
					log.info("Creating new worker...");

					Worker worker = new Worker();
					worker.receiveTask(tasks.remove(0));
					workerList.add(worker);
				}

				log.info("Size : " + workerList.size());

			} catch (Exception e) {
				e.printStackTrace();
			}

		}
	};


	private void watchForTasks() {
		log.info("Beginning watch for tasks loop...");

		ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
		executor.scheduleAtFixedRate(watchWorkers, 0, DELAY_BETWEEN_FREE_WORKER_CHECK, TimeUnit.SECONDS);
	}

	public void addTask(GetWebsiteTaskAvoidAnswer task) {
		log.info("Received new task");

		tasks.add(task);
	}

}
