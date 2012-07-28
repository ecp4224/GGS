package com.gamezgalaxy.GGS.API.plugin;

import com.gamezgalaxy.GGS.server.Server;
import com.gamezgalaxy.GGS.server.Tick;

public abstract class Game extends Plugin implements Tick {

	public Game(Server server) {
		super(server);
	}
	
	public abstract void Start();
	
	public abstract void Stop();
	
	public abstract void Tick();
}
