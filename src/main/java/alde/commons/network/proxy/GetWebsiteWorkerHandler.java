package alde.commons.network.proxy;

import alde.commons.task.WorkerHandler;

public class GetWebsiteWorkerHandler extends WorkerHandler<GetWebsiteTask> {

	public GetWebsiteWorkerHandler(int amountOfWorkers) {
		for (int i = 0; i < amountOfWorkers; i++) {
			this.addWorker(new GetWebsiteWorker());
		}
	}

}
