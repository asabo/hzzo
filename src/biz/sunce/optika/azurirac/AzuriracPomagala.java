package biz.sunce.optika.azurirac;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import biz.sunce.dao.DAO;
import biz.sunce.dao.PomagaloDAO;
import biz.sunce.dao.SearchCriteria;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.util.StringUtils;
import biz.sunce.util.Util;
import biz.sunce.util.beans.PostavkeBean;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public final class AzuriracPomagala extends Thread
{

	private static final int JEDAN_DAN = 24*60*60*1000;
	private static final String UTF_8 = "UTF-8";
	private static final String ZADNJI_SYNCH_POMAGALA = "zadnji_synch_pomagala";
	private static final String ZADNJE_SLANJE_POMAGALA = "zadnji_slanje_pomagala";
	//private static final String SERVER_URI = "http://localhost:8080/sunce_web/rest/v1/pomagala";
	private static final String SERVER_URI = "http://rest-sunce.rhcloud.com/rest/v1/pomagala";
	private static final String DEFAULT_ENCODING = UTF_8;
	
	private PomagaloDAO pomagaloDAO;
	final String pattern = "yyyy-MM-dd HH:mm:ss";
	
	TypeToken<List<PomagaloVO>> token = new TypeToken<List<PomagaloVO>>() {};

	
	public void azurirajPomagalaUThreadu(final PomagaloDAO pomagaloDAO) 
	{
		this.pomagaloDAO = pomagaloDAO;
	
		this.start();
	}
	

	@SuppressWarnings("deprecation")
	private static HttpClient httpclient = new DefaultHttpClient();
	 
	@SuppressWarnings("deprecation")
	private static String runURL(String src) 
	{
		if (true)
			return "";
		
		HttpGet httpget = new HttpGet(src);
		String responseBody = "";
		HttpResponse response = null;
		BasicHttpParams params = new BasicHttpParams();
		params.setParameter("timestamp", null);
		try {
			httpget.setParams(params);
			
			response     = httpclient.execute(httpget);
			responseBody = EntityUtils.toString(response.getEntity(),UTF_8);

		} 
		catch (Exception e) 
		{
			Logger.debug("Problem sa trazenjem novih artikala", e);		 
		}
		finally
		{	
			httpget.releaseConnection();
			httpget=null;
			response = null;
		}
		
		return responseBody;
	}
	
	@SuppressWarnings("deprecation")
	private static String runPOST(String url, Date tst, String json, String user, int uid, int sifpos, int siftvr) 
	{
		if (true)
			return "";
			
		HttpPost     httppost = new HttpPost(url);
		String   responseBody = "";
		HttpResponse response = null;
	 
		try {
		 	
		     List <NameValuePair> nvps = new ArrayList<NameValuePair>();
		        nvps.add(new BasicNameValuePair("timestamp", ""+tst.getTime()));
		        nvps.add(new BasicNameValuePair("user", user));
		        nvps.add(new BasicNameValuePair("uid", ""+uid));
		        nvps.add(new BasicNameValuePair("pos", ""+sifpos));
		        nvps.add(new BasicNameValuePair("tvr", ""+siftvr));
		        nvps.add(new BasicNameValuePair("payload", json));

		        httppost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
					
			//httppost.setEntity(new StringEntity(json, DEFAULT_ENCODING) );
			
			response = httpclient.execute(httppost);
			
			int statusCode = response.getStatusLine().getStatusCode();
			if (statusCode!=200)
			{
			 Logger.warn("Dobili status "+statusCode+" kod posta",null);
			 return null;
			}
			
			responseBody = EntityUtils.toString(response.getEntity(),DEFAULT_ENCODING);
		} 
		catch (Exception e) 
		{
			Logger.debug("Problem sa slanjem informacija o novim artikalima",e);		 
		}
		finally
		{			
			httppost.releaseConnection();
			httppost = null;
			response = null;
		}
		
		return responseBody;
	}
	
	public void run() 
	{
		setPriority(MIN_PRIORITY);
		setName("AzuriracPomagala");
		
		this.azurirajPomagala(this.pomagaloDAO);
		yield();
		this.posaljiNoveArtikle(this.pomagaloDAO);
	}

	
	public boolean azurirajPomagala(final PomagaloDAO pomagaloDAO) 
	{
		boolean rez=true;
		
		final Gson gson = new GsonBuilder().setDateFormat(
				pattern).create();

		yield();
		
		String zadnjiSynchStr = PostavkeBean.getPostavkaDB(ZADNJI_SYNCH_POMAGALA, "2015-01-07%2000:00:01");
		
		Date zadnjiSynch = Util.getDate(zadnjiSynchStr.replaceFirst("%20", " "), pattern);
		String user = GlavniFrame.getUsernameZaSynch();
		zadnjiSynchStr = ""+zadnjiSynch.getTime();
		
		//svaka 24 sata je dovoljno provjeravati ima li novih artikala
		zadnjiSynch.setTime(zadnjiSynch.getTime()+JEDAN_DAN);
		
		Date sad = new Date();
		boolean prije = Util.isBefore(zadnjiSynch, sad);
		
		if ( !prije )
			return rez;
		
		String url = SERVER_URI+"?timestamp="+zadnjiSynchStr+"&u="+user;
		String json = runURL( url );

		List<PomagaloVO> pomagala = (List<PomagaloVO>) (StringUtils.isEmpty(json) ? null : gson
				.fromJson(json, token.getType()));
		yield();
		 
		rez = pomagala == null ? false : spremiPomagalaUBazu(pomagala, pomagaloDAO);
		
		if( rez && pomagaloDAO!=null )
		{
		zadnjiSynchStr = Util.convertCalendarToStringForSQLQuery(Calendar.getInstance(), true);
		zadnjiSynchStr = zadnjiSynchStr.replaceFirst(" ", "%20");
	
		PostavkeBean.setPostavkaDB(ZADNJI_SYNCH_POMAGALA, zadnjiSynchStr);
		}
		
		return rez;
	}
	
	public boolean posaljiNoveArtikle(final PomagaloDAO pomagaloDAO) 
	{
		boolean rez=true;
	 
		final Gson gson = new GsonBuilder().setDateFormat( pattern ).create();

		yield();
		
		String zadnjiSynchStr = PostavkeBean.getPostavkaDB(ZADNJE_SLANJE_POMAGALA, "2014-12-01%2000:00:01");
		
		Date zadnjiSynch = Util.getDate(zadnjiSynchStr.replaceFirst("%20", " "), pattern);
		
		String user = GlavniFrame.getUsernameZaSynch();
		int sifkor  = GlavniFrame.getSifraKorisnikaZaSynch();
		int sifpos  = GlavniFrame.getSifraPoslovniceZaSynch();
		int siftvr  = GlavniFrame.getSifraTvrtkeZaSynch();
		
		zadnjiSynchStr = ""+zadnjiSynch.getTime();
		
		//svaka 24 sata je dovoljno provjeravati ima li novih artikala
		zadnjiSynch.setTime( zadnjiSynch.getTime()+JEDAN_DAN );
		
		Date sad = new Date();
		boolean prije = Util.isBefore(zadnjiSynch, sad);
		
		if ( !prije )
			return rez;
		
		String url = SERVER_URI;//+"?timestamp="+zadnjiSynchStr;		
			
		List<PomagaloVO> pomagala = povuciPomagalaIzBaze(zadnjiSynch, pomagaloDAO);

		yield();
		
		String podaci = gson.toJson(pomagala);
		
		String json = null; //runPOST(url, zadnjiSynch, podaci, user, sifkor, sifpos, siftvr);

		yield();
		 
		
		if ( json!=null && pomagaloDAO!=null )
		{
		StatusObrade status = gson.fromJson(json, StatusObrade.class);
			
		System.out.println("Dosao status:"+status.getPoruka());			
		zadnjiSynchStr = Util.convertCalendarToStringForSQLQuery(Calendar.getInstance(), true);
		zadnjiSynchStr = zadnjiSynchStr.replaceFirst(" ", "%20");
	
		PostavkeBean.setPostavkaDB(ZADNJE_SLANJE_POMAGALA, zadnjiSynchStr);
		}
		
		return rez;
	}
	
	private static boolean spremiPomagalaUBazu(List<PomagaloVO> pomagala, PomagaloDAO pomagaloDAO) {
		
		try
		{
		for (PomagaloVO pomagalo:pomagala)
		{
			PomagaloVO original = pomagaloDAO==null?null:pomagaloDAO.read(pomagalo.getSifraArtikla());
			
			Thread.yield();
		
			if (pomagaloDAO!=null && original==null)
			{
				pomagalo.setStatus('X'); //da DAO zna da mora postovati sistemske podatke navedene u objektu
				pomagaloDAO.insert(pomagalo);
			}
			else			
			if (pomagaloDAO!=null && noviji(pomagalo, original))
			{
				pomagalo.setStatus('X'); //da DAO zna da mora postovati sistemske podatke navedene u objektu
				pomagaloDAO.update(pomagalo);
			}
			Thread.yield();
		 }//for
		}
		catch (Exception e)
		{
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	private static List<PomagaloVO> povuciPomagalaIzBaze( Date datum, PomagaloDAO pomagaloDAO ) {
		
		List<PomagaloVO> pomagala = null;
		
		try
		{
			@SuppressWarnings("rawtypes")
			SearchCriteria kljuc = new SearchCriteria();
			kljuc.setKriterij(DAO.KRITERIJ_TIMESTAMP);
			kljuc.dodajPodatak(datum);
		    
			 pomagala = pomagaloDAO==null ? null : pomagaloDAO.findAll(kljuc);
			
			Thread.yield();
		
		}
		catch (Exception e)
		{
			Logger.warn("Problem kod povlacenja izmjenjenih artikala", e);
		}
		
		return pomagala;
	}

	private static boolean noviji(PomagaloVO pomagalo, PomagaloVO original) {
		
		 if (original==null) return true;
		
		 if ( pomagalo.getLastUpdated()>original.getLastUpdated())
			 return true;
		 
		 if ( pomagalo.getCreated()>original.getCreated())
			 return true;
		 
		 return false;
	}
}
