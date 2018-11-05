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
public class WorkerHandlerUI extends JPanel implements ActionListener {

	VerbalWorkerHandler workerHandler;

	static Logger log = LoggerFactory.getLogger(WorkerHandlerUI.class);

	/** Directory listing */
	private JTable table;
	private WorkerTableModel tableModel;

	public WorkerHandlerUI(VerbalWorkerHandler<?> workerHandler) {

		this.workerHandler = workerHandler;

		workerHandler.registerListeningForWorkerChanges(new Consumer<List<Worker>>() {
			@Override
			public void accept(List<Worker> t) {
				setTableData(t);
			}
		});

		setLayout(new BorderLayout(3, 3));

		JPanel detailView = new JPanel(new BorderLayout(3, 3));
		detailView.setBorder(new LineBorder(new Color(0, 0, 0), 4));

		table = new JTable();
		table.setRowSelectionAllowed(true);
		table.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		table.setAutoCreateRowSorter(true);
		table.setShowVerticalLines(true);
		table.setSelectionBackground(new Color(136, 23, 152));
		table.setSelectionForeground(Color.WHITE);

		table.setBackground(Color.BLACK);

		tableModel = new WorkerTableModel();
		table.setModel(tableModel);

		table.setRowHeight(40);

		JScrollPane tableScroll = new JScrollPane(table);
		Dimension d = tableScroll.getPreferredSize();
		tableScroll.setPreferredSize(new Dimension((int) d.getWidth(), (int) d.getHeight() / 2));
		detailView.add(tableScroll, BorderLayout.CENTER);

	}

	/** Update the table on the EDT */
	void setTableData(List<Worker> workers) {
		System.out.println("Received list of workers...");
		tableModel.setFiles(workers);
	}

	@Override
	public void actionPerformed(ActionEvent event) {

	}

}

class WorkerTableModel extends AbstractTableModel {

	static Logger log = LoggerFactory.getLogger(WorkerTableModel.class);

	private List<Worker> workers = new ArrayList<>();

	private String[] columns = { "Received tasks", "Completed tasks" };

	public Object getValueAt(int row, int column) {
		Worker worker = workers.get(row);
		switch (column) {
		case 0:
			return worker.receivedTasks;
		case 1:
			return worker.completedTasks;
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