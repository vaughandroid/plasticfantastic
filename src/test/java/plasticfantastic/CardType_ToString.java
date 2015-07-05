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
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link CardType#toString()}.
 */
@RunWith(Parameterized.class)
public class CardType_ToString {

    @Parameterized.Parameters
    public static Iterable<Object[]> buildParameters() {
        return Arrays.asList(new Object[][]{
                {
                        new CardType.Builder().addSingleNumberPatterns("123").validLengths(10).build(),
                        "{ patterns: { 123 }, valid lengths: { 10 } }"
                },
                {
                        new CardType.Builder().addSingleNumberPatterns("123", "456", "789").validLengths(10).build(),
                        "{ patterns: { 123, 456, 789 }, valid lengths: { 10 } }"
                },
                {
                        new CardType.Builder().addRangePatterns("12-34").validLengths(10).build(),
                        "{ patterns: { 12-34 }, valid lengths: { 10 } }"
                },
                {
                        new CardType.Builder().addRangePatterns("12-34", "56-78").validLengths(10).build(),
                        "{ patterns: { 12-34, 56-78 }, valid lengths: { 10 } }"
                },
                {
                        new CardType.Builder().addSingleNumberPatterns("123").addRangePatterns("12-34").validLengths(10).build(),
                        "{ patterns: { 123, 12-34 }, valid lengths: { 10 } }"
                },
                {
                        new CardType.Builder().addSingleNumberPatterns("123").addRangePatterns("12-34").addSingleNumberPatterns("456").validLengths(10).build(),
                        "{ patterns: { 123, 12-34, 456 }, valid lengths: { 10 } }"
                },
                {
                        new CardType.Builder().addSingleNumberPatterns("123").validLengths(10, 11, 12).build(),
                        "{ patterns: { 123 }, valid lengths: { 10, 11, 12 } }"
                },
                {
                        new CardType.Builder().addSingleNumberPatterns("123").validLengths(12, 11, 10).build(),
                        "{ patterns: { 123 }, valid lengths: { 12, 11, 10 } }"
                },
        });
    }

    private final CardType cardType;
    private final String expectedDescription;

    public CardType_ToString(CardType cardType, String expectedDescription) {
        this.cardType = cardType;
        this.expectedDescription = expectedDescription;
    }

    @Test
    public void description_matches_expected() {
        assertThat(cardType.toString(), is(equalTo(expectedDescription)));
    }
}