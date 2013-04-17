package de.nikem.jdbc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.transform.Transformers;
import org.hibernate.type.BigDecimalType;
import org.hibernate.type.BigIntegerType;
import org.hibernate.type.BinaryType;
import org.hibernate.type.BooleanType;
import org.hibernate.type.ByteType;
import org.hibernate.type.CalendarType;
import org.hibernate.type.CharacterType;
import org.hibernate.type.DateType;
import org.hibernate.type.DoubleType;
import org.hibernate.type.FloatType;
import org.hibernate.type.IntegerType;
import org.hibernate.type.LongType;
import org.hibernate.type.ObjectType;
import org.hibernate.type.ShortType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimeType;
import org.hibernate.type.TimestampType;
import org.hibernate.type.Type;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.TransactionCallback;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * <b>JdbcUtil</b> provides methods to conveniently access and use data base data. <b>JdbcUtil</b> uses either a {@link DataSource} or {@link DriverManager} to retrieve JDBC connections. You may use <b>JdbcUtil</b> as a singleton object.
 * 
 * @author andreas
 * 
 */
public class JdbcUtil {
	public static final Type COLLECTION_TYPE = new ObjectType();

	private TransactionTemplate transactionTemplate;
	private SessionFactory sessionFactory;

	private static final Logger log = Logger.getLogger(JdbcUtil.class.getName());
	private static final Pattern SQL_PARAM_PATTERN = Pattern.compile("(?i):[a-zA-z0-9_]+");

	private final Map<Type, QueryParamSetter> setterMap = QueryParamSetter.createMap();

	/**
	 * Close a connection safely without throwing any exception.
	 * 
	 * @param con
	 *            the connection to close (may be <code>null</code>)
	 */
	public static void close(Connection con) {
		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "Connection cannot be closed.", e);
			}
		}
	}

	/**
	 * Close a result set safely without throwing any exception.
	 * 
	 * @param resultSet
	 *            the result set to close (may be <code>null</code>)
	 */
	public static void close(ResultSet resultSet) {
		if (resultSet != null) {
			try {
				resultSet.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "ResultSet cannot be closed.", e);
			}
		}
	}

	/**
	 * Close a statement safely without throwing any exception.
	 * 
	 * @param statement
	 *            the statement to close (may be <code>null</code>)
	 */
	public static void close(Statement statement) {
		if (statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "Statement cannot be closed.", e);
			}
		}
	}

	public static void rollback(Connection con) {
		if (con != null) {
			try {
				con.rollback();
			} catch (SQLException e) {
				log.log(Level.SEVERE, "Connection cannot be rolled back.", e);
			}
		}
	}

	private JdbcUtil() {
	}

	protected Object changeToSqlDatatype(QueryParam param) {
		log.fine(param.toString());
		Object result = param.getValue();
		if (result instanceof Date && !(result instanceof java.sql.Date) && !(result instanceof java.sql.Time)
				&& !(result instanceof java.sql.Timestamp)) {
			log.fine(param + ": replace java.util.Date by java.sql.Timestamp");
			result = new Timestamp(((Date) result).getTime());
		}
		return result;
	}

	/**
	 * Execute a piece of work using a transaction. If there is an existing transaction associated with the current thread use the existing transaction. If not create one and bind it to the current thread. The <code>Connection</code> object
	 * is provided to the {@link Work#doWork(Connection)} method.
	 * 
	 * @param work
	 *            piece of work to be executed in an transaction
	 * @return result of the work execution
	 */
	public <T> T doInTransaction(final Work<T> work) {
		return getTransactionTemplate().execute(new TransactionCallback<T>() {
			@SuppressWarnings("unchecked")
			public T doInTransaction(TransactionStatus paramTransactionStatus) {
				final Object[] result = new Object[1];
				Session session = getSessionFactory().getCurrentSession();
				session.doWork(new org.hibernate.jdbc.Work() {

					@Override
					public void execute(Connection connection) throws SQLException {
						result[0] = work.doWork(connection);
					}
				});
				return (T) result[0];
			}
		});
	}

	/**
	 * Execute a piece of work using a transaction. If there is an existing transaction associated with the current thread use the existing transaction. If not create one and bind it to the current thread.
	 * 
	 * @param work
	 *            piece of work to be executed in an transaction
	 * @return result of the work execution
	 */
	public <T> T doInTransaction(TransactionCallback<T> transactionCallback) {
		return getTransactionTemplate().execute(transactionCallback);
	}

	/**
	 * execute a piece of work on a JDBC connection without transaction. (i.e. for read operations). Note that <b>in Hibernate there is no data access without transaction</b>. So this method is the same as {@link #doInTransaction(Work)}.
	 * 
	 * @param work
	 *            either a {@link Work} instance or a {@link Sam4Work} instance if you need access to Hibernate named queries or if you want to provide a return value.
	 * @see https://community.jboss.org/wiki/Sessionsandtransactions
	 */
	public <T> T doWithoutTransaction(final Work<T> work) {
		return (T) doInTransaction(work);
	}

	@SuppressWarnings("unchecked")
	public List<Map<String, ?>> executeNamedQuery(final String queryName, final QueryParam... queryParams) {
		return doInTransaction(new TransactionCallback<List<Map<String, ?>>>() {
			@Override
			public List<Map<String, ?>> doInTransaction(TransactionStatus status) {
				Session session = getSessionFactory().getCurrentSession();
				Query query = session.getNamedQuery(queryName);
				for (QueryParam queryParam : queryParams) {
					getSetterMap().get(queryParam.getType()).execute(query, queryParam.getName(), queryParam.getValue());
				}
				return query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).list();
			}
		});
	}

	@SuppressWarnings("unchecked")
	public Map<String, ?> executeNamedQueryUniqueResult(final String queryName, final QueryParam... queryParams) {
		return doInTransaction(new TransactionCallback<Map<String, ?>>() {
			@Override
			public Map<String, ?> doInTransaction(TransactionStatus status) {
				Session session = getSessionFactory().getCurrentSession();
				Query query = session.getNamedQuery(queryName);
				for (QueryParam queryParam : queryParams) {
					getSetterMap().get(queryParam.getType()).execute(query, queryParam.getName(), queryParam.getValue());
				}
				return (Map<String, ?>) query.setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP).uniqueResult();
			}
		});
	}

	public int executeUpdateNamedQuery(final String queryName, final QueryParam... params) {
		return doInTransaction(new TransactionCallback<Integer>() {
			@Override
			public Integer doInTransaction(TransactionStatus status) {
				Session session = getSessionFactory().getCurrentSession();
				Query query = session.getNamedQuery(queryName);
				for (QueryParam param : params) {
					getSetterMap().get(param.getType()).execute(query, param.getName(), param.getValue());
				}
				return query.executeUpdate();
			}
		});
	}

	protected String parameterMarkers(int size) {
		StringBuffer sb = new StringBuffer(size * 2);
		for (int i = 0; i < size; i++) {
			if (sb.length() > 0) {
				sb.append(',');
			}
			sb.append('?');
		}
		return sb.toString();
	}

	/**
	 * Create a <code>PreparedStatement</code> object using a connection and named query parameters
	 * 
	 * @param con
	 *            the connection to use for the statement
	 * @param queryString
	 *            query string containing named parameters (e.g. <code>:id</code>)
	 * @param queryParams
	 *            parameter name and value objects
	 * @return a prepared statement ready to be executed
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public PreparedStatement prepareStatement(Connection con, String queryString, QueryParam... queryParams) throws SQLException {
		final Map<String, QueryParam> queryParamMap = new LinkedHashMap<String, QueryParam>();
		for (QueryParam param : queryParams) {
			queryParamMap.put(param.getName(), param);
		}
		return prepareStatementMap(con, queryString, queryParamMap);
	}

	protected PreparedStatement prepareStatementMap(Connection con, String queryString, Map<String, QueryParam> queryParams) throws SQLException {
		String tmpString = queryString;
		List<QueryParam> paramList = new ArrayList<QueryParam>();

		Matcher matcher = SQL_PARAM_PATTERN.matcher(tmpString);
		StringBuffer sb = new StringBuffer();
		while (matcher.find()) {
			String paramName = matcher.group().substring(1); // cut colon
			QueryParam queryParam = queryParams.get(paramName);
			if (queryParam == null)
				throw new NikemJdbcException("Missing parameter for " + paramName);
			paramList.add(queryParam); // put parameters in the sequence of occurence in query
			Object value = queryParam.getValue();
			if (value != null) {
				if (value.getClass().isArray()) {
					if (value.getClass().getComponentType().isPrimitive()) {
						throw new UnsupportedOperationException("Please do not use arrays of primitives in de.nikem.jdbc.QueryParam");
					} else {
						Object[] coll = (Object[]) value;
						matcher.appendReplacement(sb, parameterMarkers(coll.length));
					}
				} else if (value instanceof Collection<?>) {
					Collection<?> coll = (Collection<?>) value;
					matcher.appendReplacement(sb, parameterMarkers(coll.size()));
				} else {
					matcher.appendReplacement(sb, "?");
				}
			} else {
				matcher.appendReplacement(sb, "?");
			}
		}
		matcher.appendTail(sb);
		tmpString = sb.toString();
		log.fine(tmpString);
		PreparedStatement stmt = con.prepareStatement(tmpString);
		Iterator<QueryParam> it = paramList.iterator();
		for (int i = 0; it.hasNext(); i++) {
			boolean isParameterList = false;
			Object value = changeToSqlDatatype(it.next());
			if (value != null) {
				if (value.getClass().isArray()) {
					if (value.getClass().getComponentType().isPrimitive()) {
						throw new UnsupportedOperationException("Please do not use primitive Arrays in de.nikem.jdbc.QueryParam");
					} else {
						for (Object item : (Object[]) value) {
							if (isParameterList)
								i++;
							stmt.setObject(i + 1, item);
							isParameterList = true;
						}
					}
				} else if (value instanceof Iterable) {
					Iterable<?> iterable = (Iterable<?>) value;
					for (Object item : iterable) {
						if (isParameterList)
							i++;
						stmt.setObject(i + 1, item);
						isParameterList = true;
					}
				} else {
					stmt.setObject(i + 1, value);
				}
			} else {
				stmt.setObject(i + 1, value);
			}
		}
		return stmt;
	}

	/**
	 * Create a &lt;uppercase ColumnName, ColumnValue&gt; map of the current result set row.
	 * 
	 * @param resultSet
	 *            result set pointing to the current row.
	 * @return created row map
	 * @throws SQLException
	 *             if a database access error occurs
	 */
	public Map<String, ?> resultSetRowToMap(ResultSet resultSet) throws SQLException {
		Map<String, Object> row = new LinkedHashMap<String, Object>();
		ResultSetMetaData metaData = resultSet.getMetaData();
		for (int index = 1; index <= metaData.getColumnCount(); index++) {
			row.put(metaData.getColumnName(index).toUpperCase(), resultSet.getObject(index));
		}
		return row;
	}

	/**
	 * Retrieve the query String from the SQL queries file
	 * 
	 * @param name
	 *            name of the query
	 * @return query string
	 */
	public String getNamedQuery(final String name) {
		return getTransactionTemplate().execute(new TransactionCallback<String>() {
			@Override
			public String doInTransaction(TransactionStatus status) {
				Query query = getSessionFactory().getCurrentSession().getNamedQuery(name);

				if (query == null)
					throw new NikemJdbcException("No query found for name " + name);
				return query.getQueryString();
			}
		});
	}

	/**
	 * Type dependent setter for query parameters
	 * 
	 * @author andreas
	 * 
	 */
	public static abstract class QueryParamSetter {

		public abstract void execute(Query query, String name, Object value);

		public static Map<Type, QueryParamSetter> createMap() {
			Map<Type, QueryParamSetter> map = new HashMap<Type, QueryParamSetter>();
			map.put(BigDecimalType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					query.setBigDecimal(name, (BigDecimal) value);
				}
			});
			map.put(BigIntegerType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					query.setBigInteger(name, (BigInteger) value);
				}
			});
			map.put(BinaryType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					query.setBinary(name, (byte[]) value);
				}
			});
			map.put(BooleanType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					if (value != null) {
						query.setBoolean(name, (Boolean) value);
					} else {
						query.setParameter(name, null);
					}
				}
			});
			map.put(ByteType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					if (value != null) {
						query.setByte(name, (Byte) value);
					} else {
						query.setParameter(name, null);
					}
				}
			});
			map.put(CalendarType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					query.setCalendar(name, (Calendar) value);
				}
			});
			map.put(CharacterType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					if (value != null) {
						query.setCharacter(name, (Character) value);
					} else {
						query.setParameter(name, null);
					}
				}
			});
			map.put(DateType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					query.setDate(name, (Date) value);
				}
			});
			map.put(TimeType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					query.setTime(name, (Time) value);
				}
			});
			map.put(TimestampType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					query.setTimestamp(name, (Timestamp) value);
				}
			});
			map.put(DoubleType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					if (value != null) {
						query.setDouble(name, (Double) value);
					} else {
						query.setParameter(name, null);
					}
				}
			});
			map.put(FloatType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					if (value != null) {
						query.setFloat(name, (Float) value);
					} else {
						query.setParameter(name, null);
					}
				}
			});
			map.put(IntegerType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					if (value != null) {
						query.setInteger(name, (Integer) value);
					} else {
						query.setParameter(name, null);
					}
				}
			});
			map.put(LongType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					if (value != null) {
						query.setLong(name, (Long) value);
					} else {
						query.setParameter(name, null);
					}
				}
			});
			map.put(ShortType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					if (value != null) {
						query.setShort(name, (Short) value);
					} else {
						query.setParameter(name, null);
					}
				}
			});
			map.put(StringType.INSTANCE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					query.setString(name, (String) value);
				}
			});
			map.put(COLLECTION_TYPE, new QueryParamSetter() {
				@Override
				public void execute(Query query, String name, Object value) {
					query.setParameterList(name, (Collection<?>) value);
				}
			});
			return map;
		}
	}

	public void setTxManager(PlatformTransactionManager txManager) {
		this.transactionTemplate = new TransactionTemplate(txManager);
	}

	protected TransactionTemplate getTransactionTemplate() {
		return transactionTemplate;
	}

	public void setSessionFactory(SessionFactory sessionFactory) {
		this.sessionFactory = sessionFactory;
	}

	protected SessionFactory getSessionFactory() {
		return sessionFactory;
	}

	protected Map<Type, QueryParamSetter> getSetterMap() {
		return setterMap;
	}

}
