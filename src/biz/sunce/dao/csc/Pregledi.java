/*
 * Project opticari
 *
 */
package biz.sunce.dao.csc;

import java.io.CharArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.PregledDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.PregledVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.Logger;
import biz.sunce.util.Util;


/**
 * datum:2005.05.21
 * @author dstanic
 *
 */
public final class Pregledi implements PregledDAO
{

public static final String[] zaglavlja={"datum"};

	public void insert(Object objekt) throws SQLException {

		PregledVO ulaz=(PregledVO)objekt;

		int sifra=NEPOSTOJECA_SIFRA;

		if (objekt==null)
			throw new SQLException("Insert pregled: ulazna vrijednost je null!");

		String upit =
							  " INSERT INTO PREGLEDI "
							+ "			(sifra,"
							+ "			datvrijeme,"					//1
							+ "			siflijecnika,"				//2
							+ "			napomena,"						//3
							+ "			status,"							//4
							+ "			obavljen,"						//5
							+ "			kontrolazamjeseci,"		//6
							+ "			anamneza,"						//7
							+ "			ordvod,"							//8
							+ "			ordvos,"							//9
							+ "			ordrazzjl,"						//10
							+ "			ordrazzjd,"						//11
							+ "			ordnapomena,"					//12
							+ "			visus_sc_l,"					//13 -23.05.05 -asabo- izmjenio
							+ "			visus_l,"							//14
							+ "			prijeordblizu,"				//15
							+ "			prijeorddaleko,"			//16
							+ "			prijeordlece,"				//17
							+ "			ordblizu,"						//18
							+ "			orddaleko,"						//19
							+ "			ordlece,"							//20
							+ "			created_by,"					//21
							+ "			created,"							//22
							+ "			sifklijenta,"					//23
							+ "			visus_sc_d,"					//24  -23.05.05. -asabo- dodao
							+ "			visus_d,"							//25
							+ "    suncaneNaocale)"				//26  -25.11.05. -asabo- dodano

							+ " VALUES (";
                                                        upit+=
							(sifra=DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu("PREGLEDI","sifra")) //16.01.06. -asabo- izmjenjeno
                                                        + " ,"
							+ "  ?,?,?,?,?,?,?,?,?,?,"
							+ "  ?,?,?,?,?,?,?,?,?,?,"
							+ "  ?,?,?,?,?,?)";  //-23.05.05. -asabo- dodao dva upitnika -25.11.05. -jos jedan

			Connection conn 			= null;
			PreparedStatement ps 	= null;
			conn=DAOFactory.getConnection();

			ps=conn.prepareStatement(upit);

			if (ps==null)
			{Logger.fatal("PS je null kod insertiranja podataka o pregledu!",null); return; }

			//28.05.05. -asabo- vrijeme dogovorenog pregleda pretpostavlja datum i _VRIJEME_...
			ps.setTimestamp(1,new Timestamp(ulaz.getDatVrijeme().getTimeInMillis())); // 31.05.05. -asabo- izmjenio..
			ps.setInt(2,ulaz.getSifLijecnika().intValue());//Ne smije biti null - jedino lijecnik moze obavit pregled
			ps.setString(3,ulaz.getNapomena());
			ps.setString(4,""+ulaz.getStatus());

			if (ulaz.getObavljen()!=null)
			ps.setTimestamp(5,new Timestamp(ulaz.getObavljen().getTime()));
			else
			ps.setNull(5,Types.TIMESTAMP);

			if (ulaz.getKontrolaZaMjeseci()!=null)
				ps.setInt(6,ulaz.getKontrolaZaMjeseci().intValue());
			else
				ps.setNull(6,Types.INTEGER);

			Reader r=null;
			char[] arr=null;

			if (ulaz.getAnamneza()!=null && !ulaz.getAnamneza().equals("")){
				arr=ulaz.getAnamneza().toCharArray();
				r=new CharArrayReader(arr);
			}
			if (r!=null){
				ps.setCharacterStream(7,r,arr.length);
				// nema potrebe zatvarati bins!
			}
			else
				ps.setNull(7,Types.CLOB);

		 ps.setString(8,ulaz.getOrdVOD());
		 ps.setString(9,ulaz.getOrdVOS());
		 ps.setString(10,ulaz.getOrdRazZjL());
		 ps.setString(11,ulaz.getOrdRazZjD());
		 ps.setString(12,ulaz.getOrdNapomena());
		 ps.setString(13,ulaz.getVisusScL());
		 ps.setString(14,ulaz.getVisusL());

		if (ulaz.getPrijeOrdBlizu()!=null && ulaz.getPrijeOrdBlizu().intValue()!=NEPOSTOJECA_SIFRA )
			ps.setInt(15,ulaz.getPrijeOrdBlizu().intValue());
		else
			ps.setNull(15,Types.INTEGER);

		if (ulaz.getPrijeOrdDaleko()!=null && ulaz.getPrijeOrdDaleko().intValue()!=NEPOSTOJECA_SIFRA)
			ps.setInt(16,ulaz.getPrijeOrdDaleko().intValue());
		else
			ps.setNull(16,Types.INTEGER);

		if (ulaz.getPrijeOrdLece()!=null && ulaz.getPrijeOrdLece().intValue()!=NEPOSTOJECA_SIFRA)
			ps.setInt(17,ulaz.getPrijeOrdLece().intValue());
		else
			ps.setNull(17,Types.INTEGER);

		//29.06.05 -asabo- dodao provjeru da sifra nije jednaka -1
		if (ulaz.getOrdBlizu()!=null && ulaz.getOrdBlizu().intValue()!=NEPOSTOJECA_SIFRA)
			ps.setInt(18,ulaz.getOrdBlizu().intValue());
		else
			ps.setNull(18,Types.INTEGER);

		//29.06.05 -asabo- dodao provjeru da sifra nije jednaka -1
		if (ulaz.getOrdDaleko()!=null && ulaz.getOrdDaleko().intValue()!=NEPOSTOJECA_SIFRA)
			ps.setInt(19,ulaz.getOrdDaleko().intValue());
		else
			ps.setNull(19,Types.INTEGER);

		if (ulaz.getOrdLece()!=null && ulaz.getOrdLece().intValue()!=NEPOSTOJECA_SIFRA)
			ps.setInt(20,ulaz.getOrdLece().intValue());
			else
			ps.setNull(20,Types.INTEGER);

		ps.setInt(21,ulaz.getCreatedBy().intValue()); 				// ne smije biti null
		ps.setTimestamp(22,new Timestamp(ulaz.getCreated())); //TODO tudume sjeti se šta smo se dogovorili - current_timestamp
		ps.setInt(23,ulaz.getSifKlijenta().intValue());				// ne smije biti null

		// 23.05.05. -asabo- dodao visuse za desno oko
		ps.setString(24,ulaz.getVisusScD());
		ps.setString(25,ulaz.getVisusD());

		Integer sn=ulaz.getSifSuncanihNaocala();

		if (sn==null)
		ps.setNull(26,Types.INTEGER);
		else
		ps.setInt(26,sn.intValue());

		ResultSet rs2=null;

		try {
			int kom=ps.executeUpdate();

			if (kom==1)
			{
				ulaz.setSifra(Integer.valueOf(sifra));
			}
			else
			Logger.log("Insert novog pregleda nije uspio.. ",null);

		} catch (SQLException e) {
			Logger.fatal("Greska kod inserta pregleda",e);
		}finally{
			try {if (rs2!=null) rs2.close();} catch (SQLException e1){}
			try {if (ps!=null) ps.close();} catch (SQLException e1){}
			if (conn!=null) DAOFactory.freeConnection(conn);
		}
	}//insert

	public boolean update(Object objekt) throws SQLException {

		PregledVO ulaz=(PregledVO)objekt;

		//25.11.05. -asabo- napomena: suncaneNaocale kolone _ne_bi_ trebalo biti u update nikako

		if (objekt==null)
			throw new SQLException("update pregled: ulazna vrijednost je null!");

		String upit =
							  " UPDATE PREGLEDI SET "
							+ "			datvrijeme=?,"					//1
							+ "			siflijecnika=?,"				//2
							+ "			napomena=?,"						//3
							+ "			status=?,"							//4
							+ "			obavljen=?,"						//5
							+ "			kontrolazamjeseci=?,"		//6
							+ "			anamneza=?,"						//7
							+ "			ordvod=?,"							//8
							+ "			ordvos=?,"							//9
							+ "			ordrazzjl=?,"						//10
							+ "			ordrazzjd=?,"						//11
							+ "			ordnapomena=?,"					//12
							+ "			visus_sc_l=?,"						//13 - 23.05.05. -asabo- izmjenio
							+ "			visus_l=?,"								//14
							+ "			prijeordblizu=?,"				//15
							+ "			prijeorddaleko=?,"			//16
							+ "			prijeordlece=?,"				//17
							+ "			ordblizu=?,"						//18
							+ "			orddaleko=?,"						//19
							+ "			ordlece=?,"							//20
							+ "			updated=current_timestamp,"							//21
							+ "			updated_by=?,"					//22
							+ "			sifklijenta=?,"					//23 - 31.05.05. -asabo- falio je zarez..
							+ "			visus_sc_d=?,"					//24 - 23.05.05. -asabo- dodao
							+ "			visus_d=?"							//25 - 31.05.05. -asabo- a ovdje je bio zarez viska..
							+ "	WHERE sifra=?";

			Connection conn 			= null;
			PreparedStatement ps 	= null;
			conn=DAOFactory.getConnection();

			ps=conn.prepareStatement(upit);

			ps.setTimestamp(1,new Timestamp(ulaz.getDatVrijeme().getTimeInMillis())); //31.05.05. -asabo- izmjenio..
			ps.setInt(2,ulaz.getSifLijecnika().intValue());//Ne smije biti null jedino lijecnik moze obavit pregled
			ps.setString(3,ulaz.getNapomena());
			ps.setString(4,""+ulaz.getStatus());

			if (ulaz.getObavljen()!=null)
			ps.setTimestamp(5,ulaz.getObavljen());
			else
			ps.setNull(5,Types.TIMESTAMP);

			if (ulaz.getKontrolaZaMjeseci()!=null)
				ps.setInt(6,ulaz.getKontrolaZaMjeseci().intValue());
			else
				ps.setNull(6,Types.INTEGER);

			Reader r=null;
			char[] arr=null;

			if (ulaz.getAnamneza()!=null){
				arr=ulaz.getAnamneza().toCharArray();
				r=new CharArrayReader(arr);
			}
			if (r!=null){
				ps.setCharacterStream(7,r,arr.length);
				// nema potrebe zatvarati bins!
			}
			else
				ps.setNull(7,Types.CLOB);

		 ps.setString(8,ulaz.getOrdVOD());
		 ps.setString(9,ulaz.getOrdVOS());
		 ps.setString(10,ulaz.getOrdRazZjL());
		 ps.setString(11,ulaz.getOrdRazZjD());
		 ps.setString(12,ulaz.getOrdNapomena());
		 ps.setString(13,ulaz.getVisusScL()); // 23.05.05. -asabo- izmjenio
		 ps.setString(14,ulaz.getVisusL());

		if (ulaz.getPrijeOrdBlizu()!=null && ulaz.getPrijeOrdBlizu().intValue()!=NEPOSTOJECA_SIFRA)
			ps.setInt(15,ulaz.getPrijeOrdBlizu().intValue());
		else
			ps.setNull(15,Types.INTEGER);

		if (ulaz.getPrijeOrdDaleko()!=null && ulaz.getPrijeOrdDaleko().intValue()!=NEPOSTOJECA_SIFRA)
			ps.setInt(16,ulaz.getPrijeOrdDaleko().intValue());
		else
			ps.setNull(16,Types.INTEGER);

		if (ulaz.getPrijeOrdLece()!=null && ulaz.getPrijeOrdLece().intValue()!=NEPOSTOJECA_SIFRA)
			ps.setInt(17,ulaz.getPrijeOrdLece().intValue());
		else
			ps.setNull(17,Types.INTEGER);

		if (ulaz.getOrdBlizu()!=null && ulaz.getOrdBlizu().intValue()!=NEPOSTOJECA_SIFRA)
			ps.setInt(18,ulaz.getOrdBlizu().intValue());
		else
			ps.setNull(18,Types.INTEGER);

		if (ulaz.getOrdDaleko()!=null && ulaz.getOrdDaleko().intValue()!=NEPOSTOJECA_SIFRA)
			ps.setInt(19,ulaz.getOrdDaleko().intValue());
		else
			ps.setNull(19,Types.INTEGER);

		if (ulaz.getOrdLece()!=null && ulaz.getOrdLece().intValue()!=NEPOSTOJECA_SIFRA)
			ps.setInt(20,ulaz.getOrdLece().intValue());
			else
			ps.setNull(20,Types.INTEGER);

			ps.setInt(21,ulaz.getLastUpdatedBy().intValue()); 				// ne smije biti null
			ps.setInt(22,ulaz.getSifKlijenta().intValue());						// ne smije biti null

			ps.setString(23,ulaz.getVisusScD()); // 23.05.05. -asabo- dodao
			ps.setString(24,ulaz.getVisusD());

			ps.setInt(25,ulaz.getSifra().intValue());//Where

		  try
		  {

			 int kom=ps.executeUpdate();

			// 24.05.05.-asabo- ako je insert/update prosao u redu, ovdje cemo obaviti
			// insert/update preostalih

			// ako se pregled uspjesno update-a treba pozvati update i njegovih 'satelita' - keratometrije, skiaskopije..
			if (kom==1)
			{
			if (ulaz.getKeratometrija()!=null) // 16.07.05. -asabo- dodao provjeru jeli ima podataka o objektu
			 if (ulaz.getKeratometrija().getSifPregleda().intValue()==NEPOSTOJECA_SIFRA)
			 {
			 ulaz.getKeratometrija().setSifPregleda(ulaz.getSifra());
			 DAOFactory.getInstance().getKeratometrija().insert(ulaz.getKeratometrija());
			 }
			 else
			 DAOFactory.getInstance().getKeratometrija().update(ulaz.getKeratometrija());

			if (ulaz.getSkiaskopija()!=null) // 16.07.05. -asabo- dodao provjeru jeli ima podataka o objektu
			if (ulaz.getSkiaskopija().getSifPregleda().intValue()==NEPOSTOJECA_SIFRA)
			{
				ulaz.getSkiaskopija().setSifPregleda(ulaz.getSifra());
				DAOFactory.getInstance().getSkiaskopija().insert(ulaz.getSkiaskopija());
			}
			else
			DAOFactory.getInstance().getSkiaskopija().update(ulaz.getSkiaskopija());

			//14.06.05. -asabo- dodan refraktometar
			if (ulaz.getRefraktometar()!=null) // 16.07.05. -asabo- dodao provjeru jeli ima podataka o objektu
			if (ulaz.getRefraktometar().getSifPregleda().intValue()==NEPOSTOJECA_SIFRA)
					{
						ulaz.getRefraktometar().setSifPregleda(ulaz.getSifra());
					 DAOFactory.getInstance().getRefraktometar().insert(ulaz.getRefraktometar());
					}
					else
					DAOFactory.getInstance().getRefraktometar().update(ulaz.getRefraktometar());
			}//if kom==1

		  }
			//28.06.05. -asabo- ubijen catch, exception se treba hvatati izvan update metode (pozivatelj)
			finally
			{
			try {if (ps!=null) ps.close();} catch (SQLException e1){}
			DAOFactory.freeConnection(conn);
			}
		return true;
	}//update


	public ValueObject read(Object kljuc) throws SQLException {

		PregledVO pregled	=	null;
		Integer ulazSifra = null;
		String 	ulazIme 	= null;
		Calendar dat= null;

		if (kljuc instanceof Integer){
			ulazSifra = (Integer)kljuc;
		}
		else
		//13.07.05. -asabo- dodano kao kriterij za pretrazivanje konkretnog pregleda u neko vrijeme
		if (kljuc instanceof Calendar)
		{
			dat=(Calendar)kljuc;
		}

		String upit=
									"SELECT "
								+ "		sifra,"
								+ "		datvrijeme,"
								+ "		siflijecnika,"
								+ "		napomena,"
								+ "		status,"
								+ "		obavljen,"
								+ "		kontrolazamjeseci,"
								+ "		anamneza,"
								+ "		ordvod,"
								+ "		ordvos,"
								+ "		ordrazzjl,"
								+ "		ordrazzjd,"
								+ "		ordnapomena,"
								+ "		visus_sc_l," //24.05.05. -asabo- izmjenio
								+ "		visus_l,"
								+ "		prijeordblizu,"
								+ "		prijeorddaleko,"
								+ "		prijeordlece,"
								+ "		ordblizu,"
								+ "		orddaleko,"
								+ "		ordlece,"
								+ "		created_by,"
								+ "		updated_by,"
								+ "		created,"
								+ "		updated,"
								+ "		sifklijenta,"
								+ "		visus_sc_d," // 24.05.05. -asabo- dodao
								+ "		visus_d,"
								+ "   suncaneNaocale" //25.11.05. -asabo- dodano
								+ " FROM "
								+ "			pregledi"
								+	" WHERE ";

		if (ulazSifra!=null){	upit += " sifra = ?";}
		else
		if (dat!=null){	upit += " datVrijeme = ?";}


		Connection conn 			= null;
		PreparedStatement ps 	= null;
		ResultSet rs					=	null;
		conn=DAOFactory.getConnection();
		ps=conn.prepareStatement(upit);

		if (ulazSifra!=null){
				ps.setInt(1,ulazSifra.intValue());
		}
		else
		if (dat!=null)
		ps.setTimestamp(1,new Timestamp(dat.getTimeInMillis()));

		try {
			rs = ps.executeQuery();

			if (rs.next()){

				pregled =(constructPregled(rs));

			}
		}
		//28.06.05. -asabo- ubijen catch (pozivatelj treba hvatati exception)
		finally{
			try {if (rs!=null) rs.close();} catch (SQLException sql){} rs=null;
			try {if (ps!=null) ps.close();} catch (SQLException sql1){} ps=null;
			DAOFactory.freeConnection(conn);
		}
		return pregled;
	}

  // ako je ulaz integer, onda se vade van svi pregledi klijenta zadane sifre
  // ako je ulaz Calendar, vade se van svi pregledi tog datuma ili mladji
  // dodje li SearchCriteria sa datumom kao filterom, vade se van svi pregledi samo tog datuma
	public List findAll(Object kljuc) throws SQLException {

		Integer sifKlijenta = null;
		Calendar datum=null;
		SearchCriteria kriterij=null;

		if (kljuc instanceof Integer)
			sifKlijenta=(Integer)kljuc;
			else
			if (kljuc instanceof Calendar)
				datum=(Calendar)kljuc;
				else
				if(kljuc instanceof SearchCriteria)
				{
					kriterij=(SearchCriteria)kljuc;
				}


		String upit=
									"SELECT "
								+ "		sifra,"
								+ "		datvrijeme,"
								+ "		siflijecnika,"
								+ "		napomena,"
								+ "		status,"
								+ "		obavljen,"
								+ "		kontrolazamjeseci,"
								+ "		anamneza,"
								+ "		ordvod,"
								+ "		ordvos,"
								+ "		ordrazzjl,"
								+ "		ordrazzjd,"
								+ "		ordnapomena,"
								+ "		visus_sc_l," //24.05.05. -asabo- izmjenio
								+ "		visus_l,"
								+ "		prijeordblizu,"
								+ "		prijeorddaleko,"
								+ "		prijeordlece,"
								+ "		ordblizu,"
								+ "		orddaleko,"
								+ "		ordlece,"
								+ "		created_by,"
								+ "		updated_by,"
								+ "		created,"
								+ "		updated,"
						  	+ "		sifklijenta,"
								+ "		visus_sc_d," // 24.05.05. -asabo- dodao
								+ "		visus_d,"
								+ "   suncaneNaocale"
						  	+ " FROM "
						   	+ "			pregledi"
								+	" WHERE status<>"+STATUS_DELETED+" and (suncaneNaocale is null)"; //25.11.05. -asabo- suncane naocale su vezane na pregled, ali to nije 'pregled'

				if (sifKlijenta!=null) upit += " AND sifKlijenta = ?";
				//12.07.05. -asabo- dodan datum kao kriterij pretrazivanja
				if (datum!=null) upit+=" and datVrijeme>='"+Util.convertCalendarToStringForSQLQuery(datum)+"'";

				if (kriterij!=null)
				{ Calendar cl=(Calendar)kriterij.getPodaci().get(0);
					upit+=" and date(datVrijeme)='"+Util.convertCalendarToStringForSQLQuery(cl)+"'"; }

				//26.07.05. -asabo- treba dodati order by kako bi se to sve posortiralo po datumu
				if (kriterij==null)
				upit+=" order by datVrijeme desc"; //06.10.05. -asabo- dodao desc
				else
				upit+=" order by datVrijeme Asc";

				Connection conn 			= null;
				PreparedStatement ps 	= null;
				ResultSet rs					=	null;
				conn=DAOFactory.getConnection();
				ps=conn.prepareStatement(upit);

				if (sifKlijenta!=null){
					ps.setInt(1,sifKlijenta.intValue());
				}

				List lista= new ArrayList();

				try{
					rs = ps.executeQuery();
					while (rs.next()){
							lista.add(constructPregled(rs));
					}
				} catch (SQLException e) {
			  Logger.fatal("SQL iznimka kod Pregledi.findAll",e);
				}
				finally{
					try{if (rs!=null) rs.getStatement().close();}catch(SQLException e){}
					try{if (rs!=null) rs.close();}catch(SQLException sqle){}
				}
				return lista;
			}//findAll

	public void delete(Object kljuc) throws SQLException
	{
		PregledVO pregledVO = null;

		if (kljuc==null)
			throw new SQLException("delete pregled: ulazna vrijednost je null!");

		if(kljuc instanceof Integer ){
			pregledVO = (PregledVO)this.read(kljuc);//
		}
		if(kljuc instanceof PregledVO ){
			pregledVO= (PregledVO)kljuc;
		}
		else
			throw new SQLException("Nemoze");

		// 20.07.05. -asabo-f hackon. Ako korisnik zeli fizicki izbrisati pregled iz baze podataka
		// prije poziva delete() metode treba postaviti setSifLijecnika(null); i tada ce pregled otfikariti
		// za stvarno iz baze podataka.Nekada je to potrebno radi foreign i primary key konzistencije
		if (pregledVO.getSifLijecnika()!=null)
		{
		pregledVO.setStatus(STATUS_DELETED.charAt(1));
		this.update(pregledVO);
		}
		else
		{
			// fizicko brisanje podataka o pregledu iz baze, nekada je potrebno...
			String upit;

			//prvo FK-ove treba rijesiti...
			upit="delete from skiaskopija where sifPregleda="+pregledVO.getSifra().intValue();
			DAOFactory.performUpdate(upit);

			upit="delete from refraktometar where sifPregleda="+pregledVO.getSifra().intValue();
			DAOFactory.performUpdate(upit);

			upit="delete from keratometrija where sifPregleda="+pregledVO.getSifra().intValue();
			DAOFactory.performUpdate(upit);

			upit =" delete from PREGLEDI where sifra="+pregledVO.getSifra().intValue();
			DAOFactory.performUpdate(upit);
		}// else

	}//delete


//	----------------------------Metode vezane uz prikaz----------------------------
	public Class getColumnClass(int columnIndex) {

		switch(columnIndex)
		{
			case 0: default:
			try {
				return Class.forName("java.lang.String");
			} catch (ClassNotFoundException e)
			{
			 return null;
			}
		}
	}
	public int getColumnCount() {
		return zaglavlja.length;
	}
	public String getColumnName(int rb) {
		return zaglavlja[rb];
	}
	public int getRowCount() {
		return 0;
	}
	public Object getValueAt(ValueObject vo, int kolonas) {
		if (vo==null) return null;
			PregledVO pregled=(PregledVO)vo;

			switch(kolonas){

				case 0:		return Util.convertCalendarToString(pregled.getDatVrijeme(),true); // prikazi i vrijeme
				default:	return null;
			}
	}
	public Class getVOClass() throws ClassNotFoundException {
		return Class.forName("biz.sunce.opticar.vo.PregledVO");
	}
	public boolean isCellEditable(ValueObject vo, int kolona) {
		return false;
	}
	public boolean setValueAt(ValueObject vo, Object vrijednost, int kolona) {
		return false;
	}

	private PregledVO constructPregled(ResultSet rs) throws SQLException
	{

		PregledVO p = new PregledVO();
		//------------------------------------------Stringovi
		p.setNapomena(rs.getString("NAPOMENA"));
		p.setOrdVOD(rs.getString("ORDVOD"));
		p.setOrdVOS(rs.getString("ORDVOS"));
		p.setOrdRazZjL(rs.getString("ORDRAZZJL"));
		p.setOrdRazZjD(rs.getString("ORDRAZZJD"));
		p.setOrdNapomena(rs.getString("ORDNAPOMENA"));
		p.setVisusScL(rs.getString("VISUS_SC_L"));
		p.setVisusL(rs.getString("VISUS_L"));

		// 31.05.05. -asabo- dodao
		Calendar c=Calendar.getInstance();
		Timestamp t=rs.getTimestamp("datVrijeme"); //TODO ne bi se nikad smjelo dogoditi da je null... sta cemo sa time?
		c.setTimeInMillis(t!=null?t.getTime():0L);


		p.setDatVrijeme(c);

		p.setVisusScD(rs.getString("VISUS_SC_D")); //23.05.05 -asabo- dodao
		p.setVisusD(rs.getString("VISUS_D"));

		//------------------------------------------Timestamp-ovi
		p.setObavljen(rs.getTimestamp("OBAVLJEN"));
		p.setCreated(rs.getTimestamp("CREATED").getTime());				// ne smije biti null ako redak postoji u bazi moa imati vrijeme kreiranja
		// 23.05.05. -asabo- CREATED pretvoren u UPDATED. Paziti prilikom copy-paste!
		p.setLastUpdated(rs.getTimestamp("UPDATED")!=null ? rs.getTimestamp("UPDATED").getTime():0L);
		//------------------------------------------Integer not null
		p.setKontrolaZaMjeseci(new Integer (rs.getInt("KONTROLAZAMJESECI")));
		p.setSifra(Integer.valueOf(rs.getInt("SIFRA")));
		p.setCreatedBy(Integer.valueOf(rs.getInt("CREATED_BY")));
		p.setSifKlijenta(Integer.valueOf(rs.getInt("SIFKLIJENTA")));
		//------------------------------------------Integer not null

		p.setSifLijecnika(Integer.valueOf(rs.getInt("SIFLIJECNIKA")));
		if (rs.wasNull())
			p.setSifLijecnika(null);
		p.setPrijeOrdBlizu(Integer.valueOf(rs.getInt("PRIJEORDBLIZU")));
		if (rs.wasNull())
			p.setPrijeOrdBlizu(null);
		p.setPrijeOrdDaleko(Integer.valueOf(rs.getInt("PRIJEORDDALEKO")));
		if (rs.wasNull())
			p.setPrijeOrdDaleko(null);
		p.setPrijeOrdLece(Integer.valueOf(rs.getInt("PRIJEORDLECE")));
		if (rs.wasNull())
			p.setPrijeOrdLece(null);
		p.setOrdBlizu(Integer.valueOf(rs.getInt("ORDBLIZU")));
		if (rs.wasNull())
			p.setOrdBlizu(null);
		p.setOrdDaleko(Integer.valueOf(rs.getInt("ORDDALEKO")));
		if (rs.wasNull())
			p.setOrdDaleko(null);
		p.setOrdLece(Integer.valueOf(rs.getInt("ORDLECE")));
		if (rs.wasNull())
			p.setOrdLece(null);
		p.setLastUpdatedBy(Integer.valueOf(rs.getInt("UPDATED_BY")));
		if (rs.wasNull())
			p.setLastUpdatedBy(null);

		//25.11.05. -asabo- dodana sifra suncanih naocala
		p.setSifSuncanihNaocala(Integer.valueOf(rs.getInt("suncaneNaocale")));
		if (rs.wasNull()) p.setSifSuncanihNaocala(null);

		Reader r=null;
		String anamneza=null;
		Clob cl=rs.getClob("ANAMNEZA");

		r=cl!=null?cl.getCharacterStream():null;

			if (r!=null)
		   {
		   anamneza="";
			 int rez=0,off=0,l=512;
			 char[] buf=null;


		 	 try
		 	 {
			  buf=new char[l];
			  while((rez=r.read(buf,0,l))!=-1)
			  {
			  anamneza+=new String(buf,0,rez);
				off+=rez;
		  	}//while

			 }
			 catch(IOException ioe)
			 {
				Logger.fatal("IO iznimka kod Pregledi.constructPregled",ioe);
			 }
			 finally{buf=null;}
			}//if
			p.setAnamneza(anamneza); // TODO Ako ovo proradi ni hudini ti nije ravan

		p.setStatus(rs.getString("STATUS").charAt(0));
		return p;
	}

	public GUIEditor getGUIEditor() {
		return null;
	}//construct
}//klasa
























