/**
 * @author Coder ACJHP
 * @Email hexa.octabin@gmail.com
 * @Date 15/07/2017
 */
package com.coder.hms.usrinterface;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.border.BevelBorder;
import javax.swing.border.SoftBevelBorder;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.coder.hms.daoImpl.CustomerDaoImpl;
import com.coder.hms.daoImpl.ReservationDaoImpl;
import com.coder.hms.daoImpl.RoomDaoImpl;
import com.coder.hms.entities.Blockade;
import com.coder.hms.entities.Customer;
import com.coder.hms.entities.Reservation;
import com.coder.hms.entities.Room;
import com.coder.hms.utils.BlockadeTableCellRenderer;
import com.coder.hms.utils.BlockadeTableHeaderRenderer;
import com.coder.hms.utils.CustomTableHeaderRenderer;
import com.toedter.calendar.JDateChooser;

public class Main_Blockade extends JPanel implements ActionListener {

	/**
	 * 
	 */
	
	private List<Long> rezervationIdList;
	
	private RoomDaoImpl rImpl;
	private List<Room> roomList;
	
	private ReservationDaoImpl resDaoImpl;
	private List<Reservation> resList;
	
	private CustomerDaoImpl cImpl;
	private List<Customer> customerList;
	
	private String today = "";
	private final Calendar masterDate = Calendar.getInstance();
	private String[] weekDates = new String[10];
	
	private JDateChooser dateChooser;
	private JButton previousBtn, nextBtn;
	private JPanel leftSidePanel, buttonPanel;
	private static final long serialVersionUID = 1L;
	
	final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	private JTable table, blokajTable, blokajRoomsTable, blokajCustomerTable;
	private JSplitPane mainVerticalSplitter, leftCenterSplitter, centerRightSplitter;
	private JScrollPane generalScrollPane, blokajScrollPane, roomScrollPane, customerScrollPane;
	
	private final String[] bottomTableHeader = new String[10];
	private DefaultTableModel model = new DefaultTableModel(bottomTableHeader, 0);
	
	private final String[] blokajColsName = {"REZERV. NO", "GROUP", "AGENCY", "CHECK/IN", "CHECK/OUT", "EARLY PAY"};
	private DefaultTableModel blokajModel = new DefaultTableModel(blokajColsName, 0);
	
	private final String[] blokajRoomsColsName = {"ROOM", "TYPE", "PERSON COUNT"};
	private DefaultTableModel blokajRoomsModel = new DefaultTableModel(blokajRoomsColsName, 0);
	
	private final String[] blokajCustomerColsName = {"FIRSTNAME", "LASTNAME"};
	private DefaultTableModel blokajCustomerModel = new DefaultTableModel(blokajCustomerColsName, 0);
	
	private final BlockadeTableCellRenderer cellRenderer = new BlockadeTableCellRenderer();
	private final CustomTableHeaderRenderer THR = new CustomTableHeaderRenderer();
	private final BlockadeTableHeaderRenderer THRC = new BlockadeTableHeaderRenderer();
	/**
	 * Create the frame.
	 */
	public Main_Blockade() {
						
		setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));

		this.setAutoscrolls(true);
		this.setMinimumSize(new Dimension(800, 600));
		/*make it default size of frame maximized */
		this.setMaximumSize(new Dimension(1000, 900));
		this.setLayout(new BorderLayout());
		
		mainVerticalSplitter = new JSplitPane();
		mainVerticalSplitter.setBorder(new SoftBevelBorder(BevelBorder.LOWERED, null, null, null, null));
		mainVerticalSplitter.setOneTouchExpandable(true);
		mainVerticalSplitter.setIgnoreRepaint(true);
		mainVerticalSplitter.setInheritsPopupMenu(true);
		mainVerticalSplitter.setAutoscrolls(true);
		mainVerticalSplitter.setDividerLocation(200);
		mainVerticalSplitter.setAlignmentY(Component.CENTER_ALIGNMENT);
		mainVerticalSplitter.setAlignmentX(Component.CENTER_ALIGNMENT);
		mainVerticalSplitter.setContinuousLayout(true);
		mainVerticalSplitter.setOrientation(JSplitPane.VERTICAL_SPLIT);
		mainVerticalSplitter.resetToPreferredSizes();
		setLayout(new BorderLayout(0, 0));
		add(mainVerticalSplitter, BorderLayout.CENTER);
		
		leftSidePanel = new JPanel();
		leftSidePanel.setAutoscrolls(true);
		leftSidePanel.setPreferredSize(new Dimension(10, 300));
		leftSidePanel.setBounds(0, 0, 10, 10);
		leftSidePanel.setLayout(new BorderLayout(0, 0));
		mainVerticalSplitter.setLeftComponent(leftSidePanel);
		
		leftCenterSplitter = new JSplitPane();
		leftCenterSplitter.setIgnoreRepaint(true);
		leftCenterSplitter.setInheritsPopupMenu(true);
		leftCenterSplitter.setOneTouchExpandable(true);
		leftCenterSplitter.setAlignmentX(Component.CENTER_ALIGNMENT);
		leftCenterSplitter.setContinuousLayout(true);
		leftCenterSplitter.setAutoscrolls(true);
		leftCenterSplitter.setDividerLocation(430);
		leftCenterSplitter.resetToPreferredSizes();
		leftSidePanel.add(leftCenterSplitter);
		
		blokajTable = new JTable(blokajModel);
		blokajTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		blokajTable.setGridColor(UIManager.getColor("InternalFrame.inactiveTitleForeground"));
		blokajTable.setRowSelectionAllowed(true);
		blokajTable.getTableHeader().setDefaultRenderer(THRC);
		blokajTable.addMouseListener(blokajMouseListener());
		blokajTable.setRowHeight(20);
		blokajTable.setBackground(UIManager.getColor("InternalFrame.borderColor"));
		
		blokajScrollPane = new JScrollPane();
		blokajScrollPane.setViewportView(blokajTable);
		leftCenterSplitter.setLeftComponent(blokajScrollPane);
		
		centerRightSplitter = new JSplitPane();
		centerRightSplitter.setOneTouchExpandable(true);
		centerRightSplitter.setInheritsPopupMenu(true);
		centerRightSplitter.setIgnoreRepaint(true);
		centerRightSplitter.setContinuousLayout(true);
		centerRightSplitter.setAutoscrolls(true);
		centerRightSplitter.setAlignmentX(Component.CENTER_ALIGNMENT);
		centerRightSplitter.resetToPreferredSizes();
		centerRightSplitter.setDividerLocation(430);
		leftCenterSplitter.setRightComponent(centerRightSplitter);
		
		blokajRoomsTable = new JTable(blokajRoomsModel);
		blokajRoomsTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		blokajRoomsTable.setGridColor(UIManager.getColor("InternalFrame.inactiveTitleForeground"));
		blokajRoomsTable.setColumnSelectionAllowed(false);
		blokajRoomsTable.setCellSelectionEnabled(false);
		blokajRoomsTable.setRowSelectionAllowed(true);
		blokajRoomsTable.getTableHeader().setDefaultRenderer(THRC);
		blokajRoomsTable.setRowHeight(20);
		blokajRoomsTable.setBackground(UIManager.getColor("InternalFrame.borderColor"));
		
		roomScrollPane = new JScrollPane();
		roomScrollPane.setViewportView(blokajRoomsTable);
		centerRightSplitter.setLeftComponent(roomScrollPane);
		
		blokajCustomerTable = new JTable(blokajCustomerModel);
		blokajCustomerTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
		blokajCustomerTable.setGridColor(UIManager.getColor("InternalFrame.inactiveTitleForeground"));
		blokajCustomerTable.setCellSelectionEnabled(false);
		blokajCustomerTable.setColumnSelectionAllowed(false);
		blokajCustomerTable.getTableHeader().setDefaultRenderer(THRC);
		blokajCustomerTable.setRowHeight(20);
		blokajCustomerTable.setBackground(UIManager.getColor("InternalFrame.borderColor"));
		
		customerScrollPane = new JScrollPane();
		customerScrollPane.setViewportView(blokajCustomerTable);
		centerRightSplitter.setRightComponent(customerScrollPane);
		
		cellRenderer.setHorizontalAlignment(SwingConstants.CENTER);
		THR.setHorizontalAlignment(SwingConstants.CENTER);

		table = new JTable(model);
		table.getTableHeader().setDefaultRenderer(THR);
		table.setDefaultRenderer(Object.class, cellRenderer);
		table.setGridColor(UIManager.getColor("InternalFrame.inactiveTitleForeground"));
		table.setColumnSelectionAllowed(true);
		table.setCellSelectionEnabled(true);
		table.setRowHeight(20);
		table.setFont(new Font("Dialog", Font.PLAIN, 14));
		table.setBackground(UIManager.getColor("InternalFrame.borderColor"));
		
		//populate table headers from this method.
		populateTableHeaders();

		generalScrollPane = new JScrollPane();
		generalScrollPane.setViewportView(table);
		
		mainVerticalSplitter.setRightComponent(generalScrollPane);
		
		final JPanel upperPanel = new JPanel();
		upperPanel.setBorder(new SoftBevelBorder(BevelBorder.RAISED, null, null, null, null));
		upperPanel.setAutoscrolls(true);
		upperPanel.setPreferredSize(new Dimension(300, 40));
		upperPanel.setBackground(Color.decode("#066d95"));
		upperPanel.setLayout(new BorderLayout());
		add(upperPanel, BorderLayout.NORTH);
		
		//make a panel to add date chooser and buttons with null layout.
		buttonPanel = new JPanel();
		buttonPanel.setBorder(null);
		buttonPanel.setAutoscrolls(true);
		buttonPanel.setPreferredSize(new Dimension(300, 40));
		buttonPanel.setBackground(Color.decode("#066d95"));
		buttonPanel.setLayout(null);
		upperPanel.add(buttonPanel, BorderLayout.WEST);
		
		dateChooser = new JDateChooser();
		dateChooser.setDate(new Date());
		dateChooser.setDateFormatString("yyyy-MM-dd");
		dateChooser.setBounds(55, 6, 164, 26);
		dateChooser.addPropertyChangeListener(customPropListener());
		buttonPanel.add(dateChooser);
		
		previousBtn = new JButton("");
		previousBtn.setMaximumSize(new Dimension(400, 29));
		previousBtn.setMinimumSize(new Dimension(400, 29));
		previousBtn.setAutoscrolls(true);
		previousBtn.addActionListener(this);
		previousBtn.setIcon(new ImageIcon(Main_Blockade.class.getResource("/com/coder/hms/icons/blockade_previous.png")));
		previousBtn.setBounds(6, 6, 49, 26);
		buttonPanel.add(previousBtn);
		
		nextBtn = new JButton("");
		nextBtn.addActionListener(this);
		nextBtn.setIcon(new ImageIcon(Main_Blockade.class.getResource("/com/coder/hms/icons/blockade_next.png.png")));
		nextBtn.setBounds(219, 6, 49, 26);
		buttonPanel.add(nextBtn);
		
		//add this label to upperPanel(main) to be centered.
		final JLabel lblBlockade = new JLabel("BLOCKADE");
		lblBlockade.setAutoscrolls(true);
		lblBlockade.setMinimumSize(new Dimension(70, 16));
		lblBlockade.setPreferredSize(new Dimension(70, 16));
		lblBlockade.setAlignmentX(Component.CENTER_ALIGNMENT);
		lblBlockade.setLabelFor(buttonPanel);
		lblBlockade.setLocation(571, 6);
		lblBlockade.setSize(159, 30);
		lblBlockade.setForeground(UIManager.getColor("Button.highlight"));
		lblBlockade.setHorizontalTextPosition(SwingConstants.CENTER);
		lblBlockade.setHorizontalAlignment(SwingConstants.CENTER);
		lblBlockade.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 25));
		upperPanel.add(lblBlockade, BorderLayout.CENTER);
		
		this.setVisible(true);
		
		//invoke this method at last
		getReadyForTables();
		populateBlokajTable(blokajModel);
		populateMainTable(model);
	}

	public synchronized void getReadyForTables() {
		rImpl = new RoomDaoImpl();		
		roomList = rImpl.getAllRooms();
		
		resDaoImpl = new ReservationDaoImpl();
		resList = resDaoImpl.getAllReservations();
		
		cImpl = new CustomerDaoImpl();
		customerList = cImpl.getAllCustomers();
	}
	
	public void populateTableHeaders() {
		
		final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		final Calendar c = Calendar.getInstance();
		c.setTime(masterDate.getTime());
			
		JTableHeader tableHeader = table.getTableHeader();
		TableColumnModel tableColumnModel = tableHeader.getColumnModel();
		TableColumn tableColumn;
		
		tableColumn = tableColumnModel.getColumn(0);
		tableColumn.setHeaderValue("ROOM");
		tableColumn = tableColumnModel.getColumn(1);
		tableColumn.setHeaderValue("TYPE");
		tableColumn = tableColumnModel.getColumn(2);
		tableColumn.setHeaderValue("STATUS");
		
		c.add(Calendar.DATE, -1);
		for(int i = 3; i < 10; i++) {
			c.add(Calendar.DATE, 1);
			today = sdf.format(c.getTime());
			tableColumn = tableColumnModel.getColumn(i);
			tableColumn.setHeaderValue(today);

			weekDates[i] = today;
		}
		
		tableHeader.revalidate();
		tableHeader.repaint();
		
	}

	public void populateMainTable(DefaultTableModel model) {
		
		model.setRowCount(0);
		/*Simple object POJO class (entity)*/
		Blockade blockade = null;
		
		for(int i=0; i < roomList.size(); i++) {
			
			blockade = new Blockade();
			blockade.setNumber(roomList.get(i).getNumber());
			blockade.setType(roomList.get(i).getType());
			blockade.setStatus(roomList.get(i).getUsageStatus());
			
			model.addRow(new Object[]{blockade.getNumber(), blockade.getType(), blockade.getStatus()});
			
			for (int j = 0; j < resList.size(); j++) {
				if(blockade.getNumber().equals(resList.get(j).getTheNumber()))
					for(int x=0; x < weekDates.length; x++) {
						if(resList.get(j).getCheckinDate().equals(weekDates[x])) {
							//add 3 to weekDates[x] because weekDates start from -3 in table;
							model.setValueAt(resList.get(j).getGroupName(), i, x);
						}
					}
			}			
		}	
	}
	
	public void populateBlokajTable(DefaultTableModel blokajModel) {
				
		String workingDate = sdf.format(dateChooser.getDate());
		
		final Reservation reservation = new Reservation();
		rezervationIdList = new ArrayList<>();
		
		blokajModel.setRowCount(0);
		
		for(int i=0; i < resList.size(); i++) {
			if(resList.get(i).getCheckinDate().equals(workingDate) && resList.get(i).getIsCheckedIn().equals("NO")) {
				reservation.setId(resList.get(i).getId());
				reservation.setGroupName(resList.get(i).getGroupName());
				reservation.setAgency(resList.get(i).getAgency());
				reservation.setPaymentStatus(resList.get(i).getPaymentStatus());
				reservation.setCheckinDate(resList.get(i).getCheckinDate());
				reservation.setCheckoutDate(resList.get(i).getCheckoutDate());

				blokajModel.addRow(new Object[]{reservation.getId(), reservation.getGroupName(),
						reservation.getAgency(), reservation.getCheckinDate(), reservation.getCheckoutDate(), 
						reservation.getPaymentStatus()});
				rezervationIdList.add(resList.get(i).getId());
			}
			
		}
	}

	public void populateBlokajRoomsModel(DefaultTableModel blokajRoomsModel, String reservId) {
		for (int index = 0; index < roomList.size(); index++) {
			if (roomList.get(index).getReservationId() == Long.parseLong(reservId)) {
				blokajRoomsModel.addRow(new Object[] { roomList.get(index).getNumber(), roomList.get(index).getType(),
						roomList.get(index).getPersonCount() });
				break;
			}
		}
	}

	public void populateBlokajCustomerModel(DefaultTableModel blokajCustomerModel, String reservId) {
		for (int k = 0; k < customerList.size(); k++) {
			if (customerList.get(k).getReservationId() == Long.parseLong(reservId)) {
				blokajCustomerModel.addRow(new Object[] { 
						customerList.get(k).getFirstName(), customerList.get(k).getLastName()});
			}
		}
	}

	private MouseListener blokajMouseListener() {
		final MouseAdapter adapter = new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				final int index = blokajTable.getSelectedRow();

				final String reservIdFromRow = blokajTable.getValueAt(index, 0).toString();
				blokajRoomsModel.setRowCount(0);
				populateBlokajRoomsModel(blokajRoomsModel, reservIdFromRow);
				blokajCustomerModel.setRowCount(0);
				populateBlokajCustomerModel(blokajCustomerModel, reservIdFromRow);
				super.mousePressed(e);
			}
		};
		return adapter;
	}

	//this listener for changing table headers when date chosen from two rows.
	@Override
	public void actionPerformed(ActionEvent e) {
		
		masterDate.setTime(dateChooser.getDate());
		
		if(e.getSource() == nextBtn) {
			masterDate.add(Calendar.DATE, 1);

		}
		else if(e.getSource() == previousBtn){
			masterDate.add(Calendar.DATE, -1);
			
		}
		
		dateChooser.setDate(masterDate.getTime());
		dateChooser.revalidate();
		dateChooser.repaint();
		
		populateTableHeaders();
		populateBlokajTable(blokajModel);
		populateMainTable(model);
		
	}
	
	//this listener for changing table headers when date chosen from date component.
	private PropertyChangeListener customPropListener() {
		final PropertyChangeListener propListener = new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if("date".equals(evt.getPropertyName())) {
					masterDate.setTime((Date) evt.getNewValue());
					populateTableHeaders();
					populateBlokajTable(blokajModel);
					populateMainTable(model);
				}
				
			}
		};
		return propListener;
	}
}
