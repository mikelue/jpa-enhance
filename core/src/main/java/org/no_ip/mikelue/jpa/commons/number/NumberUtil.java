package org.no_ip.mikelue.jpa.commons.number;

/**
 * 數字相關的工具類別。<p>
 */
public class NumberUtil {
	private NumberUtil() {}

	/**
	 * 將 long 的資料，轉為最接近的 int 值。<p>
	 *
	 * 若 longValue > {@link Integer#MAX_VALUE}，傳回 {@link Integer#MAX_VALUE}<p>
	 * 若 longValue < {@link Integer#MIN_VALUE}，傳回 {@link Integer#MIN_VALUE}<p>
	 * 否則傳回 int value
	 *
	 * @param longValue
	 */
	public static int intValue(long longValue)
	{
		if (longValue > Integer.MAX_VALUE) {
			return Integer.MAX_VALUE;
		}
		if (longValue < Integer.MIN_VALUE) {
			return Integer.MIN_VALUE;
		}

		return Long.valueOf(longValue).intValue();
	}
}
