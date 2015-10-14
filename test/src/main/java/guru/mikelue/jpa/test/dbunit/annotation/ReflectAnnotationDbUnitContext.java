package guru.mikelue.jpa.test.dbunit.annotation;

import guru.mikelue.jpa.test.dbunit.DbUnitBuilder;

import org.dbunit.dataset.IDataSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * This context of annotation has additional method to process annotation
 * from {@link Class} or {@link Method} directly.
 */
public class ReflectAnnotationDbUnitContext extends AnnotationDbUnitContext {
    private Logger logger = LoggerFactory.getLogger(ReflectAnnotationDbUnitContext.class);

    /**
     * Initialize this object with fundamental environment.
	 *
     * <p>This constructor would use {@link SimpleDataSetBuilder} as default {@link DataSetBuilder}.</p>
     *
     * @param newDbUnitBuilder The DbUnit environment
     * @param newDataSetBuilder The builder for generating {@link IDataSet}
     */
    public ReflectAnnotationDbUnitContext (DbUnitBuilder newDbUnitBuilder, DataSetBuilder newDataSetBuilder)
    {
        super(newDbUnitBuilder, newDataSetBuilder);
        logger.debug("Initialized with dataset builder: {}", newDbUnitBuilder.getClass().getSimpleName());
    }
    /**
     * Initialize this object with fundamental environment
     *
     * @param newDbUnitBuilder The DbUnit environment
     */
    public ReflectAnnotationDbUnitContext (DbUnitBuilder newDbUnitBuilder)
    {
        super(newDbUnitBuilder);
    }

    /**
     * Perform {@link DataSetOperation dataset operation} to database with "before action" meaning.
     *
     * <p>This clazz would use {@link #getDataSetBuilder()} to retrive the {@link IDataSet}.</p>
     *
     * <p>It is fine that if the clazz isn't annotated with {@link OpDataSet}.</p>
     *
     * @param clazz The clazz that may have {@link OpDataSet} annotation
     *
     * @see #afterOperation(Class)
     */
    public void beforeOperation(Class<?> clazz)
    {
        logger.debug("Process class at before operation: {}", clazz.getSimpleName());

        OpDataSet annotation = clazz.getAnnotation(OpDataSet.class);
        if (annotation == null) {
            logger.info("[Before Operation] Class: {} isn't annotated with @OpDataSet", clazz.getSimpleName());
            return;
        }

        super.beforeOperation(annotation);
    }
    /**
     * Perform {@link DataSetOperation dataset operation} to database with "after action" meaning.
     *
     * <p>This clazz would use {@link #getDataSetBuilder()} to retrive the {@link IDataSet}.</p>
     *
     * <p>It is fine that if the clazz doesn't annotated with {@link OpDataSet}.</p>
     *
     * @param clazz The clazz that may have {@link OpDataSet} annotation
     *
     * @see #beforeOperation(Class)
     */
    public void afterOperation(Class<?> clazz)
    {
        logger.debug("Process class at after operation: {}", clazz.getSimpleName());

        OpDataSet annotation = clazz.getAnnotation(OpDataSet.class);
        if (annotation == null) {
            logger.info("[After Operation] Class: {} isn't annotated with @OpDataSet", clazz.getSimpleName());
            return;
        }

        super.afterOperation(annotation);
    }

    /**
     * Perform {@link DataSetOperation dataset operation} to database with "before action" meaning.
     *
     * <p>This method would use {@link #getDataSetBuilder()} to retrive the {@link IDataSet}.</p>
     *
     * <p>It is fine that if the method isn't annotated with {@link OpDataSet}.</p>
     *
     * @param method The method that may have {@link OpDataSet} annotation
     *
     * @see #afterOperation(Method)
     */
    public void beforeOperation(Method method)
    {
        logger.debug("Process method at before operation: {}", method.getName());

        OpDataSet annotation = method.getAnnotation(OpDataSet.class);
        if (annotation == null) {
            logger.debug("[Before Operation] Method: {} isn't annotated with @OpDataSet", method.getName());
            return;
        }

        super.beforeOperation(annotation);
    }
    /**
     * Perform {@link DataSetOperation dataset operation} to database with "after action" meaning.
     *
     * <p>This method would use {@link #getDataSetBuilder()} to retrive the {@link IDataSet}.</p>
     *
     * <p>It is fine that if the method doesn't annotated with {@link OpDataSet}.</p>
     *
     * @param method The method that may have {@link OpDataSet} annotation
     *
     * @see #beforeOperation(Method)
     */
    public void afterOperation(Method method)
    {
        logger.debug("Process method at after operation: {}", method.getName());

        OpDataSet annotation = method.getAnnotation(OpDataSet.class);
        if (annotation == null) {
            logger.debug("[After Operation] Method: {} isn't annotated with @OpDataSet", method.getName());
            return;
        }

        super.afterOperation(annotation);
    }
}
