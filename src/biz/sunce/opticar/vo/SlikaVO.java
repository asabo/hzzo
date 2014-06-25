package biz.sunce.opticar.vo;

import java.awt.image.BufferedImage;

// Referenced classes of package biz.sunce.opticar.vo:
//            ValueObject

public final class SlikaVO extends ValueObject {

	public SlikaVO() {
	}

	public BufferedImage getSlika() {
		return slika;
	}

	@Override
	public Integer getSifra() {
		return sifra;
	}

	@Override
	public void setSifra(Integer integer) {
		sifra = integer;
	}

	public void setSlika(BufferedImage slika) {
		this.slika = slika;
	}

	private Integer sifra;
	private BufferedImage slika;
}