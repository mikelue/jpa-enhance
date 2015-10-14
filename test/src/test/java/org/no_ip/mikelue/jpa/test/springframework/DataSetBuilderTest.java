package org.no_ip.mikelue.jpa.test.springframework;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.testng.annotations.Test;
import org.testng.annotations.BeforeClass;
import org.testng.Assert;

public class DataSetBuilderTest {
    private ResourceLoader resourceLoader;

    public DataSetBuilderTest() {}

    /**
     * <p>Test the loading of resource with flat XML format.</p>
     */
    @Test
    public void buildWithFlatXml() throws DataSetException
    {
        IDataSet testDataSet = DataSetBuilder.buildWithFlatXml(
            resourceLoader.getResource(
                "classpath:org/no_ip/mikelue/jpa/test/springframework/DataSetBuilderTest-buildWithFlatXml.xml"
            )
        );

        ITable table = testDataSet.getTable("tt_user");
        Assert.assertEquals(
            table.getRowCount(), 2
        );
    }

    /**
     * <p>Test the loading of resource with normal XML format.</p>
     */
    @Test
    public void buildWithXml() throws DataSetException
    {
        IDataSet testDataSet = DataSetBuilder.buildWithXml(
            resourceLoader.getResource(
                "classpath:org/no_ip/mikelue/jpa/test/springframework/DataSetBuilderTest-buildWithXml.xml"
            )
        );

        ITable table = testDataSet.getTable("tt_user");
        Assert.assertEquals(
            table.getRowCount(), 2
        );
    }

    /**
     * <p>Test the loading of resource with <a href="http://yaml.org/">YAML</a> format.</p>
     */
    @Test
    public void buildWithYaml() throws DataSetException
    {
        IDataSet testDataSet = DataSetBuilder.buildWithYaml(
            resourceLoader.getResource(
                "classpath:org/no_ip/mikelue/jpa/test/springframework/DataSetBuilderTest-buildWithYaml.yaml"
            )
        );

        ITable table = testDataSet.getTable("tt_user");
        Assert.assertEquals(
            table.getRowCount(), 2
        );
    }

    @BeforeClass
    private void initResourceLoader()
    {
        resourceLoader = new DefaultResourceLoader();
    }
}
