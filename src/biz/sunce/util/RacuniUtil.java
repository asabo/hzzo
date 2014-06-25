package biz.sunce.util;

import java.sql.SQLException;
import java.util.ArrayList;

import biz.sunce.dao.DAOFactory;
import biz.sunce.opticar.vo.PomagaloVO;
import biz.sunce.opticar.vo.PoreznaStopaVO;
import biz.sunce.opticar.vo.StavkaRacunaVO;
import biz.sunce.optika.Logger;

public final class RacuniUtil {

	public final static int izracunajTotalRacuna(ArrayList<StavkaRacunaVO> stavkeR) {
		
		int stavkeSize=stavkeR!=null?stavkeR.size():-1;
		int iznosRacuna=0;
		
		for (int i = 0; i < stavkeSize; i++) {
			StavkaRacunaVO svo = (StavkaRacunaVO) stavkeR.get(i);

			
			iznosRacuna += getBruttoIznosStavke(svo);
		}// for i
		
		return iznosRacuna;
	}
	
	/**
	 * iako je cijena sa PDV-om i imamo kolicinu, nije dovoljno samo pomonoziti ta 2 broja, 
	 * zasada, jer hzzo drugacije obracunava pdv... 
	 * @param svo
	 * @return
	 */
	public final static int getBruttoIznosStavke(StavkaRacunaVO svo){
		return svo.getKolicina().intValue()*svo.getPoCijeni();
		/*PoreznaStopaVO stopa = nadjiPoreznuSkupinu(svo.getPoreznaStopa());
		float pdvFaktor = 1.0f+(stopa.getStopa().intValue() / 100.0f);
		float poCijeniFloat = svo.getPoCijeni().floatValue();
		int cijenaBezPDVa = (int) (poCijeniFloat / pdvFaktor + 0.5f);
		int totalBezPDVa = svo.getKolicina().intValue() * cijenaBezPDVa;
		int total = (int) (totalBezPDVa*pdvFaktor + 0.5f);
		return total;*/
	}
	
	public final static int getNettoIznosStavke(StavkaRacunaVO svo){
		PoreznaStopaVO stopa = nadjiPoreznuSkupinu(svo.getPoreznaStopa());
		float pdvFaktor = 1.00f+(stopa.getStopa().intValue() / 100.00f);
		float kolicina = svo.getKolicina().floatValue();
		float jedCijena = svo.getPoCijeni().floatValue();
		int totalBezPDVa = (int) (((kolicina*jedCijena) / pdvFaktor) + 0.500f);
		
		return totalBezPDVa;
	}

	public final static PoreznaStopaVO nadjiPoreznuSkupinu(StavkaRacunaVO svo) {
	return nadjiPoreznuSkupinu(svo.getPoreznaStopa());
	}
	public final static PoreznaStopaVO nadjiPoreznuSkupinu(Integer sifra) {
		PoreznaStopaVO ps = null;

		try {
			if (sifra != null)
				ps = (PoreznaStopaVO) DAOFactory.getInstance()
						.getPorezneStope().read(sifra);
			else
				ps = null;
		} catch (SQLException e) {
			Logger.fatal(
					"SQL iznimka kod Ispisa raèuna osn. osiguranje pri citanju porezne stope.. ",
					e);
		}

		return ps;
	}// nadjiPoreznuSkupinu
	
	public static final PomagaloVO nadjiPomagalo(String sifra) {
		PomagaloVO pom = null;

		try {
			if (sifra != null && !sifra.equals(""))
				pom = (PomagaloVO) DAOFactory.getInstance().getPomagala()
						.read(sifra);
			else
				pom = null;
		} catch (SQLException e) {
			Logger.fatal(
					"SQL iznimka pri citanju odredjenog pomagala.. (nadjiPomagalo)",
					e);
		}

		return pom;
	}// nadjiPomagalo

}
