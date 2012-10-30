package org.no_ip.mikelue.jpa.test.dbunit.annotation;

import org.dbunit.dataset.IDataSet;

import java.lang.annotation.*;

/**
 * Operation for {@link IDataSet} before/after action.<p>
 *
 * This annotation defines the behaviour about how to control data before and after at some points.
 * Currently, this annotation supports class and method scope.<p>
 *
 * The intances for {@link #dataSetClazz()} should be initialized by {@link AnnotationDbUnitContext}.<p>
 */
@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface OpDataSet {
    /**
     * The running operation for {@link #dataSetClazz()} before action.<p>
     */
    DataSetOperation beforeOperation() default DataSetOperation.None;
    /**
     * The running operation for {@link #dataSetClazz()} after action.<p>
     */
    DataSetOperation afterOperation() default DataSetOperation.None;

    /**
     * The data source of {@link IDataSet} clazz.<p>
     */
    Class<? extends IDataSet>[] dataSetClazz();
}
