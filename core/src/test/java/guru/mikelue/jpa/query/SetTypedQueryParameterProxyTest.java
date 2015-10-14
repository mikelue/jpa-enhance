package guru.mikelue.jpa.query;

import javax.persistence.TypedQuery;

import mockit.Expectations;
import mockit.Mocked;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * 測試設定 {@link TypedQuery} 物件重態參數 Porxy
 */
public class SetTypedQueryParameterProxyTest {
	public SetTypedQueryParameterProxyTest() {}

	@Mocked private TypedQuery<String> typedQuery;

	@Test
	public void setParameterIfNotNull()
	{
		final String testParamName = "p1";

		new Expectations()
		{{
			typedQuery.setParameter(testParamName, anyString);
			times = 1;
		}};

		SetTypedQueryParameterProxy<String> proxy = new SetTypedQueryParameterProxy<String>(typedQuery);

		proxy.setParameterIfNotNull(testParamName, "TestValue");  // 非 null 值的設定
		proxy.setParameterIfNotNull(testParamName, null);  // null 值的設定
	}

	@Test
	public void setAutoPositionParameterIfNotNull()
	{
		new Expectations()
		{{
			typedQuery.setParameter(1, anyString);
			times = 1;

			typedQuery.setParameter(2, anyString);
			times = 1;
		}};

		SetTypedQueryParameterProxy<String> proxy = new SetTypedQueryParameterProxy<String>(typedQuery);

		proxy.setAutoPositionParameterIfNotNull("TestValue1"); // 測試設定非 Null 值
		proxy.setAutoPositionParameterIfNotNull("TestValue2"); // 測試不會重覆設定同一 Parameter 編號
		proxy.setAutoPositionParameterIfNotNull(null); // 測試設定 Null 值
	}
}
