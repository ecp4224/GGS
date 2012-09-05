package com.gamezgalaxy.GGS.world;

public interface Generator {
	
	/**
	 * Generate the structure/world
	 * @param l
	 *         The level where to generate
	 */
	public void generate(Level l);
	
	/**
	 * Generate the structure/world
	 * @param l
	 *         The level where to generate
	 * @param x
	 *         The x position where to generate
	 * @param y
	 *         The y position where to generate
	 * @param z
	 *         The z position where to generate
	 */
	public void generate(Level l, int x, int y, int z);
}
