package alde.commons.network;

import alde.commons.task.VerbalWorkerHandler;

public class GetWebsiteWorkerHandler extends VerbalWorkerHandler<GetWebsiteTask> {

	static GetWebsiteWorkerHandler instance;

	private GetWebsiteWorkerHandler() {
		super();
	}

	public static GetWebsiteWorkerHandler get() {
		if (instance == null) {
			instance = new GetWebsiteWorkerHandler();
			for (int i = 0; i < 500; i++) {
				instance.addWorker(new GetWebsiteWorker("Worker " + i));
			}
		}
		return instance;
	}

}
