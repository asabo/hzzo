/*
 * Project opticari
 *
 */
package biz.sunce.dao.csc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.KlijentDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.dao.TransakcijeDAO;
import biz.sunce.opticar.vo.TransakcijaVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;

/**
 * datum:2005.10.21
 * @author asabo
 *
 */
public final class Transakcije implements TransakcijeDAO
{
	private static KlijentDAO klijenti;
	private static String[] zaglavlja={"tip","odl./dol.","kreirana","odobrena","stranka"};
	private String tablica="transakcije";
	private final String kljuc="id";

	private static final KlijentDAO getKlijenti()
	{
		if (klijenti==null)
		{
			klijenti=DAOFactory.getInstance().getKlijenti();
		}
		return klijenti;
	}//getKlijenti


		public void insert(TransakcijaVO objekt) throws SQLException
		{
			if (objekt==null)
					throw new SQLException("Insert "+tablica+": ulazna vrijednost je null!");

			TransakcijaVO ulaz=(TransakcijaVO)objekt;

			String upit =
					" INSERT INTO  "+tablica
						+"	(id,"				  // ... nema parametar
						+"	tip,"					//1
						+"	datVrijeme,"
						+"	sifBoje,"			//2
						+"	artikl," 			//3
						+"  odlazna,"			//4
						+"  sadrzaj,"			//5
						+"  napomena,"		//6
						+"  sifKlijenta,"	//7
						+"  vrsta,"   		//8
						+"  created,"     //
						+"  created_by,"   //
						+"  status"
						//+" ,created_by)"
						+") VALUES ("+
						DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu(tablica,"id")+
						",?,null,?,?,?,?,?,?,?,current_timestamp,"+GlavniFrame.getSifDjelatnika()+","+DAO.STATUS_UPDATED+")";

			Connection conn 			= null;
			PreparedStatement ps 	= null;
			conn=DAOFactory.getConnection();

			ps=conn.prepareStatement(upit);

			int sifBoje=ulaz.getSifBoje();
			String artikl=ulaz.getArtikl();

			ps.setShort(1,ulaz.getTip());
			if (artikl==null)
			{
			ps.setInt(2,ulaz.getSifBoje());
			ps.setNull(3,Types.VARCHAR);
			}
			else
			{
				ps.setNull(2,Types.INTEGER);
				ps.setString(3,artikl);
			}

			ps.setString(4,ulaz.isOdlazna()?DAO.DA:DAO.NE);

			byte[] sadrzaj=ulaz.getSadrzaj();
			if (sadrzaj!=null ) ps.setBytes(5,sadrzaj);
			else ps.setNull(5,Types.BLOB);

			String napomena=ulaz.getNapomena();
		  if (napomena==null)
		   ps.setNull(6,Types.VARCHAR);
		   else ps.setString(6,napomena);

		  ps.setInt(7,ulaz.getSifKlijenta()); // ne moze biti null
		  ps.setInt(8,ulaz.getVrsta()); //31.01.06. -asabo- dodano

			try {
				ps.executeUpdate();
			} catch (SQLException e) {
				Logger.fatal("Greska kod inserta "+tablica+" upit: "+upit,e);
				throw e; // bacaj dalje...
			}finally{
				try {if (ps!=null) ps.close();} catch (SQLException e1){}
				DAOFactory.freeConnection(conn);
			}
		}

		public boolean update(TransakcijaVO objekt) throws SQLException
		{
			TransakcijaVO ulaz=(TransakcijaVO)objekt;

			if (ulaz==null)
				throw new SQLException("Update "+tablica+" ulazna vrijednost je null!");


			String upit =
						" UPDATE "+tablica+" SET "
						+"		    tip=?,"				//1
						+"		sifBoje=?," 			//2
						+"		 artikl=?,"				//3
						+"		odlazna=?,"				//4
						+"		sadrzaj=?,"				//5
						+"   napomena=?,"				//6
						+"      vrsta=?"        //7
					  +"     status="+DAO.STATUS_UPDATED
						+"   WHERE id=?";				//8

			Connection conn 			= null;
			PreparedStatement ps 	= null;

			try {
			conn=DAOFactory.getConnection();
			ps=conn.prepareStatement(upit);

			String art=ulaz.getArtikl();

			ps.setShort(1,ulaz.getTip()); // ne moze biti null..

			if (art==null)
			{
				ps.setInt(2,ulaz.getSifBoje());
				ps.setNull(3,Types.VARCHAR);
			}
			else
			{
				ps.setNull(2,Types.INTEGER);
				ps.setString(3,art);
			}

			ps.setString(4,ulaz.isOdlazna()?DAO.DA:DAO.NE);

			byte[] sadrzaj=ulaz.getSadrzaj();
			if (sadrzaj!=null)
			ps.setBytes(5,sadrzaj);
			else ps.setNull(5,Types.BLOB);

			 String napomena=ulaz.getNapomena();
			 if (napomena!=null)
			 ps.setString(6,napomena);
			 else ps.setNull(6,Types.VARCHAR);

			 ps.setShort(7,ulaz.getVrsta());

			 ps.setInt(8,ulaz.getSifra().intValue());

			 ps.executeUpdate();

			} catch (SQLException e) {
				Logger.fatal("Greska kod update "+tablica,e);
				return false;
			}finally{
				try {if (ps!=null) ps.close();} catch (SQLException e1){}
				if (conn!=null) DAOFactory.freeConnection(conn);
			}
			return true;
		}//update

		public TransakcijaVO read(Object kljuc) throws SQLException {
			TransakcijaVO transakcija	=	null;
			Integer ulazSifra 	= null;
			String 	ulazIme 		= null;

			if (kljuc instanceof Integer){
				ulazSifra = (Integer)kljuc;
			}
			else
			if (kljuc instanceof String){
				ulazIme = (String)kljuc;
			}
			else
			{
				Logger.log(tablica+".read pozvani sa ulaznim parametrom klase: "+kljuc!=null?kljuc.getClass().toString():"(null obj)",null);
				return null;
			}

			String upit=
				 "  SELECT "
				+"	id,"				  //1
				+"	tip,"					//2   zahtjev za karticom, rez. dijelom, intervencijom...
				+"	datVrijeme," 	//3
				+"	sifBoje,"			//4
				+"	artikl," 			//5
				+"  odlazna,"			//6
				+"  sadrzaj,"			//7
				+"  napomena,"		//8
				+"  sifKlijenta,"	//9
				+"  status,"			//10
				+"  odobrena,"    //30.01.06. -asabo- dodano samo u selectima (ne dira ju ovaj sustav)
				+"  vrsta"        //31.01.06. -asabo- dodano (vrsta rezervnog dijela, kartice, zahtjeva za intervencijom...)
				+ " created,"
				+ " created_by,"
				+ " updated,"
				+ " updated_by"
				+"  FROM "
				+ tablica
				+ " WHERE status<>"+DAO.STATUS_DELETED+" ";

			if (ulazSifra!=null){	upit += "and id = "+ulazSifra.intValue();}
			else
			if (ulazIme!=null)	{ upit += "and napomena like '%"+ulazIme+"%'";} // iako nema smisla, ostavili smo prazan prostor jer je napomena jedini string parametar


			ResultSet rs					=	null;

			try {
				rs = DAOFactory.performQuery(upit);
				if (rs.next()){
					transakcija =(constructTransakcija(rs));
				}
			} catch (SQLException e) {
				Logger.fatal("SQL iznimka kod "+tablica+".read, upit:"+upit,e);
			}	finally{
				try {if (rs!=null) rs.close();} catch (SQLException sql){} rs=null;
			}
			
			return transakcija;
		}//read

		public List<TransakcijaVO> findAll(Object kljuc) throws SQLException
		{
			Integer sifKupca=null;
			SearchCriteria kriterij=null;

			if (kljuc!=null && kljuc instanceof Integer)
			sifKupca=(Integer)kljuc;
			else
			if (kljuc!=null && kljuc instanceof SearchCriteria)
			kriterij=(SearchCriteria)kljuc;

				String upit=
				 "  SELECT "
				+"	id,"				  //1
				+"	tip,"					//2
				+"	datVrijeme," 	//3
				+"	sifBoje,"			//4
				+"	artikl," 			//5
				+"  odlazna,"			//6
				+"  sadrzaj,"			//7
				+"  napomena,"		//8
				+"  sifKlijenta,"	//9
				+"  status,"			//10
				+"  odobrena,"    // 30.01.06. -asabo- dodano samo u selectima
				+"  vrsta,"
				+ " created,"
				+ " created_by,"
				+ " updated,"
				+ " updated_by"
				+"  FROM "
				+ tablica
				+ " WHERE status<>"+DAO.STATUS_DELETED+" ";

				if (sifKupca!=null)
				upit+="and sifKlijenta="+sifKupca.intValue();
				else
				if (kriterij!=null && kriterij.getKriterij().equalsIgnoreCase("klijenti_zahtjevi"))
				{
					sifKupca=(Integer)kriterij.getPodaci().get(0);
					Integer tipTransakcije=(Integer)kriterij.getPodaci().get(1);

					upit+="and sifKlijenta="+sifKupca.intValue()+" and tip="+tipTransakcije.intValue();
				}//if kriterij == klijenti_zahtjevi


				upit+=" order by created desc";

			ResultSet rs = null;
			List<TransakcijaVO> lista= new ArrayList<TransakcijaVO>();
				try
				{
				rs=DAOFactory.performQuery(upit);
				if (rs!=null)
					while (rs.next()){
						lista.add(constructTransakcija(rs));
				}
			} 
			catch (SQLException e) {
			Logger.fatal("SQL iznimka kod "+tablica+".findAll",e);
			}
			finally{
				try{if (rs!=null) rs.getStatement().close();}catch(SQLException e){}
				try{if (rs!=null) rs.close();}catch(SQLException sqle){} rs=null;
			}
			return lista;
		}//findAll



		public void delete(Object kljuc) throws SQLException {
			TransakcijaVO transakcijaVO = null;

			if(kljuc instanceof Integer ){
				transakcijaVO = (TransakcijaVO)this.read(kljuc);//
			}
			if(kljuc instanceof TransakcijaVO ){
				transakcijaVO= (TransakcijaVO)kljuc;
			}
			else
				throw new SQLException("Æaæu zajebaje "+tablica+ " poslao kljuc "+
				kljuc!=null?kljuc.getClass().toString():"null");

			transakcijaVO.setStatus(STATUS_DELETED.charAt(1));
			this.update(transakcijaVO);
		}//delete


//		----------------------------Metode vezane uz prikaz----------------------------
		public Class getColumnClass(int columnIndex) {
			 
			 switch(columnIndex){
				case 0:
				case 1:	case 2:	case 3: case 4:	return STRING_CLASS; //sva tri case-a vracaju String
				default:	return null;
			 }//switch
			 
		 }//getColumnClass


		public int getColumnCount() {
			return zaglavlja.length;
		}

		public String getColumnName(int rb) {
			return zaglavlja[rb];
		}

		public int getRowCount() {
			int komada=0;
			try {
				komada=this.findAll(null).size();
			}	catch (SQLException e){
				komada=0;
			}
			return komada;
		}

		public Object getValueAt(TransakcijaVO vo, int kolonas) {
			if (vo==null) return null;
			//"id","tip","odl./dol.","kreirana","odobrena"};

				TransakcijaVO transakcija=(TransakcijaVO)vo;
				
				switch(kolonas)
				{

					case 0: return transakcija.getTipZahtjeva();
					case 1:		return transakcija.isOdlazna()?"Odlazna":"Dolazna";
					case 2:		Calendar c=Calendar.getInstance();
										c.setTimeInMillis(transakcija.getCreated());
					return biz.sunce.util.Util.convertCalendarToString(c,true);
					case 3:
					if (transakcija.getOdobrena()==0L) return "";
					Calendar c2=Calendar.getInstance();
					c2.setTimeInMillis(transakcija.getOdobrena());
					return biz.sunce.util.Util.convertCalendarToString(c2,true);
					case 4:
					if (transakcija.getSifKlijenta()>0)
					try {
						return getKlijenti().read(Integer.valueOf(transakcija.getSifKlijenta())).toString();
					} catch (SQLException e) {
						return "SQLEX";
					}
					else return "";
					default:	return "?";
				}
		}//getValueAt

		public Class<TransakcijaVO> getVOClass() throws ClassNotFoundException {
			return TransakcijaVO.class;
		}

		public boolean isCellEditable(TransakcijaVO vo, int kolona) {
			return false;
		}
		public boolean setValueAt(TransakcijaVO vo, Object vrijednost, int kolona) {
			return false;
		}


		private TransakcijaVO constructTransakcija(ResultSet rs) throws SQLException
		{
			TransakcijaVO tvo = new TransakcijaVO();

			tvo.setID((rs.getInt("id")));// ne smije biti null !
			//l.setDatVrijeme(rs.getTimestamp("datVrijeme").getTime());
			tvo.setSifBoje(rs.getInt("sifBoje"));
			String art=rs.getString("artikl");
			if (art==null || art.trim().equals(""))
			tvo.setArtikl(null);
			else {tvo.setArtikl(art); tvo.setSifBoje(DAO.NEPOSTOJECA_SIFRA);}

			tvo.setTip(rs.getShort("tip"));
			tvo.setSifKlijenta(rs.getInt("sifKlijenta"));

			tvo.setOdlazna(rs.getString("odlazna").equals(DAO.DA)?true:false);
			tvo.setSadrzaj(rs.getBytes("sadrzaj"));
			tvo.setNapomena(rs.getString("napomena"));
			tvo.setSifKlijenta(rs.getInt("sifKlijenta"));
			tvo.setStatus(rs.getString("STATUS").charAt(0));
			Timestamp t=rs.getTimestamp("odobrena");
                        tvo.setOdobrena(t!=null?t.getTime():0L);
                        tvo.setVrsta(rs.getShort("vrsta"));  //31.01.06. -asabo- dodano
                        tvo.setCreated(rs.getTimestamp("CREATED").getTime());
                        tvo.setCreatedBy(Integer.valueOf(rs.getInt("CREATED_BY")));
			tvo.setLastUpdated(rs.getTimestamp("UPDATED")!=null ? rs.getTimestamp("UPDATED").getTime():0L);
			tvo.setLastUpdatedBy(Integer.valueOf(rs.getInt("UPDATED_BY")));
                        if (rs.wasNull()) tvo.setLastUpdatedBy(null);

			return tvo;
		}//constructTransakcija

		public GUIEditor getGUIEditor() {
			return null;
		}//constructTransakcija

}//klasa
