package org.no_ip.mikelue.jpa.test.springframework;

import org.no_ip.mikelue.jpa.test.dbunit.YamlDataSet;

import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.dbunit.dataset.xml.FlatXmlProducer;
import org.dbunit.dataset.xml.XmlDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;

import org.xml.sax.InputSource;

/**
 * This utility provides static methodsto build {@link IDataSet},
 * <p>which comes from {@link Resource}.</p>
 *
 * <p>This class also supports various format of data from file({@link Resource}).</p>
 */
public class DataSetBuilder {
    private static Logger logger = LoggerFactory.getLogger(DataSetBuilder.class);

    private DataSetBuilder() {}

    /**
     * <p>Build a {@link IDataSet} containing {@link FlatXmlDataSet flat xml} from {@link Resource}.</p>
     *
     * @param resource the resource of context in SpringFramework
     *
     * @return the initialized dataset
     */
    public static IDataSet buildWithFlatXml(Resource resource)
    {
        try {
            logger.info("Build flat xml dataset from file: {}", resource.getFilename());
            return new FlatXmlDataSet(new FlatXmlProducer(
                new InputSource(resource.getInputStream())
            ));
        } catch (Exception e) {
            logger.error("Build flat xml dataset error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Build a {@link IDataSet} containing {@link XmlDataSet xml} from {@link Resource}.</p>
     *
     * @param resource the resource of context in SpringFramework
     *
     * @return the initialized dataset
     */
    public static IDataSet buildWithXml(Resource resource)
    {
        try {
            logger.info("Build xml dataset from file: {}", resource.getFilename());
            return new XmlDataSet(
                resource.getInputStream()
            );
        } catch (Exception e) {
            logger.error("Build xml dataset error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * <p>Build a {@link IDataSet} containing <a href="http://yaml.org/">YAML</a> from {@link Resource}.</p>
     *
     * @param resource the resource of context in SpringFramework
     *
     * @return the initialized dataset
     */
    public static IDataSet buildWithYaml(Resource resource)
    {
        try {
            logger.info("Build YAML dataset from file: {}", resource.getFilename());
            return new YamlDataSet(
                resource.getInputStream()
            );
        } catch (Exception e) {
            logger.error("Build YAML dataset error", e);
            throw new RuntimeException(e);
        }
    }
}
