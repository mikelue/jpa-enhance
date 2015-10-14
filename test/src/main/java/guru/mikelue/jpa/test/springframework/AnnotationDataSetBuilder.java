package guru.mikelue.jpa.test.springframework;

import guru.mikelue.jpa.test.dbunit.annotation.BuildDataSetException;
import guru.mikelue.jpa.test.dbunit.annotation.DataSetBuilder;

import org.dbunit.dataset.IDataSet;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.BeansException;

/**
 * The builder which supports building {@link IDataSet} from {@link BeanFactory} in
 * <p><a href="http://www.springsource.org/">SpringFramework</a>'s environment.</p>
 *
 * <p>This object should be managed by SpringFramework for convenience.</p>
 */
public class AnnotationDataSetBuilder implements DataSetBuilder, BeanFactoryAware {
    private BeanFactory beanFactory;

    /**
     * <p>This object should be managed in SpringFramework for easier usage.</p>
     *
     * @see #setBeanFactory
     */
    public AnnotationDataSetBuilder() {}

    /**
     * <p>Build {@link IDataSet} coming from {@link BeanFactory#getBean(Class)}.</p>
     *
     * @param classOfDataSet The type of bean defined in SpringFramework
     *
     * @return Bean of {@link IDataSet} managed by SpringFramework
     */
    @Override
    public IDataSet buildDataSet(Class<? extends IDataSet> classOfDataSet) throws BuildDataSetException
    {
        try {
            return beanFactory.getBean(classOfDataSet);
        } catch (BeansException e) {
            throw new BuildDataSetException(classOfDataSet, e);
        }
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException
    {
        this.beanFactory = beanFactory;
    }
}
