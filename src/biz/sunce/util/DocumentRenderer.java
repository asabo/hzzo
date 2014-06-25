package biz.sunce.util;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.print.PrintService;
import javax.swing.JEditorPane;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;
import javax.swing.text.View;
import javax.swing.text.html.HTMLDocument;

import biz.sunce.optika.GlavniFrame;
import biz.sunce.optika.Logger;
import biz.sunce.util.beans.PostavkeBean;

public final class DocumentRenderer implements Printable {
	/*
	 * DocumentRenderer prints objects of type Document. Text attributes,
	 * including fonts, color, and small icons, will be rendered to a printed
	 * page. DocumentRenderer computes line breaks, paginates, and performs
	 * other formatting.
	 * 
	 * An HTMLDocument is printed by sending it as an argument to the
	 * print(HTMLDocument) method. A PlainDocument is printed the same way.
	 * Other types of documents must be sent in a JEditorPane as an argument to
	 * the print(JEditorPane) method. Printing Documents in this way will
	 * automatically display a print dialog.
	 * 
	 * As objects which implement the Printable Interface, instances of the
	 * DocumentRenderer class can also be used as the argument in the
	 * setPrintable method of the PrinterJob class. Instead of using the print()
	 * methods detailed above, a programmer may gain access to the formatting
	 * capabilities of this class without using its print dialog by creating an
	 * instance of DocumentRenderer and setting the document to be printed with
	 * the setDocument() or setJEditorPane(). The Document may then be printed
	 * by setting the instance of DocumentRenderer in any PrinterJob.
	 */
	protected int currentPage = -1; // Used to keep track of when
									// the page to print changes.

	protected JEditorPane jeditorPane; // Container to hold the
										// Document. This object will
										// be used to lay out the
										// Document for printing.

	protected double pageEndY = 0; // Location of the current page
									// end.

	protected double pageStartY = 0; // Location of the current page
										// start.

	protected boolean podesiSirinuDaUpadneUStranicu = true; // boolean to allow
															// control over
	// whether pages too wide to fit
	// on a page will be scaled.

	/*
	 * The DocumentRenderer class uses pFormat and pJob in its methods. Note
	 * that pFormat is not the variable name used by the print method of the
	 * DocumentRenderer. Although it would always be expected to reference the
	 * pFormat object, the print method gets its PageFormat as an argument.
	 */
	protected PageFormat pFormat;
	protected PrinterJob pJob;
	private BufferedImage zaglavlje, podnozje;
	private java.awt.Font fontPodnozje;
	private int visinaPodnozja = 0;
	public static double A4_SIRINA_PX = 595.2756d;
	public static double A4_VISINA_PX = 841.8898d;

	PostavkeBean p;

	/*
	 * The constructor initializes the pFormat and PJob variables.
	 */
	public DocumentRenderer() {
		pFormat = new PageFormat();
		Paper papir = new Paper();
		int marginaT = 40, marginaL = 28;

		papir.setSize(A4_SIRINA_PX, A4_VISINA_PX); // A4 @ 150 dpi
		papir.setImageableArea(marginaL, marginaT, papir.getWidth() - marginaL
				* 2, papir.getHeight() - marginaT * 2);

		pFormat.setPaper(papir);
		pJob = PrinterJob.getPrinterJob();

		fontPodnozje = new java.awt.Font("SansSerif", Font.ITALIC, 10);
		this.visinaPodnozja = fontPodnozje.getSize() + 3;
		p = new PostavkeBean();
	}

	public void setOrientationLandscape() {
		pFormat.setOrientation(PageFormat.LANDSCAPE);
	}

	/*
	 * Method to get the current Document
	 */
	public Document getDocument() {
		if (jeditorPane != null)
			return jeditorPane.getDocument();
		else
			return null;
	}

	/*
	 * Method to get the current choice the width scaling option.
	 */
	public boolean getScaleWidthToFit() {
		return podesiSirinuDaUpadneUStranicu;
	}

	public BufferedImage getPodnozje() {
		return podnozje;
	}

	public BufferedImage getZaglavlje() {
		return zaglavlje;
	}

	/*
	 * pageDialog() displays a page setup dialog.
	 */
	public void pageDialog() {
		pFormat = pJob.pageDialog(pFormat);
	}

	/*
	 * The print method implements the Printable interface. Although Printables
	 * may be called to render a page more than once, each page is painted in
	 * order. We may, therefore, keep track of changes in the page being
	 * rendered by setting the currentPage variable to equal the pageIndex, and
	 * then comparing these variables on subsequent calls to this method. When
	 * the two variables match, it means that the page is being rendered for the
	 * second or third time. When the currentPage differs from the pageIndex, a
	 * new page is being requested.
	 * 
	 * The highlights of the process used print a page are as follows:
	 * 
	 * I. The Graphics object is cast to a Graphics2D object to allow for
	 * scaling. II. The JEditorPane is laid out using the width of a printable
	 * page. This will handle line breaks. If the JEditorPane cannot be sized at
	 * the width of the graphics clip, scaling will be allowed. III. The root
	 * view of the JEditorPane is obtained. By examining this root view and all
	 * of its children, printView will be able to determine the location of each
	 * printable element of the document. IV. If the scaleWidthToFit option is
	 * chosen, a scaling ratio is determined, and the graphics2D object is
	 * scaled. V. The Graphics2D object is clipped to the size of the printable
	 * page. VI. currentPage is checked to see if this is a new page to render.
	 * If so, pageStartY and pageEndY are reset. VII. To match the coordinates
	 * of the printable clip of graphics2D and the allocation rectangle which
	 * will be used to lay out the views, graphics2D is translated to begin at
	 * the printable X and Y coordinates of the graphics clip. VIII. An
	 * allocation Rectangle is created to represent the layout of the Views.
	 * 
	 * The Printable Interface always prints the area indexed by reference to
	 * the Graphics object. For instance, with a standard 8.5 x 11 inch page
	 * with 1 inch margins the rectangle X = 72, Y = 72, Width = 468, and Height
	 * = 648, the area 72, 72, 468, 648 will be painted regardless of which page
	 * is actually being printed.
	 * 
	 * To align the allocation Rectangle with the graphics2D object two things
	 * are done. The first step is to translate the X and Y coordinates of the
	 * graphics2D object to begin at the X and Y coordinates of the printable
	 * clip, see step VII. Next, when printing other than the first page, the
	 * allocation rectangle must start laying out in coordinates represented by
	 * negative numbers. After page one, the beginning of the allocation is
	 * started at minus the page end of the prior page. This moves the part
	 * which has already been rendered to before the printable clip of the
	 * graphics2D object.
	 * 
	 * X. The printView method is called to paint the page. Its return value
	 * will indicate if a page has been rendered.
	 * 
	 * Although public, print should not ordinarily be called by programs other
	 * than PrinterJob.
	 */
	public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
		double scale = 1.0;
		Graphics2D g2d;
		View rootView;
	 	 
		int sirinaStranice = (int) pageFormat.getImageableWidth();
		int visinaStranice = (int) pageFormat.getImageableHeight();
		int pocetakStraniceX = (int) pageFormat.getImageableX();
		int pocetakStraniceY = (int) pageFormat.getImageableY();

		graphics.setClip(pocetakStraniceX, pocetakStraniceY, sirinaStranice,
				visinaStranice);

		int visinaZaglavlja = this.zaglavlje != null ? this.zaglavlje
				.getHeight() : 0;
		// pocetakStraniceY+=visinaZaglavlja;

		// I
		g2d = (Graphics2D) graphics;

		// jos dok nismo napravili nikakve transformacije sa graphics objektom
		// isprintati header i footer
		if (this.zaglavlje != null) {
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
					RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.setRenderingHint(RenderingHints.KEY_RENDERING,
					RenderingHints.VALUE_RENDER_QUALITY);

			g2d.drawImage(this.zaglavlje, pocetakStraniceX, pocetakStraniceY,
					this.zaglavlje.getWidth(), visinaZaglavlja, null);

			int maxSirina = 0;
			int tvrtkaPodaciX = 0, tvrtkaPodaciY = 0;

			g2d.setColor(Color.black);

			Font fontNaziv = new java.awt.Font("Serif", java.awt.Font.BOLD, 12);
			Font fontAdresa = new java.awt.Font("Serif", java.awt.Font.ITALIC,
					10);
			Font fontAdresaNormal = new java.awt.Font("Serif",
					java.awt.Font.PLAIN, 10);

			FontMetrics fm = g2d.getFontMetrics(fontNaziv);

			int sir = fm.stringWidth(p.getTvrtkaNaziv());
			String telFax = "Tel: "
					+ p.getTvrtkaTelefon()
					+ (p.getTvrtkaFax() != null && !p.getTvrtkaFax().equals("") ? " Fax: "
							+ p.getTvrtkaFax()
							: "");
			String adresa = p.getTvrtkaAdresa() + ", " + p.getMjestoRada();
			String mbZiro = "MB: " + p.getTvrtkaOIB() + " ZR:"
					+ p.getTvrtkaRacun() + " " + p.getTvrtkaBanka();
			maxSirina = sir;

			fm = g2d.getFontMetrics(fontAdresa);
			sir = fm.stringWidth(adresa);
			if (sir > maxSirina)
				maxSirina = sir;

			fm = g2d.getFontMetrics(fontAdresaNormal);
			sir = fm.stringWidth(mbZiro);
			if (sir > maxSirina)
				maxSirina = sir;
			sir = fm.stringWidth(telFax);
			if (sir > maxSirina)
				maxSirina = sir;

			tvrtkaPodaciX = pocetakStraniceX + sirinaStranice - maxSirina - 5;
			tvrtkaPodaciY = pocetakStraniceY + 10;

			g2d.setFont(fontNaziv);
			g2d.drawString(p.getTvrtkaNaziv(), tvrtkaPodaciX, tvrtkaPodaciY);

			g2d.setFont(fontAdresa);
			g2d.drawString(p.getTvrtkaAdresa() + "," + p.getMjestoRada(),
					tvrtkaPodaciX, tvrtkaPodaciY += fontNaziv.getSize2D());

			g2d.setFont(fontAdresaNormal);
			g2d.drawString(telFax, tvrtkaPodaciX,
					tvrtkaPodaciY += fontAdresaNormal.getSize2D());

			int zadY = tvrtkaPodaciY += fontAdresaNormal.getSize2D();
			g2d.drawString(mbZiro, tvrtkaPodaciX, zadY);

			String str = "stranica " + (pageIndex + 1) + ".";
			g2d.setFont(this.fontPodnozje);
			g2d.setColor(Color.black);
			int bottomY = (visinaStranice - this.visinaPodnozja)
					+ pocetakStraniceY;
			g2d.drawLine(pocetakStraniceX, bottomY, 250, bottomY);
			g2d.drawString(str, pocetakStraniceY + 4, bottomY + 7);
		}// if

		visinaZaglavlja += 15;

		// II
		jeditorPane.setSize(sirinaStranice, Integer.MAX_VALUE);

		// treba validirati iz razloga da se elementi posloze na formi kako bi
		// se mogli printati
		jeditorPane.validate();
		// III
		rootView = jeditorPane.getUI().getRootView(jeditorPane);
		// IV
		if ((podesiSirinuDaUpadneUStranicu)
				&& (jeditorPane.getMinimumSize().getWidth() > sirinaStranice)) {
			scale = sirinaStranice / jeditorPane.getMinimumSize().getWidth();
			g2d.scale(scale, scale);
		}

		// V
		g2d.setClip((int) (pocetakStraniceX / scale),
				(int) ((pocetakStraniceY + visinaZaglavlja) / scale),
				(int) (sirinaStranice / scale), (int) ((visinaStranice
						- visinaZaglavlja - this.visinaPodnozja) / scale));
		// VI
		if (pageIndex > currentPage) {
			currentPage = pageIndex;
			pageStartY += pageEndY;
			pageEndY = g2d.getClipBounds().getHeight();
		}
		// VII
		g2d.translate(g2d.getClipBounds().getX(), g2d.getClipBounds().getY());
		// VIII
		Rectangle allocation = new Rectangle(0, (int) -pageStartY,
				(int) (jeditorPane.getMinimumSize().getWidth()),
				(int) (jeditorPane.getPreferredSize().getHeight()));

		// X
		if (printView(g2d, allocation, rootView)) {

			return Printable.PAGE_EXISTS;
		} else {
			pageStartY = 0;
			pageEndY = 0;
			currentPage = -1;
			return Printable.NO_SUCH_PAGE;
		}
	}// print

	/*
	 * print(HTMLDocument) is called to set an HTMLDocument for printing.
	 */
	public void print(HTMLDocument htmlDocument) {
		print(htmlDocument, null);
	}

	public void print(HTMLDocument htmlDocument, String nazivPosla) {
		setDocument(htmlDocument);
		jeditorPane.revalidate();
		jeditorPane.setVisible(true);
		printDialog(nazivPosla);
	}

	/*
	 * print(JEditorPane) prints a Document contained within a JEDitorPane.
	 */
	public void print(JEditorPane jedPane) {
		setDocument(jedPane);
		printDialog(null);
	}

	/*
	 * print(PlainDocument) is called to set a PlainDocument for printing.
	 */
	public void print(PlainDocument plainDocument) {
		setDocument(plainDocument);
		printDialog(null);
	}

	/*
	 * A protected method, printDialog(), displays the print dialog and
	 * initiates printing in response to user input.
	 */
	protected void printDialog(String nazivPosla) {

		String printer = PostavkeBean.getPostavkaDB(
				GlavniFrame.ODABRANI_PRINTER, "");

		PrintService servis = null;

		if (!"".equals(printer)) {
			PrintService[] servisi = PrinterJob.lookupPrintServices();

			for (PrintService ps : servisi) {
				if (printer.equals(ps.getName())) {
					servis = ps;
					try {
						pJob.setPrintService(servis);
					} catch (PrinterException e) {
						Logger.warn(
								"Nismo uspjeli postaviti printer za ispis (DocumentRenderer): "
										+ printer, e);
					}

					break;
				}
			}
		}

		if (pJob.printDialog()) {
			PageFormat pf = this.pFormat;

			pJob.setPrintable(this, pf);
			pJob.setJobName(nazivPosla == null ? "Ispis" : nazivPosla);

			try {
				pJob.print();

				PrintService printService = pJob.getPrintService();
				String printerSvcName = printService == null ? null
						: printService.getName();

				if (printer != null && printerSvcName != null
						&& !printer.equals(printerSvcName))
					PostavkeBean.setPostavkaDB(GlavniFrame.ODABRANI_PRINTER,
							printerSvcName);

			} catch (PrinterException printerException) {
				pageStartY = 0;
				pageEndY = 0;
				currentPage = -1;
				Logger.fatal("Iznimka prilikom ispisa dokumenta: ",
						printerException);
			} catch (Error greska) {
				pageStartY = 0;
				pageEndY = 0;
				currentPage = -1;
				Logger.fatal("Greska sustava prilikom ispisa dokumenta: ",
						greska);
			}

		}
	}

	/*
	 * printView is a recursive method which iterates through the tree structure
	 * of the view sent to it. If the view sent to printView is a branch view,
	 * that is one with children, the method calls itself on each of these
	 * children. If the view is a leaf view, that is a view without children
	 * which represents an actual piece of text to be painted, printView
	 * attempts to render the view to the Graphics2D object.
	 * 
	 * I. When any view starts after the beginning of the current printable
	 * page, this means that there are pages to print and the method sets
	 * pageExists to true. II. When a leaf view is taller than the printable
	 * area of a page, it cannot, of course, be broken down to fit a single
	 * page. Such a View will be printed whenever it intersects with the
	 * Graphics2D clip. III. If a leaf view intersects the printable area of the
	 * graphics clip and fits vertically within the printable area, it will be
	 * rendered. IV. If a leaf view does not exceed the printable area of a page
	 * but does not fit vertically within the Graphics2D clip of the current
	 * page, the method records that this page should end at the start of the
	 * view. This information is stored in pageEndY.
	 */
	protected boolean printView(Graphics2D graphics2D, Shape allocation,
			View view) {
		boolean pageExists = false;
		Rectangle clipRectangle = graphics2D.getClipBounds();
		Shape childAllocation;
		View childView;

		if (view.getViewCount() > 0) {
			for (int i = 0; i < view.getViewCount(); i++) {
				childAllocation = view.getChildAllocation(i, allocation);
				if (childAllocation != null) {
					childView = view.getView(i);
					if (printView(graphics2D, childAllocation, childView)) {
						pageExists = true;
					}
				}
			}
		} else {
			// I
			if (allocation.getBounds().getMaxY() >= clipRectangle.getY()) {
				pageExists = true;
				// II
				if ((allocation.getBounds().getHeight() > clipRectangle
						.getHeight()) && (allocation.intersects(clipRectangle))) {
					view.paint(graphics2D, allocation);
				} else {
					// III
					if (allocation.getBounds().getY() >= clipRectangle.getY()) {
						if (allocation.getBounds().getMaxY() <= clipRectangle
								.getMaxY()) {
							view.paint(graphics2D, allocation);
						} else {
							// IV
							if (allocation.getBounds().getY() < pageEndY) {
								pageEndY = allocation.getBounds().getY();
							}
						}
					}
				}
			}
		}
		return pageExists;
	}

	/*
	 * Method to set the content type the JEditorPane.
	 */
	protected void setContentType(String type) {
		jeditorPane.setContentType(type);
	}

	/*
	 * Method to set an HTMLDocument as the Document to print.
	 */
	public void setDocument(HTMLDocument htmlDocument) {
		jeditorPane = new JEditorPane();

		setDocument("text/html", htmlDocument);
	}

	/*
	 * Method to set the Document to print as the one contained in a
	 * JEditorPane. This method is useful when Java does not provide direct
	 * access to a particular Document type, such as a Rich Text Format
	 * document. With this method such a document can be sent to the
	 * DocumentRenderer class enclosed in a JEditorPane.
	 */
	public void setDocument(JEditorPane jedPane) {
		jeditorPane = new JEditorPane();
		setDocument(jedPane.getContentType(), jedPane.getDocument());
	}

	/*
	 * Method to set a PlainDocument as the Document to print.
	 */
	public void setDocument(PlainDocument plainDocument) {
		jeditorPane = new JEditorPane();
		setDocument("text/plain", plainDocument);
	}

	/*
	 * Method to set the content type and document of the JEditorPane.
	 */
	protected void setDocument(String type, Document document) {
		setContentType(type);
		jeditorPane.setDocument(document);
	}

	/*
	 * Method to set the current choice of the width scaling option.
	 */
	public void setScaleWidthToFit(boolean scaleWidth) {
		podesiSirinuDaUpadneUStranicu = scaleWidth;
	}

	public void setPodnozje(BufferedImage podnozje) {
		this.podnozje = podnozje;
	}

	public void setZaglavlje(BufferedImage zaglavlje) {
		this.zaglavlje = zaglavlje;

	}
}// klasa
