package de.nikem.jdbc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Date;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Collection;

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
import org.hibernate.type.ShortType;
import org.hibernate.type.StringType;
import org.hibernate.type.TimeType;
import org.hibernate.type.TimestampType;
import org.hibernate.type.Type;

/**
 * named query parameter
 * 
 * @author andreas
 * 
 */
public class QueryParam {
	private final String name;
	private final Object value;
	private final Type type;

	/**
	 * Create new query parameter object
	 * 
	 * @param name
	 *            parameter name
	 * @param value
	 *            parameter value
	 * @param type
	 *            hibernate type to be chosen when setting the parameter into the query
	 */
	protected QueryParam(String name, Object value, Type type) {
		this.name = name;
		this.value = value;
		this.type = type;
	}

	public QueryParam(String name, BigDecimal value) {
		this(name, value, BigDecimalType.INSTANCE);
	}

	public QueryParam(String name, BigInteger value) {
		this(name, value, BigIntegerType.INSTANCE);
	}

	public QueryParam(String name, byte[] value) {
		this(name, value, BinaryType.INSTANCE);
	}

	public QueryParam(String name, Boolean value) {
		this(name, value, BooleanType.INSTANCE);
	}

	public QueryParam(String name, Byte value) {
		this(name, value, ByteType.INSTANCE);
	}

	public QueryParam(String name, Calendar value) {
		this(name, value, CalendarType.INSTANCE);
	}

	public QueryParam(String name, Character value) {
		this(name, value, CharacterType.INSTANCE);
	}

	public QueryParam(String name, Date value) {
		this(name, value, DateType.INSTANCE);
	}

	public QueryParam(String name, Time value) {
		this(name, value, TimeType.INSTANCE);
	}

	public QueryParam(String name, Timestamp value) {
		this(name, value, TimestampType.INSTANCE);
	}

	public QueryParam(String name, java.util.Date value) {
		this(name, new Timestamp(value.getTime()), TimestampType.INSTANCE);
	}

	public QueryParam(String name, Double value) {
		this(name, value, DoubleType.INSTANCE);
	}

	public QueryParam(String name, Float value) {
		this(name, value, FloatType.INSTANCE);
	}

	public QueryParam(String name, Integer value) {
		this(name, value, IntegerType.INSTANCE);
	}

	public QueryParam(String name, Long value) {
		this(name, value, LongType.INSTANCE);
	}

	public QueryParam(String name, Short value) {
		this(name, value, ShortType.INSTANCE);
	}

	public QueryParam(String name, String value) {
		this(name, value, StringType.INSTANCE);
	}

	public QueryParam(String name, Collection<?> value) {
		this(name, value, JdbcUtil.COLLECTION_TYPE);
	}

	public String getName() {
		return name;
	}

	public Object getValue() {
		return value;
	}

	public Type getType() {
		return type;
	}

	@Override
	public String toString() {
		return "QueryParam [" + name + "=" + value + ", " + type.getName() + "]";
	}
}
