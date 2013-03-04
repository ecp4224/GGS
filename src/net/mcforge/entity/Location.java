package net.mcforge.entity;

/**
 * The Location class is used to store a location.<br>
 * Locations consist of three {@link Double doubles} representing the X, Y and Z position.
 */
public class Location { //probably the most random package placement ever
	private double x;
    private double y;
    private double z;
    
    
    /**
     * Sets all of the positions in the location to the specified values.
     * 
     * @param x
     * 			The new X position.
     * @param y
     * 			The new Y position.
     * @param z
     * 			The new Z positoon.
     */
    public void set(double x, double y, double z) {
    	setX(x);
    	setY(y);
    	setZ(z);
    }
    
    /**
     * Sets all of the positions in the location to the values of the specified location
     * 
     * @param location
     * 				   The location to use.
     */
    public void set(Location location) {
    	set(location.getX(), location.getY(), location.getZ());
    }
    
    /**
     * Gets the location's X position.
     */
    public double getX() {
		return x;
	}

    /**
     * Sets the location's X position.
     * 
     * @param x
     * 			The new X position.
     */
	public void setX(double x) {
		this.x = x;
	}

	/**
	 * Gets the location's Y position.
	 */
	public double getY() {
		return y;
	}

	/**
	 * Sets the location's Y position.
	 * 
	 * @param y
	 * 			The new Y position.
	 */
	public void setY(double y) {
		this.y = y;
	}

	/**
	 * Gets the location's Z position.
	 */
	public double getZ() {
		return z;
	}

	/**
	 * Sets the location's Z position.
	 * 
	 * @parm z
	 * 			The new Z position.
	 */
	public void setZ(double z) {
		this.z = z;
	}
}
