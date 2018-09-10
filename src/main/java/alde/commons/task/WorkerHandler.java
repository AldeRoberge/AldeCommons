package alde.commons.task;

import java.awt.BorderLayout;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;
import org.jfree.util.Rotation;

/**
 * See alde.commons.network.proxy for a working example of Worker and Task
 * 
 * @author Alde
 */
public abstract class WorkerHandler<T extends Task> {

	List<Worker<T>> workers = new ArrayList<Worker<T>>();
	List<T> queuedTasks = new ArrayList<T>();

	List<T> allTasks = new ArrayList<T>();

	private TaskPanel<T> taskPanel = new TaskPanel<T>("Panel", allTasks);

	public TaskPanel getTaskPanel() {
		return taskPanel;
	}

	public void addTask(T task) {
		queuedTasks.add(task);
		allTasks.add(task);
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

				taskPanel.updateDataSet();

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

				try {

					synchronized (allTasks) {
						for (Task t : allTasks) {
							amountOfTasks++;

							if (t.isCompleted()) {
								amountOfCompletedTaks++;
							}
						}
					}

				} catch (Exception e) {
				}

				String toSay = getClass().getName() + " " + queuedTasks.size() + " queued tasks. "
						+ amountOfCompletedTaks + " completed tasks out of " + amountOfTasks + ". "
						+ amountOfFreeWorkers + " free workers out of " + amountOfWorkers + ".";

				System.out.println(toSay);

			}
		}, 0, 5000);
	}

}

class TaskPanel<T extends Task> extends JPanel {
	private static final long serialVersionUID = 1L;

	List<T> tasks;

	static DefaultPieDataset dataSet = new DefaultPieDataset();

	/**
	 * Creates a new demo.
	 * @param <T>
	 * @param title  the frame title.
	 */
	public TaskPanel(String title, List<T> allTasks) {
		setLayout(new BorderLayout());

		this.tasks = allTasks;

		dataSet = new DefaultPieDataset();

		add(createDemoPanel(), BorderLayout.CENTER);

		updateDataSet();
	}

	public void updateDataSet() {

		int waiting = 0;
		int completed = 0;

		try {

			for (Task t : tasks) {
				if (t.isCompleted()) {
					completed++;
				} else {
					waiting++;
				}
			}

		} catch (Exception e) {

		}

		dataSet.setValue("Waiting", waiting);
		dataSet.setValue("Completed", completed);
	}

	/**
	 * Creates a demo chart.
	 *
	 * @param dataset  the dataset.
	 * @return A chart.
	 */
	private static JFreeChart createChart(PieDataset dataset) {
		// create the chart...
		JFreeChart chart = ChartFactory.createPieChart3D("Tasks", // chart title
				dataset, // dataset
				true, // include legend
				false, false);

		PiePlot3D plot = (PiePlot3D) chart.getPlot();
		plot.setStartAngle(270);
		plot.setDirection(Rotation.ANTICLOCKWISE);
		plot.setForegroundAlpha(0.60f);
		return chart;
	}

	/**
	 * Creates a panel for the demo (used by SuperDemo.java).
	 *
	 * @return A panel.
	 */
	public static JPanel createDemoPanel() {
		JFreeChart chart = createChart(dataSet);
		return new ChartPanel(chart);
	}

}
