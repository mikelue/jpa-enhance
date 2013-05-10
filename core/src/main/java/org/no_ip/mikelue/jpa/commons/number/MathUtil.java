package org.no_ip.mikelue.jpa.commons.number;

/**
 * 數學運算相關的工具類別。<p>
 *
 * 本類別中整數({@link Integer})運算的方法，若產生溢位，會傳回最大 {@link Integer#MAX_VALUE}，最小 {@link Integer#MIN_VALUE} 的值
 *
 * @see NumberUtil
 */
public class MathUtil {
	private MathUtil() {}

	/**
	 * 相加兩個整數，會處理溢位的問題。<p>
	 *
	 * @param a 被加數
	 * @param b 加數
	 *
	 * @return 介於 {@link Integer#MAX_VALUE} 至 {@link Integer#MIN_VALUE} 的值
	 */
	public static int add(int a, int b)
	{
		return NumberUtil.intValue((long)a + (long)b);
	}
	/**
	 * 相減兩個整數，會處理溢位的問題。<p>
	 *
	 * @param a 被減數
	 * @param b 減數
	 *
	 * @return 介於 {@link Integer#MAX_VALUE} 至 {@link Integer#MIN_VALUE} 的值
	 */
	public static int subtract(int a, int b)
	{
		return NumberUtil.intValue((long)a - (long)b);
	}
	/**
	 * 相乘兩個整數，會處理溢位的問題。<p>
	 *
	 * @param a 被乘數
	 * @param b 乘數
	 *
	 * @return 介於 {@link Integer#MAX_VALUE} 至 {@link Integer#MIN_VALUE} 的值
	 */
	public static int multiply(int a, int b)
	{
		return NumberUtil.intValue((long)a * (long)b);
	}
}
