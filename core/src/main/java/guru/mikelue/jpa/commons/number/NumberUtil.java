package guru.mikelue.jpa.commons.number;

/**
 * 數字相關的工具類別.
 */
public class NumberUtil {
	private NumberUtil() {}

	/**
	 * 將 long 的資料，轉為最接近的 int 值.
	 *
	 * <p>
	 * 若 longValue &gt; {@link Integer#MAX_VALUE}，傳回 {@link Integer#MAX_VALUE}<br>
	 * 若 longValue &lt; {@link Integer#MIN_VALUE}，傳回 {@link Integer#MIN_VALUE}<br>
	 * 否則傳回 int value
	 * </p>
	 *
	 * @param longValue The value to be converted
	 *
	 * @return The result value
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
