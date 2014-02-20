package de.nikem.tools;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ReceiveThread extends Thread {
	public final int BUF_SIZE = 1000;
	private Austausch austauschobjekt;
	private DatagramSocket datagramSocket;
	private DatagramPacket datagramReceivePacket;
	private String receiveString;
	private boolean gestartet;
	byte[] receiveData;

	public synchronized void beenden() {
		this.gestartet = false;
	}

	public ReceiveThread(String name, Austausch austauschobjekt,
			DatagramSocket datagramSocket) {
		super(name);
		this.austauschobjekt = austauschobjekt;
		this.datagramSocket = datagramSocket;
	}

	public void run() {
		this.receiveData = new byte[1000];
		this.datagramReceivePacket = new DatagramPacket(this.receiveData,
				this.receiveData.length);
		this.gestartet = true;

		while (this.gestartet) {
			try {
				this.datagramSocket.receive(this.datagramReceivePacket);
				this.receiveString = new String(this.receiveData, 0, this.datagramReceivePacket.getLength());
				this.austauschobjekt.appendText(this.receiveString);
			} catch (IOException ex) {
			}

			try {
				sleep(100L);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
	}
}