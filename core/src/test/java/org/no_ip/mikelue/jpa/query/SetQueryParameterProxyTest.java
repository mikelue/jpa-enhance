package org.no_ip.mikelue.jpa.query;

import javax.persistence.Query;

import mockit.Expectations;
import mockit.Mocked;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * 測試設定 {@link Query} 物件動態參數 Porxy
 */
public class SetQueryParameterProxyTest {
	public SetQueryParameterProxyTest() {}

	@Mocked private Query query;

	/**
	 * 測試依值是不是 null，設定 Named JPQL Parameter
	 */
	@Test
	public void setParameterIfNotNull()
	{
		final String testParamName = "p1";

		new Expectations()
		{{
			query.setParameter(testParamName, anyString);
			times = 1;
		}};

		SetQueryParameterProxy proxy = new SetQueryParameterProxy(query);

		proxy.setParameterIfNotNull(testParamName, "TestValue"); // 非 null 值的設定
		proxy.setParameterIfNotNull(testParamName, null); // null 值的設定
	}

	/**
	 * 測試依值是不是 null，設定 Numeric JPQL Parameter
	 */
	@Test
	public void setAutoPositionParameterIfNotNull()
	{
		new Expectations()
		{{
			query.setParameter(1, anyString);
			times = 1;

			query.setParameter(2, anyString);
			times = 1;
		}};

		SetQueryParameterProxy proxy = new SetQueryParameterProxy(query);

		proxy.setAutoPositionParameterIfNotNull("TestValue1");  // 測試設定非 Null 值
		proxy.setAutoPositionParameterIfNotNull("TestValue2"); // 測試不會重覆設定同一 Parameter 編號
		proxy.setAutoPositionParameterIfNotNull(null); // 測試設定 Null 值
	}
}
