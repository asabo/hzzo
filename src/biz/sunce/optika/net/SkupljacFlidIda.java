package biz.sunce.optika.net;

import java.awt.Color;

import javax.swing.JTextField;

import biz.sunce.dao.gui.Racun;
import biz.sunce.util.GUI;
import biz.sunce.util.StringUtils;

public class SkupljacFlidIda extends Thread {

	JTextField jtBrojIskaznice1;
	JTextField jtBrojIskaznice2;
	JTextField jtBrojPoliceDopunsko;
	final VrstaUpita vr ;
	final String br;
	final Racun racun;
	
	public SkupljacFlidIda(JTextField jtBrojIskaznice1,JTextField jtBrojIskaznice2, JTextField jtBrojPoliceDopunsko, VrstaUpita vr, String broj, Racun racun) {
		this.jtBrojIskaznice1 = jtBrojIskaznice1;
		this.jtBrojIskaznice2 = jtBrojIskaznice2;
		this.jtBrojPoliceDopunsko = jtBrojPoliceDopunsko;
		this.vr = vr;
		this.br = broj;
		this.racun = racun;
	}
	
		public void run() {
			setPriority(Thread.MIN_PRIORITY);
			setName("Skupljac FLID-ID od HZZO-a");
			String staro = null;
			Boolean proslo = null;
			String stariTT = null;
			try {
				jtBrojIskaznice2.setBackground(Color.yellow);
				jtBrojIskaznice1.setBackground(Color.yellow);
				staro = jtBrojIskaznice1.getText();
				stariTT = jtBrojIskaznice2.getToolTipText();
				jtBrojIskaznice1.setText(">>>");
				proslo = false;
				String[] rez = HZZOFetchUtil.nadjiFlidIdDzo( br, vr, jtBrojIskaznice1, jtBrojIskaznice2 );
				if (rez == null || rez.length != 3){
					return;
				}

				String flidId = rez[0];
				String dzo = rez[1];
				String vrijediDo = rez[2];

				if (!StringUtils.isEmpty(flidId)) {
					String[] flid = flidId.split("/");
					if (flid == null || flid.length != 2){
						HZZOFetchUtil.setZadnjaKomunikacijaNeuspjesna(true);
						return;
						}
					try {
						Long.parseLong(flid[1]);
					} catch (NumberFormatException nfe) {
						if (vr == VrstaUpita.FLIDID) {
							Racun.alert("Broj kartice nije ispravan!");
							jtBrojPoliceDopunsko.setText("");
							HZZOFetchUtil.setZadnjaKomunikacijaNeuspjesna(true);
							racun.postaviOsnovnoOsiguranje(true);
						}

						return;
					}
					// if ()
					jtBrojIskaznice1.setText(flid[0]);
					String kartica = flid[1] != null ? flid[1].trim()
							: null;

					if (kartica == null)
						return;

					if (kartica.length() == 7)
						kartica = "0" + kartica;
					else if (kartica.length() == 6)
						kartica = "00" + kartica;
					else if (kartica.length() == 5)
						kartica = "000" + kartica;
					else if (kartica.length() == 4)
						kartica = "0000" + kartica;
					else if (kartica.length() == 3)
						kartica = "00000" + kartica;

					jtBrojIskaznice2.setText(kartica);

					proslo = true;
					HZZOFetchUtil.povuceniBroj = kartica;
					HZZOFetchUtil.setZadnjaKomunikacijaNeuspjesna(false);
				}// if
				else {
					jtBrojIskaznice1.setText(staro);
					HZZOFetchUtil.setZadnjaKomunikacijaNeuspjesna(true);
				}
					
				if (dzo != null) {

					dzo = dzo.trim();
					
					try {
						Long.parseLong(dzo);
					} catch (NumberFormatException nfe) {
						 HZZOFetchUtil.setZadnjaKomunikacijaNeuspjesna(true);
						 return;
						}
					
					if (dzo.length() == 7)
						dzo = "0" + dzo;
					else if (dzo.length() == 6)
						dzo = "00" + dzo;
					else if (dzo.length() == 5)
						dzo = "000" + dzo;
					else if (dzo.length() == 4)
						dzo = "0000" + dzo;

					racun.postaviOsnovnoOsiguranje(dzo.length() != 8);

					jtBrojPoliceDopunsko.setText(dzo);
					
					if ( !"".equals(dzo) )
					 GUI.odradiUpozorenjeNaElementu(jtBrojPoliceDopunsko, "Automatski smo izmjenili broj police dopunskog osiguranja!", Color.yellow);
					  else 
						GUI.odradiUpozorenjeNaElementu(jtBrojPoliceDopunsko, "Korisnik nema policu dopunskog osiguranja!", Color.orange);			

					if (vrijediDo != null && vrijediDo.length() > 9)
						jtBrojPoliceDopunsko
								.setToolTipText("Vrijedi do: "
										+ vrijediDo);
					else
						jtBrojPoliceDopunsko.setToolTipText("");
					// proslo=true;
				}// if
				else {
					GUI.odradiUpozorenjeNaElementu(jtBrojPoliceDopunsko, "Korisnik nema policu dopunskog osiguranja!", Color.orange);

					jtBrojPoliceDopunsko.setText("");
					racun.postaviOsnovnoOsiguranje(true);
				}
			} finally {
				yield();
				if (proslo != null && !proslo && staro != null)
					jtBrojIskaznice1.setText(staro);
				jtBrojIskaznice2.setToolTipText(stariTT);
				jtBrojIskaznice2.setBackground(Color.WHITE);
				jtBrojIskaznice1.setBackground(Color.WHITE);
			}
		}// run
	};