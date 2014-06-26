package biz.sunce.optika;

import javax.swing.JFrame;
import java.awt.GridBagLayout;
import javax.swing.JLabel;
import java.awt.*;
import javax.swing.JTextArea;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JScrollPane;

/**
 * <p>Title: Projekt Optièar</p>
 *
 * <p>Description: aplikacija za voðenje optièke radnje</p>
 *
 * <p>Copyright: Copyright (c) 2005</p>
 *
 *
 * @author Ante Sabo; Davor Staniæ
 * @version 0.8
 */
public final class PorukaSustavaFrame extends JFrame {
    GridBagLayout gridBagLayout1 = new GridBagLayout();
    JLabel jLabel1 = new JLabel();
    JLabel jLabel2 = new JLabel();
    JLabel jtRazina = new JLabel();
    JTextArea jtaPoruka = new JTextArea();
    JLabel jLabel3 = new JLabel();
    JTextArea jtaIznimka = new JTextArea();
    JButton jbOk = new JButton();
    JScrollPane jScrollPane1 = new JScrollPane();

    public PorukaSustavaFrame() {
        try {
            jbInit();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void jbInit() throws Exception {
        getContentPane().setLayout(gridBagLayout1);
        jLabel1.setText("Razina: ");
        jtaPoruka.setMinimumSize(new Dimension(250, 200));
        jtaPoruka.setPreferredSize(new Dimension(250, 200));
        jtaPoruka.setActionMap(null);
        jtaPoruka.setEditable(false);
        jtaPoruka.setText(" ");
        jtaPoruka.setColumns(33);
        jtaPoruka.setLineWrap(true);
        jtaPoruka.setRows(25);
        jtaPoruka.setWrapStyleWord(true);
        jLabel3.setText("Iznimka: ");
        jtaIznimka.setMinimumSize(new Dimension(250, 200));
        jtaIznimka.setPreferredSize(new Dimension(250, 200));
        jtaIznimka.setEditable(false);
        jtaIznimka.setText(" ");
        jtaIznimka.setColumns(25);
        jtaIznimka.setLineWrap(true);
        jtaIznimka.setRows(12);
        jtaIznimka.setWrapStyleWord(true);
        jbOk.setText("OK");
        jbOk.addActionListener(new PorukaSustavaFrame_jbOk_actionAdapter(this));
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setTitle("Poruka sustava");
        jScrollPane1.setMinimumSize(new Dimension(250, 200));
        jScrollPane1.setPreferredSize(new Dimension(250, 200));
        this.getContentPane().add(jLabel1,
                                  new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        jtRazina.setText("()");
        this.getContentPane().add(jLabel2,
                                  new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(jtRazina,
                                  new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(jtaPoruka,
                                  new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(jLabel3,
                                  new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(jbOk,
                                  new GridBagConstraints(2, 3, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));
        this.getContentPane().add(jScrollPane1,
                                  new GridBagConstraints(1, 2, 1, 1, 0.0, 0.0
                , GridBagConstraints.CENTER, GridBagConstraints.NONE,
                new Insets(0, 0, 0, 0), 0, 0));

        jScrollPane1.getViewport().add(jtaIznimka);
        jLabel2.setText("Poruka:");
        this.setSize(500,500);
    }

    public void jbOk_actionPerformed(ActionEvent e)
    {
        this.dispose();
    }

public void setPoruka(biz.sunce.opticar.vo.PorukaSustavaVO psvo)
{
this.jtRazina.setText(""+psvo.getRazina());
this.jtaIznimka.setText(psvo.getIznimka());
this.jtaPoruka.setText(psvo.getPoruka());
}

}//klasa



class PorukaSustavaFrame_jbOk_actionAdapter implements ActionListener {
    private PorukaSustavaFrame adaptee;
    PorukaSustavaFrame_jbOk_actionAdapter(PorukaSustavaFrame adaptee) {
        this.adaptee = adaptee;
    }

    public void actionPerformed(ActionEvent e) {
        adaptee.jbOk_actionPerformed(e);
    }
}
