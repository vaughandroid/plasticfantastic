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

    private static final String REGEX_WHITESPACE = "\\s";
    private static final String REGEX_VALID = "^[0-9]+[\\s]*[-][\\s]*[0-9]+$";

    private final String pattern;
    private final int checkLen;
    private final BigInteger minVal;
    private final BigInteger maxVal;

    /**
     * @param pattern lowest number (inclusive), then a hyphen, then the highest number (also inclusive).
     *                e.g. "12-34", "567-890"
     * @throws NullPointerException     if pattern is null
     * @throws IllegalArgumentException if pattern is not valid
     */
    RangePattern(String pattern) {
        if (pattern == null) {
            throw new NullPointerException("Pattern cannot be null");
        }
        if (!pattern.matches(REGEX_VALID)) {
            throw new IllegalArgumentException("Pattern not valid: " + pattern);
        }
        pattern = pattern.replaceAll(REGEX_WHITESPACE, "");
        this.pattern = pattern;

        int hyphenIdx = pattern.indexOf('-');
        String minStr = pattern.substring(0, hyphenIdx);
        String maxStr = pattern.substring(hyphenIdx + 1);

        if (minStr.length() != maxStr.length()) {
            throw new IllegalArgumentException("Min & max must have the same number of digits");
        }
        checkLen = minStr.length();

        try {
            minVal = new BigInteger(minStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unexpected error when parsing min: " + minStr);
        }

        try {
            maxVal = new BigInteger(maxStr);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Unexpected error when parsing max: " + maxStr);
        }

        if (minVal.compareTo(maxVal) > 0) {
            throw new IllegalArgumentException("Min must be less than max: " + pattern);
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
    public String toString() {
        return pattern;
    }
}
