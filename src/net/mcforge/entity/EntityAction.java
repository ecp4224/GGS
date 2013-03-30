/*******************************************************************************
 * Copyright (c) 2013 MCForge.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
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
