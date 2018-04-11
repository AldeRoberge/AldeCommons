package util;

import java.awt.*;

public class ZeroToOneGreenToRed {
	/**
	 *
	 * @param power
	 *            0 to 1
	 * @return green to red color
	 * 
	 * @see https://stackoverflow.com/questions/340209/generate-colors-between-red-and-green-for-a-power-meter
	 */
	public Color getColor(double power) {
		double H = power * 0.35; // Hue
		double S = 1; // Saturation
		double B = 0.75; // Brightness

		return Color.getHSBColor((float) H, (float) S, (float) B);
	}
}
