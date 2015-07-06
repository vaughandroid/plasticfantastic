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
 * Tests for {@link CardNumber#passesLuhnCheck()}.
 */
@RunWith(Parameterized.class)
public class CardNumber_LuhnCheck {

    @Parameterized.Parameters
    public static Iterable<Object[]> buildParameters() {
        return Arrays.asList(new Object[][]{
                {"79927398710", false},
                {"79927398711", false},
                {"79927398712", false},
                {"79927398713", true},
                {"79927398714", false},
                {"79927398715", false},
                {"79927398716", false},
                {"79927398717", false},
                {"79927398718", false},
                {"79927398719", false},
                {"4741908014352850", true}
        });
    }

    private final String numberString;
    private final boolean expectedResult;

    public CardNumber_LuhnCheck(String numberString, boolean expectedResult) {
        this.numberString = numberString;
        this.expectedResult = expectedResult;
    }

    @Test
    public void luhn_check_passes_or_fails_as_expected() {
        CardNumber cardNumber = new CardNumber(numberString);
        assertThat("Failed for numberString: " + numberString,
                cardNumber.passesLuhnCheck(), is(equalTo(expectedResult)));
    }
}
