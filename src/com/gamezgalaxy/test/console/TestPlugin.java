package com.gamezgalaxy.test.console;

import com.gamezgalaxy.GGS.API.GGSPlugin;
import com.gamezgalaxy.GGS.server.Player;

/**
 * Created with IntelliJ IDEA.
 * User: Oliver Yasuna
 * Date: 7/23/12
 * Time: 9:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class TestPlugin extends GGSPlugin
{
	@Override
	public void onEnable()
	{
		System.out.println("enable");

		super.onEnable();
	}

	@Override
	public void onDisable()
	{
		System.out.println("disable");

		super.onEnable();
	}

	@Override
	public void onCommand(Player sender, String command, String[] args)
	{
		if(command.equals("/g"))
		{
			System.out.println("/g was used");
		}

		super.onCommand(sender, command, args);
	}
}
