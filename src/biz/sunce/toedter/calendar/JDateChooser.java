// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 21.03.2009 13:10:57
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) fieldsfirst 
// Source File Name:   JDateChooser.java

package biz.sunce.toedter.calendar;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.MenuElement;
import javax.swing.MenuSelectionManager;
import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.toedter.calendar.DatumskoPolje;
import com.toedter.calendar.SlusacDateChoosera;


// Referenced classes of package com.toedter.calendar:
//            JTextFieldDateEditor, JCalendar, IDateEditor, JDayChooser, 
//            JMonthChooser

public final class JDateChooser extends JPanel implements ActionListener,
		PropertyChangeListener, DatumskoPolje {
	private static final long serialVersionUID = 0xc43c8c6c931ad5feL;
	
	protected com.toedter.calendar.IDateEditor dateEditor;
	protected JButton calendarButton;
	protected JCalendar jcalendar;
	protected JPopupMenu popup = new JPopupMenu() {

		private static final long serialVersionUID = 0xaba5a34f791318dfL;

		@Override
		public void setVisible(boolean b) {
			Boolean isCanceled = (Boolean) getClientProperty("JPopupMenu.firePopupMenuCanceled");
			if (b || !b && dateSelected || isCanceled != null && !b
					&& isCanceled.booleanValue())
				super.setVisible(b);
		}
	};
	
	protected boolean isInitialized;
	protected boolean dateSelected;
	protected Date lastSelectedDate;
	
	private ChangeListener changeListener = new ChangeListener() {

		boolean hasListened;

		public void stateChanged(ChangeEvent e) {
			if (hasListened) {
				hasListened = false;
				return;
			}
			if (popup.isVisible()
					&& jcalendar.getMonthChooser().getComboBox().hasFocus()) {
				MenuElement me[] = MenuSelectionManager.defaultManager()
						.getSelectedPath();
				MenuElement newMe[] = new MenuElement[me.length + 1];
				newMe[0] = popup;
				for (int i = 0; i < me.length; i++)
					newMe[i + 1] = me[i];

				hasListened = true;
				MenuSelectionManager.defaultManager().setSelectedPath(newMe);
			}
			hasListened = false;
		}
	};
	private ArrayList slusaci;

	public void dodajSlusaca(SlusacDateChoosera slusac) {
		slusaci.add(slusac);
	}

	public JDateChooser() {
		this(null, null, null, null);
	}

	public JDateChooser(com.toedter.calendar.IDateEditor dateEditor) {
		this(null, null, null, dateEditor);
	}

	public JDateChooser(Date date) {
		this(date, null);
	}

	public JDateChooser(Date date, String dateFormatString) {
		this(date, dateFormatString, ((com.toedter.calendar.IDateEditor) (null)));
	}

	public JDateChooser(Date date, String dateFormatString,
			com.toedter.calendar.IDateEditor dateEditor) {
		this(null, date, dateFormatString, dateEditor);
	}

	public JDateChooser(String datePattern, String maskPattern, char placeholder) {
		this(null, null, datePattern, ((new JTextFieldDateEditor(datePattern,
				maskPattern, placeholder))));
	}

	public JDateChooser(JCalendar jcal, Date date, String dateFormatString,
			com.toedter.calendar.IDateEditor dateEditor) {
		slusaci = new ArrayList(1);
		setName("JDateChooser");
		this.dateEditor = dateEditor;
		if (this.dateEditor == null)
			this.dateEditor =  new JTextFieldDateEditor();
		this.dateEditor.addPropertyChangeListener("date", this);
		if (jcal == null) {
			jcalendar = new JCalendar(date);
		} else {
			jcalendar = jcal;
			if (date != null)
				jcalendar.setDate(date);
		}
		setLayout(new BorderLayout());
		jcalendar.getDayChooser().addPropertyChangeListener("day", this);
		jcalendar.getDayChooser().setAlwaysFireDayProperty(true);
		setDateFormatString(dateFormatString);
		setDate(date);
		java.net.URL iconURL = getClass().getResource(
				"/com/toedter/calendar/images/JDateChooserIcon.gif");
		ImageIcon icon = new ImageIcon(iconURL);
		calendarButton = new JButton(icon) {

			private static final long serialVersionUID = 0xe570ee583159f29cL;

			@Override
			public boolean isFocusable() {
				return false;
			}

		};

		calendarButton.setMargin(new Insets(0, 0, 0, 0));
		calendarButton.addActionListener(this);
		calendarButton.setMnemonic(67);
		add(calendarButton, "East");
		add(this.dateEditor.getUiComponent(), "Center");
		calendarButton.setMargin(new Insets(0, 0, 0, 0));
		popup.setLightWeightPopupEnabled(true);
		popup.add(jcalendar);
		lastSelectedDate = date;
		MenuSelectionManager.defaultManager().addChangeListener(changeListener);
		isInitialized = true;
	}

	public void actionPerformed(ActionEvent e) {
		int x = calendarButton.getWidth()
				- (int) popup.getPreferredSize().getWidth();
		int y = calendarButton.getY() + calendarButton.getHeight();
		Calendar calendar = Calendar.getInstance();
		Date date = dateEditor.getDate();
		if (date != null)
			calendar.setTime(date);
		jcalendar.setCalendar(calendar);
		popup.show(calendarButton, x, y);
		dateSelected = false;
	}

	public void propertyChange(PropertyChangeEvent evt) {
		if (evt.getPropertyName().equals("day")) {
			if (popup.isVisible()) {
				dateSelected = true;
				popup.setVisible(false);
				setDate(jcalendar.getCalendar().getTime());
			}
		} else if (evt.getPropertyName().equals("date"))
			if (evt.getSource() == dateEditor)
				firePropertyChange("date", evt.getOldValue(), evt.getNewValue());
			else
				setDate((Date) evt.getNewValue());
	}

	@Override
	public void updateUI() {
		super.updateUI();
		setEnabled(isEnabled());
		if (jcalendar != null)
			SwingUtilities.updateComponentTreeUI(popup);
	}

	@Override
	public void setLocale(Locale l) {
		super.setLocale(l);
		dateEditor.setLocale(l);
		jcalendar.setLocale(l);
	}

	public String getDateFormatString() {
		return dateEditor.getDateFormatString();
	}

	public void setDateFormatString(String dfString) {
		dateEditor.setDateFormatString(dfString);
		invalidate();
	}

	public Date getDate() {
		return dateEditor.getDate();
	}

	public void setDate(Date date) {
		dateEditor.setDate(date);
		setDateFormatString("dd.MM.yyyy");
		if (getParent() != null)
			getParent().invalidate();
		obavijestiSlusaceOPromjeniDatuma();
	}

	private void obavijestiSlusaceOPromjeniDatuma() {
		if (slusaci != null) {
			for (int i = 0; i < slusaci.size(); i++) {
				SlusacDateChoosera sl = (SlusacDateChoosera) slusaci.get(i);
				if (sl != null)
					sl.datumIzmjenjen(this);
			}

		}
	}

	public void setDatum(Calendar c) {
		Date d = new Date();
		if (c == null) {
			setDate(null);
			return;
		} else {
			d.setTime(c.getTimeInMillis());
			setDate(d);
			return;
		}
	}

	public Calendar getDatum() {
		if (getDate() == null) {
			return null;
		} else {
			Calendar c = Calendar.getInstance();
			c.setTime(getDate());
			return c;
		}
	}

	public Calendar getCalendar() {
		Date date = getDate();
		if (date == null) {
			return null;
		} else {
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			return calendar;
		}
	}

	public void setCalendar(Calendar calendar) {
		if (calendar == null)
			dateEditor.setDate(null);
		else
			dateEditor.setDate(calendar.getTime());
	}

	@Override
	public void setEnabled(boolean enabled) {
		super.setEnabled(enabled);
		if (dateEditor != null) {
			dateEditor.setEnabled(enabled);
			calendarButton.setEnabled(enabled);
		}
	}

	@Override
	public boolean isEnabled() {
		return super.isEnabled();
	}

	public void setIcon(ImageIcon icon) {
		calendarButton.setIcon(icon);
	}

	@Override
	public void setFont(Font font) {
		if (isInitialized) {
			dateEditor.getUiComponent().setFont(font);
			jcalendar.setFont(font);
		}
		super.setFont(font);
	}

	public JCalendar getJCalendar() {
		return jcalendar;
	}

	public JButton getCalendarButton() {
		return calendarButton;
	}

	public com.toedter.calendar.IDateEditor getDateEditor() {
		return dateEditor;
	}

	public void setSelectableDateRange(Date min, Date max) {
		jcalendar.setSelectableDateRange(min, max);
		dateEditor.setSelectableDateRange(jcalendar.getMinSelectableDate(),
				jcalendar.getMaxSelectableDate());
	}

	public void setMaxSelectableDate(Date max) {
		jcalendar.setMaxSelectableDate(max);
		dateEditor.setMaxSelectableDate(max);
	}

	public void setMinSelectableDate(Date min) {
		jcalendar.setMinSelectableDate(min);
		dateEditor.setMinSelectableDate(min);
	}

	public Date getMaxSelectableDate() {
		return jcalendar.getMaxSelectableDate();
	}

	public Date getMinSelectableDate() {
		return jcalendar.getMinSelectableDate();
	}

	public void cleanup() {
		MenuSelectionManager.defaultManager().removeChangeListener(
				changeListener);
		changeListener = null;
	}

	public static void main(String s[]) {
		JFrame frame = new JFrame("JDateChooser");
		JDateChooser dateChooser = new JDateChooser();
		frame.getContentPane().add(dateChooser);
		frame.pack();
		frame.setVisible(true);
	}
}