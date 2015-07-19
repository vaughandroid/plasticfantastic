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
 * Tests for {@link SingleNumberPattern}.
 */
@RunWith(Parameterized.class)
public class SingleNumberPattern_MatchingAndLength {

    @Parameterized.Parameters
    public static Iterable<Object[]> buildParameters() {
        return Arrays.asList(new Object[][]{
                {"1234", "1234", true, 4},
                {"1234", "1234567890", true, 4},
                {"1234", "123", false, 4},
                {"1234", "1", false, 4},
                {"1234", "4321", false, 4},
                {"1234", "234", false, 4},
                {"1234", "5", false, 4},
                {"01234", "01234", true, 5},
                {"01234", "012345", true, 5},
                {"1234", "0123", false, 4},
                {"012345", "12345", false, 6},
        });
    }

    private final String patternString;
    private final String cardNumberString;
    private final boolean shouldMatch;
    private final int expectedLength;

    public SingleNumberPattern_MatchingAndLength(String patternString, String cardNumberString, boolean shouldMatch,
                                                 int expectedLength) {
        this.patternString = patternString;
        this.cardNumberString = cardNumberString;
        this.shouldMatch = shouldMatch;
        this.expectedLength = expectedLength;
    }

    @Test
    public void potential_matches_correctly_identified() {
        SingleNumberPattern pattern = new SingleNumberPattern(patternString);
        CardNumber cardNumber = new CardNumber(cardNumberString);
        assertThat("Pattern: " + patternString + ", card number: " + cardNumberString,
                pattern.isMatch(cardNumber), is(equalTo(shouldMatch)));
    }

    @Test
    public void length_matches_expected() {
        SingleNumberPattern pattern = new SingleNumberPattern(patternString);
        assertThat("Pattern: " + patternString,
                pattern.getLength(), is(equalTo(expectedLength)));
    }
}
