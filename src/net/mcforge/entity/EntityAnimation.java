package net.mcforge.entity;

public enum EntityAnimation {
	NO_ANIMATION(0), SWING_ARM(1), DAMAGE_ANIMATION(2), LEAVE_BED(3), EAT_FOOD(5), UNKNOWN(102), CROUCH(104), UNCROUCH(105);
	
	EntityAnimation(int animationID) {
		this.animationID = animationID;
	}
	
	private int animationID;
	
	public int getAnimationID() {
		return animationID;
	}
}
