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
 * Tests for passing invalid length values to {@link plasticfantastic.CardType.Builder#validLengths(int...)}.
 */
@RunWith(Parameterized.class)
public class CardTypeBuilder_InvalidLengths {

    @Parameterized.Parameters
    public static Iterable<Object[]> buildParameters() {
        return Arrays.asList(new Object[][]{
                {null, NullPointerException.class},
                {new int[]{}, IllegalArgumentException.class},
                {new int[]{0}, IllegalArgumentException.class},
                {new int[]{-1}, IllegalArgumentException.class},
        });
    }

    private final int[] lengths;
    private final Class<Exception> expectedException;

    public CardTypeBuilder_InvalidLengths(int[] lengths, Class<Exception> expectedException) {
        this.lengths = lengths;
        this.expectedException = expectedException;
    }

    @Test
    public void pattern_is_accepted_or_throws_IllegalArgumentException() {
        Throwable caught = null;
        try {
            new CardType.Builder("Type Name")
                    .addSingleNumberPatterns("123")
                    .validLengths(lengths)
                    .build();
        } catch (Throwable t) {
            caught = t;
        }
        assertThat("Lengths: " + lengths,
                caught, is(instanceOf(expectedException)));
    }
}
