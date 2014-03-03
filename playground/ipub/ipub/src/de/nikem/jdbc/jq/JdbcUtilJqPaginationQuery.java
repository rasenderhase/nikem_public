package de.nikem.jdbc.jq;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import de.nikem.dataj.Gruppenwechsel;
import de.nikem.dataj.jq.JqPaginationQuery;
import de.nikem.jdbc.JdbcUtil;
import de.nikem.jdbc.QueryParam;

public class JdbcUtilJqPaginationQuery extends JqPaginationQuery<Map<String, ?>> {

	private final JdbcUtil jdbcUtil;
	private final QueryParam[] preprocessQueryParams;
	private final QueryParam[] queryParams;
	
	
	public JdbcUtilJqPaginationQuery(JdbcUtil jdbcUtil, String queryName,
			Map<String, String[]> requestParameterMap, QueryParam[] preprocessQueryParams, QueryParam... queryParams) {
		this(jdbcUtil, queryName, requestParameterMap, null, null, preprocessQueryParams, queryParams);
	}

	public JdbcUtilJqPaginationQuery(JdbcUtil jdbcUtil, String queryName, Map<String, String[]> requestParameterMap, String countGroupBy, Gruppenwechsel<Map<String, ?>> gruppenwechsel, QueryParam[] preprocessQueryParams, QueryParam... queryParams) {
		super(jdbcUtil.getDataSource(), jdbcUtil.getNamedQuery(queryName), requestParameterMap, countGroupBy, gruppenwechsel);
		this.jdbcUtil = jdbcUtil;
		this.preprocessQueryParams = preprocessQueryParams;
		this.queryParams = queryParams;
	}

	@Override
	protected PreparedStatement prepareStatement(Connection aCon, String tmpString) throws SQLException {
		return jdbcUtil.prepareStatement(aCon, tmpString, getPreprocessQueryParams(), getQueryParams());
	}

	@Override
	protected Map<String, ?> mapRow(ResultSet rs) throws SQLException {
		return jdbcUtil.resultSetRowToMap(rs);
	}

	public QueryParam[] getPreprocessQueryParams() {
		return preprocessQueryParams;
	}

	public QueryParam[] getQueryParams() {
		return queryParams;
	}

	
}
