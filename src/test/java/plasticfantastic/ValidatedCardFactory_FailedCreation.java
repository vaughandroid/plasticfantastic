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
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link ValidatedCardFactory#create(String)} and {@link ValidatedCardFactory#create(CardNumber)}
 */
@RunWith(Parameterized.class)
public class ValidatedCardFactory_FailedCreation {

    private static final CardType TYPE_1 = new CardType.Builder("Type Name").addNumberPatterns("123").validLengths(10).build();
    private static final CardType TYPE_2 = new CardType.Builder("Type Name").addNumberPatterns("1234").validLengths(11).build();
    private static final CardType TYPE_3 = new CardType.Builder("Type Name").addNumberPatterns("1234").validLengths(10).build();

    private static final ValidatedCardFactory FACTORY = new ValidatedCardFactory(TYPE_1, TYPE_2, TYPE_3);

    @Parameterized.Parameters
    public static Iterable<Object[]> buildParameters() {
        return Arrays.asList(new Object[][]{
                {"5678"},
                {"01234"},
                {"12"},
        });
    }

    private final String numberString;

    public ValidatedCardFactory_FailedCreation(String numberString) {
        this.numberString = numberString;
    }

    @Test
    public void create_with_String_returns_null() {
        assertThat("Failed for card number: " + numberString,
                FACTORY.create(numberString), is(nullValue()));
    }

    @Test
    public void create_with_CardNumber_returns_null() {
        assertThat("Failed for card number: " + numberString,
                FACTORY.create(new CardNumber(numberString)), is(nullValue()));
    }
}
