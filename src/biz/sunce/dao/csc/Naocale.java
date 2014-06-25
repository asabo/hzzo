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
import biz.sunce.dao.NaocaleDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.NaocaleVO;
import biz.sunce.opticar.vo.NaocalnaLecaVO;
import biz.sunce.opticar.vo.ProizvodjacVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.optika.OsobineNaocalaPanel;
import biz.sunce.util.Util;

/**
 * datum:2005.06.21
 * @author asabo
 *
 */
public class Naocale implements NaocaleDAO
{
	// da se kasnije upit moze lakse preraditi za neku slicnu tablicu
	private final static String tablica="IZDANE_NAOCALE";

	public void insert(Object objekt) throws SQLException
  {
		NaocaleVO ul=(NaocaleVO)objekt;

			if (ul==null)
				throw new SQLException("Insert naocale, ulazna vrijednost je null!");

                         int sifra=DAO.NEPOSTOJECA_SIFRA;

			String upit =
								" INSERT INTO "+tablica
							+ "		(id,"
							+	"		 datum,"				//1
							+	"    model," 				//2 //6
							+ "    sifBoje,"			//3 //7
							+ "    created,"		  //
							+ "    updated,"			//
							+ "    created_by,"	  //4
							+ "    updated_by,"   //null, nema '?'
							+ "    za_sunce," // 04.10.05. 5 -asabo- dodano jeli cvika moze i za sunce
							+ "    napomena," // 04.10.05. 6 -asabo- dodano napomena na razini cvike
							+ "    broj_kartice," // 04.10.05. 7 -asabo- ako je kupljena na loaylty karticu
							+ "    SIF_PROIZV_STAKALA," // 09.10.05. 8 -asabo- sifra proizvodjaca stakala, moze biti null
							+ "    fi1,"   // 13.03.06. -asabo- dodano
							+ "    fi2,"
							+ "    add_stakla,"
							+ "    kvaliteta_leca,"
							+ "    sloj"
							+ " ) VALUES ("+
							(sifra=DAOFactory.vratiSlijedecuSlobodnuSifruZaTablicu(tablica,"id"));

							upit+=	",?,?,?,current_timestamp,null,?,null,?,?,?,?, ?,?,?,?,?)";	//8+5	 komada

			Connection conn 			= null;
			PreparedStatement ps 	= null;
			ResultSet 		 rstemp = null;


   		 try
   		 {
				conn=DAOFactory.getConnection();

				ps=conn.prepareStatement(upit);
				NaocalnaLecaVO ulaz=ul.getLijeva();

				if (ul.getDatum()!=null)
				ps.setTimestamp(1,new Timestamp(ul.getDatum().getTimeInMillis()));
				else
				ps.setNull(1,Types.TIMESTAMP);

				if (ul.getSifBoje()!=null && ul.getSifBoje().intValue()!=DAO.NEPOSTOJECA_SIFRA)
				{
				 ps.setNull(2,Types.VARCHAR);
				 ps.setInt(3,ul.getSifBoje().intValue());
				}
				else
				{
				 ps.setString(2,ul.getModel());
				 ps.setNull(3,Types.INTEGER);
				}

				ps.setInt(4,ul.getCreatedBy().intValue());


				ps.setString(5, ul.isZaSunce()?DAO.DA:DAO.NE); //jeli cvika i za sunce ili nije..

				String nap=ul.getNapomena();

				if (nap==null || nap.trim().equals(""))
				ps.setNull(6,Types.VARCHAR);
				else
				ps.setString(6,ul.getNapomena().trim());

				nap=null; // nece spasit svijet, ali eto navika...

				long serBr=ul.getBrojKartice();
				if (serBr==0L || serBr==DAO.NEPOSTOJECA_SIFRA)
				ps.setNull(7,Types.BIGINT);
				else
				ps.setLong(7,serBr);

				if (ul.getSifProizvodjacaStakla()!=null)
				ps.setInt(8,ul.getSifProizvodjacaStakla().intValue());
				else
				ps.setNull(8,Types.INTEGER);


				//13.03.06. -asabo- dodano
				ps.setString( 9,ul.getFi1());
				ps.setString(10,ul.getFi2());
				ps.setString(11,ul.getAdd());
				ps.setString(12,ul.getKvalitetaLeca());
				ps.setString(13,ul.getSloj());

				int kom=ps.executeUpdate();

				if (kom==1)
				{

				if (sifra==DAO.NEPOSTOJECA_SIFRA)
				{
					Logger.fatal("ne mozemo ocitati zadnju sifru insertirane naocale!",null);
					return;
				}
				else
				ul.setSifra(Integer.valueOf(sifra)); // po tome i pozivac zna da je insert uspio...

				}//if kom==1
				else
				{
					Logger.fatal("neuspio insert zapisa u tablicu "+tablica,null);
					return;
				}

				try{if(ps!=null) ps.close();}catch(SQLException e){}

				ulaz=ul.getDesna();


				String tablica2="DIOPTRIJA_NAOCALA";

				upit =
							" INSERT INTO "+tablica2
							+ "		(dl,"				//1
							+	"		 db,"				//2
							+	"    dsph,"  		//3
							+ "    dcyl,"			//4
							+ "    ax,"		  	//5
							+ "    napomena,"	//6
							+ "    id,"	  		//7
							+ "    PD,"	   		//8  20.11.06. -asabo- dodano
							+ "    pris,"     //9  13.12.06. -asabo- dodana prizma,brprizma i adicija
							+ "    bpris,"    //10
							+ "    adicija,"	//11		
							+ "    created,"  //
							+ "    created_by"//12			
							+ " ) values(?,?,?,?,?,?,?,?,?,?,?,current_timestamp,?)";	//12 komada

				ps=conn.prepareStatement(upit);

				//			unos desnog stakla
				ps.setString(1,DAO.DESNO);
				ps.setString(2,ulaz.getDb());
				ps.setString(3,ulaz.getDsph());
				ps.setString(4,ulaz.getDcyl());
				ps.setString(5,ulaz.getAx());
				ps.setString(6,ulaz.getNapomena());
				ps.setInt(7,sifra);
				ps.setString(8,ulaz.getPd()); //20.11.06. -asabo- dodano
				
				ps.setString(9,ulaz.getPris());  //13.12.06. -asabo- dodano
				ps.setString(10,ulaz.getBpris());
				ps.setString(11,ulaz.getAdicija());
				ps.setInt(12,GlavniFrame.getSifDjelatnika());

				kom=ps.executeUpdate();

				if (kom!=1)
				Logger.fatal("Dioptrija naocala nije uspjesno insertirana! ",null);
				else
				{   // unos lijevog stakla
					ulaz=ul.getLijeva();

					ps.setString(1,DAO.LIJEVO);
					ps.setString(2,ulaz.getDb());
					ps.setString(3,ulaz.getDsph());
					ps.setString(4,ulaz.getDcyl());
					ps.setString(5,ulaz.getAx());
					ps.setString(6,ulaz.getNapomena());
					ps.setInt(7,sifra);
					ps.setString(8,ulaz.getPd());

					ps.setString(9,ulaz.getPris());  //13.12.06. -asabo- dodano
					ps.setString(10,ulaz.getBpris());
					ps.setString(11,ulaz.getAdicija());
					ps.setInt(12,GlavniFrame.getSifDjelatnika());

					kom=ps.executeUpdate();
				}//else
			  }
			  catch (SQLException e) {
				Logger.fatal("Greska kod inserta tablice "+tablica,e);
			  }finally{
				try {if (ps!=null) ps.close();} catch (SQLException e1){}
				try{if(rstemp!=null) rstemp.close();}catch(SQLException sqle){}
				DAOFactory.freeConnection(conn);
			}
		}//insert

	public boolean update(Object objekt) throws SQLException
	{
		NaocaleVO ul=(NaocaleVO)objekt;

			if (ul==null)
				throw new SQLException("Update "+tablica+", ulazna vrijednost je null!");

			String upit =
								" update "+tablica+" set "
							+	"		  datum=?,"				//1
							+	"		  model=?,"				//2
							+	"		sifBoje=?,"				//3   //05.07.05. -asabo- falio zarez...
							+ "   updated_by="+GlavniFrame.getSifDjelatnika()+"," //29.06.05 -asabo- dodano 2 retka
							+ "   updated=current_timestamp,"
							+ "   napomena=?," //04.10.05. 4-asabo- dodao
							+ "   za_sunce=?," //04.10.05. 5-asabo- dodao
							+ "   broj_kartice=?,"//04.10.05. 6-asabo- dodao
							+ " SIF_PROIZV_STAKALA=?,"//09.10.05. 7 -asabo- dodao
							+ "    fi1=?,"   // 13.03.06. -asabo- dodano
							+ "    fi2=?,"
							+ "    add_stakla=?,"
							+ "    kvaliteta_leca=?,"
							+ "    sloj=?"
							+	" where id=?"; // primary key ...

			Connection conn 			= null;
			PreparedStatement ps 	= null;

			try
			{
				conn=DAOFactory.getConnection();

				if (conn==null) throw new SQLException("veza prema bazi podataka je null?!?");

				ps=conn.prepareStatement(upit);

				ps.setTimestamp(1,new Timestamp(ul.getDatum().getTimeInMillis()));

				// ili ce sifBoje biti null, a u model polju pisati naziv modela kojeg nema u bazi podataka
				// ili ce polje model biti null, a sifBoje ce nositi podatak o sifri boje
				if (ul.getSifBoje()!=null && ul.getSifBoje().intValue()!=DAO.NEPOSTOJECA_SIFRA)
					{
					 ps.setNull(2,Types.VARCHAR);
					 ps.setInt(3,ul.getSifBoje().intValue());
					}
					else
					{
					 ps.setString(2,ul.getModel());
					 ps.setNull(3,Types.INTEGER);
					}

					//04.10.05. -asabo- kolone 4,5,6 dodane

					String nap=ul.getNapomena();

					if (nap==null || nap.trim().equals(""))
					ps.setNull(4,Types.VARCHAR);
					else
					ps.setString(4,ul.getNapomena().trim());

					nap=null; // nece spasit svijet, ali eto navika...

  				ps.setString(5, ul.isZaSunce()?DAO.DA:DAO.NE); //jeli cvika i za sunce ili nije..

					long serBr=ul.getBrojKartice();
					if (serBr==0L || serBr==DAO.NEPOSTOJECA_SIFRA)
					ps.setNull(6,Types.BIGINT);
					else
					ps.setLong(6,serBr);

					if (ul.getSifProizvodjacaStakla()!=null)
					ps.setInt(7,ul.getSifProizvodjacaStakla().intValue());
					else
					ps.setNull(7,Types.INTEGER);

					ps.setString(8,ul.getFi1());
					ps.setString(9,ul.getFi2());
					ps.setString(10,ul.getAdd());
					ps.setString(11,ul.getKvalitetaLeca());
					ps.setString(12,ul.getSloj());
 
					ps.setInt(13,ul.getSifra().intValue());

					int kom=ps.executeUpdate();

					if (kom==0)
						Logger.log("Naocale.update nije uspio promjeniti podatke u retku?!? Sifra: "+(ul!=null&&ul.getSifra()!=null?ul.getSifra().intValue():-1),null);

				String tablica2="DIOPTRIJA_NAOCALA";

				NaocalnaLecaVO ulaz=ul.getLijeva();

					upit =
								" update "+tablica2
								+ "	set "
								+	"		 db=?,"				//1
								+	"    dsph=?,"  		//2
								+ "    dcyl=?,"			//3
								+ "    ax=?,"		  	//4
								+ "    napomena=?,"	//5
								+ "    PD=?,"			  //6 20.11.06. -asabo- dodano
								+ "    pris=?,"     //7 13.12.06. -asabo- dodano
								+ "    bpris=?,"		//8
								+ "    adicija=?,"  //9
								+ "    updated=current_timestamp,"
								+ "    updated_by="+GlavniFrame.getSifDjelatnika()								
								+ "  where id=? and dl=?";	//primary key..

  				try {if (ps!=null) ps.close();} catch (SQLException e1){}
					ps=conn.prepareStatement(upit);

					//			unos desnog stakla
					ps.setString(1,ulaz.getDb());
					ps.setString(2,ulaz.getDsph());
					ps.setString(3,ulaz.getDcyl());
					ps.setString(4,ulaz.getAx());
					ps.setString(5,ulaz.getNapomena());
					ps.setString(6,ulaz.getPd());
					ps.setString(7,ulaz.getPris());
					ps.setString(8,ulaz.getBpris());
					ps.setString(9,ulaz.getAdicija());
					
					ps.setInt(10,ul.getSifra().intValue());
					ps.setString(11,DAO.LIJEVO);

					kom=ps.executeUpdate();

				      	// desno staklo
				      	ulaz=ul.getDesna();

        				ps.setString(1,ulaz.getDb());
								ps.setString(2,ulaz.getDsph());
								ps.setString(3,ulaz.getDcyl());
								ps.setString(4,ulaz.getAx());
								ps.setString(5,ulaz.getNapomena());
								ps.setString(6,ulaz.getPd());
								ps.setString(7,ulaz.getPris());
								ps.setString(8,ulaz.getBpris());
								ps.setString(9,ulaz.getAdicija());
					
								ps.setInt(10,ul.getSifra().intValue());
								ps.setString(11,DAO.DESNO);

								kom+=ps.executeUpdate();

				return kom==2;
				}
				//28.06.05. -asabo- NEMA CATCH-anja! - sve ide pozivatelju...
				finally{
				try {if (ps!=null) ps.close();} catch (SQLException e1){}
				if (conn!=null) DAOFactory.freeConnection(conn);
				return false;
			}//finally
	}//update

	public void delete(Object kljuc) throws SQLException {
	}

	public ValueObject read(Object kljuc) throws SQLException {
		Integer sifra = null;
			if (kljuc instanceof Integer){
				sifra=(Integer)kljuc;
			}
			String upit=
								"SELECT "
							+ "   id,"
							+ "		datum,"
							+ "		model,"
							+ "		sifBoje,"
							+ "		created,"
							+ "		updated,"
							+ "		created_by,"
							+ "   updated_by,"
							+ "     napomena,"
							+ "     za_sunce,"
							+ " broj_kartice,"
							+ " SIF_PROIZV_STAKALA,"
							+ " created,"
							+ " updated,"
							+ " created_by,"
							+ " updated_by,"
							+ "    fi1,"   // 13.03.06. -asabo- dodano
							+ "    fi2,"
							+ "    add_stakla,"
							+ "    kvaliteta_leca,"
							+ "    sloj"

							+ " FROM "
							+ "	 "+tablica;

				if (sifra!=null) upit += " where id =  "+sifra.intValue();

				upit+=" order by id";

				ResultSet rs					=	null;
				ResultSet rs2					=	null;
				PreparedStatement ps  = null;
				Connection 			 con  = null;

				String tablica2="dioptrija_naocala";
				String upit2="select dl,db,dsph,dcyl,ax,napomena,PD,pris,bpris,adicija from "+tablica2+" where id=? and dl=?";

				rs=DAOFactory.performQuery(upit);

			NaocaleVO  nvo=null;

					try
					{
					  con=DAOFactory.getConnection();

					  if (con==null)
				   	throw new SQLException("Nema veze na bazu podataka");

					  ps=con.prepareStatement(upit2);

					  Integer sf=null;

					  if (rs.next())
						 {
						  nvo=constructCvika(rs);
						  NaocalnaLecaVO tmp=null;

							//LIJEVA LECA
						  ps.setInt(1,nvo.getSifra().intValue());
						  ps.setString(2,DAO.LIJEVO);

						  rs2=ps.executeQuery();

						  if (rs2!=null && rs2.next())
						  {
						  tmp=constructNaocalnaLecaStaklo(rs2,nvo);
						  nvo.setLijeva(tmp);
						  }

							try{if(rs2!=null) rs2.close(); rs2=null;}	catch(SQLException sqle){}

							// DESNA LECA
							ps.setInt(1,nvo.getSifra().intValue());
							ps.setString(2,DAO.DESNO);

							rs2=ps.executeQuery();

							if (rs2!=null && rs2.next())
							{
							tmp=constructNaocalnaLecaStaklo(rs2,nvo);
							nvo.setDesna(tmp);
							}

							try{if(rs2!=null) rs2.close(); rs2=null;}	catch(SQLException sqle){}
							}//if
				  }
				  //28.06.05. -asabo- nema CATCH-anja ...
				  finally
				  {
					try{if (rs!=null) rs.close();}catch(SQLException sqle){}
					try{if(rs2!=null) rs2.close();}	catch(SQLException sqle){}
					try{if(ps!=null) ps.close(); ps=null;}	catch(SQLException sqle){}
					if (con!=null) DAOFactory.freeConnection(con);
				}
				return nvo;
	}//read

	public List findAll(Object kljuc) throws SQLException {
		Integer sifra = null;
		SearchCriteria kriterij=null;
		Integer sifKupca=null;
		List lista=new ArrayList();

		// zasada se nije pojavila neka potreba traziti izdane naocale po nekom kriteriju
		if (kljuc instanceof Integer){
			sifra=(Integer)kljuc;
		}
		else if (kljuc instanceof SearchCriteria)
		{
			kriterij=(SearchCriteria)kljuc; //kriterij zasada ide samo za sunce
			if (kriterij.getKriterij()!=null && kriterij.getKriterij().equals(SearchCriteria.KRITERIJ_KLJUC))
			{
				sifKupca=(Integer)kriterij.getPodaci().get(0);
			}
		}

		String upit=
						"SELECT " //14.11.05. -asabo- izmjenjen upit da bude kao u read
					+ tablica+".id,"
					+ tablica+".datum,"
					+ tablica+".model,"
					+ tablica+".sifBoje,"
  				+ tablica+".napomena,"
					+ tablica+".za_sunce,"
					+ tablica+".broj_kartice,"
					+ tablica+".SIF_PROIZV_STAKALA,"
					+ tablica+".created,"
					+ tablica+".updated,"
					+ tablica+".created_by,"
					+ tablica+".updated_by,"
					+ tablica+".fi1,"   // 13.03.06. -asabo- dodano
					+ tablica+".fi2,"
					+ tablica+".add_stakla,"
					+ tablica+".kvaliteta_leca,"
					+ tablica+".sloj"
					+ " FROM "
					+ "	 "+tablica;

		if (sifra!=null) upit += " where id =  "+sifra.intValue();
		 else
		 {
		 	upit+=" left join pregledi on pregledi.ordDaleko=id or pregledi.ordBlizu=id or pregledi.suncaneNaocale=id ";
			if (kriterij!=null) upit+= " where pregledi.sifra is not null and za_sunce='"+DAO.DA+"'";
		  if (sifKupca!=null) upit+=" and pregledi.sifKlijenta="+sifKupca.intValue();

		 }

		upit+=" order by 2 desc";

		ResultSet rs					=	null;
		ResultSet rs2					=	null;
		PreparedStatement ps  = null;
		Connection 			 con  = null;

		String tablica2="dioptrija_naocala";
		String upit2="select dl,db,dsph,dcyl,ax,napomena,PD from "+tablica2+" where id=? and dl=?";

		rs=DAOFactory.performQuery(upit);

		NaocaleVO  nvo=null;

			try
			{
				con=DAOFactory.getConnection();

				if (con==null)
				throw new SQLException("Nema veze na bazu podataka");

				ps=con.prepareStatement(upit2);

				Integer sf=null;

				while (rs.next())
				 {
					nvo=constructCvika(rs);
					NaocalnaLecaVO tmp=null;

					//LIJEVA LECA
					ps.setInt(1,nvo.getSifra().intValue());
					ps.setString(2,DAO.LIJEVO);

					rs2=ps.executeQuery();

					if (rs2!=null && rs2.next())
					{
					tmp=constructNaocalnaLecaStaklo(rs2,nvo);
					nvo.setLijeva(tmp);
					}

					try{if(rs2!=null) rs2.close(); rs2=null;}	catch(SQLException sqle){}

					// DESNA LECA
					ps.setInt(1,nvo.getSifra().intValue());
					ps.setString(2,DAO.DESNO);

					rs2=ps.executeQuery();

					if (rs2!=null && rs2.next())
					{
					tmp=constructNaocalnaLecaStaklo(rs2,nvo);
					nvo.setDesna(tmp);
					}

					try{if(rs2!=null) rs2.close(); rs2=null;}	catch(SQLException sqle){}
					lista.add(nvo);
					}//while
			}
			catch (SQLException e) {
		  Logger.fatal("SQL iznimka kod findAll() za tablicu "+tablica,e);
			}
			finally
			{
				try{if (rs!=null) rs.getStatement().close();}catch(SQLException e){}
				try{if (rs!=null) rs.close();}catch(SQLException sqle){}
				try{if (rs2!=null) rs2.close();}catch(SQLException sqle){}
				try{if (ps!=null) ps.close();}catch(SQLException sqle){}
				DAOFactory.freeConnection(con);
			}
			return lista;
	}//findAll

	public Class getVOClass() throws ClassNotFoundException {
		return null;
	}
	private String[] kolone={"datum","model","leæe"};
	public String getColumnName(int rb) {
		return kolone[rb];
	}
	public int getColumnCount() {
		return kolone.length;
	}
	public Class getColumnClass(int columnIndex) {
		return String.class;
	}
	public Object getValueAt(ValueObject vo, int kolonas)
	{
		NaocaleVO nvo=(NaocaleVO)vo;

		switch(kolonas)
		{
			case 0:
			return Util.convertCalendarToString(nvo.getDatum());
			case 1:
			return OsobineNaocalaPanel.getNazivModela(nvo);
			case 2:
			ProizvodjacVO pvo;
			pvo=OsobineNaocalaPanel.getProizvodjac(nvo);
			return pvo!=null?pvo.getNaziv():"";
			default:
			return null;
		}
	}//getValueAt

	public boolean setValueAt(ValueObject vo, Object vrijednost, int kolona) {
		return false;
	}
	public boolean isCellEditable(ValueObject vo, int kolona) {
		return false;
	}
	public int getRowCount() {
		return 0;
	}

	private NaocaleVO constructCvika(ResultSet rs) throws SQLException
	{
		NaocaleVO nvo=new NaocaleVO();
		Calendar c=Calendar.getInstance();

		nvo.setSifra(Integer.valueOf(rs.getInt("id")));

		c.setTimeInMillis(rs.getTimestamp("datum").getTime() );
		nvo.setDatum(c);
		nvo.setModel(rs.getString("model"));
		if (rs.wasNull())
		nvo.setSifBoje(Integer.valueOf(rs.getInt("sifBoje")));
		else nvo.setSifBoje(null);

		Timestamp tmp=rs.getTimestamp("updated");

		nvo.setCreated(rs.getTimestamp("created").getTime());
		nvo.setLastUpdated(tmp!=null?tmp.getTime():0L);

		nvo.setCreatedBy(Integer.valueOf(rs.getInt("created_by")));
		nvo.setLastUpdatedBy(Integer.valueOf(rs.getInt("updated_by")));
		if (rs.wasNull()) nvo.setLastUpdatedBy(null);

		// 05.10.05. -asabo- dodane nove tri kolone
		nvo.setNapomena(rs.getString("napomena"));
		nvo.setBrojKartice(rs.getLong("broj_kartice"));
		if (rs.wasNull()) nvo.setBrojKartice(0L);
		String sn=rs.getString("za_sunce");
		nvo.setZaSunce(sn!=null && sn.equals(DAO.DA));

		//09.10.05. -asabo- dodano
		nvo.setSifProizvodjacaStakla(Integer.valueOf(rs.getInt("SIF_PROIZV_STAKALA")));
		if (rs.wasNull()) nvo.setSifProizvodjacaStakla(null);

		nvo.setFi1(rs.getString("fi1"));
		nvo.setFi2(rs.getString("fi2"));
		nvo.setAdd(rs.getString("add_stakla"));
		nvo.setKvalitetaLeca(rs.getString("kvaliteta_leca"));
		nvo.setSloj(rs.getString("sloj"));

		return nvo;
	}//constructCvika

	//nvo u ovoj situaciji nije potreban, ali je ostavljen za neke buduce potrebe..
	private NaocalnaLecaVO constructNaocalnaLecaStaklo (ResultSet rs,NaocaleVO nvo)throws SQLException
	{
		if (rs==null) return null;

		NaocalnaLecaVO l=new NaocalnaLecaVO();

		l.setDl(rs.getString("dl"));
		l.setDb(rs.getString("db"));
		l.setDsph(rs.getString("dsph"));
		l.setDcyl(rs.getString("dcyl"));
		l.setAx(rs.getString("ax"));
		l.setPd(rs.getString("PD")); //20.11.06. -asabo- dodan razmak zjenica za oko
		l.setPris(rs.getString("pris")); //13.12.06. -asabo- dodane prizma, bprizma i adicija
		l.setBpris(rs.getString("bpris"));
		l.setAdicija(rs.getString("adicija"));
		l.setNapomena(rs.getString("napomena"));

		return l;
	}//constructNaocalnaLecaStaklo

	public GUIEditor getGUIEditor() {
		return null;
	}//constructNaocalnaLecaStaklo
}//klasa

