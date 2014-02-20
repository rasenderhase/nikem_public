package de.nikem.tools;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class SocketUDP extends JFrame {
	private static final long serialVersionUID = 1L;
	
	DatagramSocket datagramReceiveSocket;
	DatagramSocket datagramSendSocket;
	DatagramPacket datagramSendPacket;
	byte[] sendData;
	ReceiveThread rt = null;
	private JButton jButtonClear;
	private JButton jButtonOeffnen;
	private JButton jButtonSenden;
	private JLabel jLabel7;
	private JLabel jLabel8;
	private JLabel jLabel9;
	private JLabel jLabelUrl;
	private JScrollPane jScrollPaneEmpfangen;
	private JScrollPane jScrollPaneGesendet;
	private JTextArea jTextAreaEmpfangen;
	private JTextArea jTextAreaGesendet;
	private JTextField jTextFieldLocalPort;
	private JTextField jTextFieldRemoteHost;
	private JTextField jTextFieldRemotePort;
	private JTextField jTextFieldSendetelegramm;

	public SocketUDP() {
		initComponents();
		setTitle("SocketUDP");
	}

	private void initComponents() {
		this.jScrollPaneEmpfangen = new JScrollPane();
		this.jTextAreaEmpfangen = new JTextArea();
		this.jLabelUrl = new JLabel();
		this.jLabel7 = new JLabel();
		this.jTextFieldLocalPort = new JTextField();
		this.jLabel8 = new JLabel();
		this.jButtonClear = new JButton();
		this.jTextFieldRemoteHost = new JTextField();
		this.jButtonSenden = new JButton();
		this.jScrollPaneGesendet = new JScrollPane();
		this.jTextAreaGesendet = new JTextArea();
		this.jTextFieldSendetelegramm = new JTextField();
		this.jButtonOeffnen = new JButton();
		this.jTextFieldRemotePort = new JTextField();
		this.jLabel9 = new JLabel();

		getContentPane().setLayout(null);

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent evt) {
				SocketUDP.this.exitForm(evt);
			}
		});
		this.jTextAreaEmpfangen.setEditable(false);
		this.jTextAreaEmpfangen.setToolTipText("empfangene Daten");
		this.jScrollPaneEmpfangen.setViewportView(this.jTextAreaEmpfangen);

		getContentPane().add(this.jScrollPaneEmpfangen);
		this.jScrollPaneEmpfangen.setBounds(10, 360, 620, 170);

		this.jLabelUrl.setFont(new Font("MS Sans Serif", 1, 14));
		this.jLabelUrl.setForeground(new Color(51, 51, 255));
		this.jLabelUrl.setText("SocketUDP");
		getContentPane().add(this.jLabelUrl);
		this.jLabelUrl.setBounds(10, 20, 610, 30);

		this.jLabel7.setText("lokaler Port");
		getContentPane().add(this.jLabel7);
		this.jLabel7.setBounds(10, 60, 70, 15);

		this.jTextFieldLocalPort
				.setToolTipText("local Port, auf dem empfangen wird.");
		getContentPane().add(this.jTextFieldLocalPort);
		this.jTextFieldLocalPort.setBounds(90, 60, 70, 21);

		this.jLabel8.setText("remote Host");
		getContentPane().add(this.jLabel8);
		this.jLabel8.setBounds(310, 130, 70, 15);

		this.jButtonClear.setText("Löschen");
		this.jButtonClear
				.setToolTipText("gesendete und empfangene Daten löschen");
		this.jButtonClear.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SocketUDP.this.jButtonClearActionPerformed(evt);
			}
		});
		getContentPane().add(this.jButtonClear);
		this.jButtonClear.setBounds(530, 545, 100, 20);

		this.jTextFieldRemoteHost
				.setToolTipText("Internet-Adresse, an die die Daten geschickt werden");
		getContentPane().add(this.jTextFieldRemoteHost);
		this.jTextFieldRemoteHost.setBounds(390, 130, 120, 20);

		this.jButtonSenden.setText("Senden");
		this.jButtonSenden
				.setToolTipText("an Remote Port und Remote Host senden");
		this.jButtonSenden.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SocketUDP.this.jButtonSendenActionPerformed(evt);
			}
		});
		getContentPane().add(this.jButtonSenden);
		this.jButtonSenden.setBounds(520, 130, 100, 20);

		this.jTextAreaGesendet.setEditable(false);
		this.jTextAreaGesendet.setToolTipText("gesendete Daten");
		this.jScrollPaneGesendet.setViewportView(this.jTextAreaGesendet);

		getContentPane().add(this.jScrollPaneGesendet);
		this.jScrollPaneGesendet.setBounds(10, 170, 620, 160);

		this.jTextFieldSendetelegramm.setToolTipText("zu sendende Daten");
		getContentPane().add(this.jTextFieldSendetelegramm);
		this.jTextFieldSendetelegramm.setBounds(10, 100, 610, 21);

		this.jButtonOeffnen.setText("Öffnen");
		this.jButtonOeffnen.setToolTipText("lokalen Port öffnen");
		this.jButtonOeffnen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				SocketUDP.this.jButtonOeffnenActionPerformed(evt);
			}
		});
		getContentPane().add(this.jButtonOeffnen);
		this.jButtonOeffnen.setBounds(190, 60, 100, 20);

		this.jTextFieldRemotePort
				.setToolTipText("remote Port, an den die Daten geschickt werden.");
		getContentPane().add(this.jTextFieldRemotePort);
		this.jTextFieldRemotePort.setBounds(90, 130, 70, 21);

		this.jLabel9.setText("remote Port");
		getContentPane().add(this.jLabel9);
		this.jLabel9.setBounds(10, 130, 70, 15);

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		setBounds((screenSize.width - 649) / 2, (screenSize.height - 601) / 2,
				649, 601);
	}

	private void jButtonOeffnenActionPerformed(ActionEvent evt) {
		if (this.datagramReceiveSocket != null) {
			this.datagramReceiveSocket.close();
			this.datagramReceiveSocket = null;
			this.rt.beenden();
			this.rt = null;
			this.jButtonOeffnen.setText("Öffnen");
			this.jTextFieldLocalPort.setEditable(true);
		} else {
			try {
				this.datagramReceiveSocket = new DatagramSocket(
						Integer.parseInt(this.jTextFieldLocalPort.getText()));

				this.rt = new ReceiveThread("ReceiveThred", new Austausch(
						this.jTextAreaEmpfangen), this.datagramReceiveSocket);
				this.rt.start();

				this.jButtonOeffnen.setText("Schließen");

				this.jTextFieldLocalPort.setEditable(false);
			} catch (NumberFormatException ex) {
				this.jTextAreaGesendet.append("Fasches Format local Port. "
						+ ex.getMessage());
				this.jTextAreaGesendet.append("\n");
			} catch (IOException ex) {
				this.jTextAreaGesendet.append(ex.getMessage());
				this.jTextAreaGesendet.append("\n");
			} catch (IllegalArgumentException ex) {
				this.jTextAreaGesendet.append("Fasches Format local Port. "
						+ ex.getMessage());
				this.jTextAreaGesendet.append("\n");
			}
		}
	}

	private void jButtonClearActionPerformed(ActionEvent evt) {
		this.jTextAreaEmpfangen.setText(null);
		this.jTextAreaGesendet.setText(null);
	}

	private void jButtonSendenActionPerformed(ActionEvent evt) {
		try {
			if (this.datagramReceiveSocket == null) {
				this.datagramReceiveSocket = new DatagramSocket();
			}
			this.sendData = this.jTextFieldSendetelegramm.getText().getBytes();
			this.datagramSendPacket = new DatagramPacket(this.sendData,
					this.jTextFieldSendetelegramm.getText().length(),
					InetAddress.getByName(this.jTextFieldRemoteHost.getText()),
					Integer.parseInt(this.jTextFieldRemotePort.getText()));

			this.datagramReceiveSocket.send(this.datagramSendPacket);
			this.jTextAreaGesendet.append(this.jTextFieldSendetelegramm
					.getText());
			this.jTextAreaGesendet.append("\n");
		} catch (NumberFormatException ex) {
			this.jTextAreaGesendet
					.append("Falsches Format remote Host oder remote Port. "
							+ ex.getMessage());
			this.jTextAreaGesendet.append("\n");
		} catch (IOException ex) {
			this.jTextAreaGesendet.append("Fehler beim Senden. "
					+ ex.getMessage());
			this.jTextAreaGesendet.append("\n");
		} catch (IllegalArgumentException ex) {
			this.jTextAreaGesendet.append("Fasches Format remote Port. "
					+ ex.getMessage());
			this.jTextAreaGesendet.append("\n");
		}
	}

	private void exitForm(WindowEvent evt) {
		System.exit(0);
	}

	public static void main(String[] args) {
		SocketUDP socketUDP = new SocketUDP();
		socketUDP.setVisible(true);
	}
}