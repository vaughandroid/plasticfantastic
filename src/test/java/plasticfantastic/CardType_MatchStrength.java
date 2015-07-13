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
 * Tests for {@link CardType#getMatchStrength(CardNumber)}.
 */
@RunWith(Parameterized.class)
public class CardType_MatchStrength {

    private static CardType CARD_TYPE = new CardType.Builder("Type Name")
            .withNumberPatterns("1", "1234567890", "20-30", "40000-50000")
            .withValidLengths(12)
            .build();

    @Parameterized.Parameters
    public static Iterable<Object[]> buildParameters() {
        return Arrays.asList(new Object[][]{
                {"999", 0},
                {"9999 9999 9999", 0},
                {"1", 2},
                {"123", 2},
                {"1234 5678 90", 20},
                {"1234 5678 9012", 21},
                {"20", 4},
                {"200", 4},
                {"2000 0000 0000", 5},
                {"4000 0", 10},
                {"4000 0000 0000", 11},
        });
    }

    private final CardNumber cardNumber;
    private final int expectedMatchStrength;

    public CardType_MatchStrength(String cardNumberString, int expectedMatchStrength) {
        cardNumber = new CardNumber(cardNumberString);
        this.expectedMatchStrength = expectedMatchStrength;
    }

    @Test
    public void match_strength_returns_expected() {
        assertThat("Card number: " + cardNumber.toString(),
                CARD_TYPE.getMatchStrength(cardNumber), is(equalTo(expectedMatchStrength)));
    }
}
