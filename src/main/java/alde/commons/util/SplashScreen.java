package alde.commons.util;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.TimerTask;

public class SplashScreen {

	private TimerTask close;

	public SplashScreen(final BufferedImage inImage, final BufferedImage outImage,
			final BufferedImage titleImage, final JFrame parentComponent, final boolean automaticClose,
			final int secondsBeforeClose) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {

				final JFrame frame = new JFrame();
				frame.add(new SplashScreenPane(inImage, outImage, titleImage));
				frame.setUndecorated(true);
				frame.setBackground(new Color(0, 0, 0, 0));
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setOpacity(0f);
				frame.setVisible(true);

				/* Fade in */

				new Thread("Fade in") {

					@Override
					public void run() {
						float previousOpacity = 0;
						float increments = 0.2f;
						boolean go = true;

						while (go) { //gradually increase opacity
							if ((previousOpacity + increments) >= 1) {
								go = false;
								frame.setOpacity(1f);
							} else {
								frame.setOpacity(frame.getOpacity() + increments);
								previousOpacity = frame.getOpacity() + increments;
							}
						}
					}
				}.start();

				/* Automatic close */

				if (automaticClose) {
					close = new TimerTask() {
						@Override
						public void run() {
							parentComponent.setVisible(true); //Show the program

							frame.dispose();
							close.cancel();
						}
					};

					java.util.Timer timer = new java.util.Timer();
					timer.schedule(close, secondsBeforeClose * 1000);

					//

					WindowListener exitListener = new WindowAdapter() {
						@Override
						public void windowClosing(WindowEvent e) {
							close.run();
						}
					};
					frame.addWindowListener(exitListener);

				}

			}
		});
	}

	class SplashScreenPane extends JPanel {
		private static final long serialVersionUID = 1L;

		static final long RUNNING_TIME = 800;

		private BufferedImage bgInImage;
		private BufferedImage bgOutImage;

		private BufferedImage textImage;

		private float alpha = 0f;
		private long startTime = -1;

		SplashScreenPane(BufferedImage bgInImage, BufferedImage bgOutImage, BufferedImage titleImage) {

			this.bgInImage = bgInImage;
			this.bgOutImage = bgOutImage;
			this.textImage = titleImage;

			//

			setLayout(new GridBagLayout());

			go();

			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					close.run();
				}

			});

		}

		void go() {
			alpha = 0f;
			BufferedImage tmp = bgInImage;
			bgInImage = bgOutImage;
			bgOutImage = tmp;

			final Timer timer = new Timer(0, new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (startTime < 0) {
						startTime = System.currentTimeMillis();
					} else {

						long time = System.currentTimeMillis();
						long duration = time - startTime;
						if (duration >= RUNNING_TIME) {
							startTime = -1;
							((Timer) e.getSource()).stop();
							alpha = 0f;
						} else {
							alpha = 1f - ((float) duration / (float) RUNNING_TIME);
						}
						repaint();
					}
				}
			});

			timer.start();

		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension((int) (bgInImage.getWidth() / 1.5), (int) (bgInImage.getHeight() / 1.5));
		}

		@Override
		protected void paintComponent(Graphics g) {

			super.paintComponent(g);

			Graphics2D g2d = (Graphics2D) g;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
			g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION,
					RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
			g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

			g2d.setComposite(AlphaComposite.SrcAtop.derive(alpha));
			g2d.drawImage(bgInImage, 0, 0, getWidth(), getHeight(), this);

			g2d.setComposite(AlphaComposite.SrcAtop.derive(1f - alpha));
			g2d.drawImage(bgOutImage, 0, 0, getWidth(), getHeight(), this);

			g2d.setComposite(AlphaComposite.SrcAtop.derive(1f));
			g2d.drawImage(textImage, 0, 0, getWidth(), getHeight(), this);

			g2d.dispose();

		}

	}

}