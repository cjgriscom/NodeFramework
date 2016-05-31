package com.quirkygaming.nodeframework.util;

public class WebColor {
	private short red;
	private short green;
	private short blue;
	private short alpha;
	
	public WebColor(int red, int green, int blue) {
		this(red, green, blue, 255);
	}
	
	public WebColor(int red, int green, int blue, int alpha) {
		setRed(red);
		setGreen(green);
		setBlue(blue);
		setAlpha(alpha);
	}
	
	public void setRed(int value) {
		this.red = parseIntValue(value);
	}
	
	public void setGreen(int value) {
		this.green = parseIntValue(value);
	}
	
	public void setBlue(int value) {
		this.blue = parseIntValue(value);
	}
	
	public void setAlpha(int value) {
		this.alpha = parseIntValue(value);
	}

	public short getRed() {
		return red;
	}
	
	public short getGreen() {
		return green;
	}
	
	public short getBlue() {
		return blue;
	}
	
	public short getAlpha() {
		return alpha;
	}
	
	public String getHexRGB() {
		// Format for hexadecimal
		String r = Integer.toHexString(red);
		String g = Integer.toHexString(green);
		String b = Integer.toHexString(blue);
		
		return 	(r.length() < 2 ? "0" : "") + r +
				(g.length() < 2 ? "0" : "") + g +
				(b.length() < 2 ? "0" : "") + b;
	}
	
	private short parseIntValue(int colorValue) {
		if (colorValue >= 0 && colorValue <= 255) {
			return (short) colorValue;
		} else {
			throw new RuntimeException("Color values must be between 0 and 255.");
		}
	}
}