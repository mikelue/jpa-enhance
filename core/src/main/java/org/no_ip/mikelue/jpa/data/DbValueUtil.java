package org.no_ip.mikelue.jpa.data;

import java.util.EnumSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The utility to process {@link DbValueGetter}.<p>
 *
 * This class generates unmodifiable map which map the integral value to enumeration type.<p>
 *
 * @see DbValueGetter
 */
public class DbValueUtil {
	private DbValueUtil() {}

	/**
	 * Convert a {@link DbValueGetter} type(in enum type) to Number/Enum map.<p>
	 *
	 * @param typeOfDbValueGetter The class being converted
	 *
	 * @return An unmodifidiable map
	 *
	 * @see #convertToStringMap(Class)
	 */
	public static <T extends Number, ET extends Enum<ET> & DbValueGetter<T>> Map<T, ET> convertToIntMap(Class<ET> typeOfDbValueGetter)
	{
		Map<T, ET> dbValueMap = new HashMap<T, ET>(typeOfDbValueGetter.getEnumConstants().length);

		for (ET enumValue: typeOfDbValueGetter.getEnumConstants()) {
			dbValueMap.put(enumValue.getDbValue(), enumValue);
		}

		return Collections.unmodifiableMap(dbValueMap);
	}

	/**
	 * Convert a {@link DbValueGetter} type(in enum type) to String(from integral value)/Enum map.<p>
	 *
	 * @param typeOfDbValueGetter The class being converted
	 *
	 * @return An unmodifidiable map
	 *
	 * @see #convertToIntMap(Class)
	 */
	public static <ET extends Enum<ET> & DbValueGetter> Map<String, ET> convertToStringMap(Class<ET> typeOfDbValueGetter)
	{
		Map<String, ET> dbValueMap = new HashMap<String, ET>(typeOfDbValueGetter.getEnumConstants().length);

		for (ET enumValue: typeOfDbValueGetter.getEnumConstants()) {
			dbValueMap.put(String.valueOf(enumValue.getDbValue()), enumValue);
		}

		return Collections.unmodifiableMap(dbValueMap);
	}


    /**
     * Convert a joined(with "|" bit operator) numeric value to {@link EnumSet} of {@link DbValueGetter}.<p>
     *
     * @param <T> type of persisted number
     * @param <ET> type of {@link DbValueGetter} enumeration
     * @param typeOfDbValueGetter type of {@link DbValueGetter} enumeration
     * @param dbValueSet the persisted numeric value
     *
     * @return result set of enumeration
     *
     * @see #enumSetToJoinedValue
     */
    public static <T extends Number, ET extends Enum<ET> & DbValueGetter<T>>
        EnumSet<ET> joinedValueToEnumSet(Class<ET> typeOfDbValueGetter, T dbValueSet)
    {
        EnumSet<ET> resultEnumSet = EnumSet.noneOf(typeOfDbValueGetter);

        if (dbValueSet == null) {
            return resultEnumSet;
        }

        for (ET dbValueGetter: typeOfDbValueGetter.getEnumConstants()) {
            long longOfEnum = dbValueGetter.getDbValue().longValue();
            if (
                (dbValueSet.longValue() & longOfEnum) == longOfEnum
            ) {
                resultEnumSet.add(dbValueGetter);
            }
        }

        return resultEnumSet;
    }
    /**
     * Convert an {@link EnumSet} to joined(with "|" bit operator) numeric value.<p>
     *
     * This method would use "|"(bit operator) operator to join the value of {@link DbValueGetter}.
     *
     * @param <T> type of persisted number
     * @param <ET> type of {@link DbValueGetter} enumeration
     * @param typeOfDbValue type of database value
     * @param enumSetValue the value of {@link EnumSet}
     *
     * @return result set of enumeration
     *
     * @see #joinedValueToEnumSet
     */
    public static <T extends Number, ET extends Enum<ET> & DbValueGetter<T>>
        T enumSetToJoinedValue(Class<T> typeOfDbValue, EnumSet<ET> enumSetValue)
    {
        Long resultValue = 0L;

        if (enumSetValue != null) {
            for (ET dbValueGetter: enumSetValue) {
                resultValue |= dbValueGetter.getDbValue().longValue();
            }
        }

        if (typeOfDbValue.equals(Byte.class)) {
            return typeOfDbValue.cast(resultValue.byteValue());
        } else if (typeOfDbValue.equals(Short.class)) {
            return typeOfDbValue.cast(resultValue.shortValue());
        } else if (typeOfDbValue.equals(Integer.class)) {
            return typeOfDbValue.cast(resultValue.intValue());
        } else if (typeOfDbValue.equals(Short.class)) {
            return typeOfDbValue.cast(resultValue.longValue());
        } else {
            throw new IllegalArgumentException("Unknown integral type: " + typeOfDbValue.toString());
        }
    }
}
