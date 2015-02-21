package biz.sunce.optika.azurirac;

public final class StatusObrade 
{
	private String poruka;
	private int komada;
	private boolean uspjesno;
	
	public int getKomada() {
		return komada;
	}
	public void setKomada(int komada) {
		this.komada = komada;
	}
	public String getPoruka() {
		return poruka;
	}
	public void setPoruka(String poruka) {
		this.poruka = poruka;
	}
	public boolean isUspjesno() {
		return uspjesno;
	}
	public void setUspjesno(boolean uspjesno) {
		this.uspjesno = uspjesno;
	}

}
