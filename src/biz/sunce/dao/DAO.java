/*
 * Created on 2005.04.23
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package biz.sunce.dao;

import java.sql.SQLException;

import biz.sunce.opticar.vo.ValueObject;

/**
 * @author dstanic
 *
 */
public interface DAO<VO extends ValueObject>
{
	public static Class<String>  STRING_CLASS  = String.class;
	public static Class<Integer> INTEGER_CLASS = Integer.class;
	public static Class<Boolean> BOOLEAN_CLASS = Boolean.class;
	
	public static final String UPDATED_BY = "updated_by";
	public static final String UPDATED = "updated";
	public static final String CREATED = "created";
	public static final String CREATED_BY = "created_by";

	
public void insert(VO objekt) throws SQLException;
public boolean update (VO objekt)throws SQLException;

public void delete (Object kljuc)throws SQLException;

public VO read (Object kljuc)throws SQLException;

// ako kljuc==null onda se vracaju svi podaci...
public java.util.List<VO> findAll(Object kljuc)throws SQLException;

public Class<VO> getVOClass()  throws ClassNotFoundException;

//30.11.05. -asabo- dodana metoda za povrat gui editora
public GUIEditor<VO> getGUIEditor();


//metode potrebne da bi se podaci DAO interfacea mogli prikazivati u GUI tablicama
public String getColumnName(int rb);
public int getColumnCount();
// prva kolona je 0, pa ide do N-1
public Class<?> getColumnClass(int columnIndex);
public Object getValueAt(VO vo,int kolonas);
public boolean setValueAt(VO vo, Object vrijednost,int kolona);
public boolean isCellEditable(VO vo, int kolona);
public int getRowCount();

//29.11.05. -asabo- dodana metoda za vracanje GUI uredjivaca objekta
//public GUIEditor getGUIEditor();

// 10.05.05 -asabo- osnovne varijable
public final static String STATUS_UPDATED_PS="U";
public final static String STATUS_DELETED_PS="D";
public final static String STATUS_ARCHIVED_PS="A";
public final static String STATUS_STORNIRAN_PS="S";

public final static String STATUS_DELETED="'"+STATUS_DELETED_PS+"'";
public final static String STATUS_UPDATED="'"+STATUS_UPDATED_PS+"'";
public final static String STATUS_ARCHIVED="'"+STATUS_ARCHIVED_PS+"'";
public final static String STATUS_STORNIRAN="'"+STATUS_STORNIRAN_PS+"'";

public final static String DA="D";
public final static String NE="N";
public final static String NULL="null";
public final static int NEPOSTOJECA_SIFRA=-1;
//23.05.05. -asabo-
public static int PRVA_SIFRA=1;
public static String DESNO="d";
public static String LIJEVO="l";

//16.06.05. -asabo- dodana jedinica minuta unutar kojih se moze zakazati pregled
public static int PREGLEDI_JEDINICA_MINUTA=15;
//17.08.05. -asabo- koliko dana unaprijed ce se gledati kojima se sve ljudima treba zakazati pregled
public static int PREGLEDI_DANA_ZA_ZAKAZATI_PREGLED_UNAPRIJED_SE_GLEDA=10;
public static int PREGLEDI_DANA_ZA_GLEDATI_RODJENDAN=7;

public static int KONTROLA_ZA_MJESECI=12;

public static String KRITERIJ_KLIJENT_DATUM_RODJENJA="kr_kl_dat_rodj";
public static String KRITERIJ_SLIKA_NAOCALE="kr_sl_naoc";
public static String KRITERIJ_SLIKA_BOJE="kr_sl_boje";
public static String KRITERIJ_KLIJENT_LIMIT_1000="kr_kl_limit_1000";

public static String GUI_DAO_ROOT="biz.sunce.dao.gui";

}//DAO
