package com.gamezgalaxy.GGS.console;

import java.util.Scanner;

import com.gamezgalaxy.GGS.server.Server;

public class Main {
	
	public static void main(String[] args) {
		Server s = new Server("Test", 25565, "Test");
		s.Start();
		new Scanner(System.in).nextLine();
	}

}
