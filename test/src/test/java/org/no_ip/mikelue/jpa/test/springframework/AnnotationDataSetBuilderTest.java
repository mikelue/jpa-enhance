package org.no_ip.mikelue.jpa.test.springframework;

import org.no_ip.mikelue.jpa.test.dbunit.annotation.DataSetBuilder;

import org.dbunit.dataset.DefaultDataSet;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.testng.annotations.Test;
import org.testng.Assert;

public class AnnotationDataSetBuilderTest {
    public AnnotationDataSetBuilderTest() {}

    /**
     * <p>Test the building for {@link IDataSet} coming from SpringFramework.</p>
     */
    @Test
    public void buildFromSpringFramework()
    {
        BeanFactory beanFactory = new AnnotationConfigApplicationContext(ContextWithDataSet.class);

        DataSetBuilder springDataSetBuilder = beanFactory.getBean(DataSetBuilder.class);

        Assert.assertNotNull(
            springDataSetBuilder.buildDataSet(DataSetManagedBySpring.class)
        );
    }
}

@Configuration
class ContextWithDataSet {
    ContextWithDataSet() {}

    @Bean
    DataSetManagedBySpring buildTestDataSet()
    {
        return new DataSetManagedBySpring();
    }
    @Bean
    DataSetBuilder buildDataSetBuilder()
    {
        return new AnnotationDataSetBuilder();
    }
}

class DataSetManagedBySpring extends DefaultDataSet {
    DataSetManagedBySpring() {}
}
