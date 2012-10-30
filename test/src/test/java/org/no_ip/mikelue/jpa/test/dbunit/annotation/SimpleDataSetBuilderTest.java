package org.no_ip.mikelue.jpa.test.dbunit.annotation;

import org.no_ip.mikelue.jpa.test.dbunit.YamlDataSet;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.testng.annotations.Test;
import org.testng.Assert;

public class SimpleDataSetBuilderTest {
    public SimpleDataSetBuilderTest() {}

    @Test
    public void buildDataSet() throws DataSetException
    {
        IDataSet resultDataSet = new SimpleDataSetBuilder()
            .buildDataSet(SimpleYamlDataSet.class);

        Assert.assertEquals(
            resultDataSet.getTableNames()[0],
            "tt_table"
        );
    }
}

class SimpleYamlDataSet extends YamlDataSet {
    SimpleYamlDataSet()
    {
        super(
            " tt_table: [" +
            "   {tb_id: 1, tb_name: 'Name of 1'}," +
            "   {tb_id: 2, tb_name: 'Name of 2'}" +
            " ]"
        );
    }
}
