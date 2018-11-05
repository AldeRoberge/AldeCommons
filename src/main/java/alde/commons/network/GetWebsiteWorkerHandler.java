package alde.commons.network;

import alde.commons.task.VerbalWorkerHandler;

public class GetWebsiteWorkerHandler extends VerbalWorkerHandler<GetWebsiteTask> {

	public static final int NUMBER_OF_WORKERS = 500;

	static GetWebsiteWorkerHandler instance;

	private GetWebsiteWorkerHandler() {
		for (int i = 0; i < NUMBER_OF_WORKERS; i++) {
			addWorker(new GetWebsiteWorker("Worker " + i));
		}
	}

	public static GetWebsiteWorkerHandler get() {
		if (instance == null) {
			instance = new GetWebsiteWorkerHandler();
		}
		return instance;
	}

}
