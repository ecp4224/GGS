package com.gamezgalaxy.test.console;

import com.gamezgalaxy.GGS.API.GGSPlugin;

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
		super.onEnable();

		System.out.println("enable");
	}

	@Override
	public void onDisable()
	{
		super.onDisable();

		System.out.println("disable");
	}
}
