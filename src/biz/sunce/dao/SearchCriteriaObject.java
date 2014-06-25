/*
 * Project opticari
 *
 */
package biz.sunce.dao;
 

/**
 * datum:2007.01.20
 * @author asabo
 *  klasa sadrzi u sebi naziv kolone, vrijednost koja se trazi 
 *  te vrstu pretrazivanja - striktno, rijec bilo gdje, rijec na pocetku,
 * rijec na kraju itd..
 */
public class SearchCriteriaObject 
{
	public static final int CRITERIA_WORD_ANYWHERE=1;
	public static final int CRITERIA_WORD_STRICT=2;
	public static final int CRITERIA_WORD_AT_START=3;
	public static final int CRITERIA_WORD_AT_END=4;
	
	String name;
	Object value;
	int dataType;
	int criteria;
	String filterValue; // sta mozda korisnik unese u formi
	String descriptorColumn; // koja ce se kolona koristiti kao descriptor
	
	public static SearchCriteriaObject getInstance(String name, Object value, int dataType, int criteriaType)
	{
		return new SearchCriteriaObject(name,value,dataType,criteriaType);
	}
	
	private SearchCriteriaObject(String name, Object value, int dataType, int criteriaType)
	{
   		this.name=name;
   		this.value=value;
   		this.dataType=dataType;
   		this.criteria=criteriaType;
	}



	public int getCriteria() {
		return criteria;
	}

	public int getDataType() {
		return dataType;
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	public void setCriteria(int i) {
		criteria = i;
	}

	public void setDataType(int i) {
		dataType = i;
	}

	public void setName(String string) {
		name = string;
	}

	public void setValue(Object object) {
		value = object;
	}

	public String getFilterValue() {
		return filterValue;
	}

	public void setFilterValue(String string) {
		filterValue = string;
	}

}
