package net.mcforge.world;

/**
 * The Rotation class is used to store a rotation.<br>
 * rotations consist of three {@link Float floats} representing the yaw and the pitch.<br>
 * Yaw is an object's rotation along the X axis.<br>
 * Pitch is an object's rotation along the Y axis.
 */
public class Rotation {
	private float yaw;
    private float pitch;
    
	/**
	 * Gets the rotation's yaw.<br>
	 * Yaw is the rotation along the X axis.
	 */
    public float getYaw() {
		return yaw;
	}

	/**
	 * Gets the rotation's yaw.<br>
	 * Yaw is the rotation along the X axis.
	 * 
	 * @param yaw
	 * 			  The new yaw.
	 */
	public void setYaw(float yaw) {
		this.yaw = yaw;
	}

	/**
	 * Gets the rotation's pitch.<br>
	 * Pitch is the rotation along the Y axis.
	 */
	public float getPitch() {
		return pitch;
	}

	/**
	 * Sets the rotation's pitch.<br>
	 * Pitch is the rotation along the Y axis.
	 * 
	 * @param pitch
	 * 				The new pitch.
	 */
	public void setPitch(float pitch) {
		this.pitch = pitch;
	}
}
