package alde.commons.task;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class VerbalWorkerHandler<T extends Task> extends WorkerHandler<T> {

	List<Consumer<List<Worker>>> listeningForWorkerChanges = new ArrayList<Consumer<List<Worker>>>();

	WorkerHandlerUI workerHandlerUI = new WorkerHandlerUI(this);

	public WorkerHandlerUI getUI() {
		return workerHandlerUI;
	}

	public void registerListeningForWorkerChanges(Consumer<List<Worker>> consumer) {
		System.out.println(consumer + " " + listeningForWorkerChanges);
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
