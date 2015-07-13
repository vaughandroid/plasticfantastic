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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * Miscellaneous tests for {@link ValidatedCard}.
 */
public class ValidatedCard_Misc {

    private static final CardType CARD_TYPE = new CardType.Builder("Type Name")
            .withNumberPatterns("1234")
            .withValidLengths(10)
            .build();

    @Test(expected = NullPointerException.class)
    public void pass_null_CardNumber_to_constructor_throws_NullPointerException() {
        new ValidatedCard(null, CARD_TYPE);
    }

    @Test(expected = NullPointerException.class)
    public void pass_null_CardType_to_constructor_throws_NullPointerException() {
        new ValidatedCard(new CardNumber("1234"), null);
    }

    public void getNumber_returns_given_instance() {
        CardNumber cardNumber = new CardNumber("1234");
        assertThat(new ValidatedCard(cardNumber, CARD_TYPE).getNumber(), is(sameInstance(cardNumber)));
    }

    public void getType_returns_given_instance() {
        assertThat(new ValidatedCard(new CardNumber("1234"), CARD_TYPE).getType(), is(sameInstance(CARD_TYPE)));
    }

    public void isValid_returns_true_for_valid_card_number() {
        // 7 is the valid Luhn check digit
        assertThat(new ValidatedCard(new CardNumber("123456789 7"), CARD_TYPE).isNumberValid(), is(equalTo(true)));
    }

    public void isValid_returns_false_for_wrong_card_number_length() {
        // 3 is the valid Luhn check digit
        assertThat(new ValidatedCard(new CardNumber("1234567890 3"), CARD_TYPE).isNumberValid(), is(equalTo(false)));
    }

    public void isValid_returns_false_for_wrong_card_number_pattern() {
        // 7 is the valid Luhn check digit
        assertThat(new ValidatedCard(new CardNumber("987654321 7"), CARD_TYPE).isNumberValid(), is(equalTo(false)));
    }

    public void isValid_returns_false_for_failed_luhn_check() {
        // 7 is the valid Luhn check digit
        assertThat(new ValidatedCard(new CardNumber("123456789 8"), CARD_TYPE).isNumberValid(), is(equalTo(false)));
    }
}
