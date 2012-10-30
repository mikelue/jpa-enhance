package org.no_ip.mikelue.jpa.test.springframework;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testng.Assert;
import org.testng.annotations.Test;

import javax.inject.Named;

@Configuration
public class ResourceYamlDataSetTest {
    public ResourceYamlDataSetTest() {}

    /**
     * Tests the loading of data set by spring's resource
     */
    @Test
    public void load() throws DataSetException
    {
        final String beanName = "fakeDataset";

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(ResourceYamlDataSetTest.class);
        context.refresh();

        IDataSet testDataSet = context.getBean(IDataSet.class);
        ITable testTable = testDataSet.getTable("tab_1");

        Assert.assertEquals(2, testTable.getRowCount());
        Assert.assertEquals(testTable.getValue(0, "col_1"), "name_1");
        Assert.assertEquals(testTable.getValue(1, "col_2"), new Integer(20));
    }

    @Bean
    public IDataSet buildTestDataSet()
    {
        return new ResourceYamlDataSet("classpath:org/no_ip/mikelue/jpa/test/springframework/ResourceYamlDataSetTest.yaml");
    }
}
