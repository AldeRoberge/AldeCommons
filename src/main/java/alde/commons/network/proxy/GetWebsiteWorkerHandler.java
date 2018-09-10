package alde.commons.network.proxy;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import alde.commons.task.WorkerHandler;

public class GetWebsiteWorkerHandler extends WorkerHandler<GetWebsiteTask> {

	static GetWebsiteWorkerHandler instance;

	public static GetWebsiteWorkerHandler get() {
		if (instance == null) {
			instance = new GetWebsiteWorkerHandler();
			for (int i = 0; i < 100; i++) {
				instance.addWorker(new GetWebsiteWorker());
			}
		}
		return instance;
	}

	public static void main(String[] args) {
		System.out.println("Testing 'WorkerHandler'...");

		JFrame f = new JFrame();
		f.setLayout(new BorderLayout());
		f.getContentPane().add(GetWebsiteWorkerHandler.get().getTaskPanel(), BorderLayout.CENTER);

		f.setVisible(true);
		
	}



}
