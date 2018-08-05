package alde.commons.util;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import javax.swing.JFrame;
import javax.swing.JPanel;

class SplashScreen {

	private TimerTask close;

	public SplashScreen(final BufferedImage inImage, final BufferedImage outImage, final BufferedImage titleImage,
			final JFrame parentComponent, final boolean automaticClose, final int secondsBeforeClose) {

		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {

				final JFrame frame = new JFrame();

				SplashScreenPane s = new SplashScreenPane(inImage, outImage, titleImage, frame);

				frame.add(s);
				frame.setUndecorated(true);
				frame.setBackground(new Color(0, 0, 0, 0));
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setOpacity(0f);
				frame.setVisible(true);

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

	private float currentFrameOpacity = 0F;

	class SplashScreenPane extends JPanel {
		private static final long serialVersionUID = 1L;

		static final long RUNNING_TIME = 2000;

		private BufferedImage bgInImage;
		private BufferedImage bgOutImage;

		private BufferedImage textImage;

		private float alpha = 0f;
		private long startTime = -1;

		SplashScreenPane(BufferedImage bgInImage, BufferedImage bgOutImage, BufferedImage titleImage,
				final JFrame frame) {

			this.bgInImage = bgInImage;
			this.bgOutImage = bgOutImage;
			this.textImage = titleImage;

			//

			setLayout(new GridBagLayout());

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

			alpha = 0f;
			BufferedImage tmp = bgInImage;
			bgInImage = bgOutImage;
			bgOutImage = tmp;

			final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

			Runnable helloRunnable = new Runnable() {
				@Override
				public void run() {
					if (startTime < 0) {
						startTime = System.currentTimeMillis();
					} else {

						long time = System.currentTimeMillis();
						long duration = time - startTime;

						if ((currentFrameOpacity + alpha) >= 1) {
							currentFrameOpacity = 1;

							frame.setOpacity(1);
						} else {
							currentFrameOpacity = alpha;
							frame.setOpacity(currentFrameOpacity);
						}

						if (duration >= RUNNING_TIME) {
							startTime = -1;
							executor.shutdown();
							alpha = 0f;
						} else {
							alpha = 1f - ((float) duration / (float) RUNNING_TIME);
						}
						repaint();
					}
				}
			};

			executor.scheduleAtFixedRate(helloRunnable, 0, 50, TimeUnit.MILLISECONDS);

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

			Rectangle r = new Rectangle(0, 0, (int) (bgOutImage.getWidth() / 1.5) - 1,
					(int) (bgOutImage.getHeight() / 1.5) - 1);

			g2d.setColor(Color.WHITE);
			g2d.draw(r);

			centerString(g2d, r, "Version 3", new Font("Arial", Font.BOLD, 12),
					(int) (bgOutImage.getHeight() / 1.5 / 3));

			g2d.dispose();

		}

		void centerString(Graphics g, Rectangle r, String s, Font font, int yOffset) {
			FontRenderContext frc = new FontRenderContext(null, true, true);

			Rectangle2D r2D = font.getStringBounds(s, frc);
			int rWidth = (int) Math.round(r2D.getWidth());
			int rHeight = (int) Math.round(r2D.getHeight());
			int rX = (int) Math.round(r2D.getX());
			int rY = (int) Math.round(r2D.getY());

			int a = (r.width / 2) - (rWidth / 2) - rX;
			int b = (r.height / 2) - (rHeight / 2) - rY;

			g.setFont(font);
			g.drawString(s, r.x + a, r.y + b + yOffset);
		}

	}

}