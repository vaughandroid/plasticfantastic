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

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Miscellaneous tests for {@link ValidatedCardFactory}.
 */
public class ValidatedCardFactory_Misc {

    @Test(expected = NullPointerException.class)
    public void pass_null_to_constructor_using_varargs_throws_NullPointerException() {
        new ValidatedCardFactory((CardType[]) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void pass_empty_array_to_constructor_throws_IllegalArgumentException() {
        new ValidatedCardFactory(new CardType[0]);
    }

    @Test(expected = NullPointerException.class)
    public void pass_null_to_constructor_using_list_throws_NullPointerException() {
        new ValidatedCardFactory((List<CardType>) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void pass_empty_list_to_constructor_throws_IllegalArgumentException() {
        new ValidatedCardFactory(new ArrayList<CardType>());
    }

    @Test
    public void create_using_string_for_list_including_null_doesnt_throw_exception() {
        ValidatedCardFactory factory = new ValidatedCardFactory(CardType.VISA, CardType.MASTERCARD, null);
        factory.create("123");
    }

    @Test(expected = NullPointerException.class)
    public void create_for_null_string_throws_NullPointerException() {
        ValidatedCardFactory factory = new ValidatedCardFactory(CardType.VISA, CardType.MASTERCARD);
        factory.create((String) null);
    }

    @Test
    public void create_using_card_number_for_list_including_null_doesnt_throw_exception() {
        ValidatedCardFactory factory = new ValidatedCardFactory(CardType.VISA, CardType.MASTERCARD, null);
        factory.create(new CardNumber("123"));
    }

    @Test(expected = NullPointerException.class)
    public void create_for_null_card_number_throws_NullPointerException() {
        ValidatedCardFactory factory = new ValidatedCardFactory(CardType.VISA, CardType.MASTERCARD);
        factory.create((CardNumber) null);
    }
}
