package org.no_ip.mikelue.jpa.paging;

import org.testng.Assert;
import org.testng.annotations.Test;

/**
 * 介面要求分頁資料測試
 */
public class PagingRequestBeanTest {
	public PagingRequestBeanTest() {}

	/**
	 * 測試預設值
	 */
	@Test
	public void validDefaultValue()
	{
		PagingRequestBean bean = new PagingRequestBean(10);

		Assert.assertEquals(1, bean.getPageNumberOfTarget());
		Assert.assertEquals(0, bean.getPageNumberAfterTarget());
	}

	/**
	 * 測試不合法的目前頁碼
	 */
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void invalidPageNumberOfTarget()
	{
		PagingRequestBean bean = new PagingRequestBean();
		bean.setPageNumberOfTarget(-2);
	}
	/**
	 * 測試不合法的每頁筆數
	 */
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void invalidPageSize()
	{
		PagingRequestBean bean = new PagingRequestBean();
		bean.setPageSize(0);
	}
	/**
	 * 測試不合法的本頁後顯示頁數
	 */
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void invalidPageNumberAfterTarget()
	{
		PagingRequestBean bean = new PagingRequestBean();
		bean.setPageNumberAfterTarget(-1);
	}
	/**
	 * 測試設定最後一頁
	 */
	@Test
	public void lastPage()
	{
		PagingRequestBean bean = new PagingRequestBean();
		bean.setPageNumberOfTarget(PagingRequestBean.LAST_PAGE);
	}
	/**
	 * 測試檢查 Bean 的方法
	 */
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void checkValidPagingBean()
	{
		PagingRequestBean bean = new PagingRequestBean();
		PagingRequestBean.checkValidPagingBean(bean);
	}

	/**
	 * 測試是否還有前一分頁的檢查
	 */
	@Test
	public void hasPreviousPage()
	{
		PagingRequestBean bean = new PagingRequestBean(2, 10, 0);

		Assert.assertTrue(bean.hasPreviousPage());
		bean.setPageNumberOfTarget(1);
		Assert.assertFalse(bean.hasPreviousPage());
	}
}
