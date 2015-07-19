/*
 * Copyright 2015 Chris Vaughan
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package plasticfantastic;

import java.math.BigInteger;

/**
 * Matches {@link CardNumber}s against a range of prefixes.
 * </p>
 * A limitation is that the low and high numbers must have the same number of digits.
 */
class RangePattern implements NumberPattern {

    private static final String REGEX_NUMBERS_ONLY = "^[0-9]+$";

    private final int checkLen;
    private final BigInteger minVal;
    private final BigInteger maxVal;

    /**
     * @param min lowest number (inclusive)
     * @param max highest number (inclusive)
     * @throws NullPointerException     if min or max is null
     * @throws IllegalArgumentException if min or max is not a valid number
     */
    RangePattern(String min, String max) {
        if (min == null || max == null) {
            throw new NullPointerException("min and max cannot be null");
        }
        if (!min.matches(REGEX_NUMBERS_ONLY)) {
            throw new IllegalArgumentException("min not valid: " + min);
        }
        if (!max.matches(REGEX_NUMBERS_ONLY)) {
            throw new IllegalArgumentException("max not valid: " + max);
        }
        if (min.length() != max.length()) {
            throw new IllegalArgumentException("Min & max must have the same number of digits");
        }
        checkLen = min.length();

        try {
            minVal = new BigInteger(min);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unexpected error when parsing min: " + min);
        }

        try {
            maxVal = new BigInteger(max);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unexpected error when parsing max: " + max);
        }

        if (minVal.compareTo(maxVal) > 0) {
            throw new IllegalArgumentException("Min (" + min + ") cannot be greater than max (" + max + ")");
        }
    }

    @Override
    public boolean isMatch(CardNumber cardNumber) {
        String checkStr = cardNumber.getNumberString();
        boolean result = false;
        if (checkStr.length() >= checkLen) {
            checkStr = checkStr.substring(0, checkLen);
            BigInteger checkVal = new BigInteger(checkStr);
            result = minVal.compareTo(checkVal) <= 0 && checkVal.compareTo(maxVal) <= 0;
        }
        return result;
    }

    @Override
    public int getLength() {
        return checkLen;
    }

    @Override
    public String toString() {
        return minVal + "-" + maxVal;
    }
}
