package guru.mikelue.jpa.data;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AccessorHelperTest {
    public AccessorHelperTest() {}

    /**
     * Tests the convertion from {@link Enum} to {@link String}.<p>
     */
    @Test
    public void getStringFromEnum()
    {
        Assert.assertNull(AccessorHelper.<FooBarType>getStringFromEnum(null));
        Assert.assertEquals(AccessorHelper.getStringFromEnum(FooBarType.Foo), "Foo");
    }
    /**
     * Tests the convertion from {@link Enum} to {@link String}.<p>
     */
    @Test
    public void getEnumFromString()
    {
        Assert.assertNull(AccessorHelper.getEnumFromString(FooBarType.class, null));
        Assert.assertEquals(AccessorHelper.getEnumFromString(FooBarType.class, "Bar"), FooBarType.Bar);
    }
}

enum FooBarType {
    Foo, Bar;
}
