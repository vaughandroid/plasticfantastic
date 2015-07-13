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
import java.util.Arrays;
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

    public static final CardType AMERICAN_EXPRESS = new Builder()
            .addSingleNumberPatterns("34", "37")
            .validLengths(15)
            .build();
    public static final CardType CHINA_UNIONPAY = new Builder()
            .addSingleNumberPatterns("62")
            .validLengths(16, 17, 18, 19)
            .build();
    public static final CardType DINERS_CLUB_CARTE_BLANCHE = new Builder()
            .addRangePatterns("300-305")
            .validLengths(14)
            .build();
    public static final CardType DINERS_CLUB_INTERNATIONAL = new Builder()
            .addSingleNumberPatterns("309", "36", "38", "39")
            .addRangePatterns("300-305")
            .validLengths(14)
            .build();
    public static final CardType DINERS_CLUB_US_AND_CANADA = new Builder()
            .addSingleNumberPatterns("54", "55")
            .validLengths(16)
            .build();
    public static final CardType DISCOVER = new Builder()
            .addSingleNumberPatterns("6011", "65")
            .addRangePatterns("622126-622925", "644-649")
            .validLengths(16)
            .build();
    public static final CardType INTERPAYMENT = new Builder()
            .addSingleNumberPatterns("636")
            .validLengths(16, 17, 18, 19)
            .build();
    public static final CardType INSTAPAYMENT = new Builder()
            .addRangePatterns("637-639")
            .validLengths(16)
            .build();
    public static final CardType JCB = new Builder()
            .addRangePatterns("3528-3589")
            .validLengths(16)
            .build();
    public static final CardType MAESTRO = new Builder()
            .addRangePatterns("500000-509999", "560000-699999")
            .validLengths(12, 13, 14, 15, 16, 17, 18, 19)
            .build();
    public static final CardType DANKORT = new Builder()
            .addSingleNumberPatterns("5019")
            .validLengths(16)
            .build();
    public static final CardType MASTERCARD = new Builder()
            .addRangePatterns("51-55")
            .validLengths(16)
            .build();
    public static final CardType VISA = new Builder()
            .addSingleNumberPatterns("4")
            .validLengths(13, 16)
            .build();
    public static final CardType UATP = new Builder()
            .addSingleNumberPatterns("1")
            .validLengths(15)
            .build();

    public static List<CardType> createListWithAllTypes() {
        return Arrays.asList(
                AMERICAN_EXPRESS,
                CHINA_UNIONPAY,
                DINERS_CLUB_CARTE_BLANCHE,
                DINERS_CLUB_INTERNATIONAL,
                DINERS_CLUB_US_AND_CANADA,
                DISCOVER,
                INTERPAYMENT,
                INSTAPAYMENT,
                JCB,
                MAESTRO,
                DANKORT,
                MASTERCARD,
                VISA,
                UATP);
    }

    /**
     * Builder for {@link CardType} instances.
     * <p>
     * You must specify at least one pattern (single number/range) and at least one valid length for the card type.
     */
    public static class Builder {
        private final List<NumberPattern> patternList = new ArrayList<NumberPattern>();
        private int[] validLengths;

        /**
         * Add a set of single number prefixes for the card type. e.g. "1234", "56", "789"
         * <p>
         * Calling this method multiple times will add more patterns.
         *
         * @param patterns one or more strings of digits
         * @return the builder instance, for method chaining
         * @throws NullPointerException     if patterns is null, or one or more of the pattern strings is null
         * @throws IllegalArgumentException if one or more of the patterns is invalid
         */
        public Builder addSingleNumberPatterns(String... patterns) {
            if (patterns == null) {
                throw new NullPointerException();
            }
            for (int i = 0; i < patterns.length; i++) {
                patternList.add(new SingleNumberPattern(patterns[i]));
            }
            return this;
        }

        /**
         * A set of range prefixes for the card type. Each range pattern should consist of the lowest number
         * (inclusive), then a hyphen, then the highest number (also inclusive). e.g. "12-34", "567-890".
         * <p>
         * A limitation is that the low and high numbers must have the same number of digits, so a pattern such as
         * "1-20" is considered invalid. If needed, this can be achieved by simply adding 2 patterns - e.g. "1-9" &amp;
         * "10-20".
         * <p>
         * Calling this method multiple times will add more patterns.
         *
         * @param patterns one or more strings of range patterns
         * @return the builder instance, for method chaining
         * @throws NullPointerException     if patterns is null, or one or more of the pattern strings is null
         * @throws IllegalArgumentException if one or more of the patterns is invalid
         */
        public Builder addRangePatterns(String... patterns) {
            if (patterns == null) {
                throw new NullPointerException();
            }
            for (int i = 0; i < patterns.length; i++) {
                patternList.add(new RangePattern(patterns[i]));
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
        public Builder validLengths(int... validLengths) {
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
            if (patternList.size() == 0) {
                throw new IllegalStateException("Must define one or more card number patterns.");
            }
            if (validLengths == null || validLengths.length == 0) {
                throw new IllegalStateException("Must define one or more length values.");
            }
            NumberPattern[] patterns = patternList.toArray(new NumberPattern[patternList.size()]);
            return new CardType(patterns, validLengths);
        }
    }

    private final NumberPattern[] numberPatterns;
    private final int[] validLengths;

    private CardType(NumberPattern[] numberPatterns, int[] validLengths) {
        this.numberPatterns = numberPatterns;
        this.validLengths = validLengths;
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
        sb.append("{patterns:[");
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
