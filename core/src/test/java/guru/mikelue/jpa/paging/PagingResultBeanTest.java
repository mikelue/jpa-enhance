package guru.mikelue.jpa.paging;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.Assert;

/**
 * 查詢資料結果分頁物件測試
 */
public class PagingResultBeanTest {
	public PagingResultBeanTest() {}

	/**
	 * 錯誤的 {@link PagingRequestBean} 測試
	 */
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void chekcValidPagingRequestBean()
	{
		new PagingResultBean(new PagingRequestBean());
	}

	/**
	 * [取得資料開始筆數] {@link PagingRequestBean} 為 null 測試
	 */
	@Test(expectedExceptions=IllegalStateException.class)
	public void getFirstRecordNumberWithNullPagingRequestBean()
	{
		new PagingResultBean().getFirstRecordNumber();
	}

	/**
	 * [取得資料最後筆數] {@link PagingRequestBean} 為 null 測試
	 */
	@Test(expectedExceptions=IllegalStateException.class)
	public void getLastRecordNumberWithNullPagingRequestBean()
	{
		new PagingResultBean().getLastRecordNumber();
	}

	/**
	 * [設定結果分頁資料] 結果筆數不合法測試
	 */
	@Test(expectedExceptions=IllegalArgumentException.class)
	public void setResultSizeWithInvalidResultSize()
	{
		new PagingResultBean(new PagingRequestBean(10)).setResultSize(-1);
	}

	/**
	 * [設定結果分頁資料] {@link PagingRequestBean} 為 null 測試
	 */
	@Test(expectedExceptions=IllegalStateException.class)
	public void setResultSizeWithNullPagingRequestBean()
	{
		new PagingResultBean().setResultSize(20);
	}

	/**
	 * [取得資料開始筆數] 相關測試(包含極大值)
	 *
	 * @param requestBean 測試分頁請求
	 * @param expectedFirstRecordNumber 預期的傳回值
	 */
	@Test(dataProvider="variousFirstRecordNumber")
	public void getFirstRecordNumber(
		PagingRequestBean requestBean, int expectedFirstRecordNumber
	) {
		Assert.assertEquals(
			new PagingResultBean(requestBean).getFirstRecordNumber(),
			expectedFirstRecordNumber
		);
	}
	/**
	 * [取得資料最後筆數] 相關測試(包含極大值)
	 *
	 * @param requestBean 測試分頁請求
	 * @param expectedLastRecordNumber 預期的傳回值
	 */
	@Test(dataProvider="variousLastRecordNumber")
	public void getLastRecordNumber(
		PagingRequestBean requestBean, int expectedLastRecordNumber
	) {
		Assert.assertEquals(
			new PagingResultBean(requestBean).getLastRecordNumber(),
			expectedLastRecordNumber
		);
	}

	/**
	 * 測試設定第一階段查詢結果測試
	 *
	 * @param testRequestBean 要測試的分頁請求設定
	 * @param testDataCount 測試的資料結果筆數
	 * @param expectedResultStatus 預期的結果狀態
	 * @param expectedPageNumberOfTarget 預期的所在頁碼
	 * @param expectedPageNumberAfterTarget 預期本頁後頁碼
	 */
	@Test(dataProvider="variousResultSizeInFirstPhase")
	public void setResultSizeInFirstPhase(
		PagingRequestBean testRequestBean, int testDataCount,
		ResultStatus expectedResultStatus, int expectedPageNumberOfTarget, int expectedPageNumberAfterTarget
	) {
		PagingResultBean resultBean = new PagingResultBean(testRequestBean);
		resultBean.setResultSize(testDataCount);
		assertPagingResult(
			resultBean,
			expectedResultStatus, expectedPageNumberOfTarget, expectedPageNumberAfterTarget
		);
	}

	/**
	 * 測試設定最後階段查詢結果測試
	 *
	 * @param testRequestBean 要測試的分頁請求設定
	 * @param testDataCount 測試的資料結果筆數
	 * @param expectedResultStatus 預期的結果狀態
	 * @param expectedPageNumberOfTarget 預期的所在頁碼
	 * @param expectedPageNumberAfterTarget 預期本頁後頁碼
	 */
	@Test(dataProvider="variousResultSizeInLastPhase")
	public void setResultSizeInLastPhase(
		PagingRequestBean testRequestBean, int testDataCount,
		ResultStatus expectedResultStatus, int expectedPageNumberOfTarget, int expectedPageNumberAfterTarget
	)
	{
		PagingResultBean resultBean = new PagingResultBean(testRequestBean);
		resultBean.setResultSize(testDataCount, FetchPhase.LastFetch);
		assertPagingResult(
			resultBean,
			expectedResultStatus, expectedPageNumberOfTarget, expectedPageNumberAfterTarget
		);
	}

	private static void assertPagingResult(
		PagingResultBean resultBean, ResultStatus expectedResultStatus,
		int expectedPageNumberOfTarget, int expectedPageNumberAfterTarget
	) {
		Assert.assertEquals(resultBean.getResultStatus(), expectedResultStatus);
		Assert.assertEquals(resultBean.getResultPageNumberOfTarget(), expectedPageNumberOfTarget);
		Assert.assertEquals(resultBean.getResultTotalPageNumber(), expectedPageNumberAfterTarget);
	}

	@DataProvider(name="variousResultSizeInLastPhase")
	private Object[][] variousResultSizeInLastPhase()
	{
		return new Object[][] {
			/**
			 * 一般情況測試
			 */
			new Object[] {
				new PagingRequestBean(5, 10, 3), 40,
				ResultStatus.ReachLastPage, 4, 0
			},
			// :~)
			/**
			 * 一般情況測試(模擬第二次的資料足夠)
			 */
			new Object[] {
				new PagingRequestBean(5, 10, 3), 60,
				ResultStatus.ReachLastPage, 6, 0
			},
			// :~)
			/**
			 * 一般情況測試(1 筆資料)
			 */
			new Object[] {
				new PagingRequestBean(5, 10, 3), 1,
				ResultStatus.ReachLastPage, 1, 0
			},
			/**
			 * 一般情況測試(結果筆數無法整除每頁筆數)
			 */
			new Object[] {
				new PagingRequestBean(5, 10, 3), 77,
				ResultStatus.ReachLastPage, 8, 0
			},
			/**
			 * 最後一頁測試
			 */
			new Object[] {
				new PagingRequestBean(PagingRequestBean.LAST_PAGE, 10, 3), 11,
				ResultStatus.ReachLastPage, 2, 0
			},
			// :~)
			/**
			 * 極大值測試(要求頁數)
			 */
			new Object[] {
				new PagingRequestBean(Integer.MAX_VALUE, 10, 3), 77,
				ResultStatus.ReachLastPage, 8, 0
			},
			/**
			 * 極大值測試(每頁筆數)
			 */
			new Object[] {
				new PagingRequestBean(5, Integer.MAX_VALUE, 3), 77,
				ResultStatus.ReachLastPage, 1, 0
			},
			// :~)
			/**
			 * 極大值測試(每頁筆數與結果筆數)
			 */
			new Object[] {
				new PagingRequestBean(5, Integer.MAX_VALUE, 3), Integer.MAX_VALUE,
				ResultStatus.ReachLastPage, 1, 0
			},
			// :~)
			/**
			 * 找不到資料
			 */
			new Object[] {
				new PagingRequestBean(5, 10, 3), 0,
				ResultStatus.EmptyData, 0, 0
			}
			// :~)
		};
	}
	@DataProvider(name="variousResultSizeInFirstPhase")
	private Object[][] variousResultSizeInFirstPhase()
	{
		return new Object[][] {
			/**
			 * 一般情況測試
			 */
			new Object[] {
				new PagingRequestBean(5, 10, 3), 41,
				ResultStatus.HasMorePage, 5, 3
			},
			/**
			 * 一般情況測試(滿後 N 頁，已到最後一頁)
			 */
			new Object[] {
				new PagingRequestBean(5, 10, 3), 38,
				ResultStatus.ReachLastPage, 5, 3
			},
			/**
			 * 一般情況測試(不滿後 N 頁)
			 */
			new Object[] {
				new PagingRequestBean(5, 10, 3), 20,
				ResultStatus.ReachLastPage, 5, 1
			},
			// :~)
			/**
			 * 一般情況測試(只有一筆資料)
			 */
			new Object[] {
				new PagingRequestBean(5, 10, 3), 1,
				ResultStatus.ReachLastPage, 5, 0
			},
			/**
			 * 最後一頁測試
			 */
			new Object[] {
				new PagingRequestBean(PagingRequestBean.LAST_PAGE, 10, 3), 30,
				ResultStatus.ReachLastPage, 3, 0
			},
			// :~)
			/**
			 * 一般情況測試(後 0 頁，後面還有其它頁)
			 */
			new Object[] {
				new PagingRequestBean(5, 10, 0), 11,
				ResultStatus.HasMorePage, 5, 0
			},
			// :~)
			/**
			 * 一般情況測試(後 0 頁，後面沒有其它頁)
			 */
			new Object[] {
				new PagingRequestBean(5, 10, 0), 10,
				ResultStatus.ReachLastPage, 5, 0
			},
			// :~)
			/**
			 * 極大值測試(每頁筆數)
			 */
			new Object[] {
				new PagingRequestBean(1, Integer.MAX_VALUE, 3), 1,
				ResultStatus.ReachLastPage, 1, 0
			},
			/**
			 * 極大值測試(要求頁後頁數)
			 */
			new Object[] {
				new PagingRequestBean(1, 10, Integer.MAX_VALUE), 20,
				ResultStatus.ReachLastPage, 1, 1
			},
			/**
			 * 極大值測試(要求頁)
			 */
			new Object[] {
				new PagingRequestBean(Integer.MAX_VALUE, 10, Integer.MAX_VALUE), 20,
				ResultStatus.ReachLastPage, Integer.MAX_VALUE, 1
			},
			/**
			 * 極大值測試(結果筆數)
			 */
			new Object[] {
				new PagingRequestBean(5, 10, 3), Integer.MAX_VALUE,
				ResultStatus.HasMorePage, 5, 3
			},
			// :~)
			/**
			 * 全部極大值測試
			 */
			new Object[] {
				new PagingRequestBean(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE), Integer.MAX_VALUE,
				ResultStatus.ReachLastPage, Integer.MAX_VALUE, 0
			},
			// :~)
			/**
			 * 沒有任何資料
			 */
			new Object[] {
				new PagingRequestBean(5, 10, 3), 0,
				ResultStatus.EmptyData, 0, 0
			}
			// :~)
		};
	}
	@DataProvider(name="variousLastRecordNumber")
	private Object[][] variousLastRecordNumber()
	{
		return new Object[][] {
			/**
			 * 本頁後顯示頁數為 0
			 */
			new Object[] {
				new PagingRequestBean(2, 20, 0), 21
			},
			// :~)
			/**
			 * 本頁後顯示頁數為 N
			 */
			new Object[] {
				new PagingRequestBean(2, 20, 3), 81
			},
			// :~)
			/**
			 * 最後一頁測試
			 */
			new Object[] {
				new PagingRequestBean(PagingRequestBean.LAST_PAGE, 20, 3), Integer.MAX_VALUE
			},
			// :~)
			/**
			 * 極大值測試(要求頁)
			 */
			new Object[] {
				new PagingRequestBean(Integer.MAX_VALUE, 10, 0), 11
			},
			// :~)
			/**
			 * 極大值測試(每頁筆數)
			 */
			new Object[] {
				new PagingRequestBean(1, Integer.MAX_VALUE, 1), Integer.MAX_VALUE
			},
			/**
			 * 極大值測試(要求頁後筆數)
			 */
			new Object[] {
				new PagingRequestBean(1, Integer.MAX_VALUE, Integer.MAX_VALUE), Integer.MAX_VALUE
			}
		};
	}
	@DataProvider(name="variousFirstRecordNumber")
	private Object[][] variousFirstRecordNumber()
	{
		return new Object[][] {
			/**
			 * 第一頁測試
			 */
			new Object[] {
				new PagingRequestBean(1, 20, 3), 0
			},
			// :~)
			/**
			 * 第一頁測試(極端值)
			 */
			new Object[] {
				new PagingRequestBean(1, Integer.MAX_VALUE, Integer.MAX_VALUE), 0
			},
			// :~)
			/**
			 * 第 N 頁測試
			 */
			new Object[] {
				new PagingRequestBean(5, 5, 3), 20
			},
			// :~)
			/**
			 * 第 N 頁測試(極端值)
			 */
			new Object[] {
				new PagingRequestBean(5, 5, Integer.MAX_VALUE), 20
			},
			// :~)
			/**
			 * 最後一頁測試
			 */
			new Object[] {
				new PagingRequestBean(PagingRequestBean.LAST_PAGE, 10, 0), 0
			},
			// :~)
			/**
			 * 最後一頁測試(極端值)
			 */
			new Object[] {
				new PagingRequestBean(PagingRequestBean.LAST_PAGE, 10, Integer.MAX_VALUE), 0
			},
			// :~)
			/**
			 * 極大值測試(每頁筆數極大)
			 */
			new Object[] {
				new PagingRequestBean(1, Integer.MAX_VALUE, Integer.MAX_VALUE), 0
			},
			// :~)
			/**
			 * 極大值測試(要求頁數極大)
			 */
			new Object[] {
				new PagingRequestBean(Integer.MAX_VALUE, 1, 0), Integer.MAX_VALUE - 1
			}
			// :~)
		};
	}
}
