/*******************************************************************************
 * Copyright (c) 2012 GamezGalaxy.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the GNU Public License v3.0
 * which accompanies this distribution, and is available at
 * http://www.gnu.org/licenses/gpl.html
 ******************************************************************************/
package com.gamezgalaxy.GGS.console;

import java.util.Scanner;

import com.gamezgalaxy.GGS.server.Server;

public class Main {
	
	public static void main(String[] args) {
		Server s = new Server("Test", 25558, "Test");
		s.Start();
		new Scanner(System.in).nextLine();
	}

}
