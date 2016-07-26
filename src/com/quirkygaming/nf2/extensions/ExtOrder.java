package com.quirkygaming.nf2.extensions;

public enum ExtOrder {

	USER_FORCE_LOWEST(-300), VERY_LOW(-200), LOW(-100), NORMAL(0), HIGH(100), VERY_HIGH(200);
	
	private final int power;
	
	ExtOrder(int power) {
		this.power = power;
	}
	
	public int getPower() {
		return power;
	}
}
