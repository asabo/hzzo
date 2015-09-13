package biz.sunce.optika.net;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JTextField;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpRequestRetryHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import sun.net.ConnectionResetException;

public final class HZZOFetchUtil 
{
	// broj kartice, zadnje povuceni preko neta
	public static String povuceniBroj = null;
	private static boolean zadnjaKomunikacijaGreska = false;
	private static boolean zadnjaKomunikacijaNeuspjesna = false;
	 
	private static final String ISO_8859_2 = "iso-8859-2";
	
	private static final String UPOSID = "uposid";
	private static final String UPOSFLID = "uposflid";
	private static final String UPOSOIB = "upoib";
	
	
	private static final String HZZO_WEBSITE = "http://www.hzzo-net.hr";

	private static final String Y2 = "y";
	private static final String X2 = "x";

	/**
	 * na temelju ispravnog OIB-a vrsi upit prema HZZO-u i pokušava povuæi
	 * Flid-id te eventualno broj dopunskog, ako ga ima, od tamo..
	 * 
	 * @param broj
	 * @return prvi param je flidId, drugi je broj DZO
	 */
	public static String[] nadjiFlidIdDzo(
			String broj, 
			VrstaUpita vrstaUpita,
			final JTextField jtBrojIskaznice1,
			final JTextField jtBrojIskaznice2
			) {

		try {
			String uri = HZZO_WEBSITE + "/cgi-bin/"
					+ vrstaUpita.stranica() + ".cgi";// +"status_osiguranja_NOVO.cgi";

			 DefaultHttpClient httpclient = getHttpClient();
			
			//httpclient.getParams().setParameter(
			//		CoreProtocolPNames.HTTP_CONTENT_CHARSET, UTF_8);

			HttpPost post = new HttpPost(uri);
			String refererStr = HZZO_WEBSITE +"/"+ vrstaUpita.stranica() + ".htm";
			post.setHeader("referer", refererStr);
			post.setHeader("DNT","1");
			post.setHeader("Host","www.hzzo-net.hr");
			post.setHeader("Connection","keep-alive");
			post.setHeader("Accept-Language","hr-hr,hr;q=0.8,en-us;q=0.5,en;q=0.3");

			List<NameValuePair> formparams = new ArrayList<NameValuePair>();

			if (vrstaUpita != VrstaUpita.FLIDID) {

				formparams
						.add(new BasicNameValuePair(vrstaUpita.naziv(), broj));
				formparams.add(new BasicNameValuePair(X2, "0"));
				formparams.add(new BasicNameValuePair(Y2, "0"));
				formparams.add(new BasicNameValuePair("answer", "7wrp8"));
				
			 
			} else {
				if (povuceniBroj != null && broj.endsWith(povuceniBroj))
					return null;
				String[] prm = broj.split("/");
				formparams.add(new BasicNameValuePair(UPOSFLID, prm[0]));
				formparams.add(new BasicNameValuePair(UPOSID, prm[1]));
				formparams.add(new BasicNameValuePair(X2, "0"));
				formparams.add(new BasicNameValuePair(Y2, "0"));
				formparams.add(new BasicNameValuePair("answer", "w8rh3"));

				prm=null;
			}

			UrlEncodedFormEntity entity = new UrlEncodedFormEntity( formparams );
			
			post.setEntity(entity);
				
			HttpContext localContext = new BasicHttpContext();
			
			HttpResponse response = httpclient.execute(post, localContext);

			StringBuilder sb = new StringBuilder(16384);

			jtBrojIskaznice2
					.setToolTipText("Pokušavamo se spojiti na HZZO-net.hr");
			HttpEntity ent = response.getEntity();
			String[] j = { ">", ">>", ">>>" };
			int br = 0;

			if (ent != null) {
				InputStream instream = ent.getContent();
				int l;
				byte[] tmp = new byte[2048];
				jtBrojIskaznice2
						.setToolTipText("Prenosimo podatke sa HZZO-net.hr");
				jtBrojIskaznice2.setBackground(Color.green);
				jtBrojIskaznice1.setBackground(Color.green);

				while ((l = instream.read(tmp)) != -1) {
					sb.append(new String(tmp, 0, l, ISO_8859_2));

					jtBrojIskaznice1.setText(j[br++ % 3]);
				}// while

				tmp = null;
				String str = sb.toString();
				sb.setLength(0);
				sb = null;
				
				String token = "FLID/ID: &nbsp;&nbsp;</td><td width=\"310\" height=\"45\" bgcolor=\"#FFFFFF\" class=\"prepisslika\">";
				int tokIndex = str.indexOf(token);
				int poc = tokIndex==-1 ? -1 : tokIndex + token.length();

				String flidid = "";
				if (poc== -1)
				{
				
				}
				else
				{
				 str = str.substring(poc);
				 int kr = str.indexOf("</td>");
				 flidid = str.substring(0, kr);
				 flidid = flidid.replaceAll(" ", "");
				}
				
				String dzo = "";
				String datDop = "";

				String dzotoken = "class=\"isteklapolica\"";
				if ( str.contains(dzotoken) )
				{
					dzo="";
				}
				else
				{
				dzotoken="class=\"narancasta\"";
				int dzopoc = str.indexOf(dzotoken) + dzotoken.length()+1;

				str = str.substring(dzopoc);
				int dkr = str.indexOf("</td>");
				dzo = str.substring(0, dkr);
				dzo = dzo.replaceAll(" ", "");
				}
				// TODO: tu treba još ekstrahirati van datum do kad vrijedi
				// dopunsko
				// i vratiti ga kao treæi element polja
//				String dopdattok = ">Vrijedi od - do</td><td width=\"207\" bgcolor=\"#FFFFFF\" class=\"ispis\">";
//
//				int dpdatpoc = str.indexOf(dopdattok) + dopdattok.length();
//
//				str = str.substring(dpdatpoc);
//				int ddkr = str.indexOf("</td>");
//				datDop = str.substring(0, ddkr);
//				datDop = datDop.replaceAll(" ", "");
//				String[] datumi = datDop.split("-");
//				datDop = datumi != null && datumi.length == 2 ? datumi[1] : "";
				String[] rez = new String[3];
				rez[0] = flidid;
				rez[1] = dzo;
				rez[2] = datDop;

				httpclient = null;
				post = null;
				j=null;

				return rez;
			}// if ent!=null

		} 
		catch(SocketException socke)
		{
			postaviPorukuZaFlidId("Nema dostupa na internet!", true,jtBrojIskaznice1,jtBrojIskaznice2);
		}
		catch (UnknownHostException unk) 
		{
			postaviPorukuZaFlidId("Nema dostupa na internet!", true,jtBrojIskaznice1,jtBrojIskaznice2);
		} 
		catch (Exception e) 
		{
			postaviPorukuZaFlidId("Problem pri dohvatu podataka", true,jtBrojIskaznice1,jtBrojIskaznice2);
			e.printStackTrace();
		}
		return null;
	} // nadjiFlidId

	@SuppressWarnings("deprecation")
	private static DefaultHttpClient getHttpClient() {
		DefaultHttpClient httpClient = new DefaultHttpClient();
		httpClient.setHttpRequestRetryHandler(
				new HttpRequestRetryHandler() {
		     
		    public boolean retryRequest(IOException exception, int executionCount, 
		                                HttpContext context) {
		        if (executionCount > 4) {
		           System.out.println("Maximum tries reached for client http pool ");
		                return false;
		        }
		        if (exception instanceof org.apache.http.NoHttpResponseException) {
		        	System.out.println("No response from server on " + executionCount + " call");
		            return true;
		        }
		        return false;
		      }
		   });
		return httpClient;
	}
	
	public static boolean isZadnjaKomunikacijaGreska(){
		return zadnjaKomunikacijaGreska;
	}
	
	public static boolean isZadnjaKomunikacijaNeuspjesna(){
		return zadnjaKomunikacijaNeuspjesna;
	}
	
	public static boolean isProblemHzzo(){
		return zadnjaKomunikacijaNeuspjesna || zadnjaKomunikacijaGreska;
	}
	
	/*
	 * ako je koumunikacija sa hzzo-om bila uspjesna ali hzzo nije vratio potrebnu informaciju
	 */
	public static void setZadnjaKomunikacijaNeuspjesna(boolean zadnjaKomunikacija){
		 zadnjaKomunikacijaNeuspjesna = zadnjaKomunikacija;
	}
	
	private static void postaviPorukuZaFlidId(final String poruka, final boolean greska, 
			final JTextField jtBrojIskaznice1,
			final JTextField jtBrojIskaznice2
			) 
	 {
		Thread t = new Thread() {
			public void run() {
				setPriority(Thread.MIN_PRIORITY);
				String stariTT = jtBrojIskaznice2.getToolTipText();
				jtBrojIskaznice2.setToolTipText(poruka);
				if (greska) {
					zadnjaKomunikacijaGreska = true;
					jtBrojIskaznice2.setBackground(Color.red);
					jtBrojIskaznice1.setBackground(Color.red);					
				}
				else
					zadnjaKomunikacijaGreska = false;

				try {
					sleep(5000);
				} catch (InterruptedException inte) {
				}

				jtBrojIskaznice2.setBackground(Color.WHITE);
				jtBrojIskaznice1.setBackground(Color.WHITE);
				jtBrojIskaznice2.setToolTipText(stariTT);
			}// run
		};
		t.run();
		// SwingUtilities.invokeLater(t);
	}// postaviPorukuZaFlidId

}
