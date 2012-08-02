package com.gamezgalaxy.GGS.sql;

import java.sql.ResultSet;

public interface ISQL {
	
	void ExecuteQuery(String command);
	
	void ExecuteQuery(String[] commands);
	
	ResultSet fillData(String command);

}
