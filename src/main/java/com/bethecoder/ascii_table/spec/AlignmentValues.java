package com.bethecoder.ascii_table.spec;

/**
 * Settings for use in aligning things
 * 
 * @author ben
 *
 */
public enum AlignmentValues {
	/**
	 * Align things to the left margin
	 */
	ALIGN_LEFT(-1),
	/**
	 * Align things away from both margins
	 */
	ALIGN_CENTER(0),
	/**
	 * Align things to the right margin
	 */
	ALIGN_RIGHT(1);

	private final int value;

	private AlignmentValues(int value) {
		this.value = value;
	}

	/**
	 * Get the number value of this alignment
	 * 
	 * @return The number value of this alignment
	 */
	public int getValue() {
		return value;
	}
}
