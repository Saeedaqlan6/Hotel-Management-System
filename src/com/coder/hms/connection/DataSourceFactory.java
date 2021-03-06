/**
 * @author Coder ACJHP
 * @Email hexa.octabin@gmail.com
 * @Date 15/07/2017
 */
package com.coder.hms.connection;

import java.awt.Toolkit;
import java.util.logging.Logger;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import com.coder.hms.entities.Reservation;

public class DataSourceFactory {

	final static JDialog dialog = new JDialog();
	private static SessionFactory sessionFactory = null;
	private static final Logger LOGGER = Logger.getLogger(DataSourceFactory.class.getName());

	public DataSourceFactory() {

		dialog.setAlwaysOnTop(true);
	}

	public static synchronized void createConnection() {
		try {

			if (sessionFactory == null) {
				sessionFactory = new Configuration().configure("com/coder/hms/connection/hibernate.cfg.xml")
						.addAnnotatedClass(Reservation.class).buildSessionFactory();
				LOGGER.info("Session created successfully.");
			}

		} catch (Exception e) {
			Toolkit.getDefaultToolkit().beep();
			JOptionPane.showMessageDialog(dialog, "\tFatal error!\nCannot connect to the database,"
					+ "\nplease check your internet or datasource connection.\n(Close the *LOGIN* window at first.)",
					JOptionPane.MESSAGE_PROPERTY, JOptionPane.WARNING_MESSAGE);
			System.exit(1);
		}
	}

	public SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	public void shutDown() {
		LOGGER.info("Closing SessionFactory...");
		if(sessionFactory.isOpen())
			DataSourceFactory.sessionFactory.close();
	}
}
