package de.nikem.tools;

import javax.swing.JTextArea;

public class Austausch {
	private JTextArea jTextArea;

	public Austausch(JTextArea jTextArea) {
		this.jTextArea = jTextArea;
	}

	public synchronized void appendText(String text) {
		this.jTextArea.append(text);
		this.jTextArea.append("\n");
	}
}