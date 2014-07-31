package de.nikem.playground.model;

import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.MultivaluedMap;

public class MyModel extends Model {
	private final NumberFormat betragFormat = NumberFormat.getNumberInstance();
	{
		betragFormat.setMinimumFractionDigits(2);
		betragFormat.setMaximumFractionDigits(2);
	}
	private final NumberFormat anzahlFormat = NumberFormat.getIntegerInstance();

	private final Formatter anzahl = new Formatter() {
		public String format(String key, Object p) {
			String formatted = null;
			if (p instanceof Number) {
				formatted = anzahlFormat.format(p);
			}
			return formatted;
		};

		protected Object parse(String key, String value) {
			Number number = null;
			if (!MyModel.this.isEmpty(value)) {
				try {
					number = anzahlFormat.parse(value);
				} catch (ParseException e) {
					getErrors().put(key, e.getMessage());
				}
			}
			return number;
		};
	};

	private final Formatter betrag = new Formatter() {
		public String format(String key, Object p) {
			String formatted = null;
			if (p instanceof Number) {
				formatted = betragFormat.format(p);
			}
			return formatted;
		};

		protected Object parse(String key, String value) {
			Number number = null;
			if (!MyModel.this.isEmpty(value)) {
				try {
					number = betragFormat.parse(value);
				} catch (ParseException e) {
					getErrors().put(key, e.getMessage());
				}
			}
			return number;
		};
	};

	private List<String> changed;

	public MyModel() {
		anzahl.setModel(this);
		betrag.setModel(this);

		Map<String, Formatter> f = getFormatters();
		f.put("EINZELPREIS", getBetrag());
		f.put("STUECKZAHL", getAnzahl());
		f.put("GESAMTPREIS", getBetrag());
	}

	public Formatter getAnzahl() {
		return anzahl;
	}

	public Formatter getBetrag() {
		return betrag;
	}

	@Override
	public boolean validate() {
		Set<String> emptyFields = new HashSet<String>();
		Number gp = (Number) getPersistent().get("GESAMTPREIS");
		Number ep = (Number) getPersistent().get("EINZELPREIS");
		Number st = (Number) getPersistent().get("STUECKZAHL");

		String key = "NAME";
		if (!getErrors().containsKey(key) && isDisplayEmpty(key)) {
			getErrors().put(key, "Pflichtfeld");
		}

		key = "STUECKZAHL";
		if (!getErrors().containsKey(key) && isDisplayEmpty(key)) {
			emptyFields.add(key);
		} else if (!getErrors().containsKey(key) && st.intValue() < 0) {
			getErrors().put(key, "Bitte geben Sie einen Wert > 0 ein");
		}

		key = "EINZELPREIS";
		if (!getErrors().containsKey(key) && isDisplayEmpty(key)) {
			emptyFields.add(key);
		} else if (!getErrors().containsKey(key) && ep.doubleValue() < 0.01) {
			getErrors().put(key, "Bitte geben Sie einen Wert > 0,01 ein");
		}

		key = "GESAMTPREIS";
		if (!getErrors().containsKey(key) && isDisplayEmpty(key)) {
			emptyFields.add(key);
		} else if (!getErrors().containsKey(key) && gp.doubleValue() < 0.01) {
			getErrors().put(key, "Bitte geben Sie einen Wert > 0,01 ein");
		}

		if (emptyFields.size() > 1) {
			for (String ekey : emptyFields) {
				getErrors().put(ekey, "Nur eins der Felder 'Einzelpreis', 'Anzahl', 'Gesamt' darf leer sein.");
			}
		}

		return super.validate();
	}

	public void setClientData(MultivaluedMap<String, String> form) {
		for (String key : form.keySet()) {
			String value = form.getFirst(key);
			getFormat().put(key, value);
		}

		changed = form.get("changed");
	}

	public void calculate() {
		Number gp = (Number) getPersistent().get("GESAMTPREIS");
		Number ep = (Number) getPersistent().get("EINZELPREIS");
		Number st = (Number) getPersistent().get("STUECKZAHL");

		if (gp == null) {
			calculateGesamtpreis(ep, st);
		} else if (ep == null) {
			calculateEinzelpreis(gp, st);
		} else if (st == null) {
			calculateStueckzahl(gp, ep);
		} else if (!changed.contains("GESAMTPREIS")) {
			calculateGesamtpreis(ep, st);
		} else if (!changed.contains("EINZELPREIS")) {
			calculateEinzelpreis(gp, st);
		} else if (!changed.contains("STUECKZAHL")) {
			calculateStueckzahl(gp, ep);
		}
	}

	private void calculateStueckzahl(Number gp, Number ep) {
		getPersistent().put("STUECKZAHL", gp.doubleValue() / ep.doubleValue());
		invalidateDisplay();
	}

	private void calculateEinzelpreis(Number gp, Number st) {
		getPersistent().put("EINZELPREIS", gp.doubleValue() / st.intValue());
		invalidateDisplay();
	}

	private void calculateGesamtpreis(Number ep, Number st) {
		getPersistent().put("GESAMTPREIS", ep.doubleValue() * st.intValue());
		invalidateDisplay();
	}

	private boolean isDisplayEmpty(String key) {
		return isEmpty(getDisplay().get(key));
	}

	private boolean isEmpty(String s) {
		return s == null || s.length() == 0;
	}

	public List<String> getChanged() {
		if (changed == null) {
			changed = new ArrayList<String>(Arrays.<String> asList(null, null));
		}
		return changed;
	}

	public void setChanged(List<String> changed) {
		this.changed = changed;
	}
}
