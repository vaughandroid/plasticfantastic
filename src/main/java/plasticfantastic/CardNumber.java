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

/**
 * A whole or partial credit card number.
 * <p>
 * Credit card numbers cannot be represented with simple numeric types since it is possible for them to have leading
 * zeros. See <a href="https://en.wikipedia.org/wiki/ISO/IEC_7812">here</a> for more information.
 */
public final class CardNumber {

    private static final String REGEX_NUMBERS_ONLY = "^[0-9]+$";
    private static final String REGEX_WHITESPACE = "\\s";

    private final String numberString;

    /**
     * @param numberString partial or complete card number (must consist only of digits, and optional whitespace)
     * @throws java.lang.NullPointerException  if numberString is null
     * @throws java.lang.NumberFormatException if numberString is not a valid number, or is negative
     */
    public CardNumber(String numberString) {
        if (numberString == null) {
            throw new NullPointerException("numberString cannot be null");
        }
        final String normalised = numberString.replaceAll(REGEX_WHITESPACE, "");
        if (!normalised.matches(REGEX_NUMBERS_ONLY)) {
            throw new NumberFormatException("Not a valid card number: '" + numberString + "'");
        }
        this.numberString = normalised;
    }

    /**
     * @return the normalised form of the card number (i.e. digits only, no whitespace)
     */
    public String getNumberString() {
        return numberString;
    }

    /**
     * @return the number of digits comprising the card number
     */
    public int getLength() {
        return numberString.length();
    }

    /**
     * Check whether the card number passes a Luhn algorithm check.
     * See <a href="http://en.wikipedia.org/wiki/Luhn_algorithm">here</a> for more information.
     *
     * @return true if the number is valid according to the Luhn formula.
     */
    public boolean passesLuhnCheck() {
        /* Description from Wikipedia:
         * 1. From the rightmost digit, which is the check digit, moving left, double the value of every second digit;
         * if the product of this doubling operation is greater than 9 (e.g., 8 * 2 = 16), then sum the digits of the
         * products (e.g., 16: 1 + 6 = 7, 18: 1 + 8 = 9).
         * 2. Take the sum of all the digits.
         * 3. If the total modulo 10 is equal to 0 (if the total ends in zero) then the number is valid according to
         * the Luhn formula; else it is not valid.
         */

        int sum = 0;
        boolean doubleValue = false;
        for (int i = numberString.length() - 1; i >= 0; i--) {
            int value = Character.getNumericValue(numberString.charAt(i));
            if (doubleValue) {
                // Double it.
                value *= 2;
                if (value > 9) {
                    // Sum the digits.
                    // Since we know that the 10 <= val <= 18, we can just subtract 9 for the same result.
                    value = value - 9;
                }
            }
            sum += value;
            doubleValue = !doubleValue;
        }
        return sum % 10 == 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardNumber that = (CardNumber) o;

        return numberString.equals(that.numberString);
    }

    @Override
    public int hashCode() {
        return numberString.hashCode();
    }

    @Override
    public String toString() {
        return numberString;
    }
}
