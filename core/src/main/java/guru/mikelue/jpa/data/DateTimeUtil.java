package guru.mikelue.jpa.data;

import org.apache.commons.lang3.time.DateUtils;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import static java.util.Calendar.getInstance;

/**
 * The utility to process Date/Time/Timestamp data between SQL and Java.
 * In some circumstance, the time/precision of time would be erased.
 *
 * <p>With {@link Date} type, the time would be 00:00:00</p>
 * <p>With {@link Timestamp} type, milliseconds would be erased</p>
 */
public class DateTimeUtil {
	private DateTimeUtil() {}

	/**
	 * Compare SQL date type, time value would be erased.
	 *
	 * @param leftDate The left side of operator
	 * @param rightDate The right side of operator
	 *
	 * @return See {@link Calendar#compareTo(Calendar)}
	 */
	public static int compare(Date leftDate, Date rightDate)
	{
		Calendar leftCalender = getInstance();
		leftCalender.setTime(convertToDate(leftDate));
		Calendar rightCalender = getInstance();
		rightCalender.setTime(convertToDate(rightDate));

		return leftCalender.compareTo(rightCalender);
	}
	/**
	 * Compare SQL timestamp type, milliseconds would be erased.
	 *
     * @param leftTime The left side of operator
     * @param rightTime The right side of operator
	 *
	 * @return See {@link Calendar#compareTo(Calendar)}
	 */
	public static int compare(Timestamp leftTime, Timestamp rightTime)
	{
		Calendar leftCalender = getInstance();
		leftCalender.setTime(convertToTimestamp(leftTime));
		Calendar rightCalender = getInstance();
		rightCalender.setTime(convertToTimestamp(rightTime));

		return leftCalender.compareTo(rightCalender);
	}

	/**
	 * Generate current date.
	 *
	 * @return current date
	 *
	 * @see #getCurrentTimestamp()
	 */
	public static Date getCurrentDate()
	{
		return convertToDate(getInstance());
	}
	/**
	 * Generate current date with time
	 *
	 * @return current date date with time
	 *
	 * @see #getCurrentDate()
	 */
	public static Timestamp getCurrentTimestamp()
	{
		return convertToTimestamp(getInstance());
	}

	/**
	 * Erase the time potion of Calendar.
	 *
	 * @param calendar source calendar object
	 *
	 * @return new calendar object
	 *
	 * @see #trimToSQLTimestamp(Calendar)
	 */
	public static Calendar trimToSQLDate(Calendar calendar)
	{
		// Erase time potion
		return DateUtils.truncate(calendar, Calendar.DATE);
	}
	/**
	 * Erase the milliseconds of Calendar.
	 *
	 * @param calendar source calendar object
	 *
	 * @return new calendar object
	 *
	 * @see #trimToSQLDate(Calendar)
	 */
	public static Calendar trimToSQLTimestamp(Calendar calendar)
	{
		// Erase milliseconds potion
		return DateUtils.truncate(calendar, Calendar.SECOND);
	}

	/**
	 * Convert {@link Calendar} to {@link Date}.
	 *
	 * @param calendar source calendar object
	 *
	 * @return Date type in SQL
	 *
	 * @see #convertToTimestamp(Calendar)
	 */
	public static Date convertToDate(Calendar calendar)
	{
		return new Date(trimToSQLDate(calendar).getTimeInMillis());
	}

	/**
	 * Convert {@link Calendar} to {@link Timestamp}.
	 *
	 * @param calendar source calendar object
	 *
	 * @return Timestamp type in SQL
	 *
	 * @see #convertToDate(Calendar)
	 */
	public static Timestamp convertToTimestamp(Calendar calendar)
	{
		return new Timestamp(trimToSQLTimestamp(calendar).getTimeInMillis());
	}

	/**
	 * Erase the time portion and convert {@link java.util.Date} to {@link Date}.
	 *
	 * @param srcDate source date object
	 *
	 * @return Date type in SQL
	 *
	 * @see #convertToTimestamp(java.util.Date)
	 */
	public static Date convertToDate(java.util.Date srcDate)
	{
		Calendar srcCalender = Calendar.getInstance();
		srcCalender.setTime(srcDate);
		return convertToDate(srcCalender);
	}
	/**
	 * Erase the milliseconds and convert {@link java.util.Date} to {@link Timestamp}.
	 *
	 * @param srcDate source date object
	 *
	 * @return Timestamp type in SQL
	 *
	 * @see #convertToDate(java.util.Date)
	 */
	public static Timestamp convertToTimestamp(java.util.Date srcDate)
	{
		Calendar srcCalender = Calendar.getInstance();
		srcCalender.setTime(srcDate);
		return convertToTimestamp(srcCalender);
	}
}
