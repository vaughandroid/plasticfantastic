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
 * Tests for checking a {@link CardNumber} against a {@link CardType}.
 */
@RunWith(Parameterized.class)
public class CardType_CardNumberChecks {

    private static CardType CARD_TYPE = new CardType.Builder()
            .addSingleNumberPatterns("101", "202")
            .addRangePatterns("300-400", "501-502")
            .validLengths(10, 12)
            .build();

    @Parameterized.Parameters
    public static Iterable<Object[]> buildParameters() {
        return Arrays.asList(new Object[][]{
                {"101", true, false},
                {"202", true, false},
                {"350", true, false},
                {"501", true, false},
                {"10", false, false},
                {"1014567890", true, true},
                {"10145678901", true, false},
                {"10145678902", true, false},
                {"5014567890", true, true},
                {"2345678901", false, true},
        });
    }

    private final CardNumber cardNumber;
    private final boolean prefixMatches;
    private final boolean lengthIsValid;

    public CardType_CardNumberChecks(String cardNumberString, boolean prefixMatches, boolean lengthIsValid) {
        cardNumber = new CardNumber(cardNumberString);
        this.prefixMatches = prefixMatches;
        this.lengthIsValid = lengthIsValid;
    }

    @Test
    public void pattern_check() {
        assertThat("Card number: " + cardNumber.toString(),
                CARD_TYPE.checkPattern(cardNumber), is(equalTo(prefixMatches)));
    }

    @Test
    public void length_check() {
        assertThat("Card number:" + cardNumber.toString(),
                CARD_TYPE.checkLength(cardNumber), is(equalTo(lengthIsValid)));
    }
}
