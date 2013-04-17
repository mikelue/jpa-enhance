package org.no_ip.mikelue.jpa.test.testng;

import org.testng.ITestNGMethod;
import static org.apache.commons.lang3.Validate.isTrue;

import java.util.Arrays;

import static org.apache.commons.lang3.StringUtils.join;

/**
 * This class provides some utility for TestNG.<p>
 */
public class TestNGUtil {
    private TestNGUtil() {}

    /**
     * Determines whether method belongs to any of groups.<p>
     *
     * @param method The determined method
     * @param groups The groups to check
     *
     * @return true if the method belongs to all groups
     */
    public static boolean isMethodBelongGroups(ITestNGMethod method, String... groups)
    {
        isTrue(groups != null && groups.length > 0, "Groups for check is null");

        String[] groupsOfMethod = method.getGroups();
        if (groupsOfMethod == null || groupsOfMethod.length == 0) {
            return false;
        }

        /**
         * Compare with one and only one element in both groups.
         */
        if (groupsOfMethod.length == 1 && groups.length == 1) {
            return groupsOfMethod[0].equals(groups[0]);
        }
        // :~)

        /**
         * Compare with multiple groups
         */
        String[] sortedGroup = groupsOfMethod.length < groups.length ?
            groupsOfMethod : groups;
        Arrays.sort(sortedGroup);
        String[] checkedGroups = groupsOfMethod.length >= groups.length ?
            groupsOfMethod : groups;

        for (String checkGroup: checkedGroups) {
            if (Arrays.binarySearch(sortedGroup, checkGroup) >= 0) {
                return true;
            }
        }
        // :~)

        return false;
    }
}
