package de.nikem.ipub.app;

import java.io.IOException;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp.ConnectionFactory;
import org.apache.commons.dbcp.DriverManagerConnectionFactory;
import org.apache.commons.dbcp.PoolableConnectionFactory;
import org.apache.commons.dbcp.PoolingDataSource;
import org.apache.commons.pool.impl.GenericObjectPool;

import de.nikem.jdbc.JdbcUtil;
import de.nikem.jdbc.NikemJdbcException;

public class SingletonFactory {
	private final Logger log = Logger.getLogger(getClass().getName());

	protected DataSource dataSource = null;
	protected JdbcUtil jdbcUtil = null;
	protected Properties dbProperties = null;

	public Properties getDbProperties() {
		if (dbProperties == null) {
			String environment = System.getProperty("de.nikem.ipub.environment");
			String resourceName = "/de/nikem/ipub/db" + (environment != null ? "." + environment : "") + ".properties";
			log.fine("Load properties resource " + resourceName);
			dbProperties = new Properties();
			try {
				dbProperties.load(getClass().getResourceAsStream(resourceName));
			} catch (IOException e) {
				throw new NikemJdbcException("Cannot load " + resourceName, e);
			}
		}
		return dbProperties;
	}

	public DataSource getDataSource() {
		if (dataSource == null) {
			log.info("create dataSource");
			try {
				Class.forName("com.mysql.jdbc.Driver");
				@SuppressWarnings({"rawtypes", "unchecked"})
				GenericObjectPool connectionPool = new GenericObjectPool(null);
				Properties dbProperties = getDbProperties();
				ConnectionFactory connectionFactory = new DriverManagerConnectionFactory(dbProperties.getProperty("connection.url"),
						dbProperties.getProperty("connection.username"), dbProperties.getProperty("connection.password"));
				String validationQuery = "select 1";
				@SuppressWarnings("unused")
				PoolableConnectionFactory poolableConnectionFactory = new PoolableConnectionFactory(connectionFactory, connectionPool, null,
						validationQuery, false, true);
				dataSource = new PoolingDataSource(connectionPool);
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("No DB driver available", e);
			}
		}
		return dataSource;
	}

	public JdbcUtil getJdbcUtil() {
		if (jdbcUtil == null) {
			log.info("create jdbcUtil");
			jdbcUtil = JdbcUtil.createDataSourceInstance(getDataSource());
			jdbcUtil.addQueries("/de/nikem/ipub/Queries.xml");
		}
		return jdbcUtil;
	}
}
