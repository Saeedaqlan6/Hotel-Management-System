/**
 * @author Coder ACJHP
 * @Email hexa.octabin@gmail.com
 * @Date 15/07/2017
 */
package com.coder.hms.ui.external;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import javax.swing.DefaultComboBoxModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import com.coder.hms.daoImpl.CustomerDaoImpl;
import com.coder.hms.daoImpl.HotelDaoImpl;
import com.coder.hms.daoImpl.ReservationDaoImpl;
import com.coder.hms.daoImpl.RoomDaoImpl;
import com.coder.hms.entities.Customer;
import com.coder.hms.entities.Hotel;
import com.coder.hms.entities.Reservation;
import com.coder.hms.entities.Room;
import com.coder.hms.entities.RoomCountRow;
import com.coder.hms.entities.RoomInfoRow;
import com.coder.hms.utils.ApplicationLogoSetter;
import com.coder.hms.utils.RoomNumberMaker;
import com.toedter.calendar.JDateChooser;

public class NewReservationWindow extends JDialog {

	/**
	 * 
	 */
	private int value = 0;
	private Date startDate;
	private Date endDate;
	private JButton btnAddRoom;
	private Object[] ROOM_NUMS;
	private String[] ROOM_TYPES;
	private JTable table, ePaymentTable;
	private double priceValue = 0.0;
	private NumberFormat formatter;
	private JSpinner personCountSpinner; 
	private final JTextArea noteTextArea;
	private JFormattedTextField priceField;
	private boolean completionStatus = false;
	private JButton chancelBtn, SaveBtn, reportBtn;
	private JDateChooser checkinDate, checkoutDate;
	private static final long serialVersionUID = 1L;
	private final PaymentWindow payWin = new PaymentWindow();
	
	final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	
	private final RoomNumberMaker roomNumberMaker = new RoomNumberMaker();
	private ApplicationLogoSetter logoSetter = new ApplicationLogoSetter();
	private JLabel agencyLbl, creditTypeLbl, customerCountryLbl;
	private JTextField rezIdField, nameSurnameField, totalDaysField;
	
	private final RoomDaoImpl roomDaoImpl = new RoomDaoImpl();
	private final CustomerDaoImpl cImpl = new CustomerDaoImpl();
	private final ReservationDaoImpl rImpl = new ReservationDaoImpl();
	
	private final JTable roomCountTable;
	private final String[] ROOMCOUNT_TABLE_HEADERS = {"ROOM NUM.", "TYPE", "PERSON COUNT", "PRICE", "CURRENCY"};
	private final DefaultTableModel roomCountModel = new DefaultTableModel(ROOMCOUNT_TABLE_HEADERS, 0);
	
	private final String[] INFO_TAB_TABLE_HEADERS = {"ROOM", "TYPE", "PERSON NAME", "SURNAME"};
	private final DefaultTableModel model = new DefaultTableModel(INFO_TAB_TABLE_HEADERS, 0);
	
	private final String[] HOST_TYPES = {"B.B", "F.B", "H.B", "O.B"};
	private final String[] RESERV_STS = {"GUARANTEE", "WAITLIST", "CANCEL"};
	private final String LOGOPATH = "/com/coder/hms/icons/main_logo(128X12).png";
	private final String[] cmbList = { "TURKISH LIRA", "DOLLAR", "EURO", "POUND"};
	private final String[] CREDIT_TYPES = {"BLACK LIST", "INFINITY CREDIT", "STANDART CUSTOMER CREDIT"};
	
	private final String[] EARLY_PAYMENT_LIST = {"TITLE", "NAME", "AMOUNT", "CURRENCY", "EXCHANGE", "NOTE"};
	private final DefaultTableModel earlyPaymetModel = new DefaultTableModel(EARLY_PAYMENT_LIST, 0);
	
	private final String[] AGENCY_LIST = {"WALKIN", "WEB", "OTHER"};
	private JComboBox<String> agencyCmbBox, hostCmbBox, creaditTypeCmbBox, rezervStatusCmbBox, customerCountryCmbBox, 
	currencyCmbBox, roomTypeCmbBox;
	private JComboBox<Object> roomNumCmbBox;
	private final String[] COUNTRY_LIST = {"Afghanistan", "Albania","Algeria", "American Samoa", "Andorra",
		"Angola", "Anguilla", "Antarctica", "Antigua and Barbuda", "Argentina", "Armenia", "Aruba", "Australia", "Austria",
		 "Brazil", "Egypt", "Finland", "France", "Germany", "Hong Kong", "India", "Iran", "Iraq", "Ireland", "Israel", "Islands",
		 "Italy", "Jamaica", "Japan", "Republic of Korea", "Kuwait", "Lebanon", "Malaysia", "Mexico", "Nigeria", "Poland",
		 "Portugal", "Puerto Rico,PR", "Qatar", "Romania", "Russian Federation", "Saudi Arabia", "Singapore", "Spain", "Sweden",
		 "Switzerland", "Syrian Arab Republic", "Thailand", "Tunisia", "Turkey", "Turkmenistan", "Ukraine", "United Arab Emirates",
		 "United Kingdom", "United States", "Uruguay", "Uzbekistan", "Virgin Islands U.S.", "Yemen", "Zambia", "Zimbabwe"};

	/**
	 * Create the dialog.
	 */
	public NewReservationWindow() {
		// set upper icon for dialog frame
		logoSetter.setApplicationLogoJDialog(this, LOGOPATH);

		getContentPane().setForeground(new Color(255, 99, 71));
		getContentPane().setFocusCycleRoot(true);
		getContentPane().setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
		getContentPane().setFont(new Font("Verdana", Font.BOLD, 12));
		setModalExclusionType(ModalExclusionType.APPLICATION_EXCLUDE);
		setModal(true);
		setResizable(false);

		this.setTitle("Coder for HMS - [New Reservation]");

		/* Set default size of frame */
		this.setSize(780, 550);
		this.setLocationRelativeTo(null);
		this.getContentPane().setBackground(Color.decode("#066d95"));
		getContentPane().setLayout(null);
		
		JLabel rezIdLbl = new JLabel("Reservation Id : ");
		rezIdLbl.setForeground(new Color(255, 255, 255));
		rezIdLbl.setFont(new Font("Verdana", Font.BOLD, 12));
		rezIdLbl.setBounds(10, 11, 132, 23);
		getContentPane().add(rezIdLbl);
		
		rezIdField = new JTextField();
		rezIdField.setEnabled(false);
		rezIdField.setEditable(false);
		rezIdField.setColumns(10);
		rezIdField.setBounds(152, 12, 136, 20);
		getContentPane().add(rezIdField);
		
		JLabel nameSurnameLbl = new JLabel("Name  & Surname : ");
		nameSurnameLbl.setForeground(new Color(255, 255, 255));
		nameSurnameLbl.setFont(new Font("Verdana", Font.BOLD, 12));
		nameSurnameLbl.setBounds(10, 43, 136, 23);
		getContentPane().add(nameSurnameLbl);
		
		nameSurnameField = new JTextField();
		nameSurnameField.setBounds(152, 43, 209, 20);
		getContentPane().add(nameSurnameField);
		nameSurnameField.setColumns(10);
		
		JLabel checkinLbl = new JLabel("Checkin : ");
		checkinLbl.setForeground(new Color(255, 255, 255));
		checkinLbl.setFont(new Font("Verdana", Font.BOLD, 12));
		checkinLbl.setBounds(10, 75, 90, 23);
		getContentPane().add(checkinLbl);
		
		checkinDate = new JDateChooser();
		checkinDate.setCalendar(Calendar.getInstance());
		checkinDate.setDateFormatString("yyyy-MM-dd");
		checkinDate.setBounds(152, 75, 209, 20);
		checkinDate.addPropertyChangeListener(chechkDates());
		getContentPane().add(checkinDate);
		
		JLabel lblCheckout = new JLabel("Checkout : ");
		lblCheckout.setForeground(new Color(255, 255, 255));
		lblCheckout.setFont(new Font("Verdana", Font.BOLD, 12));
		lblCheckout.setBounds(10, 106, 90, 23);
		getContentPane().add(lblCheckout);
		
		checkoutDate = new JDateChooser();
		checkinDate.setCalendar(Calendar.getInstance());
		checkoutDate.setDateFormatString("yyyy-MM-dd");
		checkoutDate.setBounds(152, 106, 209, 20);
		checkoutDate.addPropertyChangeListener(chechkDates());
		getContentPane().add(checkoutDate);
		
		JLabel totalDaysLbl = new JLabel("Total days : ");
		totalDaysLbl.setForeground(new Color(255, 255, 255));
		totalDaysLbl.setFont(new Font("Verdana", Font.BOLD, 12));
		totalDaysLbl.setBounds(10, 136, 90, 23);
		getContentPane().add(totalDaysLbl);
		
		totalDaysField = new JTextField();
		totalDaysField.setColumns(10);
		totalDaysField.setEnabled(true);
		totalDaysField.setEditable(false);
		totalDaysField.setText(value + "");
		totalDaysField.setBounds(152, 137, 136, 20);
		getContentPane().add(totalDaysField);
		
		agencyLbl = new JLabel("Agency : ");
		agencyLbl.setForeground(new Color(255, 255, 255));
		agencyLbl.setFont(new Font("Verdana", Font.BOLD, 12));
		agencyLbl.setBounds(384, 11, 90, 23);
		getContentPane().add(agencyLbl);
		
		agencyCmbBox = new JComboBox<String>(new DefaultComboBoxModel<>(AGENCY_LIST));
		agencyCmbBox.setBounds(539, 11, 209, 20);
		getContentPane().add(agencyCmbBox);
		
		JLabel hostTypeLbl = new JLabel("Host type : ");
		hostTypeLbl.setForeground(new Color(255, 255, 255));
		hostTypeLbl.setFont(new Font("Verdana", Font.BOLD, 12));
		hostTypeLbl.setBounds(384, 44, 90, 23);
		getContentPane().add(hostTypeLbl);
		
		hostCmbBox = new JComboBox<String>(new DefaultComboBoxModel<>(HOST_TYPES));
		hostCmbBox.setSelectedIndex(0);
		hostCmbBox.setBounds(539, 42, 209, 20);
		getContentPane().add(hostCmbBox);
		
		creditTypeLbl = new JLabel("Credit type : ");
		creditTypeLbl.setForeground(new Color(255, 255, 255));
		creditTypeLbl.setFont(new Font("Verdana", Font.BOLD, 12));
		creditTypeLbl.setBounds(384, 73, 90, 23);
		getContentPane().add(creditTypeLbl);
		
		creaditTypeCmbBox = new JComboBox<String>(new DefaultComboBoxModel<>(CREDIT_TYPES));
		creaditTypeCmbBox.setSelectedIndex(2);
		creaditTypeCmbBox.setBounds(539, 73, 209, 20);
		getContentPane().add(creaditTypeCmbBox);
		
		JLabel reservationNoteLbl = new JLabel("Reservation Note : ");
		reservationNoteLbl.setForeground(new Color(255, 255, 255));
		reservationNoteLbl.setFont(new Font("Verdana", Font.BOLD, 12));
		reservationNoteLbl.setBounds(10, 191, 151, 23);
		getContentPane().add(reservationNoteLbl);
		
		noteTextArea = new JTextArea();
		noteTextArea.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));
		noteTextArea.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		noteTextArea.setBackground(SystemColor.info);
		noteTextArea.setLineWrap(true);
		noteTextArea.setBounds(162, 170, 491, 64);
		getContentPane().add(noteTextArea);
		
		JLabel reservationStatusLbl = new JLabel("Reservation Status : ");
		reservationStatusLbl.setForeground(new Color(255, 255, 255));
		reservationStatusLbl.setFont(new Font("Verdana", Font.BOLD, 12));
		reservationStatusLbl.setBounds(384, 106, 146, 23);
		getContentPane().add(reservationStatusLbl);
		
		rezervStatusCmbBox = new JComboBox<String>(new DefaultComboBoxModel<>(RESERV_STS));
		rezervStatusCmbBox.setSelectedIndex(0);
		rezervStatusCmbBox.setBounds(540, 107, 209, 20);
		getContentPane().add(rezervStatusCmbBox);
		
		customerCountryLbl = new JLabel("Customer Country : ");
		customerCountryLbl.setForeground(new Color(255, 255, 255));
		customerCountryLbl.setFont(new Font("Verdana", Font.BOLD, 12));
		customerCountryLbl.setBounds(384, 136, 146, 23);
		getContentPane().add(customerCountryLbl);
		
		customerCountryCmbBox = new JComboBox<String>(new DefaultComboBoxModel<>(COUNTRY_LIST));
		customerCountryCmbBox.setBounds(539, 137, 209, 20);
		customerCountryCmbBox.setSelectedIndex(0);
		getContentPane().add(customerCountryCmbBox);
		
		final JPanel buttonsPanel = new JPanel();
		buttonsPanel.setBounds(341, 463, 423, 49);
		buttonsPanel.setForeground(new Color(95, 158, 160));
		buttonsPanel.setBackground(Color.decode("#066d95"));
		getContentPane().add(buttonsPanel);
		buttonsPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		chancelBtn = new JButton("CANCEL");
		chancelBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
				setCompletionStatus(false);
			}
		});
		chancelBtn.setIcon(new ImageIcon(NewReservationWindow.class.getResource("/com/coder/hms/icons/login_clear.png")));
		chancelBtn.setForeground(new Color(220, 20, 60));
		chancelBtn.setOpaque(true);
		chancelBtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		chancelBtn.setPreferredSize(new Dimension(110, 40));
		chancelBtn.setFont(new Font("Verdana", Font.BOLD, 15));
		buttonsPanel.add(chancelBtn);

		
		reportBtn = new JButton("REPORT");
		reportBtn.setIcon(new ImageIcon(NewReservationWindow.class.getResource("/com/coder/hms/icons/rezaerv_report.png")));
		reportBtn.setForeground(new Color(0, 128, 128));
		reportBtn.setOpaque(true);
		reportBtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		reportBtn.setPreferredSize(new Dimension(110, 40));
		reportBtn.setFont(new Font("Verdana", Font.BOLD, 15));
		reportBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				
			}
		});
		buttonsPanel.add(reportBtn);
		
		SaveBtn = new JButton("SAVE");
		SaveBtn.setMaximumSize(new Dimension(120, 23));
		SaveBtn.setMinimumSize(new Dimension(120, 23));
		SaveBtn.setMnemonic(KeyEvent.VK_ENTER);
		SaveBtn.setToolTipText("Press ALT + ENTER keys for shortcut");
		SaveBtn.setIcon(new ImageIcon(NewReservationWindow.class.getResource("/com/coder/hms/icons/reserv_save.png")));
		SaveBtn.setForeground(new Color(0, 191, 255));
		SaveBtn.setOpaque(true);
		SaveBtn.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		SaveBtn.setPreferredSize(new Dimension(130, 40));
		SaveBtn.setFont(new Font("Verdana", Font.BOLD, 15));
		SaveBtn.addActionListener(newReservAction());
		buttonsPanel.add(SaveBtn);
		
		final JPanel panel = new JPanel();
		panel.setBounds(10, 245, 754, 205);
		getContentPane().add(panel);
		panel.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		panel.setBackground(SystemColor.activeCaption);
		panel.setAutoscrolls(true);
		panel.setLayout(new BorderLayout(0, 0));
		
		
		final JPanel roomPanel = new JPanel();
		final JPanel infoPanel = new JPanel();
		final JPanel earlyPanel = new JPanel();
		
		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.addTab("RoomEx Type", roomPanel);
		
		final JPanel roomTypePanel = new JPanel();
		roomTypePanel.setAutoscrolls(true);
		roomTypePanel.setPreferredSize(new Dimension(745, 65));
		roomTypePanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		roomTypePanel.setBackground(SystemColor.activeCaption);
		roomPanel.add(roomTypePanel);
		roomTypePanel.setLayout(null);
		
		final JScrollPane roomCountPanelContainer = new JScrollPane();
		roomCountPanelContainer.setPreferredSize(new Dimension(720, 75));
		roomCountTable = new JTable(roomCountModel);
        roomCountTable.setCellSelectionEnabled(false);
        roomCountTable.setRowSelectionAllowed(true);
		roomCountPanelContainer.setViewportView(roomCountTable);
		roomPanel.add(roomCountPanelContainer);
		
		final JLabel lblNewLabel = new JLabel("Room Type");
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setBounds(172, 12, 89, 14);
		roomTypePanel.add(lblNewLabel);
		
		
		final JLabel lblPersonCount = new JLabel("Person");
		lblPersonCount.setHorizontalAlignment(SwingConstants.CENTER);
		lblPersonCount.setBounds(318, 12, 61, 14);
		roomTypePanel.add(lblPersonCount);
		
		final JLabel lblCurrency = new JLabel("Currency");
		lblCurrency.setHorizontalAlignment(SwingConstants.CENTER);
		lblCurrency.setBounds(498, 12, 86, 14);
		roomTypePanel.add(lblCurrency);
		
		final HotelDaoImpl hotelDaoImpl = new HotelDaoImpl();
		final Hotel hotel = hotelDaoImpl.getHotel();
		ROOM_TYPES = hotel.getRoomTypes().split(" ");
		
		roomTypeCmbBox = new JComboBox<String>(new DefaultComboBoxModel<>(ROOM_TYPES));
		roomTypeCmbBox.setBounds(154, 35, 140, 20);
		roomTypeCmbBox.addActionListener(roomTypeActionListener());
		roomTypePanel.add(roomTypeCmbBox);
		
		personCountSpinner = new JSpinner();
		personCountSpinner.setModel(new SpinnerNumberModel(0, 0, 10, 1));
		personCountSpinner.setBounds(327, 35, 40, 20);
		roomTypePanel.add(personCountSpinner);
		
		currencyCmbBox = new JComboBox<String>(new DefaultComboBoxModel<>(cmbList));
		currencyCmbBox.setBounds(490, 35, 140, 20);
		currencyCmbBox.addItemListener(currencyActionListener());
		roomTypePanel.add(currencyCmbBox);
		
		final JLabel lblPrice = new JLabel("Price");
		lblPrice.setHorizontalAlignment(SwingConstants.CENTER);
		lblPrice.setBounds(408, 12, 46, 14);
		roomTypePanel.add(lblPrice);
		
		formatter = NumberFormat.getCurrencyInstance();
		formatter.setMinimumFractionDigits(2);
		
		priceField = new JFormattedTextField(formatter);
		priceField.setValue(new Double(priceValue));
		priceField.setColumns(10);
		priceField.setBounds(401, 35, 65, 20);
		roomTypePanel.add(priceField);
		
		JLabel roomLbl = new JLabel("Room");
		roomLbl.setHorizontalAlignment(SwingConstants.CENTER);
		roomLbl.setBounds(41, 11, 61, 16);
		roomTypePanel.add(roomLbl);
		
		///////////////////////////////////////////////////////////////
		//Populate this comboBox with special method that return just//
		//empty rooms, all 'BLOCKED' & 'FULL' rooms removed because  // 
		//when we create a new reservation we will see all rooms this//
		//is a big mistake, we have to choose rooms that not blocked //
		//or not full                                                //
		ROOM_NUMS = new String[]{};
		ROOM_NUMS = roomNumberMaker.getNotReservedRooms(sdf.format(new Date()));
		
		roomNumCmbBox = new JComboBox<Object>(new DefaultComboBoxModel<>(ROOM_NUMS));
		roomNumCmbBox.setBounds(34, 35, 86, 20);
		roomNumCmbBox.addActionListener(privateItemListener());
		roomTypePanel.add(roomNumCmbBox);
		
		btnAddRoom = new JButton("Add Room");
		btnAddRoom.setBounds(636, 17, 95, 29);
		btnAddRoom.addActionListener(addRoomActionListener());
		roomTypePanel.add(btnAddRoom);
		
		tabbedPane.addTab("Informations", infoPanel);
		infoPanel.setLayout(new BorderLayout(0, 0));
		
		JScrollPane scrollPane = new JScrollPane();
		infoPanel.add(scrollPane, BorderLayout.CENTER);
		
		
		table = new JTable(model);
		scrollPane.setViewportView(table);
		
		tabbedPane.addTab("Early Payment", earlyPanel);
		earlyPanel.setLayout(null);
		
		JButton btnNewButton = new JButton("Add new Payment");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				SwingUtilities.invokeLater(new Runnable() {
					
					@Override
					public void run() {
						payWin.setReadyPaymentWindow(roomNumCmbBox.getSelectedItem().toString());
						
					}
				}); 
			}
		});
		btnNewButton.setIcon(new ImageIcon(NewReservationWindow.class.getResource("/com/coder/hms/icons/newReserv_payment.png")));
		btnNewButton.setSelectedIcon(new ImageIcon(NewReservationWindow.class.getResource("/com/coder/hms/icons/newReserv_payment.png")));
		btnNewButton.setHorizontalAlignment(SwingConstants.RIGHT);
		btnNewButton.setBounds(10, 11, 150, 35);
		earlyPanel.add(btnNewButton);
		
		final JPanel ePaymentTableHolder = new JPanel();
		ePaymentTableHolder.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		ePaymentTableHolder.setBounds(10, 57, 723, 103);
		earlyPanel.add(ePaymentTableHolder);
		ePaymentTableHolder.setLayout(new BorderLayout(0, 0));
		
		final JScrollPane ePaymentScrollPane = new JScrollPane();
		ePaymentTableHolder.add(ePaymentScrollPane, BorderLayout.CENTER);
		
		ePaymentTable = new JTable(earlyPaymetModel);
		ePaymentScrollPane.setViewportView(ePaymentTable);
		panel.add(tabbedPane, BorderLayout.CENTER);
		
		if(payWin.getTableRowData() != null) {
			earlyPaymetModel.setRowCount(0);
			earlyPaymetModel.addRow(payWin.getTableRowData());
		}
	}
	
	private PropertyChangeListener chechkDates() {
		final PropertyChangeListener listener = new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {

				// here leave ancestors empty because we need dates not ancestor
				if (evt.getPropertyName().equals("ancestor")) {
					return;
				}
				// main reason
				else {
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							boolean showed = false;
							 startDate = checkinDate.getDate();
							 endDate = checkoutDate.getDate();

							if (startDate != null && endDate != null) {
								// add to calendar to be able get day of date
								// and compare
								Calendar cs = Calendar.getInstance();
								cs.setTime(startDate);
								Calendar ce = Calendar.getInstance();
								ce.setTime(endDate);

								// compare if start date greater than end date
								if (cs.after(ce) && !showed) {
									JOptionPane.showMessageDialog(null, "Start date is after end date!",
											JOptionPane.MESSAGE_PROPERTY, JOptionPane.WARNING_MESSAGE);
									showed = true;
								}
								// or both is same date
								else if (cs.get(Calendar.DAY_OF_YEAR) == ce.get(Calendar.DAY_OF_YEAR) && !showed) {
									JOptionPane.showMessageDialog(null,
											"Start date equals end date!\nPlease be sure you're choose right date.",
											JOptionPane.MESSAGE_PROPERTY, JOptionPane.WARNING_MESSAGE);
									showed = true;
								}
								// other odds
								else {
									value = (int) ((startDate.getTime() - endDate.getTime()) / (1000 * 60 * 60 * 24));
									totalDaysField.setText(Math.abs(value) + "");
									repaint();
								}
							}

						}
					});
				}
			}
		};
		return listener;
	}
	
	private ActionListener newReservAction() {
		final ActionListener theListener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				final Reservation reservation = new Reservation();
				
				reservation.setTheNumber(roomNumCmbBox.getSelectedItem().toString());
				reservation.setGroupName(nameSurnameField.getText());
				reservation.setCheckinDate(sdf.format(startDate));
				reservation.setCheckoutDate(sdf.format(endDate));
				reservation.setTotalDays(Integer.parseInt(totalDaysField.getText()));
				reservation.setAgency(agencyCmbBox.getSelectedItem().toString());
				reservation.setHostType(hostCmbBox.getSelectedItem().toString());
				reservation.setCreditType(creaditTypeCmbBox.getSelectedItem().toString());
				reservation.setNote(noteTextArea.getText());
				reservation.setBookStatus(rezervStatusCmbBox.getSelectedItem().toString());
				reservation.setIsCheckedIn("NO");				
				
				if(payWin.getPaymentStatus()) {
					reservation.setPaymentStatus(true);
				}
				rImpl.saveReservation(reservation);
								
				
				final Room theRoom = roomDaoImpl.getRoomByRoomNumber(roomNumCmbBox.getSelectedItem().toString());
				theRoom.setNumber(roomNumCmbBox.getSelectedItem().toString());
				theRoom.setCurrency(currencyCmbBox.getSelectedItem().toString());
				theRoom.setPersonCount((int)personCountSpinner.getValue());
				theRoom.setPrice(priceValue);
				theRoom.setType(roomTypeCmbBox.getSelectedItem().toString());
				theRoom.setCustomerGrupName(nameSurnameField.getText());
				theRoom.setUsageStatus("BLOCKED");
				
				final Reservation lastReserv = rImpl.getLastReservation();
				theRoom.setReservationId(lastReserv.getId());
				
				double lastPrice = theRoom.getPrice() * reservation.getTotalDays();
				theRoom.setTotalPrice(String.valueOf(lastPrice));
				
				lastPrice = lastPrice - Double.valueOf(theRoom.getBalance());
				theRoom.setRemainingDebt(lastPrice);
				
				roomDaoImpl.saveRoom(theRoom);
				
				System.out.println("ROOM SAVED");
				
				final int rowCount = model.getRowCount();
					
				Customer theCustomer = null;
				
				for(int i=0; i < rowCount; i++) {
		
					theCustomer = new Customer();
					
					theCustomer.setFirstName(model.getValueAt(i, 2).toString());
					theCustomer.setLastName(model.getValueAt(i, 3).toString());
					theCustomer.setCountry(customerCountryCmbBox.getSelectedItem().toString());
					theCustomer.setReservationId(lastReserv.getId());
					cImpl.save(theCustomer);
					completionStatus = true;
				
				}
				dispose();
			}
		};
		return theListener;
	}
	
	public ItemListener currencyActionListener() {
		ItemListener ac = new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent event) {

				final String choosed = currencyCmbBox.getSelectedItem().toString();
				NumberFormatter nf = null;
				DefaultFormatterFactory dfc = null;
				priceField.removeAll();
				
					switch (choosed) {
					case "TURKISH LIRA":
						
						formatter.setCurrency(Currency.getInstance(Locale.getDefault()));
						nf = new NumberFormatter(formatter);
						dfc = new DefaultFormatterFactory(nf);
						priceField.setFormatterFactory(dfc);
						priceField.revalidate();
						priceField.repaint();
						break;
					case "DOLLAR":
						formatter.setCurrency(Currency.getInstance(Locale.US));
						nf = new NumberFormatter(formatter);
						dfc = new DefaultFormatterFactory(nf);
						priceField.setFormatterFactory(dfc);
						priceField.revalidate();
						priceField.repaint();
						break;
					case "EURO":
						formatter.setCurrency(Currency.getInstance(Locale.FRANCE));
						nf = new NumberFormatter(formatter);
						dfc = new DefaultFormatterFactory(nf);
						priceField.setFormatterFactory(dfc);
						priceField.revalidate();
						priceField.repaint();
						break;
					case "POUND":
						formatter.setCurrency(Currency.getInstance(Locale.UK));
						nf = new NumberFormatter(formatter);
						dfc = new DefaultFormatterFactory(nf);
						priceField.setFormatterFactory(dfc);
						priceField.revalidate();
						priceField.repaint();
						break;
					default:
						break;
					}
					repaint();
			}

		};
		return ac;
	}
	
	public ActionListener roomTypeActionListener() {
		ActionListener rta = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				final String TYPE = roomTypeCmbBox.getSelectedItem().toString();
				
				switch (TYPE) {
				case "SINGLE":
					personCountSpinner.setValue(1);
					break;
				case "DOUBLE":
					personCountSpinner.setValue(2);
					break;
				case "TWIN":
					personCountSpinner.setValue(2);
					break;
				case "TRIPLE":
					personCountSpinner.setValue(3);
					break;
				default:
					break;
				}
				repaint();
			}
		};
		return rta;
	}
	
	public ActionListener addRoomActionListener(){
		ActionListener acl = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				final String roomNumber = roomNumCmbBox.getSelectedItem().toString();
				final String roomType = roomTypeCmbBox.getSelectedItem().toString();
				final String currency = currencyCmbBox.getSelectedItem().toString();
				final int personCount = (int)personCountSpinner.getValue();
				final String val = priceField.getValue().toString();
				priceValue = Double.valueOf(val);

				Object[] row = new Object[]{roomNumber, roomType, personCount, val, currency};
				roomCountModel.addRow(row);
				
				for(int i=0; i < personCount; i++) {
					model.addRow(new Object[]{roomNumber, roomType});
				}
			}
		};
		return acl;
	}
	
	private ActionListener privateItemListener() {
		ActionListener listener = new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				final String roomNumber = roomNumCmbBox.getSelectedItem().toString();
				if(!roomNumber.isEmpty()) {
					Room theRoom = roomDaoImpl.getRoomByRoomNumber(roomNumber);
					roomTypeCmbBox.setSelectedItem(theRoom.getType());
				}
				repaint();
				
			}
		};
		return listener;
	}
	
	public long getRezIdText() {
		return Long.parseLong(this.rezIdField.getText());
	}
	
	public void setRezIdField(long reservationId) {
		this.rezIdField.setText(String.valueOf(reservationId));
	}
	
	public String getNameSurnameField() {
		return this.nameSurnameField.getText();
	}
	
	public void setNameSurnameField(String NameSurname) {
		this.nameSurnameField.setText(NameSurname);
	}
	
	public int getTotalDaysField() {
		return Integer.parseInt(totalDaysField.getText());
	}
	
	public void setTotalDaysField(int totalDays) {
		this.totalDaysField.setText(totalDays + "");
	}
	
	public String getCheckinDate() {
		return sdf.format(checkinDate.getDate());
	}
	
	public void setCheckinDate(String theCheckinDate) {
		LocalDate localDate = LocalDate.parse(theCheckinDate);
		final Date date = java.sql.Date.valueOf(localDate);
		this.checkinDate.setDate(date);
	}
	
	public String getCheckoutDate() {
		return sdf.format(checkoutDate.getDate());
	}
	
	public void setCheckoutDate(String theCheckoutDate) {
		final LocalDate localDate = LocalDate.parse(theCheckoutDate);
		final Date date = java.sql.Date.valueOf(localDate);
		this.checkoutDate.setDate(date);
	}
	
	public String getAgency() {
		return this.agencyCmbBox.getSelectedItem().toString();
	}
	
	public void setAgency(String theAgency) {
		for(int index=0; index < agencyCmbBox.getItemCount(); index++) {
			if(agencyCmbBox.getItemAt(index).equalsIgnoreCase(theAgency)) {
				this.agencyCmbBox.setSelectedItem(theAgency);
				break;
			}
		}
				
	}
	
	public String getHostType() {
		return this.hostCmbBox.getSelectedItem().toString();
	}
	
	public void setHostType(String theHostType) {
		for(int index=0; index < hostCmbBox.getItemCount(); index++) {
			if(hostCmbBox.getItemAt(index).equalsIgnoreCase(theHostType)) {
				this.hostCmbBox.setSelectedItem(theHostType);
				break;
			}
		}
	}
	
	public String getCreditType() {
		return this.creaditTypeCmbBox.getSelectedItem().toString();
	}
	
	public void setCreditType(String theCreditType) {
		for(int index=0; index < creaditTypeCmbBox.getItemCount(); index++) {
			if(creaditTypeCmbBox.getItemAt(index).equalsIgnoreCase(theCreditType)) {
				this.creaditTypeCmbBox.setSelectedItem(theCreditType);
				break;
			}
		}
	}
	
	
	public String getReservStatus() {
		return this.rezervStatusCmbBox.getSelectedItem().toString();
	}
	
	public void setReservStatus(String theReservStatus) {
		for(int index=0; index < rezervStatusCmbBox.getItemCount(); index++) {
			if(rezervStatusCmbBox.getItemAt(index).equalsIgnoreCase(theReservStatus)) {
				this.rezervStatusCmbBox.setSelectedItem(theReservStatus);
				break;
			}
		}
	}
	
	public String getCustomerCountry() {
		return this.customerCountryCmbBox.getSelectedItem().toString();
	}
	
	public void setCustomerCountry(String theCustomerCountry) {
		for(int index=0; index < customerCountryCmbBox.getItemCount(); index++) {
			if(customerCountryCmbBox.getItemAt(index).equalsIgnoreCase(theCustomerCountry)) {
				this.customerCountryCmbBox.setSelectedItem(theCustomerCountry);
				break;
			}
		}
	}
	
	public String getReservNote() {
		return this.noteTextArea.getText();
	}
	
	public void setReservNote(String theNote) {
		this.noteTextArea.setText(theNote);
	}
	
	
	public String getRoomNumber() {
		return this.roomNumCmbBox.getSelectedItem().toString();
	}
	
	public void setRoomNumber(String theRoomNumber) {
		for(int index=0; index < roomNumCmbBox.getItemCount(); index++) {
			if(roomNumCmbBox.getItemAt(index).toString().equalsIgnoreCase(theRoomNumber)) {
				this.roomNumCmbBox.setSelectedItem(theRoomNumber);
				roomNumCmbBox.revalidate();
				roomNumCmbBox.repaint();
				break;
			}
		}
	}
	
	
	public String getRoomType() {
		return this.roomTypeCmbBox.getSelectedItem().toString();
	}
	
	public void setRoomType(String theRoomType) {
		for(int index=0; index < roomTypeCmbBox.getItemCount(); index++) {
			if(roomTypeCmbBox.getItemAt(index).toString().equalsIgnoreCase(theRoomType)) {
				this.roomTypeCmbBox.setSelectedItem(theRoomType);
				break;
			}
		}
	}
	
	public String getCurrency() {
		return this.currencyCmbBox.getSelectedItem().toString();
	}
	
	public void setCurrency(String theCurrency) {
		for(int index=0; index < currencyCmbBox.getItemCount(); index++) {
			if(currencyCmbBox.getItemAt(index).toString().contains(theCurrency)) {
				this.currencyCmbBox.setSelectedIndex(index);
				break;
			}
		}
	}
	
	public int getPersonCountSpinner() {
		return (int) this.personCountSpinner.getValue();
	}
	
	public void setPersonCountSpinner(int thePersonCount) {
		this.personCountSpinner.setValue(thePersonCount);
	}
	
	public double getPriceOfRoom() {
		return Double.parseDouble(this.priceField.getValue().toString());
	}
	
	public void setPriceOfRoom(double thePrice) {
		this.priceField.setValue(thePrice);
	}
	
	public Object[] getRoomCountTableRows() {
		RoomCountRow rc = null;
		for(int index=0; index < roomCountModel.getRowCount(); index++) {
			rc = new RoomCountRow();
			for(int i=0; i < 5; i++) {
				rc.setRoomNumber(roomCountModel.getValueAt(index, i));
				rc.setRoomType(roomCountModel.getValueAt(index, i));
				rc.setPersonCount(roomCountModel.getValueAt(index, i));
				rc.setPrice(roomCountModel.getValueAt(index, i));
				rc.setCurrency(roomCountModel.getValueAt(index, i));
			}
		}
		return new Object[]{rc.getRoomNumber(), rc.getRoomType(), rc.getPersonCount(), rc.getPrice(), rc.getCurrency()};
	}
	
	public void setRoomCountTableRows(Object[] tableRows) {
		this.roomCountModel.setRowCount(0);
		this.roomCountModel.addRow(tableRows);
	}
	
	public void setRoomInfoTableRows(Object[] tableRows) {
		this.model.setRowCount(0);
		this.model.addRow(tableRows);
	}

	public Object[] getRoomInfoTableRows() {
		RoomInfoRow rc = null;
		for(int index=0; index < model.getRowCount(); index++) {
			rc = new RoomInfoRow();
			for(int i=0; i < 5; i++) {
				rc.setRoomNumber(model.getValueAt(index, i));
				rc.setRoomType(model.getValueAt(index, i));
				rc.setCustomerName(model.getValueAt(index, i));
				rc.setCustomerSurname(model.getValueAt(index, i));
			}
		}
		return new Object[]{rc.getRoomNumber(), rc.getRoomType(),
				rc.getCustomerName(), rc.getCustomerSurname()};
	}
	

	
	public boolean getCompletionStatus() {
		return completionStatus;
	}

	public void setCompletionStatus(boolean completionStatus) {
		this.completionStatus = completionStatus;
	}
}



