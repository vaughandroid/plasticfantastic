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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link ValidatedCardFactory#create(String)} and {@link ValidatedCardFactory#create(CardNumber)}
 */
@RunWith(Parameterized.class)
public class ValidatedCardFactory_SucessfulCreation {

    private static final CardType TYPE_1 = new CardType.Builder("Type Name").withNumberPatterns("1234").withValidLengths(10).build();
    private static final CardType TYPE_2 = new CardType.Builder("Type Name").withNumberPatterns("12345").withValidLengths(11).build();
    private static final CardType TYPE_3 = new CardType.Builder("Type Name").withNumberPatterns("123").withValidLengths(10).build();
    private static final CardType TYPE_4 = new CardType.Builder("Type Name").withNumberPatterns("12345").withValidLengths(12).build();

    private static final ValidatedCardFactory FACTORY = new ValidatedCardFactory(TYPE_1, TYPE_2, TYPE_3, TYPE_4);

    @Parameterized.Parameters
    public static Iterable<Object[]> buildParameters() {
        return Arrays.asList(new Object[][]{
                {"1234", TYPE_1},
                {"12345", TYPE_2},
                {"123", TYPE_3},
                {"12345678901", TYPE_2},
                {"1234567890", TYPE_2},
                {"123456789012", TYPE_4},
                {"1234 0 67890", TYPE_1},
                {"123 0 567890", TYPE_3},

        });
    }

    private final String numberString;
    private final CardType expectedCardType;

    public ValidatedCardFactory_SucessfulCreation(String numberString, CardType expectedCardType) {
        this.numberString = numberString;
        this.expectedCardType = expectedCardType;
    }

    @Test
    public void create_with_String_has_expected_CardNumber() {
        assertThat("Failed for card number: " + numberString,
                FACTORY.create(numberString).getNumber(), is(equalTo(new CardNumber(numberString))));
    }

    @Test
    public void create_with_String_has_expected_card_type() {
        assertThat("Failed for card number: " + numberString,
                FACTORY.create(numberString).getType(), is(equalTo(expectedCardType)));
    }

    @Test
    public void create_with_CardNumber_has_expected_CardNumber() {
        CardNumber cardNumber = new CardNumber(numberString);
        assertThat("Failed for card number: " + numberString,
                FACTORY.create(cardNumber).getNumber(), is(equalTo(cardNumber)));
    }

    @Test
    public void create_with_CardNumber_has_expected_card_type() {
        assertThat("Failed for card number: " + numberString,
                FACTORY.create(new CardNumber(numberString)).getType(), is(equalTo(expectedCardType)));
    }
}
