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
package plasticfantastic.internal;

import plasticfantastic.CardNumber;

/**
 * Matches {@link CardNumber}s against a prefix.
 */
public class SingleNumberPattern implements NumberPattern {

    private static final String REGEX_NUMBERS_ONLY = "^[0-9]+$";

    private final String numberPattern;

    /**
     * @param numberPattern a string of digits. e.g. "123456", or "007"
     * @throws NullPointerException     if numberPattern is null
     * @throws IllegalArgumentException if numberPattern is invalid
     */
    public SingleNumberPattern(String numberPattern) throws IllegalArgumentException {
        if (numberPattern == null) {
            throw new NullPointerException("Pattern cannot be null");
        }
        if (!numberPattern.matches(REGEX_NUMBERS_ONLY)) {
            throw new IllegalArgumentException("Invalid single number pattern: \"" + numberPattern + "\". " +
                    "Must be non-empty and consist of digits 0-9.");
        }
        this.numberPattern = numberPattern;
    }

    @Override
    public boolean isMatch(CardNumber cardNumber) {
        String toCheck = cardNumber.getNumberString();
        boolean result;
        if (toCheck.length() >= numberPattern.length()) {
            result = numberPattern.equals(toCheck.substring(0, numberPattern.length()));
        } else {
            result = false;
        }
        return result;
    }

    @Override
    public int getLength() {
        return numberPattern.length();
    }

    @Override
    public String toString() {
        return numberPattern;
    }
}
