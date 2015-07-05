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

import java.util.Arrays;
import java.util.List;

/**
 * A factory for {@link ValidatedCard} instances.
 */
public class ValidatedCardFactory {

    private final List<CardType> cardTypes;

    /**
     * Create a new factory with the given card types.
     *
     * @param cardTypes list of card types
     * @throws NullPointerException if cardTypes is null
     * @throws IllegalArgumentException if cardTypes is empty
     */
    public ValidatedCardFactory(CardType... cardTypes) {
        this(Arrays.asList(cardTypes));
    }

    /**
     * Create a new factory with the given card types.
     *
     * @param cardTypes list of card types
     * @throws NullPointerException if cardTypes is null
     * @throws IllegalArgumentException if cardTypes is empty
     */
    public ValidatedCardFactory(List<CardType> cardTypes) {
        if (cardTypes == null) {
            throw new NullPointerException("cardTypes cannot be null");
        }
        if (cardTypes.isEmpty()) {
            throw new IllegalArgumentException("cardTypes cannot be empty");
        }
        this.cardTypes = cardTypes;
    }

    /**
     * Create a {@link ValidatedCard}.
     * <p/>
     * In the case where there is more than one {@link CardType} matching the card number's pattern, the one which
     * also matches the card number's length is chosen. If neither (or both) match the length, the <em>last</em> match
     * is chosen.
     *
     * @param cardNumberString partial or complete card number (must consist only of digits, and optional whitespace)
     * @return a new {@link ValidatedCard} instance, or null if a card type matching the card number could not be found
     * @throws NullPointerException if cardNumberString is null
     * @throws NumberFormatException if cardNumberString is not a valid card number
     */
    public ValidatedCard create(String cardNumberString) {
        return create(new CardNumber(cardNumberString));
    }

    /**
     * See {@link #create(String)}
     *
     * @param cardNumber card number to match
     * @return a new {@link ValidatedCard} instance, or null if a card type matching the card number could not be found
     * @throws NullPointerException if cardNumber is null
     */
    public ValidatedCard create(CardNumber cardNumber) {
        if (cardNumber == null) {
            throw new NullPointerException("cardNumber cannot be null");
        }
        CardType bestMatch = null;
        boolean bestLengthOk = false;
        for (CardType cardType : cardTypes) {
            if (cardType != null
                    && cardType.checkPattern(cardNumber)) {
                boolean lengthOk = cardType.checkLength(cardNumber);
                if (bestMatch == null
                        || (lengthOk || !bestLengthOk)) {
                    bestMatch = cardType;
                    bestLengthOk = lengthOk;
                }
            }
        }

        return bestMatch != null ? new ValidatedCard(cardNumber, bestMatch) : null;
    }
}
