package biz.sunce.optika.net;

public enum VrstaUpita {
	OIB("upoib", "statos_OIB"), 
	MBO("upmbo", "statos_MBO"),
	FLIDID("uposid", "statos_id_flid"), 
	DZO("upodzo", "statos_DZO"); // po broju dop. osig.

	private String naziv;
	private String stranica;
	
	VrstaUpita(String naziv, String stranica) {
		this.naziv = naziv;
		this.stranica = stranica;
	}

	public String naziv() {
		return this.naziv;
	}

	public String stranica() {
		return this.stranica;
	}
};