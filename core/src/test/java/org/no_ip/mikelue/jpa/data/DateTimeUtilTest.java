package org.no_ip.mikelue.jpa.data;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Calendar;
import java.sql.Date;
import java.sql.Timestamp;

public class DateTimeUtilTest {
    public DateTimeUtilTest() {}

    @Test(dataProvider="CompareDate")
    public void compareDate(
        Date testLeftDate, Date testRightDate, int expectedCompareResult
    ) {
        Assert.assertEquals(
            DateTimeUtil.compare(testLeftDate, testRightDate),
            expectedCompareResult
        );
    }
    @Test(dataProvider="CompareTimestamp")
    public void compareTimestamp(
        Timestamp testLeftTimestamp, Timestamp testRightTimestamp, int expectedCompareResult
    ) {
        Assert.assertEquals(
            DateTimeUtil.compare(testLeftTimestamp, testRightTimestamp),
            expectedCompareResult
        );
    }

    @Test(dataProvider="TrimToSQLDate")
    public void trimToSQLDate(
        Calendar testCalendar, Calendar expectedCalendar
    ) {
        Assert.assertEquals(
            DateTimeUtil.trimToSQLDate(testCalendar),
            expectedCalendar
        );
    }
    @Test(dataProvider="TrimToSQLTimestamp")
    public void trimToSQLTimestamp(
        Calendar testCalendar, Calendar expectedCalendar
    ) {
        Assert.assertEquals(
            DateTimeUtil.trimToSQLTimestamp(testCalendar),
            expectedCalendar
        );
    }

    @DataProvider(name="TrimToSQLDate")
    private Object[][] getTrimToSQLDate()
    {
        return new Object[][] {
            {
                buildDateTime(2000, 1, 1, 0, 0, 0, 0),
                buildDateTime(2000, 1, 1, 0, 0, 0, 0)
            },
            { // The correction of trimming
                buildDateTime(2000, 1, 1, 2, 2, 2, 32),
                buildDateTime(2000, 1, 1, 0, 0, 0, 0)
            }
        };
    }

    @DataProvider(name="TrimToSQLTimestamp")
    private Object[][] getTrimToSQLTimestamp()
    {
        return new Object[][] {
            {
                buildDateTime(2000, 1, 1, 2, 2, 2, 0),
                buildDateTime(2000, 1, 1, 2, 2, 2, 0)
            },
            { // The correction of trimming
                buildDateTime(2000, 1, 1, 2, 25, 10, 77),
                buildDateTime(2000, 1, 1, 2, 25, 10, 0)
            }
        };
    }

    @DataProvider(name="CompareDate")
    private Object[][] getCompareDate()
    {
        return new Object[][] {
            {
                buildDate(2000, 1, 1, 0, 0, 0, 0),
                buildDate(2000, 1, 1, 0, 0, 0, 0),
                0
            },
            {
                buildDate(2000, 1, 2, 0, 0, 0, 0),
                buildDate(2000, 1, 1, 0, 0, 0, 0),
                1
            },
            {
                buildDate(2000, 1, 1, 0, 0, 0, 0),
                buildDate(2000, 1, 2, 0, 0, 0, 0),
                -1
            },
            { // The correction of trimming
                buildDate(2000, 1, 1, 2, 2, 2, 2),
                buildDate(2000, 1, 1, 0, 0, 0, 0),
                0
            }
        };
    }
    @DataProvider(name="CompareTimestamp")
    private Object[][] getCompareTimestamp()
    {
        return new Object[][] {
            {
                buildTimestamp(2000, 1, 1, 0, 0, 0, 0),
                buildTimestamp(2000, 1, 1, 0, 0, 0, 0),
                0
            },
            {
                buildTimestamp(2000, 1, 1, 4, 0, 0, 0),
                buildTimestamp(2000, 1, 1, 2, 0, 0, 0),
                1
            },
            {
                buildTimestamp(2000, 1, 1, 2, 0, 0, 0),
                buildTimestamp(2000, 1, 1, 4, 0, 0, 0),
                -1
            },
            { // The correction of trimming
                buildTimestamp(2000, 1, 1, 2, 2, 2, 20),
                buildTimestamp(2000, 1, 1, 2, 2, 2, 400),
                0
            }
        };
    }

    private Calendar buildDateTime(int year, int month, int date, int hourOfDay, int minute, int second, int millisecond)
    {
        Calendar timeTemplate = Calendar.getInstance();
        timeTemplate.set(year, month, date, hourOfDay, minute, second);
        timeTemplate.set(Calendar.MILLISECOND, millisecond);
        return timeTemplate;
    }
    private Date buildDate(int year, int month, int date, int hourOfDay, int minute, int second, int millisecond)
    {
        return new Date(
            buildDateTime(year, month, date, hourOfDay, minute, second, millisecond).getTimeInMillis()
        );
    }
    private Timestamp buildTimestamp(int year, int month, int date, int hourOfDay, int minute, int second, int millisecond)
    {
        return new Timestamp(
            buildDateTime(year, month, date, hourOfDay, minute, second, millisecond).getTimeInMillis()
        );
    }
}
