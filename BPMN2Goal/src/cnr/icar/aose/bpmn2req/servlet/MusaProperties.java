package cnr.icar.aose.bpmn2req.servlet;

public class MusaProperties {
	
	private static String dbMusaAdress="localhost";
	private static String dbPort="3306";
	private static String userDB="root";
	private static String passDB="root";
	private static String dbName="musa_workflow";
	private static String port="2004";
	
	//IPUBLIC CONFIGURAZIONI
//	private static String ipAdress="192.168.111.64";
//	private static String dbMusaAdress="192.168.111.64";
//	private static String dbPort="3306";
//	private static String userDB="root";
//	private static String passDB="password";
//	private static String dbName="musa_workflow";
//	private static String port="2004";
	
	public static String getPort() {
		return port;
	}
	public static void setPort(String port) {
		MusaProperties.port = port;
	}
	public static String getDbMusaAdress() {
		return dbMusaAdress;
	}
	public static void setDbMusaAdress(String dbMusaAdress) {
		MusaProperties.dbMusaAdress = dbMusaAdress;
	}
	public static String getDbPort() {
		return dbPort;
	}
	public static void setDbPort(String dbPort) {
		MusaProperties.dbPort = dbPort;
	}
	public static String getUserDB() {
		return userDB;
	}
	public static void setUserDB(String userDB) {
		MusaProperties.userDB = userDB;
	}
	public static String getPassDB() {
		return passDB;
	}
	public static void setPassDB(String passDB) {
		MusaProperties.passDB = passDB;
	}
	public static String getDbName() {
		return dbName;
	}
	public static void setDbName(String dbName) {
		MusaProperties.dbName = dbName;
	}

	
	
}
