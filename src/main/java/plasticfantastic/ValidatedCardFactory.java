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

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.util.Arrays;
import java.util.List;

/**
 * A factory for {@link ValidatedCard} instances.
 */
public class ValidatedCardFactory {

    /**
     * Parse a JSON string into a factory.
     * <p>
     * TODO: describe JSON format
     *
     * @param json to parse
     * @return a new factory, initialised with the given data
     * @throws NullPointerException if json is null
     * @throws JsonSyntaxException if json syntax is not valid
     * @throws JsonParseException if the data is not valid for some reason
     */
    public static ValidatedCardFactory fromJson(String json) {
        if (json == null) {
            throw new NullPointerException("json cannot be null");
        }

        CardTypeDefinition[] typeDefinitions = new Gson().fromJson(json, CardTypeDefinition[].class);

        CardType[] cardTypes = new CardType[typeDefinitions.length];

        for (int i = 0; i < typeDefinitions.length; i++) {
            try {
                CardTypeDefinition definition = typeDefinitions[i];
                if (definition.name == null) {
                    throw new JsonSyntaxException("Item " + i + " is missing 'name'.");
                }
                CardType.Builder builder = new CardType.Builder(); // TODO: use name

                if (definition.numberPatterns == null
                        || definition.numberPatterns.length == 0) {
                    throw new JsonSyntaxException("Item " + i + " is missing 'numberPatterns'.");
                }
                builder.addNumberPatterns(definition.numberPatterns);

                if (definition.validLengths == null
                        || definition.validLengths.length == 0) {
                    throw new JsonSyntaxException("Item " + i + " is missing 'validLengths'.");
                }
                builder.validLengths(definition.validLengths);

                cardTypes[i] = builder.build();
            } catch (IllegalArgumentException e) {
                throw new JsonParseException("Item " + i + " has invalid data.", e);
            }
        }

        return new ValidatedCardFactory(cardTypes);
    }

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
     * If there are multiple matches, the {@link CardType} with the strongest match is used. If there are multiple
     * matches with the same strength, the first of them is used.
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
        int resultStrength = 0;
        for (CardType cardType : cardTypes) {
            if (cardType != null) {
                int strength = cardType.getMatchStrength(cardNumber);
                if (strength > resultStrength) {
                    result = new ValidatedCard(cardNumber, cardType);
                    resultStrength = strength;
                }
            }
        }

        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{cardTypes:[");
        for (int i = 0; i < cardTypes.size(); i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append(cardTypes.get(i).toString());
        }
        sb.append("]}");
        return sb.toString();
    }
}
