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

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for passing invalid arguments to {@link plasticfantastic.CardType.Builder#addRangePatterns(String...)}.
 */
@RunWith(Parameterized.class)
public class CardTypeBuilder_InvalidRangeNumberPatterns {

    @Parameterized.Parameters
    public static Iterable<Object[]> buildParameters() {
        return Arrays.asList(new Object[][]{
                {new String[]{null}, NullPointerException.class},
                {new String[]{""}, IllegalArgumentException.class},
                {new String[]{"1 2"}, IllegalArgumentException.class},
                {new String[]{","}, IllegalArgumentException.class},
                {new String[]{"123"}, IllegalArgumentException.class},
                {new String[]{"-"}, IllegalArgumentException.class},
                {new String[]{"123-"}, IllegalArgumentException.class},
                {new String[]{"-123"}, IllegalArgumentException.class},
                {new String[]{"1-23"}, IllegalArgumentException.class},
                {new String[]{"99-11"}, IllegalArgumentException.class},
        });
    }

    private final String[] numberPatterns;
    private final Class<Exception> expectedException;

    public CardTypeBuilder_InvalidRangeNumberPatterns(String[] numberPatterns, Class<Exception> expectedException) {
        this.numberPatterns = numberPatterns;
        this.expectedException = expectedException;
    }

    @Test
    public void pattern_is_accepted_or_throws_IllegalArgumentException() {
        Throwable caught = null;
        try {
            new CardType.Builder().addRangePatterns(numberPatterns).validLengths(10).build();
        } catch (Throwable t) {
            caught = t;
        }
        assertThat(numberPatterns.toString(), caught, is(instanceOf(expectedException)));
    }
}