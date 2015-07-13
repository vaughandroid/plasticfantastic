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
import static org.junit.Assert.assertThat;

/**
 * Miscellaneous tests for the {@link plasticfantastic.CardType.Builder}.
 */
public class CardTypeBuilder_Misc {

    @Test(expected = NullPointerException.class)
    public void null_name_throws_NullPointerException() {
        new CardType.Builder(null);
    }

    @Test
    public void single_number_pattern_and_length_builds_successfully() {
        new CardType.Builder("Type Name")
                .addSingleNumberPatterns("123")
                .validLengths(10)
                .build();
    }

    @Test
    public void single_number_pattern_multiple_calls_all_included() {
        CardType cardType = new CardType.Builder("Type Name")
                .addSingleNumberPatterns("123")
                .addSingleNumberPatterns("456")
                .validLengths(10)
                .build();

        assertThat(cardType.patternMatches(new CardNumber("123")), is(equalTo(true)));
        assertThat(cardType.patternMatches(new CardNumber("456")), is(equalTo(true)));
    }

    @Test
    public void range_pattern_multiple_calls_all_included() {
        CardType cardType = new CardType.Builder("Type Name")
                .addRangePatterns("100-200")
                .addRangePatterns("300-400")
                .validLengths(10)
                .build();

        assertThat(cardType.patternMatches(new CardNumber("150")), is(equalTo(true)));
        assertThat(cardType.patternMatches(new CardNumber("350")), is(equalTo(true)));
    }

    @Test(expected = IllegalStateException.class)
    public void no_patterns_or_lengths_throws_IllegalStateException() {
        new CardType.Builder("Type Name").build();
    }

    @Test(expected = IllegalStateException.class)
    public void no_lengths_throws_IllegalStateException() {
        new CardType.Builder("Type Name").addSingleNumberPatterns("123").build();
    }

    @Test(expected = IllegalStateException.class)
    public void no_patterns_throws_IllegalStateException() {
        new CardType.Builder("Type Name").validLengths(10).build();
    }
}
