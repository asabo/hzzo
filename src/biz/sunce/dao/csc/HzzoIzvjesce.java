/*
 * Project opticari
 *
 */
package biz.sunce.dao.csc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import biz.sunce.dao.DAO;
import biz.sunce.dao.DAOFactory;
import biz.sunce.dao.GUIEditor;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.HzzoStavkaIzvjescaVO;
import biz.sunce.opticar.vo.ValueObject;
import biz.sunce.util.Util;

/**
 * datum:2006.06.28
 * @author asabo
 *
 */
public final class HzzoIzvjesce implements biz.sunce.dao.HzzoIzvjesce
{
	String[] kolone=
	 {
	 "tip","naziv pomagala","proizvoðaè","broj potvrde","kolièina","ime","prezime","standardno pomagalo","osoba nadoplatila", "šifra nestd. pom."
	 };
	 ArrayList zadnjiL=null;
	 
	public void insert(Object objekt) throws SQLException {
	}

	public boolean update(Object objekt) throws SQLException {
		return false;
	}

	public void delete(Object kljuc) throws SQLException {
	}

	public ValueObject read(Object kljuc) throws SQLException {
		return null;
	}

	public List findAll(Object kljuc) throws SQLException 
	{
	ArrayList l=new ArrayList();
	SearchCriteria krit=null;
	
	if(kljuc==null) throw new SQLException("Kriterij pretrazivanja je prazan!");
	
	if (kljuc instanceof SearchCriteria)	krit=(SearchCriteria)kljuc;
	
	if (krit!=null)
	{	
	ArrayList p=(ArrayList)krit.getPodaci();
	Calendar dOd, dDo;
	dOd=(Calendar) p.get(0);
	dDo=(Calendar) p.get(1);
	
	
	String upit="select r.datum_izdavanja,sr.sif_artikla,sr.sif_proizvodjaca as srsfp,r.sif_proizvodjaca as rsfp,sr.kolicina,a.naziv, k.ime, k.prezime, r.broj_potvrde1, r.broj_potvrde2, r.uzet_skuplji_model,r.osnovno_osiguranje,r.iznos_sudjelovanja,sr.po_cijeni,r.sifra,r.iznos_sudjelovanja,sr.tvrtka_sifra_art"+ 
				" from stavke_racuna sr, racuni r,artikli a, klijenti k where r.sifra=sr.sif_racuna and k.sifra=r.sif_klijenta"+
				" and a.sifra=sr.sif_artikla and r.datum_izdavanja>='"+Util.convertCalendarToStringForSQLQuery(dOd)+"'"+
		    " and r.datum_izdavanja<='"+Util.convertCalendarToStringForSQLQuery(dDo)+"'"+
		    " and r.status!="+biz.sunce.dao.DAO.STATUS_DELETED+" and r.roba_isporucena='"+DAO.DA+"'"; //02.07.06. -asabo- dodano	
		    
	ResultSet rs=null;
	try
	{
		rs=DAOFactory.performQuery(upit);
		
		if (rs==null) throw new SQLException("ResultSet prazan kod HzzoIzvjesca findAll ");
		
		while (rs.next())
		{
			l.add(constructStavkuIzvjesca(rs));
		}
			    
	}
	finally
	{
		try{if(rs!=null) rs.close();}catch(SQLException sqle){} rs=null;
	}
	} //if krit!=null
	this.zadnjiL=l;
  return l;
	}

	public Class getVOClass() throws ClassNotFoundException {
		return Class.forName("biz.sunce.opticar.vo.HzzoStavkaIzvjescaVO");
	}
	
	private final HzzoStavkaIzvjescaVO constructStavkuIzvjesca(ResultSet rs) throws SQLException
	{
		HzzoStavkaIzvjescaVO s=new HzzoStavkaIzvjescaVO();
		
	  java.sql.Date d;
    Calendar c;
    int sfp;
    
    d=rs.getDate("datum_izdavanja");
    c=Calendar.getInstance();
    c.setTimeInMillis(d.getTime());
    
    //gledamo sifru proizvodjaca prvo na razini stavke racuna, zatim na razini racuna 
    //na taj nacin covjek ne mora upisivati ponovno sifru proizvodjaca za stavke racuna
    sfp=rs.getInt("srsfp");
    if (rs.wasNull())
    {
    	String stmp=rs.getString("rsfp");
			if (rs.wasNull() || stmp.equals("")) sfp=biz.sunce.dao.DAO.NEPOSTOJECA_SIFRA;
			else
			{
				try{sfp=Integer.parseInt(stmp);}catch(NumberFormatException nfe)
				{sfp=biz.sunce.dao.DAO.NEPOSTOJECA_SIFRA;}
			}//else    	
    }// if rs.wasNull()
    
       
     s.setDatIzdavanja(c);
     s.setSifArtikla(rs.getString("sif_artikla"));
    
     if (sfp!=biz.sunce.dao.DAO.NEPOSTOJECA_SIFRA)
     s.setSifProizvodjaca(Integer.valueOf(sfp)); else s.setSifProizvodjaca(null); //just in case
		     
     s.setKolicina(Integer.valueOf(rs.getInt("kolicina")));
     s.setNazivArtikla(rs.getString("naziv"));
     s.setIme(rs.getString("ime"));
		 s.setPrezime(rs.getString("prezime"));
		 s.setBrojPotvrde(rs.getString("broj_potvrde1")+"/"+rs.getString("broj_potvrde2"));
		 s.setUzetSkupljiModel(Boolean.valueOf(rs.getString("tvrtka_sifra_art")!=null));
		 s.setTvrtkaSifraNestandardnogArtikla(rs.getString("tvrtka_sifra_art"));
  	 s.setStrankaNadoplatila(Boolean.valueOf(rs.getString("osnovno_osiguranje").equals(biz.sunce.dao.DAO.DA)
														&& rs.getInt("iznos_sudjelovanja")>0));
												
  	 s.setCijena(Integer.valueOf(rs.getInt("po_cijeni"))); // -asabo- dodano 12.09.06.
  	 s.setSifRacuna(Integer.valueOf(rs.getInt("sifra")));
  	 s.setIznosSudjelovanja(Integer.valueOf(rs.getInt("iznos_sudjelovanja")));
		
		return s;
	}//constructStavkuIzvjesca

	public GUIEditor getGUIEditor() {
		return null;
	}

	public String getColumnName(int rb) {
		return kolone[rb];
	}

	public int getColumnCount() {
		return kolone.length;
	}

	public Class getColumnClass(int columnIndex) {
		try
	{
	
		switch(columnIndex)
	{
	
		case 0: case 1: case 2: case 3:  return Class.forName("java.lang.String");
		case 4: return Class.forName("java.lang.Integer");
		case 5: case 6: case 7: case 8: case 9: return Class.forName("java.lang.String"); 
		default: 
		return null;
	}
	}
	catch(ClassNotFoundException cnfe)
	{
		return null;
	}
	}//getColumnClass

	public Object getValueAt(ValueObject vo, int kolonas) {
		if (vo==null) return null;
		HzzoStavkaIzvjescaVO s=(HzzoStavkaIzvjescaVO)vo;
  
			switch(kolonas)
			{
				case 0:
				return s.getSifArtikla();
				case 1:
				return s.getNazivArtikla();
				case 2:
				return s.getSifProizvodjaca();
				case 3:
				return s.getBrojPotvrde();
				case 4: 
				return s.getKolicina();
				case 5:
				return s.getIme();
				case 6: 
				return s.getPrezime();
				case 7:
				return s.getUzetSkupljiModel().booleanValue()?"NE":"DA";
				case 8:
				return s.getStrankaNadoplatila().booleanValue()?"DA":"NE";
				case 9:
				return s.getTvrtkaSifraNestandardnogArtikla()!=null?s.getTvrtkaSifraNestandardnogArtikla():"";
			}
  
			return null;
	}//getValueAt

	public boolean setValueAt(ValueObject vo, Object vrijednost, int kolona) {
		return false;
	}

	public boolean isCellEditable(ValueObject vo, int kolona) {
		return false;
	}

	public int getRowCount() {
		return this.zadnjiL!=null?this.zadnjiL.size():0;
	}

}
