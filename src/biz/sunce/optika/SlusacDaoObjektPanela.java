package biz.sunce.optika;

import biz.sunce.opticar.vo.ValueObject;

public interface SlusacDaoObjektPanela<VO extends ValueObject> {

	public void objektSpremljen(VO objekt);
}
