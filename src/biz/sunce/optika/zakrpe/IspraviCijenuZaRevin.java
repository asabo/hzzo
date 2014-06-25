/*
 * Project opticari
 *
 */
package biz.sunce.optika.zakrpe;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.csc.SynchModul;
import biz.sunce.optika.Logger;
import biz.sunce.util.beans.PostavkeBean;

public final class IspraviCijenuZaRevin {
	String[] upiti = {
			"update artikli set po_cijeni = 2100 where sifra='210306140102'",
			"update stavke_racuna set po_cijeni=2100 where sif_artikla='210306140102' and sif_racuna in ( select sifra from racuni where datum_izdavanja>'2014-01-01' )" };

	String zakrpa = "IspraviCijene";

	public int zakrpaj() {
	  
		String zakrpanRevin = "ZakrpanRevin";
		String zakrpanoStr = PostavkeBean.getPostavkaDB(zakrpanRevin, "nije");

		SynchModul sm = (SynchModul) DAOFactory.getInstance().getSynchModul();
		int zakrpano = 0;
		// zakrpa je vec obavljena? Ako je vracamo nazad poruku kao da smo
		// zakrpali..
		if (zakrpanoStr.equals("nije")) {

			Connection con = null;
			Statement stmt = null;

			try {
				con = DAOFactory.getConnection();
				stmt = con.createStatement();

				int upLen = upiti.length;
				
				for (int i = 0; i < upLen; i++) {
					try {
						zakrpano += stmt.executeUpdate(upiti[i]);
					} catch (SQLException sqle) {
						if (sm != null)
							sm.log("SQL iznimka kod izvrsavanja zakrpe: "
									+ sqle + " upit[" + i + "] " + upiti[i]);
					} // nema hvatanja, ako ne prodje mozda je vec zakrpano

				}// for i
			} catch (SQLException e) {
				if (sm != null)
					sm.log("SQL iznimka kod izvrsavanja zakrpe: " + e);

				Logger.fatal("SQL iznimka kod zakrpe " + zakrpa, e);
				return zakrpano;
			} catch (Exception e) {
				if (sm != null)
					sm.log("Opæa iznimka kod izvršavanja zakrpe: " + e);

				Logger.fatal("Opæenita iznimka kod zakrpe " + zakrpa, e);
				return zakrpano;
			} finally {
				try {
					if (stmt != null)
						stmt.close();
				} catch (SQLException sqle) {
				}
				try {
					if (con != null)
						con.close();
				} catch (SQLException sqle) {
				}
				con = null;
			}
		}// if nije zakrpano
	return zakrpano;
	}// zakrpaj

}// klasa
