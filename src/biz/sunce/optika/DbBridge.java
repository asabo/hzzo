/*
 * Created on 2005.04.17
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package biz.sunce.optika;

import java.sql.Connection;
import java.sql.DriverManager;


public final class DbBridge {
	private static boolean alive = false;
	private static Connection con = null;

	static {
		openConnection();
	}// static

	public static final void openConnection() {
		try {
			Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
			con = DriverManager.getConnection("jdbc:derby:opticardb/optika",
					"opticar", "");
			alive = true;
		} catch (ClassNotFoundException e) {
			e.printStackTrace();

			alive = false;
		} catch (java.sql.SQLException e) {
			e.printStackTrace();
			alive = false;
		}
	}

	public static Connection getConnection() {
		return con;
	}

	public static void closeConnection() {
		try {
			if (con != null)
				con.close();
		} catch (Exception e) {
		}
	}

	public boolean isAlive() {
		return alive;
	}
}// klasa

