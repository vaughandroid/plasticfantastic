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

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for passing invalid single number arguments to
 * {@link plasticfantastic.CardType.Builder#withNumberPatterns(String...)}.
 */
@RunWith(Parameterized.class)
public class CardTypeBuilder_InvalidSingleNumberPatterns {

    @Parameterized.Parameters
    public static Iterable<Object[]> buildParameters() {
        return Arrays.asList(new Object[][]{
                {new String[]{""}},
                {new String[]{"1 2"}},
                {new String[]{","}},
        });
    }

    private final String[] numberPatterns;

    public CardTypeBuilder_InvalidSingleNumberPatterns(String[] numberPatterns) {
        this.numberPatterns = numberPatterns;
    }

    @Test(expected = IllegalArgumentException.class)
    public void pattern_is_accepted_or_throws_IllegalArgumentException() {
        new CardType.Builder("Type Name").withNumberPatterns(numberPatterns).withValidLengths(10).build();
    }
}
