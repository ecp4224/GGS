package com.gamezgalaxy.GGS.sql;

import java.sql.Connection;
import java.sql.ResultSet;

import com.gamezgalaxy.GGS.server.Server;

public interface ISQL {
	
	void ExecuteQuery(String command);
	
	void ExecuteQuery(String[] commands);
	
	ResultSet fillData(String command);
	
	void Connect(Server server);
	
	void setPrefix(String prefix);
	
	String getPrefix();
	
	Connection getConnection();

}
