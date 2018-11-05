package alde.commons.task;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.border.LineBorder;
import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
UI for WorkerHandler
*/
public class WorkerHandlerUI extends JPanel {

	VerbalWorkerHandler workerHandler;

	static Logger log = LoggerFactory.getLogger(WorkerHandlerUI.class);

	/** Directory listing */
	private JTable table;
	private WorkerTableModel tableModel;

	public WorkerHandlerUI(VerbalWorkerHandler<?> workerHandler) {
		this.workerHandler = workerHandler;

		setLayout(new BorderLayout(0, 0));

		JScrollPane scrollPane = new JScrollPane();
		add(scrollPane, BorderLayout.CENTER);

		table = new JTable();
		table.setAutoCreateRowSorter(true);
		tableModel = new WorkerTableModel();
		table.setModel(tableModel);
		table.setRowHeight(40);

		scrollPane.setViewportView(table);

		workerHandler.registerListeningForWorkerChanges(new Consumer<List<Worker>>() {
			@Override
			public void accept(List<Worker> t) {
				setTableData(t);
			}
		});

	}

	/** Update the table on the EDT */
	void setTableData(List<Worker> workers) {
		log.info("Received list of workers...");
		tableModel.setFiles(workers);
	}

}

class WorkerTableModel extends AbstractTableModel {

	static Logger log = LoggerFactory.getLogger(WorkerTableModel.class);

	private List<Worker> workers = new ArrayList<>();

	private String[] columns = { "Name", "Received tasks", "Completed tasks" };

	public Object getValueAt(int row, int column) {
		Worker worker = workers.get(row);
		switch (column) {
		case 0:
			return worker.workerStats.name;
		case 1:
			return worker.workerStats.receivedTasks;
		case 2:
			return worker.workerStats.completedTasks;
		default:
			System.err.println("Logic Error");
		}
		return "";
	}

	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

	@Override
	public int getRowCount() {
		return workers.size();
	}

	public void setFiles(List<Worker> workers) {
		this.workers = workers;
		fireTableDataChanged();
	}

}