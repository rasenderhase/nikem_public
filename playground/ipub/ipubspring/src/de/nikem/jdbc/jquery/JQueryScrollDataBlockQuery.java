package de.nikem.jdbc.jquery;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import de.nikem.jdbc.JdbcUtil;
import de.nikem.jdbc.QueryParam;
import de.nikem.jdbc.Work;

/**
 * SQL-Query, das die vom JQuery Datatables Plugin übertragenen Parameter benutzt, um Page- Ausschnitte des Suchergebnisses zu liefern.
 * 
 * @author dda1ak
 * @see http://datatables.net/usage/server-side
 */
public class JQueryScrollDataBlockQuery {

	private final String echo;
	private final String queryString;
	private final String countGroupBy;
	private final int displayStart;
	private final int displayLength;
	private final String orderBy;
	private final String[] dataProps;
	private final QueryParam[] queryParams;

	private final Gruppenwechsel<Map<String, ?>> gruppenwechsel;
	private final JdbcUtil jdbcUtil;

	private int rowIdx = 0;

	/**
	 * @param queryName
	 *            SQL-Query-String. Parameter werden im SQL-Query mit <code>:paramName</code> angegeben.
	 * @param parameterMap
	 *            HTTP-Request-Parameter des JQuery-Datatables-Plugin falls das ResultSet Gruppen enthält (z.B. <code>"group by ID"</code>).
	 * @param queryParams
	 *            Parameter des SQL-Query, die mit den Parameters im <code>queryString</code> übereinstimmen.
	 */
	public JQueryScrollDataBlockQuery(JdbcUtil jdbcUtil, String queryName, Map<String, String[]> parameterMap, QueryParam... queryParams) {
		this(jdbcUtil, queryName, parameterMap, "", null, queryParams);
	}

	/**
	 * @param queryName
	 *            SQL-Query-String. Parameter werden im SQL-Query mit <code>:paramName</code> angegeben.
	 * @param parameterMap
	 *            HTTP-Request-Parameter des JQuery-Datatables-Plugin
	 * @param countGroupBy
	 *            hier kann ein group-by-String für das Zählen der Treffer angegeben werden, falls das ResultSet Gruppen enthält (z.B. <code>"group by ID"</code>).
	 * @param gruppenwechsel
	 *            Gruppenwechsel-Objekt, das die korrekte Zählung beim Ermitteln der Datensätze, die zur gewünschten Ergebnis-Page gehören, sicherstellt.
	 * @param queryParams
	 *            Parameter des SQL-Query, die mit den Parameters im <code>queryString</code> übereinstimmen.
	 */
	public JQueryScrollDataBlockQuery(JdbcUtil jdbcUtil, String queryName, Map<String, String[]> parameterMap, String countGroupBy,
			Gruppenwechsel<Map<String, ?>> gruppenwechsel, QueryParam... queryParams) {
		this.jdbcUtil = jdbcUtil;
		this.queryString = jdbcUtil.getNamedQuery(queryName);
		this.countGroupBy = countGroupBy;
		this.gruppenwechsel = gruppenwechsel;

		this.queryParams = queryParams;

		// decode JQuery request query parameters
		displayStart = Integer.parseInt(getParameter(parameterMap, "iDisplayStart"));
		displayLength = Integer.parseInt(getParameter(parameterMap, "iDisplayLength"));
		echo = getParameter(parameterMap, "sEcho");
		// Anzahl der dargestellten Spalten
		int columns = Integer.parseInt(getParameter(parameterMap, "iColumns"));
		dataProps = new String[columns];
		for (int i = 0; i < columns; i++) {
			dataProps[i] = getParameter(parameterMap, "mDataProp_" + i);
		}

		// Anzahl der Sortier-Spalten
		int sortingCols = Integer.parseInt(getParameter(parameterMap, "iSortingCols"));
		String[] sorts = new String[sortingCols];
		for (int i = 0; i < sortingCols; i++) {
			// Spaltenname
			String col = dataProps[Integer.parseInt(getParameter(parameterMap, "iSortCol_" + i))];
			// Sortierrichtung
			String dir = getParameter(parameterMap, "sSortDir_" + i);
			sorts[i] = col.toUpperCase() + " " + dir;
			if (i == 0) {
				sorts[i] = " order by " + sorts[i];
			}
		}

		StringBuilder orderByBuilder = new StringBuilder();
		for (String sort : sorts) {
			if (orderByBuilder.length() > 0) {
				orderByBuilder.append(',');
			}
			orderByBuilder.append(sort);
		}
		this.orderBy = orderByBuilder.toString();
	}

	public JQueryListPage execute() {
		JQueryListPage page = new JQueryListPage();
		page.setEcho(echo);
		page.setTotalDisplayRecords(countTotalDisplayRecords());

		if (page.getTotalDisplayRecords() > 0) {
			page.setAaData(getData());
		} else {
			page.setAaData(new ArrayList<Map<String, ?>>());
		}

		return page;
	}

	protected List<Map<String, ?>> getData() {

		final String tmpString = queryString + " " + orderBy;

		return jdbcUtil.doWithoutTransaction(new Work<List<Map<String, ?>>>() {
			@Override
			public List<Map<String, ?>> doWork(Connection con) throws SQLException {
				List<Map<String, ?>> data = new ArrayList<Map<String, ?>>();
				PreparedStatement stmt = null;
				ResultSet rs = null;
				try {
					stmt = jdbcUtil.prepareStatement(con, tmpString, queryParams);
					rs = stmt.executeQuery();
					ResultSetMetaData metaData = rs.getMetaData();

					while (rs.next()) {
						Map<String, Object> row = new LinkedHashMap<String, Object>();
						for (int i = 0; i < metaData.getColumnCount(); i++) {
							Object value = rs.getObject(i + 1);
							row.put((metaData.getColumnName(i + 1).toUpperCase()), value);
						}
						increaseRowIdx(row);

						if (getRowIdx() > displayStart && getRowIdx() <= displayStart + displayLength) {
							data.add(row);
						}

						if (getRowIdx() > displayStart + displayLength) {
							// ResultSet war noch nicht zu Ende. Es gibt noch mehr Ergebnisse.
							break;
						}
					}
				} finally {
					JdbcUtil.close(rs);
					JdbcUtil.close(stmt);
				}

				return data;
			}
		});
	}

	protected int countTotalDisplayRecords() {
		return jdbcUtil.doWithoutTransaction(new Work<Integer>() {
			@Override
			public Integer doWork(Connection con) throws SQLException {
				String tmpString = queryString;
				// keine Sortierung beim Count
				tmpString = tmpString.replace(":sorts", "");
				tmpString = "select count(1) from (select 1 from (" + tmpString + ") as _count_1 " + countGroupBy + ") as _count_2";

				PreparedStatement stmt = null;
				ResultSet rs = null;
				try {
					stmt = jdbcUtil.prepareStatement(con, tmpString, queryParams);
					rs = stmt.executeQuery();
					rs.next();
					return rs.getInt(1);
				} finally {
					JdbcUtil.close(rs);
					JdbcUtil.close(stmt);
				}
			}
		});
	}

	protected int getRowIdx() {
		return rowIdx;
	}

	protected void setRowIdx(int rowIdx) {
		this.rowIdx = rowIdx;
	}

	protected void increaseRowIdx(Map<String, ?> rowMap) throws SQLException {
		if (gruppenwechsel != null) {
			gruppenwechsel.processRow(rowMap);
			if (gruppenwechsel.hasGruppenwechsel()) {
				setRowIdx(getRowIdx() + 1);
			}
		} else {
			setRowIdx(getRowIdx() + 1);
		}
	}

	/**
	 * Holt einen einzelnen Parameter aus einer Request-Parameter-Map.
	 * 
	 * @param parameterMap
	 * @param key
	 * @return
	 */
	protected static String getParameter(Map<String, String[]> parameterMap, String key) {
		String parameter = null;
		String[] parameters = parameterMap.get(key);
		if (parameters != null && parameters.length > 0) {
			parameter = parameters[0];
		}
		return parameter;
	}
}
