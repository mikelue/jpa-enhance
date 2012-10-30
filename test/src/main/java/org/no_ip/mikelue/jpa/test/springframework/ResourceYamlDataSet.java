package org.no_ip.mikelue.jpa.test.springframework;

import org.no_ip.mikelue.jpa.test.dbunit.YamlDataSet;
import org.no_ip.mikelue.jpa.test.springframework.DataSetBuilder;

import org.dbunit.dataset.AbstractDataSet;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITableIterator;
import org.springframework.core.io.ResourceLoader;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

/**
 * This bean <b>must be managed in Spring environment</b>.<p>
 *
 * The {@link YamlDataSet} would be initialized after this bean has been initialized.<p>
 */
public class ResourceYamlDataSet extends AbstractDataSet {
    @Inject
    private ResourceLoader resourceLoader;

    private IDataSet yamlDataSet;
    private String resourcePath;

    public ResourceYamlDataSet(String newResourcePath)
    {
        resourcePath = newResourcePath;
    }

    /**
     * This method would be delegated to {@link YamlDataSet}.<p>
     */
    @Override
    protected ITableIterator createIterator(boolean reversed)
        throws DataSetException
    {
        if (reversed) {
            return yamlDataSet.reverseIterator();
        }

        return yamlDataSet.iterator();
    }

    @PostConstruct
    private void initializeYamlDataSet()
    {
        yamlDataSet = DataSetBuilder.buildWithYaml(
            resourceLoader.getResource(resourcePath)
        );
    }
}
