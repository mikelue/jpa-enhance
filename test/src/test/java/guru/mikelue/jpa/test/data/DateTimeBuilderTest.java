package guru.mikelue.jpa.test.data;

import guru.mikelue.jpa.data.DateTimeUtil;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

public class DateTimeBuilderTest {
    public DateTimeBuilderTest() {}

    /**
     * <p>Test building for {@link Date}.</p>
     */
    @Test(dataProvider="AddedNumberForDate")
    public void addToDate(
        Date testDate,
        DateTimeBuilder.DateTimeUnit dateTimeUnit, int addedNumber,
        int expectedCompareResult
    ) {
        Date resultDate = new DateTimeBuilder(testDate)
            .add(dateTimeUnit, addedNumber)
            .getResultDate();

        Assert.assertEquals(
            DateTimeUtil.compare(testDate, resultDate), expectedCompareResult
        );
    }
    /**
     * <p>Test building for {@link Timestamp}.</p>
     */
    @Test(dataProvider="AddedNumberForTimestamp")
    public void addToTimestamp(DateTimeBuilder.DateTimeUnit dateTimeUnit, int addedNumber, int expectedCompareResult)
    {
        Timestamp testTimestamp = DateTimeUtil.getCurrentTimestamp();
        Timestamp resultTimestamp = new DateTimeBuilder(testTimestamp)
            .add(dateTimeUnit, addedNumber)
            .getResultTimestamp();

        Assert.assertEquals(
            testTimestamp.compareTo(resultTimestamp), expectedCompareResult
        );
    }

    private DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private Date buildDate(String dateString)
    {
        try {
            return new Date(dateFormat.parse(dateString).getTime());
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @DataProvider(name="AddedNumberForDate")
    private Object[][] getAddedNumberForDate()
    {
        return new Object[][] {
            { buildDate("2010-05-10 00:00:00.000"), DateTimeBuilder.Year, 1, -1 },
            { buildDate("2010-05-10 00:00:00.000"), DateTimeBuilder.Year, -1, 1 },
            { buildDate("2010-05-10 00:00:00.000"), DateTimeBuilder.Year, 0, 0 },
            { buildDate("2010-05-10 00:00:00.000"), DateTimeBuilder.Month, 1, -1 },
            { buildDate("2010-05-10 00:00:00.000"), DateTimeBuilder.Month, -1, 1 },
            { buildDate("2010-05-10 00:00:00.000"), DateTimeBuilder.Month, 0, 0 },
            { buildDate("2010-05-10 00:00:00.000"), DateTimeBuilder.Day, 1, -1 },
            { buildDate("2010-05-10 00:00:00.000"), DateTimeBuilder.Day, -1, 1 },
            { buildDate("2010-05-10 00:00:00.000"), DateTimeBuilder.Day, 0, 0 },
            { buildDate("2010-05-11 22:00:00.000"), DateTimeBuilder.Hour, 1, 0 },
            { buildDate("2010-05-11 22:00:00.000"), DateTimeBuilder.Hour, 2, -1 },
            { buildDate("2010-05-11 01:00:00.000"), DateTimeBuilder.Hour, -1, 0 },
            { buildDate("2010-05-11 01:00:00.000"), DateTimeBuilder.Hour, -2, 1 }
        };
    }
    @DataProvider(name="AddedNumberForTimestamp")
    private Object[][] getAddedNumberForTimestamp()
    {
        return new Object[][] {
            { DateTimeBuilder.Hour, 1, -1 },
            { DateTimeBuilder.Hour, -1, 1 },
            { DateTimeBuilder.Hour, 0, 0 },
            { DateTimeBuilder.Minute, 1, -1 },
            { DateTimeBuilder.Minute, -1, 1 },
            { DateTimeBuilder.Minute, 0, 0 },
            { DateTimeBuilder.Second, 1, -1 },
            { DateTimeBuilder.Second, -1, 1 },
            { DateTimeBuilder.Second, 0, 0 }
        };
    }
}
