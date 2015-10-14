package guru.mikelue.jpa.test.dbunit;

import guru.mikelue.jpa.data.DateTimeUtil;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.ITableMetaData;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Date;

public class YamlDataSetTest {
    public YamlDataSetTest() {}

    /**
     * <p>Tests the loading of normal YAML data.</p>
     */
    @Test
    public void normalLoad() throws DataSetException
    {
        /**
         * Columns(5): d1_id, d1_account, d1_password, d1_word, d1_time_created
         */
        IDataSet testDataSet = new YamlDataSet(
            " tt_d1: [" +
            "     { d1_id: 101, d1_account: 'u001', d1_time_created: 2008-03-01 }," +
            "     { d1_id: 102, d1_account: 'u002', d1_time_created: \"As String\" }," +
            "     { d1_id: 103, d1_account: 'u003', d1_password: 'password 2', d1_time_created: 2009-01-11 }," +
            "     { d1_id: 104, d1_account: 'u004', d1_word: 'word 3', d1_time_created: 2010-11-21 }" +
            " ]\n" +
            " tt_d2: {" +
            "     d2_id: 100," +
            "     d2_account: 'u001'" +
            " }"
        );
        // :~)

        /**
         * Assert tables' name which must be loaded
         */
        testDataSet.getTable("tt_d1");
        testDataSet.getTable("tt_d2");
        // :~)

        ITable d1Table = testDataSet.getTable("tt_d1");
        ITable d2Table = testDataSet.getTable("tt_d2");

        /**
         * Assert meta data of table(throw exception if column doens't exist)
         */
		ITableMetaData testedMetaData = d1Table.getTableMetaData();

		org.slf4j.LoggerFactory.getLogger(YamlDataSetTest.class).warn(
			 "Columns: {}", testedMetaData
		);
		Assert.assertEquals(testedMetaData.getColumns().length, 5);

        testedMetaData.getColumnIndex("d1_id");
        testedMetaData.getColumnIndex("d1_account");
        testedMetaData.getColumnIndex("d1_time_created");
        testedMetaData.getColumnIndex("d1_password");
        testedMetaData.getColumnIndex("d1_word");
        // :~)

        /**
         * Assert data of single row
         */
        Assert.assertEquals(d2Table.getRowCount(), 1);
        Assert.assertEquals(d2Table.getValue(0, "d2_id"), 100);
        Assert.assertEquals(d2Table.getValue(0, "d2_account"), "u001");
        // :~)

        /**
         * Assert data of multiple rows
         */
        Assert.assertEquals(d1Table.getRowCount(), 4);
        Assert.assertEquals(d1Table.getValue(0, "d1_id"), 101);
        Assert.assertEquals(d1Table.getValue(2, "d1_password"), "password 2");

        // Null column
        Assert.assertNull(d1Table.getValue(0, "d1_password"));

        // :~)

        /**
         * Assert date of date type
         */
        Calendar expectedDate = Calendar.getInstance();
        expectedDate.set(2010, 10, 21);

        Assert.assertEquals(
            DateTimeUtil.convertToDate((Date)d1Table.getValue(3, "d1_time_created")),
            DateTimeUtil.convertToDate(expectedDate)
        );
        // :~)
    }

    /**
     * Tests the loading from {@link InputStream}
     */
    @Test(dependsOnMethods="normalLoad")
    public void loadFromInputStream() throws DataSetException
    {
        InputStream testInputStream = Thread.currentThread().getContextClassLoader()
            .getResourceAsStream("guru/mikelue/jpa/test/dbunit/file.yaml");

        IDataSet testDataSet = new YamlDataSet(testInputStream);

        ITable dataTable = testDataSet.getTable("tt_table");
        Assert.assertEquals(dataTable.getRowCount(), 2);
    }

    /**
     * Tests the loading from {@link Reader}
     */
    @Test(dependsOnMethods="normalLoad")
    public void loadFromReader() throws DataSetException
    {
        Reader testReader = new InputStreamReader(
            Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("guru/mikelue/jpa/test/dbunit/file.yaml"),
            Charset.forName("UTF-8")
        );

        IDataSet testDataSet = new YamlDataSet(testReader);

        ITable dataTable = testDataSet.getTable("tt_table");
        Assert.assertEquals(dataTable.getRowCount(), 2);
    }

    /**
     * <p>Tests if The content of YAML can't be recoginized as table data.</p>
     */
    @Test(expectedExceptions=RuntimeException.class, expectedExceptionsMessageRegExp=".*Mapping.*")
    public void wrongTableSyntax()
    {
        new YamlDataSet(
            "tt_data: Wrong Table Structure"
        );
    }
}
