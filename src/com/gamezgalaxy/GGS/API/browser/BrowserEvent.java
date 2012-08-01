package com.gamezgalaxy.GGS.API.browser;

import com.gamezgalaxy.GGS.API.Event;
import com.gamezgalaxy.GGS.iomodel.Browser;

public abstract class BrowserEvent extends Event {
	private Browser _b;
	
	public BrowserEvent(Browser b) {
		this._b = b;
	}
	
	public Browser getBrowserObject() {
		return _b;
	}
}
