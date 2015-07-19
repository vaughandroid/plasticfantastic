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
import com.google.gson.JsonIOException;
import com.google.gson.JsonParseException;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A factory for {@link ValidatedCard} instances.
 * <p>
 * <strong>JSON format</strong><br>
 * A factory can be created from appropriately structured JSON data. The data must consist of a list of objects, each
 * with the following fields:
 * <ul>
 *     <li>&quot;name&quot; <em>(String)</em> - the card type's name</li>
 *     <li>&quot;numberPatterns&quot; <em>(list of Strings)</em> - the number patterns for the card type</li>
 *     <li>&quot;validLengths&quot; <em>(list of ints)</em> - the allowed lengths for the card type</li>
 * </ul>
 * Number patterns can be either a single number or a range. Ranges are specified as {@code "min-max"} with optional
 * whitespace around the hyphen.
 * <p>
 * For example:
 * <pre>
 * [
 *   {
 *     "name": "American Express",
 *     "numberPatterns": [ "34", "35" ],
 *     "validLengths": [ 15 ]
 *   },
 *   {
 *     "name": "China UnionPay",
 *     "numberPatterns": [ "62" ],
 *     "validLengths": [ 16, 17, 18, 19 ]
 *   }
 * ]</pre>
 */
public class ValidatedCardFactory {

    private static final Logger LOGGER = Logger.getLogger(ValidatedCardFactory.class.getName());

    /**
     * Create a factory using the default set of card types, as defined in "plasticfantastic_card_types.json".
     *
     * @return a new factory, initialised with the given data
     * @throws IOException if there was some problem reading the file from the filesystem
     */
    public static ValidatedCardFactory withDefaultCardTypes() throws IOException {
        String path = ValidatedCardFactory.class.getResource("/plasticfantastic_card_types.json").getFile();
        return fromFile(new File(path));
    }

    /**
     * Parse a JSON string into a factory.
     * <p/>
     * See {@link ValidatedCardFactory} for details of the expected JSON structure.
     *
     * @param json to parse
     * @return a new factory, initialised with the given data
     * @throws NullPointerException if json is null
     * @throws JsonParseException   if the data is not valid for some reason
     */
    public static ValidatedCardFactory fromJsonString(String json) {
        if (json == null) {
            throw new NullPointerException("json cannot be null");
        }

        CardTypeDefinition[] typeDefinitions = new Gson().fromJson(json, CardTypeDefinition[].class);

        return fromParsedCardTypeDefinitions(typeDefinitions);
    }

    /**
     * Create a factory from a JSON file.
     * <p/>
     * See {@link ValidatedCardFactory} for details of the expected JSON structure.
     * 
     * @param file the file to read
     * @return a new factory, initialised with the given data
     * @throws NullPointerException if file is null
     * @throws IOException          if there was some problem reading the file from the filesystem
     * @throws JsonParseException   if the data is not valid or could not be read for some reason
     */
    public static ValidatedCardFactory fromFile(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException();
        }
        FileReader reader = new FileReader(file);
        try {
            return fromReader(reader);
        } catch (JsonIOException e) {
            throw new IOException(e);
        } finally {
            // Close silently, but at least log issues.
            try {
                reader.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, e.toString(), e);
            }
        }
    }

    /**
     * Parse the JSON output of a {@link Reader} a into a factory.
     * <p/>
     * See {@link ValidatedCardFactory} for details of the expected JSON structure.
     *
     * @param reader which provides the JSON to be parsed
     * @return a new factory, initialised with the given data
     * @throws NullPointerException if json is null
     * @throws JsonParseException   if the data is not valid or could not be read for some reason
     */
    public static ValidatedCardFactory fromReader(Reader reader) {
        if (reader == null) {
            throw new NullPointerException("json cannot be null");
        }

        CardTypeDefinition[] typeDefinitions = new Gson().fromJson(reader, CardTypeDefinition[].class);
        if (typeDefinitions == null) {
            throw new JsonParseException("reader is at EOF");
        }

        return fromParsedCardTypeDefinitions(typeDefinitions);
    }

    /**
     * Create a factory from  a list of {@link CardTypeDefinition}s.
     *
     * @param typeDefinitions
     * @return a new factory, initialised with the given data
     * @throws IllegalArgumentException if typeDefinitions is empty, or any of them have missing/invalid data
     */
    public static ValidatedCardFactory fromCardTypeDefinitions(CardTypeDefinition... typeDefinitions) {
        if (typeDefinitions == null) {
            throw new NullPointerException("typeDefinitions cannot be null");
        }
        if (typeDefinitions.length == 0) {
            throw new IllegalArgumentException("typeDefinitions cannot be empty");
        }

        CardType[] cardTypes = new CardType[typeDefinitions.length];

        for (int i = 0; i < typeDefinitions.length; i++) {
            try {
                CardTypeDefinition definition = typeDefinitions[i];
                if (definition.name == null) {
                    throw new IllegalArgumentException("'name' is missing");
                }
                CardType.Builder builder = new CardType.Builder(definition.name);

                if (definition.numberPatterns == null
                        || definition.numberPatterns.length == 0) {
                    throw new IllegalArgumentException("'numberPatterns' is missing or empty");
                }
                builder.withNumberPatterns(definition.numberPatterns);

                if (definition.validLengths == null
                        || definition.validLengths.length == 0) {
                    throw new IllegalArgumentException("'validLengths' is missing or empty");
                }
                builder.withValidLengths(definition.validLengths);

                cardTypes[i] = builder.build();
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid data at position " + i, e);
            }
        }

        return new ValidatedCardFactory(cardTypes);
    }

    private static ValidatedCardFactory fromParsedCardTypeDefinitions(CardTypeDefinition[] typeDefinitions) {
        try {
            return fromCardTypeDefinitions(typeDefinitions);
        } catch (IllegalArgumentException e) {
            throw new JsonParseException(e);
        }
    }

    private final List<CardType> cardTypes;

    /**
     * Create a new factory with the given card types.
     * <p/>
     * Note that the list of card types is in priority order. i.e. When matching, the first {@link CardType} matching
     * the card number's pattern will be used.
     *
     * @param cardTypes list of card types
     * @throws NullPointerException     if cardTypes is null
     * @throws IllegalArgumentException if cardTypes is empty
     */
    public ValidatedCardFactory(CardType... cardTypes) {
        this(Arrays.asList(cardTypes));
    }

    /**
     * Create a new factory with the given card types.
     * <p/>
     * Note that the list of card types is in priority order. i.e. When matching, the first {@link CardType} matching
     * the card number's pattern will be used.
     *
     * @param cardTypes list of card types
     * @throws NullPointerException     if cardTypes is null
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
     * @throws NullPointerException  if cardNumberString is null
     * @throws NumberFormatException if cardNumberString is not a valid card number
     */
    public ValidatedCard create(String cardNumberString) {
        return create(new CardNumber(cardNumberString));
    }

    /**
     * Create a {@link ValidatedCard}.
     * <p/>
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
