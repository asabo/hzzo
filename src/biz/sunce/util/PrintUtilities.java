package biz.sunce.util;

import java.awt.*;
import javax.swing.*;
import java.awt.print.*;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.ResolutionSyntax;
import javax.print.attribute.standard.PrinterResolution;
import javax.print.attribute.HashPrintRequestAttributeSet;

/** A simple utility class that lets you very simply print
 *  an arbitrary component. Just pass the component to the
 *  PrintUtilities.printComponent. The component you want to
 *  print doesn't need a print method and doesn't have to
 *  implement any interface or do anything special at all.
 *  <P>
 *  If you are going to be printing many times, it is marginally more
 *  efficient to first do the following:
 *  <PRE>
 *    PrintUtilities printHelper = new PrintUtilities(theComponent);
 *  </PRE>
 *  then later do printHelper.print(). But this is a very tiny
 *  difference, so in most cases just do the simpler
 *  PrintUtilities.printComponent(componentToBePrinted).
 *
 *  7/99 Marty Hall, http://www.apl.jhu.edu/~hall/java/
 *  May be freely used or adapted.
 */

public class PrintUtilities implements Printable {
  private Component componentToBePrinted;
  private PageFormat pf=null;
	public static double A4_SIRINA_PX=595.2756d;
	public static double A4_VISINA_PX=841.8898d;

  public static void printComponent(Component c) {
    new PrintUtilities(c).print();
  }

  public static void printComponentLandscape(Component c) {
    new PrintUtilities(c).printLandscape();
  }

  public static void printBarcode(Component c) {
    new PrintUtilities(c).printBarcode();
  }


  public static void printComponent300DPI(Component c) {
    new PrintUtilities(c).printComponent300DPI();
  }


  public static void printCard(Component card)
  {
   new PrintUtilities(card).printCard();
  }

  public PrintUtilities(Component componentToBePrinted) {
    this.componentToBePrinted = componentToBePrinted;
  }

  public void printCard(PageFormat pf)
  {
   PrinterJob printJob = PrinterJob.getPrinterJob();

      printJob.printDialog();

      printJob.setPrintable(this,pf);

     if (true)
       try {
         printJob.print();
       } catch(PrinterException pe) {
         System.out.println("Error printing (pc/pf): " + pe);
       }

  }//printCard

  public void printCard()
  {
   PrinterJob printJob = PrinterJob.getPrinterJob();

       pf=printJob.defaultPage();
       //PrintService ps=printJob.getPrintService();
       //DocPrintJob dpj=ps.createPrintJob();
       PrintRequestAttributeSet atributi;

       //pf.setOrientation(PageFormat.LANDSCAPE);

       PrinterResolution ps = new PrinterResolution(72 , 72,
       ResolutionSyntax.DPI);

       atributi = new HashPrintRequestAttributeSet();

       atributi.add(ps);
       atributi.add(javax.print.attribute.standard.PrintQuality.HIGH);

       //atributi.add(Fidelity.FIDELITY_TRUE);

       Paper papir = new Paper();
       //double marginaL = 450, marginaT=205;
       double marginaL = 0, marginaT=0;

       papir.setSize(1240.0d*2, 1754.0d*2); // A4 @ 150 dpi
       //papir.setSize(900, 1260); // 300 DPI za papir
       papir.setImageableArea(marginaL, marginaT,
       papir.getWidth() - marginaL * 2,
       papir.getHeight() - marginaT * 2);

       pf.setPaper(papir);

       printJob.setPrintable(this,pf);

      if (printJob.printDialog())
        try {
         printJob.setJobName("Ispis loyalty kartice");
          printJob.print(atributi);
        } catch(PrinterException pe) {
          System.out.println("Error printing: " + pe);
        }
  }//printCard

  public void printComponent300DPI()
  {
   PrinterJob printJob = PrinterJob.getPrinterJob();

       pf=printJob.defaultPage();
       //PrintService ps=printJob.getPrintService();
       //DocPrintJob dpj=ps.createPrintJob();
       PrintRequestAttributeSet atributi;

       PrinterResolution ps = new PrinterResolution(300, 300,
       ResolutionSyntax.DPI);

       atributi = new HashPrintRequestAttributeSet();
       atributi.add(ps);
       atributi.add(javax.print.attribute.standard.PrintQuality.HIGH);
       atributi.add(javax.print.attribute.standard.OrientationRequested.PORTRAIT);

       printJob.setJobName("Grafika");
       boolean rez=printJob.printDialog();
       
       if (!rez) return;
       
       Paper papir = new Paper();
       double marginaL = 00, marginaT=00;

       papir.setSize(A4_SIRINA_PX, A4_VISINA_PX); // A4 @ 150 dpi
       //papir.setSize(900, 1260); // 300 DPI za papir
       papir.setImageableArea(marginaL, marginaT,
       papir.getWidth() - marginaL * 2,
       papir.getHeight() - marginaT * 2);

       pf.setPaper(papir);
       

       printJob.setPrintable(this,pf);

      if (rez)
        try {
          printJob.print(atributi);
        } catch(PrinterException pe) {
          System.out.println("Error printing: " + pe);
        }
  }//printComponent300DPI


  public void print() {
    PrinterJob printJob = PrinterJob.getPrinterJob();
    //pf.setOrientation(PageFormat.LANDSCAPE);

    if (this.pf!=null)
    printJob.setPrintable(this,pf);
    else
    printJob.setPrintable(this);


    if (printJob.printDialog()) //
      try {
        printJob.print();
      } catch(PrinterException pe) {
        System.out.println("Error printing: " + pe);
      }
  }//print

  public void printBarcode()
  {
   PrinterJob printJob = PrinterJob.getPrinterJob();
   //pf.setOrientation(PageFormat.LANDSCAPE);

    PageFormat pf=printJob.defaultPage();

         if (pf==null)
          pf=new PageFormat();

         Paper papir = new Paper();
         double marginaL = 27, marginaT=0;

         papir.setSize(1240.0d*2, 1754.0d*2); // A4 @ 150 dpi
         //papir.setSize(900, 1260); // 300 DPI za papir
         papir.setImageableArea(marginaL, marginaT,
         papir.getWidth() - marginaL * 2,
         papir.getHeight() - marginaT * 2);

         if (pf!=null)
         pf.setPaper(papir);

         if (pf!=null)
         printJob.setPrintable(this,pf);
         else
         {
         System.out.println("Page Format je NULL!!!!");
         printJob.setPrintable(this);
         }

   if (printJob.printDialog())
     try {
       printJob.print();
     } catch(PrinterException pe) {
       System.out.println("Error printing: " + pe);
     }

  }//printBarcode

  public void printLandscape() {
   PrinterJob printJob = PrinterJob.getPrinterJob();

   if (this.pf==null) this.pf=printJob.defaultPage();

   pf.setOrientation(PageFormat.LANDSCAPE);

   printJob.setPrintable(this,pf);

   if (printJob.printDialog())
     try {
       printJob.print();
     } catch(PrinterException pe) {
       System.out.println("Error printing: " + pe);
     }
 }//print


  public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
    if (pageIndex > 0) {
      return(NO_SUCH_PAGE);
    } else {
      Graphics2D g2d = (Graphics2D)g;

      g2d.translate(pageFormat.getImageableX(),pageFormat.getImageableY());
      disableDoubleBuffering(componentToBePrinted);

      // nekakav sistem treba smislit kako da se posalju kodovi magnetskom encoderu...
      //g2d.drawString("<STX><ESC>E5<ACK><CAN>12345<CR>Kg<CR>12345 6<CR>7-09-02<CR>4218<ETB><FF><ETX>",0,9);

      componentToBePrinted.paint(g2d);
      enableDoubleBuffering(componentToBePrinted);
      return(PAGE_EXISTS);
    }
  }//print


  /** The speed and quality of printing suffers dramatically if
   *  any of the containers have double buffering turned on.
   *  So this turns if off globally.
   *  @see enableDoubleBuffering
   */
  public static void disableDoubleBuffering(Component c) {
    RepaintManager currentManager = RepaintManager.currentManager(c);
    currentManager.setDoubleBufferingEnabled(false);
  }

  /** Re-enables double buffering globally. */

  public static void enableDoubleBuffering(Component c) {
    RepaintManager currentManager = RepaintManager.currentManager(c);
    currentManager.setDoubleBufferingEnabled(true);
  }
}
