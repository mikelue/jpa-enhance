package org.no_ip.mikelue.jpa.test.dbunit;

import org.no_ip.mikelue.jpa.test.DatabaseEnvUtil;
import org.no_ip.mikelue.jpa.test.dbunit.YamlDataSet;

import org.dbunit.dataset.Column;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.DefaultDataSet;
import org.dbunit.dataset.DefaultTable;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.datatype.DataType;
import org.dbunit.dataset.datatype.IDataTypeFactory;
import org.dbunit.ext.hsqldb.HsqldbDataTypeFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.testng.annotations.BeforeClass;

import java.sql.SQLException;
import javax.sql.DataSource;

/**
 * Provides common, and rough utility in testing for DBUnit.<p>
 */
public abstract class AbstractDbUnitEnvTestBase {
    private JdbcTemplate jdbcTmpl;
    private DataSource dataSource;
    private IDataTypeFactory dataTypeFactory;

    public AbstractDbUnitEnvTestBase() {}

    public JdbcTemplate getJdbcTmpl()
    {
        return jdbcTmpl;
    }
    public DataSource getDataSource()
    {
        return dataSource;
    }
    public IDataTypeFactory getDataTypeFactory()
    {
        return dataTypeFactory;
    }
    public DbUnitBuilder getDbUnitBuilder()
    {
        return DbUnitBuilder.build(
            getDataSource(), getDataTypeFactory()
        );
    }

    public IDataSet buildTestData()
    {
        return new SimpleYamlDataSet();
    }

    @BeforeClass
    protected void prepareDataSource()
    {
        dataSource = DatabaseEnvUtil.buildDataSource(getClass().getSimpleName());
        dataTypeFactory = new HsqldbDataTypeFactory();
        jdbcTmpl = new JdbcTemplate(dataSource);
    }

    /**
     * Database schema for generating test data
     */
    private final static String TEST_DB_SCHEMA_DROP =
        " DROP TABLE tt_person IF EXISTS";
    private final static String TEST_DB_SCHEMA =
        " CREATE TABLE tt_person(" +
        "   ps_id INTEGER," +
        "   ps_name VARCHAR(16)," +
        "   CONSTRAINT pk_tt_person PRIMARY KEY(ps_id)" +
        " )";
    @BeforeClass(dependsOnMethods="prepareDataSource")
    protected void prepareSchema() throws SQLException
    {
        getJdbcTmpl().update(TEST_DB_SCHEMA_DROP);
        getJdbcTmpl().update(TEST_DB_SCHEMA);
    }
    // :~)

    /**
     * Simple dataset for generating test data
     */
    public static class SimpleYamlDataSet extends YamlDataSet {
        public SimpleYamlDataSet()
        {
            super(
                " tt_person: [" +
                "   {ps_id: 1, ps_name: \"Name of 1\"}," +
                "   {ps_id: 2, ps_name: \"Name of 2\"}" +
                " ]"
            );
        }
    }
    // :~)

    /**
     * Database schema which has foreign key
     */
    private final static String TEST_DB_SCHEMA_FK_DROP =
        " DROP TABLE tt_box IF EXISTS;" +
        " DROP TABLE tt_key IF EXISTS;";
    private final static String TEST_DB_SCHEMA_FK =
        " CREATE TABLE tt_key(" +
        "   key_id INTEGER," +
        "   CONSTRAINT pk_tt_key PRIMARY KEY(key_id)" +
        " );" +
        " CREATE TABLE tt_box(" +
        "   box_id INTEGER," +
        "   box_key_id INTEGER," +
        "   CONSTRAINT pk_tt_box PRIMARY KEY(box_id)," +
        "   CONSTRAINT fk_tt_box__tt_key FOREIGN KEY(box_key_id)" +
        "       REFERENCES tt_key(key_id)" +
        " );";
    protected void prepareForeignKeySchema() throws SQLException
    {
        getJdbcTmpl().update(TEST_DB_SCHEMA_FK_DROP);
        getJdbcTmpl().update(TEST_DB_SCHEMA_FK);
    }
    // :~)
    /**
     * Dataset has foreign key for generating test data
     */
    public static class ForeignKeyYamlDataSet1 extends YamlDataSet {
        public ForeignKeyYamlDataSet1()
        {
            super(
                " tt_key: [" +
                "   {key_id: 1}," +
                "   {key_id: 2}" +
                " ]\n" +
                " tt_box: [" +
                "   {box_id: 1, box_key_id: 1}," +
                "   {box_id: 2, box_key_id: 2}" +
                " ]"
            );
        }
    }
    public static class ForeignKeyYamlDataSet2 extends YamlDataSet {
        public ForeignKeyYamlDataSet2()
        {
            super(
                " tt_box: [" +
                "   {box_id: 3, box_key_id: 1}," +
                "   {box_id: 4, box_key_id: 2}" +
                " ]"
            );
        }
    }
    // :~)
}
