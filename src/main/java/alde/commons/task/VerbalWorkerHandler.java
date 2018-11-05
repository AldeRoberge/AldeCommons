package alde.commons.task;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import alde.commons.network.proxy.ProxyLeecher;

public class VerbalWorkerHandler<T extends Task> extends WorkerHandler<T> {

	private static Logger log = LoggerFactory.getLogger(ProxyLeecher.class);

	List<Consumer<List<Worker>>> listeningForWorkerChanges = new ArrayList<Consumer<List<Worker>>>();

	WorkerHandlerUI workerHandlerUI = new WorkerHandlerUI(this);

	public WorkerHandlerUI getUI() {
		return workerHandlerUI;
	}

	public void registerListeningForWorkerChanges(Consumer<List<Worker>> consumer) {
		log.info(consumer + " " + listeningForWorkerChanges);
		listeningForWorkerChanges.add(consumer);
	}

	private void triggerWorkersChanged() {
		for (Consumer<List<Worker>> consumer : listeningForWorkerChanges) {
			consumer.accept(workers);
		}
	}

	@Override
	public void addWorker(Worker<T> t) {
		super.addWorker(t);
		triggerWorkersChanged();
	}

}
