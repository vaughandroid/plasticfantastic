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
 * A card number, type, and information about whether the card number passes validation checks.
 */
public final class ValidatedCard {

    private final CardNumber number;
    private final CardType type;
    private final boolean isNumberValid;

    /**
     * @param number card number
     * @param type   card type
     * @throws NullPointerException If number or type is null.
     */
    public ValidatedCard(CardNumber number, CardType type) {
        if (number == null) {
            throw new NullPointerException("number cannot be null");
        }
        if (type == null) {
            throw new NullPointerException("type cannot be null");
        }
        this.number = number;
        this.type = type;
        isNumberValid = type.checkPattern(number)
                && type.checkLength(number)
                && number.passesLuhnCheck();
    }

    /**
     * @return the card number
     */
    public CardNumber getNumber() {
        return number;
    }

    /**
     * @return the card type
     */
    public CardType getType() {
        return type;
    }

    /**
     * Checks that the card number:
     * <ul>
     *     <li>passes a Luhn check</li>
     *     <li>matches at least one of the card type's patterns</li>
     *     <li>is a valid length for the card type</li>
     * </ul>
     *
     * @return true if the card number passes validation checks
     */
    public boolean isNumberValid() {
        return isNumberValid;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("{ ")
                .append(number.getNumberString())
                .append(", ")
                .append(isNumberValid ? "valid" : "invalid")
                .append(", ")
                .append(type != null ? type.toString() : "unidentified")
                .append(" }")
                .toString();
    }
}
