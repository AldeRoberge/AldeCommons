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

/**
 * SplashScreen s = new SplashScreen().setTitle().setSubtitle();
 * s.show();
 */
public class SplashScreen extends JFrame {

	private static final long serialVersionUID = 1L;

	private TimerTask closeTask;

	private SplashScreen splashScreen;

	private Runnable runAfterClose;

	SplashScreenPane splashScreenPane;

	public SplashScreen(BufferedImage backgroundImage, BufferedImage backgroundToImage, BufferedImage titleImage) {
		this.splashScreen = this;

		SplashScreenPane splashScreenPane = new SplashScreenPane(backgroundImage, backgroundToImage, titleImage);

		add(splashScreenPane);
		setUndecorated(true);
		setBackground(Color.BLACK);
		pack();
		setLocationRelativeTo(null);
		setOpacity(0f);

		closeTask = new TimerTask() {
			@Override
			public void run() {
				dispose();
				closeTask.cancel();

				if (runAfterClose != null) {
					runAfterClose.run();
				}
			}
		};

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				closeTask.run();
			}
		});
	}

	public SplashScreen setAutomaticClose(int secondsBeforeClose) {
		java.util.Timer timer = new java.util.Timer();
		timer.schedule(closeTask, secondsBeforeClose * 1000);

		return splashScreen;
	}

	public SplashScreen setRunnableAfterClose(final Runnable runAfterClose) {
		this.runAfterClose = runAfterClose;
		return splashScreen;
	}

	@Override
	public void show() {
		super.setVisible(true);
		splashScreenPane.startFading();
	}

	class SplashScreenPane extends JPanel {
		private static final long serialVersionUID = 1L;

		public float currentFrameOpacity = 0F;

		static final long RUNNING_TIME = 2000;

		private BufferedImage bgInImage;
		private BufferedImage bgOutImage;
		private BufferedImage textImage;

		private float alpha = 0f;
		private long startTime = -1;
		
		String subTitle = "Version 0";

		SplashScreenPane(BufferedImage bgInImage, BufferedImage bgOutImage, BufferedImage titleImage) {

			this.bgInImage = bgInImage;
			this.bgOutImage = bgOutImage;
			this.textImage = titleImage;

			setLayout(new GridBagLayout());

			addMouseListener(new MouseAdapter() {

				@Override
				public void mouseReleased(MouseEvent e) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}

					closeTask.run();
				}

			});

			alpha = 0f;
			BufferedImage tmp = bgInImage;
			bgInImage = bgOutImage;
			bgOutImage = tmp;

		}

		public void startFading() {
			final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);

			executor.scheduleAtFixedRate(new Runnable() {
				@Override
				public void run() {
					if (startTime < 0) {
						startTime = System.currentTimeMillis();
					} else {

						long time = System.currentTimeMillis();
						long duration = time - startTime;

						if ((currentFrameOpacity + alpha) >= 1) {
							currentFrameOpacity = 1;

							setOpacity(1);
						} else {
							currentFrameOpacity = alpha;
							setOpacity(currentFrameOpacity);
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
			}, 0, 50, TimeUnit.MILLISECONDS);
			
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

			drawCenteredString(g2d, r, subTitle, new Font("Arial", Font.BOLD, 12),
					(int) (bgOutImage.getHeight() / 1.5 / 3));

			g2d.dispose();

		}

		void drawCenteredString(Graphics graphics, Rectangle rectangle, String toPrint, Font font, int yOffset) {
			FontRenderContext frc = new FontRenderContext(null, true, true);
			Rectangle2D r2D = font.getStringBounds(toPrint, frc);
			
			int rWidth = (int) Math.round(r2D.getWidth());
			int rHeight = (int) Math.round(r2D.getHeight());
			
			int rX = (int) Math.round(r2D.getX());
			int rY = (int) Math.round(r2D.getY());

			int a = (rectangle.width / 2) - (rWidth / 2) - rX;
			int b = (rectangle.height / 2) - (rHeight / 2) - rY;

			graphics.setFont(font);
			graphics.drawString(toPrint, rectangle.x + a, rectangle.y + b + yOffset);
		}

	}

}