package biz.sunce.util.tablice.sort;

import java.util.*;

public class ColumnComparator
  implements Comparator
{
  protected int index;
  protected boolean ascending;

  //private Collator croTextOrder;
  private SabotovSortModel model=null;
  private final boolean modelPrisutan;


  public ColumnComparator(int index, boolean ascending)
  {
    this.index = index;
    this.ascending = ascending;
    //this.croTextOrder= Collator.getInstance(new java.util.Locale("hr", "hr"));
    
    this.modelPrisutan=false;
  }

  public ColumnComparator(int index, boolean ascending, SabotovSortModel model)
  {
    this.index = index;
    this.ascending = ascending;
    //this.croTextOrder= Collator.getInstance(new java.util.Locale("hr", "hr"));
    this.model=model;
    if (this.model!=null) this.modelPrisutan=true; else this.modelPrisutan=false;
  }

  public int compare(Object one, Object two)
  {
   Object oOne=null;
   Object oTwo=null;

   if (this.modelPrisutan)
   {
    if (ascending)
    return this.model.usporediDvaRetka(one,two,index);
     else
      return this.model.usporediDvaRetka(two,one,index);
   }
   else
   {

    if (one instanceof Vector &&
        two instanceof Vector)
    {
     Vector vOne = (Vector) one;
     Vector vTwo = (Vector) two;
     oOne= vOne.elementAt(index);
     oTwo = vTwo.elementAt(index);
    }
    if (one instanceof UvaljivoUTablicu &&
        two instanceof UvaljivoUTablicu)
    {
     UvaljivoUTablicu uOne, uTwo;
     uOne=(UvaljivoUTablicu)one;
     uTwo=(UvaljivoUTablicu)two;
     if (ascending)
         {
           //return this.croTextOrder.compare(cOne,cTwo);
           return uOne.compareAtColumn(uTwo,index);
         }
         else
         {
          // return this.croTextOrder.compare(cTwo,cOne);
           return uTwo.compareAtColumn(uOne,index);
         }
    }//if

   }//else 
   
    return 1;
  }//compare

}//klasa

