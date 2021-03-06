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

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import plasticfantastic.CardNumber;

import java.util.Arrays;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link RangePattern}.
 */
@RunWith(Parameterized.class)
public class RangePattern_MatchingAndLength {

    @Parameterized.Parameters
    public static Iterable<Object[]> buildParameters() {
        return Arrays.asList(new Object[][]{
                {"200", "400", "200", true, 3},
                {"200", "400", "300", true, 3},
                {"200", "400", "400", true, 3},
                {"200", "400", "199", false, 3},
                {"200", "400", "401", false, 3},
                {"200", "400", "0300", false, 3},
                {"200", "400", "030", false, 3},
                {"200", "400", "30", false, 3},
                {"010", "020", "010", true, 3},
                {"010", "020", "015", true, 3},
                {"010", "020", "020", true, 3},
                {"010", "020", "15", false, 3},
                {"010", "020", "0015", false, 3},
                {"1234", "1234", "1234", true, 4},
                {"123456", "123456", "123456", true, 6},
        });
    }

    private final RangePattern pattern;
    private final String cardNumberString;
    private final boolean shouldMatch;
    private final int expectedLength;

    public RangePattern_MatchingAndLength(String min, String max, String cardNumberString, boolean shouldMatch,
                                          int expectedLength) {
        pattern = new RangePattern(min, max);
        this.cardNumberString = cardNumberString;
        this.shouldMatch = shouldMatch;
        this.expectedLength = expectedLength;
    }

    @Test
    public void potential_matches_correctly_identified() {
        CardNumber cardNumber = new CardNumber(cardNumberString);
        assertThat("Pattern: " + pattern.toString() + ", card number: " + cardNumberString,
                pattern.isMatch(cardNumber), is(equalTo(shouldMatch)));
    }

    @Test
    public void length_matches_expected() {
        CardNumber cardNumber = new CardNumber(cardNumberString);
        assertThat("Pattern: " + pattern.toString(),
                pattern.getLength(), is(equalTo(expectedLength)));
    }
}
