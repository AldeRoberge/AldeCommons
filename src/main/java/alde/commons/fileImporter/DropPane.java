package alde.commons.fileImporter;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.TooManyListenersException;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.awt.GridBagConstraints;
import java.awt.Insets;

/**
 * Based on https://stackoverflow.com/questions/13597233/how-to-drag-and-drop-files-from-a-directory-in-java
 */
public class DropPane extends JPanel {

	private DropTarget dropTarget;
	private DropTargetHandler dropTargetHandler;
	private Point dragPoint;

	private boolean dragOver = false;
	private BufferedImage target;

	private final JLabel title = new JLabel("Drag and drop files here");
	private final JLabel subTitle = new JLabel("0 files to be imported.");

	private FileImporter fileImporter;

	public DropPane(FileImporter f) {
		this.fileImporter = f;

		/*try {
			//target = ImageIO.read(new File(Icons.CROSS.getImagePath()));
		} catch (IOException ex) {
			ex.printStackTrace();
		}*/

		setLayout(new GridBagLayout());

		title.setFont(title.getFont().deriveFont(Font.BOLD, 20));
		GridBagConstraints gbc_title = new GridBagConstraints();
		gbc_title.insets = new Insets(0, 0, 5, 5);
		gbc_title.gridx = 0;
		gbc_title.gridy = 0;
		add(title, gbc_title);

		GridBagConstraints gbc_subTitle = new GridBagConstraints();
		gbc_subTitle.insets = new Insets(0, 0, 0, 5);
		gbc_subTitle.gridx = 0;
		gbc_subTitle.gridy = 1;
		add(subTitle, gbc_subTitle);

		updateMessage();

	}

	@Override
	public Dimension getPreferredSize() {
		return new Dimension(400, 400);
	}

	private DropTarget getMyDropTarget() {
		if (dropTarget == null) {
			dropTarget = new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, null);
		}
		return dropTarget;
	}

	private DropTargetHandler getDropTargetHandler() {
		if (dropTargetHandler == null) {
			dropTargetHandler = new DropTargetHandler();
		}
		return dropTargetHandler;
	}

	@Override
	public void addNotify() {
		super.addNotify();
		try {
			getMyDropTarget().addDropTargetListener(getDropTargetHandler());
		} catch (TooManyListenersException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void removeNotify() {
		super.removeNotify();
		getMyDropTarget().removeDropTargetListener(getDropTargetHandler());
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (dragOver) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setColor(new Color(0, 255, 0, 64));
			g2d.fill(new Rectangle(getWidth(), getHeight()));
			if (dragPoint != null && target != null) {
				int x = dragPoint.x - 12;
				int y = dragPoint.y - 12;
				g2d.drawImage(target, x, y, this);
			}
			g2d.dispose();
		}
	}

	class DropTargetHandler implements DropTargetListener {

		private Logger log = LoggerFactory.getLogger(DropTargetHandler.class);

		void processDrag(DropTargetDragEvent dtde) {
			if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				dtde.acceptDrag(DnDConstants.ACTION_COPY);
			} else {
				dtde.rejectDrag();
			}

			SwingUtilities.invokeLater(new DragUpdate(true, dtde.getLocation()));
			repaint();
		}

		@Override
		public void dragEnter(DropTargetDragEvent dtde) {
			processDrag(dtde);
		}

		@Override
		public void dragOver(DropTargetDragEvent dtde) {
			processDrag(dtde);
		}

		@Override
		public void dropActionChanged(DropTargetDragEvent dtde) {
		}

		@Override
		public void dragExit(DropTargetEvent dte) {
			SwingUtilities.invokeLater(new DragUpdate(false, null));
			repaint();
		}

		@Override
		public void drop(DropTargetDropEvent dtde) {

			SwingUtilities.invokeLater(new DragUpdate(false, null));

			Transferable transferable = dtde.getTransferable();
			if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
				dtde.acceptDrop(dtde.getDropAction());
				try {

					List<File> droppedFiles = (List<File>) transferable.getTransferData(DataFlavor.javaFileListFlavor);

					log.info(droppedFiles + ", size : " + droppedFiles.size());

					if (droppedFiles != null && droppedFiles.size() > 0) {
						fileImporter.importAll(droppedFiles);
						dtde.dropComplete(true);
					}

				} catch (Exception ex) {
					ex.printStackTrace();
				}
			} else {
				dtde.rejectDrop();
			}
		}
	}

	class DragUpdate implements Runnable {

		private boolean dragOver;
		private Point dragPoint;

		DragUpdate(boolean dragOver, Point dragPoint) {
			this.dragOver = dragOver;
			this.dragPoint = dragPoint;
		}

		@Override
		public void run() {
			DropPane.this.dragOver = dragOver;
			DropPane.this.dragPoint = dragPoint;
			DropPane.this.repaint();
		}
	}

	public void updateMessage() {
		int nbFiles = fileImporter.getTotalFilesToImport();

		if (nbFiles == 1) {
			subTitle.setText("1 file to be imported.");
		} else {
			subTitle.setText(nbFiles + " files to be imported.");
		}

	}
}