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
     * <p>
     * Note that the list of card types is in priority order. i.e. When matching, the first {@link CardType} matching
     * the card number's pattern will be used.
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
     * <p>
     * Note that the list of card types is in priority order. i.e. When matching, the first {@link CardType} matching
     * the card number's pattern will be used.
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
     * See {@link #create(plasticfantastic.CardNumber)}
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
     * Create a {@link ValidatedCard}.
     * <p>
     * The first {@link CardType} matching the card number's pattern is used, the first match is chosen.
     *
     * @param cardNumber card number to match
     * @return a new {@link ValidatedCard} instance, or null if a card type matching the card number could not be found
     * @throws NullPointerException if cardNumber is null
     */
    public ValidatedCard create(CardNumber cardNumber) {
        if (cardNumber == null) {
            throw new NullPointerException("cardNumber cannot be null");
        }
        ValidatedCard result = null;
        for (CardType cardType : cardTypes) {
            if (cardType != null
                    && cardType.patternMatches(cardNumber)) {
                result = new ValidatedCard(cardNumber, cardType);
                break;
            }
        }

        return result;
    }
}
