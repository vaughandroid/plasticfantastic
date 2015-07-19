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

import java.util.ArrayList;
import java.util.List;

/**
 * A type of credit card. Identifies a set of known card number patterns, and one or more valid lengths.
 * <h3>Card Number Patterns</h3>
 * Card number patterns are used to identify the card type based on the first few digits of a card number. This is
 * possible since the first 6 digits of any card number form the Issuer Identification Number (IIN). In many cases
 * though, fewer than 6 digits are required to identify some providers. See
 * <a href="https://en.wikipedia.org/wiki/Bank_card_number#Issuer_identification_number_.28IIN.29">here</a> for more
 * information.
 */
public class CardType {

    /**
     * Builder for {@link CardType} instances.
     * <p>
     * You must specify at least one pattern (single number/range) and at least one valid length for the card type.
     */
    public static class Builder {
        private static final String REGEX_WHITESPACE = "\\s";
        private static final String REGEX_SINGLE_NUMBER = "^[0-9]+$";
        private static final String REGEX_RANGE = "^[0-9]+[-][0-9]+$"; // TODO: could allow spaces too

        private final String name;
        private final List<NumberPattern> patternList = new ArrayList<NumberPattern>();
        private int[] validLengths;

        /**
         * @param name name of the card type
         */
        public Builder(String name) {
            if (name == null) {
                throw new NullPointerException("name cannot be null");
            }
            this.name = name;
        }

        /**
         * Add a list of number patterns for the card type.
         * <p>
         * * A set of prefixes for the card type, which can be either single numbers or a range.
         * <p>
         * Each range pattern should consist of the lowest number (inclusive), then a hyphen, then the highest number
         * (also inclusive). e.g. "12-34", "567-890".
         * <p>
         * A limitation is that the low and high numbers must have the same number of digits, so a pattern such as
         * "1-20" is considered invalid. If needed, this can be achieved by simply adding 2 patterns - e.g. "1-9" &amp;
         * "10-20".
         *
         * @param patterns one or more strings of digits
         * @return the builder instance, for method chaining
         * @throws IllegalArgumentException if one or more of the patterns is invalid
         */
        public Builder withNumberPatterns(String... patterns) {
            if (patterns == null) {
                throw new NullPointerException();
            }
            for (int i = 0; i < patterns.length; i++) {
                if (patterns[i] != null) {
                    if (patterns[i].matches(REGEX_SINGLE_NUMBER)) {
                        patternList.add(new SingleNumberPattern(patterns[i]));
                    } else if (patterns[i].matches(REGEX_RANGE)) {
                        int hyphenIdx = patterns[i].indexOf('-');
                        String min = patterns[i].substring(0, hyphenIdx);
                        String max = patterns[i].substring(hyphenIdx + 1);
                        patternList.add(new RangePattern(min, max));
                    } else {
                        throw new IllegalArgumentException("Unrecognised pattern: \"" + patterns[i] + "\"");
                    }
                }
            }
            return this;
        }

        /**
         * Set the allowable lengths for the card type.
         *
         * @param validLengths one or more valid lengths for the card type
         * @return the builder instance, for method chaining
         * @throws NullPointerException     if validLengths is null
         * @throws IllegalArgumentException if validLengths is empty, or one or more of the length values is &lt;= 0
         */
        public Builder withValidLengths(int... validLengths) {
            if (validLengths == null) {
                throw new NullPointerException();
            }
            if (validLengths.length == 0) {
                throw new IllegalArgumentException("Must specify at least one valid length");
            }
            for (int i = 0; i < validLengths.length; i++) {
                if (validLengths[i] <= 0) {
                    throw new IllegalArgumentException("Lengths must be greater than 0.");
                }
            }
            this.validLengths = validLengths;
            return this;
        }

        /**
         * Build the card type instance.
         *
         * @return new card type instance
         * @throws IllegalStateException if no number patterns (single number or range) and/or no valid lengths have
         *                               been defined.
         */
        public CardType build() {
            if (patternList == null || patternList.size() == 0) {
                throw new IllegalStateException("Must define one or more card number patterns.");
            }
            if (validLengths == null || validLengths.length == 0) {
                throw new IllegalStateException("Must define one or more length values.");
            }
            NumberPattern[] patterns = patternList.toArray(new NumberPattern[patternList.size()]);
            return new CardType(name, patterns, validLengths);
        }
    }

    private final String name;
    private final NumberPattern[] numberPatterns;
    private final int[] validLengths;

    private CardType(String name, NumberPattern[] numberPatterns, int[] validLengths) {
        this.name = name;
        this.numberPatterns = numberPatterns;
        this.validLengths = validLengths;
    }

    /**
     * @return the name of the card type
     */
    public String getName() {
        return name;
    }

    /**
     * Stronger matches will return a larger number. Will return 0 if there is no match.
     * <p>
     * Match strength is based on:
     * <ol>
     *     <li>The length of the longest pattern which matches the card number.</li>
     *     <li>Whether the card number is a valid length for the card type. (But only if the pattern matches.)</li>
     * </ol>
     *
     * @param cardNumber to check
     * @return 0 for no match, or &gt;0 for a match
     */
    public int getMatchStrength(CardNumber cardNumber) {
        int result = 0;
        for (int i = 0; i < numberPatterns.length; i++) {
            if (numberPatterns[i].isMatch(cardNumber)) {
                // TODO: if we sort the patterns by length, could exit this loop as soon as a match is found
                int length = numberPatterns[i].getLength();
                if (length > result) {
                    result = length;
                }
            }
        }
        if (result > 0) {
            // Factor in whether the card number is a valid length for the card type.
            result <<= 1;
            if (lengthMatches(cardNumber)) {
                result++;
            }
        }
        return result;
    }

    /**
     * Check that the leading digits of the given card number match one or more of the allowed patterns for this card
     * type.
     *
     * @param cardNumber to check
     * @return true if the card number matches at least one defined patterns for this card type
     */
    public boolean patternMatches(CardNumber cardNumber) {
        return getMatchStrength(cardNumber) > 0;
    }

    /**
     * Check that the length of the given card number is valid for this card type.
     *
     * @param cardNumber to check
     * @return true if the card number length matches one of the valid lengths defined for this card type
     */
    public boolean lengthMatches(CardNumber cardNumber) {
        boolean result = false;
        if (validLengths != null) {
            for (int i = 0; i < validLengths.length; i++) {
                if (cardNumber.getLength() == validLengths[i]) {
                    result = true;
                    break;
                }
            }
        }
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{name:\"");
        sb.append(name);
        sb.append("\", patterns:[");
        for (int i = 0; i < numberPatterns.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(numberPatterns[i]);
        }
        sb.append("], lengths:[");
        for (int i = 0; i < validLengths.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(validLengths[i]);
        }
        sb.append("]}");

        return sb.toString();
    }
}
