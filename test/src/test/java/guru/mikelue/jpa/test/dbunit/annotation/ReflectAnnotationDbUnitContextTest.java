package guru.mikelue.jpa.test.dbunit.annotation;

import guru.mikelue.jpa.test.dbunit.DbUnitBuilder;

import mockit.Expectations;
import mockit.Mock;
import mockit.Mocked;
import mockit.MockUp;
import org.springframework.util.ReflectionUtils;
import org.testng.annotations.Test;
import org.testng.Assert;

import java.lang.reflect.Method;

public class ReflectAnnotationDbUnitContextTest {
    @Mocked
    private DbUnitBuilder mockDbUnitBuilder;

    public ReflectAnnotationDbUnitContextTest() {}

    /**
     * <p>Test the process for {@link OpDataSet} annotating a {@link Class} in before operation.</p>
     */
    @Test
    public void beforeOperationForClass()
    {
        final ReflectAnnotationDbUnitContext testContext = buildContext();

        new Expectations(testContext) {{
            testContext.beforeOperation((OpDataSet)any);
            times = 1;
        }};

        testContext.beforeOperation(FakeAnnotatedClass.class);
    }
    /**
     * <p>Test the process for {@link OpDataSet} annotating a {@link Class} in after operation.</p>
     */
    @Test
    public void afterOperationForClass()
    {
        final ReflectAnnotationDbUnitContext testContext = buildContext();

        new Expectations(testContext) {{
            testContext.afterOperation((OpDataSet)any);
            times = 1;
        }};

        testContext.afterOperation(FakeAnnotatedClass.class);
    }
    /**
     * <p>Test the process for {@link OpDataSet} annotating a {@link Method} in before operation.</p>
     */
    @Test
    public void beforeOperationForMethod()
    {
        final ReflectAnnotationDbUnitContext testContext = buildContext();

        new Expectations(testContext) {{
            testContext.beforeOperation((OpDataSet)any);
            times = 1;
        }};

        testContext.beforeOperation(ReflectionUtils.findMethod(ReflectAnnotationDbUnitContextTest.class, "fakeMethod"));
    }
    /**
     * <p>Test the process for {@link OpDataSet} annotating a {@link Method} in after operation.</p>
     */
    @Test
    public void afterOperationForMethod()
    {
        final ReflectAnnotationDbUnitContext testContext = buildContext();

        new Expectations(testContext) {{
            testContext.afterOperation((OpDataSet)any);
            times = 1;
        }};

        testContext.afterOperation(ReflectionUtils.findMethod(ReflectAnnotationDbUnitContextTest.class, "fakeMethod"));
    }

    private ReflectAnnotationDbUnitContext buildContext()
    {
        return new ReflectAnnotationDbUnitContext(mockDbUnitBuilder);
    }

    /**
     * Annotated object for testing
     */
    @OpDataSet(dataSetClazz={})
    class FakeAnnotatedClass {}

    @OpDataSet(dataSetClazz={})
    private void fakeMethod() {}
    // :~)
}
