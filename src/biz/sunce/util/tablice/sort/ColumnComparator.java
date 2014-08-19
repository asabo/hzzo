package biz.sunce.util.tablice.sort;

import java.util.*;

import biz.sunce.opticar.vo.ValueObject;

public class ColumnComparator<VO extends ValueObject>
  implements Comparator<VO>
{
  protected int index;
  protected boolean ascending;

  //private Collator croTextOrder;
  private SabotovSortModel<VO> model=null;
  private final boolean modelPrisutan;


  public ColumnComparator(int index, boolean ascending)
  {
    this.index = index;
    this.ascending = ascending;
    //this.croTextOrder= Collator.getInstance(new java.util.Locale("hr", "hr"));
    
    this.modelPrisutan=false;
  }

  public ColumnComparator(int index, boolean ascending, SabotovSortModel<VO> model)
  {
    this.index = index;
    this.ascending = ascending; 
    this.model=model;
    if (this.model!=null) this.modelPrisutan=true; else this.modelPrisutan=false;
  }

  public int compare(VO one, VO two)
  {
    
   if (this.modelPrisutan)
   {
    if (ascending)
    return this.model.usporediDvaRetka(one,two,index);
     else
      return this.model.usporediDvaRetka(two,one,index);
   }
    return 1;
  }//compare

}//klasa

