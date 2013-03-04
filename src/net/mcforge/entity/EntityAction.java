package net.mcforge.entity;

public enum EntityAction {
	CROUCH(1), UNCROUCH(2), LEAVE_BED(3), START_SPRINTING(4), STOP_SPRINTING(5);
	
	EntityAction(int actionID) {
		this.actionID = actionID;
	}
	
	private int actionID;
	
	public int getActionID() {
		return actionID;
	}
}
