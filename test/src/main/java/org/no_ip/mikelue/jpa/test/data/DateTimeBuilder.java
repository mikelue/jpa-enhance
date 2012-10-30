package org.no_ip.mikelue.jpa.test.data;

import org.no_ip.mikelue.jpa.data.DateTimeUtil;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.Calendar;
import static java.util.Calendar.getInstance;

/**
 * The utility is used for building date/timestamp by adding certain value at time unit.<p>
 *
 * This class would use {@link DateTimeUtil} to erase time precision when retrieve date/time value.<p>
 *
 * This class is non thread-safe.<p>
 */
public class DateTimeBuilder {
    /**
     * The unit of time used whne calculate in particular date/time.
     */
    public enum DateTimeUnit {
        /**
         * The unit of number is year
         */
        Year,
        /**
         * The unit of number is month
         */
        Month,
        /**
         * The unit of number is day(of year)
         */
        Day,
        /**
         * The unit of number is hour
         */
        Hour,
        /**
         * The unit of number is minute
         */
        Minute,
        /**
         * The unit of number is second
         */
        Second;
    }

    /**
     * The synonym of {@link DateTimeUnit#Year}
     */
    public static final DateTimeUnit Year = DateTimeUnit.Year;
    /**
     * The synonym of {@link DateTimeUnit#Month}
     */
    public static final DateTimeUnit Month = DateTimeUnit.Month;
    /**
     * The synonym of {@link DateTimeUnit#Day}
     */
    public static final DateTimeUnit Day = DateTimeUnit.Day;
    /**
     * The synonym of {@link DateTimeUnit#Hour}
     */
    public static final DateTimeUnit Hour = DateTimeUnit.Hour;
    /**
     * The synonym of {@link DateTimeUnit#Minute}
     */
    public static final DateTimeUnit Minute = DateTimeUnit.Minute;
    /**
     * The synonym of {@link DateTimeUnit#Second}
     */
    public static final DateTimeUnit Second = DateTimeUnit.Second;

    private Calendar sourceCalendar = null;

    /**
     * Initialize build with SQL {@link Date} object
     *
     * @param sourceDate the source SQL date
     */
    public DateTimeBuilder(Date sourceDate)
    {
        initCalendar(sourceDate);
    }
    /**
     * Initialize build with SQL {@link Timestamp} object
     *
     * @param sourceTimestamp the source SQL date with time
     */
    public DateTimeBuilder(Timestamp sourceTimestamp)
    {
        initCalendar(sourceTimestamp);
    }

    /**
     * Add specific number of time unit into date/time.<p>
     *
     * Adding a negative number means calculating time in the past.
     *
     * @param dateTimeUnit the unit of added number
     * @param addedNumber the added number
     *
     * @return the same object for cascading method call
     */
    public DateTimeBuilder add(DateTimeUnit dateTimeUnit, int addedNumber)
    {
        switch (dateTimeUnit) {
            case Year:
                sourceCalendar.add(Calendar.YEAR, addedNumber);
                break;
            case Month:
                sourceCalendar.add(Calendar.MONTH, addedNumber);
                break;
            case Day:
                sourceCalendar.add(Calendar.DAY_OF_YEAR, addedNumber);
                break;
            case Hour:
                sourceCalendar.add(Calendar.HOUR_OF_DAY, addedNumber);
                break;
            case Minute:
                sourceCalendar.add(Calendar.MINUTE, addedNumber);
                break;
            case Second:
                sourceCalendar.add(Calendar.SECOND, addedNumber);
                break;
            default:
                throw new IllegalArgumentException("Time unit not support: " + dateTimeUnit);
        }

        return this;
    }

    /**
     * Get the result of computation in SQL {@link Date}.</p>
     *
     * @return the time portion would be erased
     *
     * @see #getResultTimestamp
     */
    public Date getResultDate()
    {
        return DateTimeUtil.convertToDate(sourceCalendar);
    }
    /**
     * Get the result of computation in SQL {@link Timestamp}.</p>
     *
     * @return the millisecond portion would be erased
     *
     * @see #getResultDate
     */
    public Timestamp getResultTimestamp()
    {
        return DateTimeUtil.convertToTimestamp(sourceCalendar);
    }

    private void initCalendar(java.util.Date sourceDateTime)
    {
        sourceCalendar = Calendar.getInstance();
        sourceCalendar.setTime(sourceDateTime);
    }
}
