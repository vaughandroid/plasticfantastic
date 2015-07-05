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
 * Miscellaneous tests for {@link CardNumber}.
 */
public class CardNumber_Misc {

    @Test
    public void valid_string_without_whitespace_is_unchanged() {
        CardNumber cardNumber = new CardNumber("123456789");
        assertThat(cardNumber.getNumberString(), is(equalTo("123456789")));
    }

    @Test
    public void valid_string_with_whitespace_strips_whitespace() {
        CardNumber cardNumber = new CardNumber(" 1234\t5678\n9");
        assertThat(cardNumber.getNumberString(), is(equalTo("123456789")));
    }

    @Test
    public void length_without_whitespace() {
        CardNumber cardNumber = new CardNumber("123456789");
        assertThat(cardNumber.getLength(), is(equalTo(9)));
    }

    @Test
    public void length_with_whitespace() {
        CardNumber cardNumber = new CardNumber("1 2\t34\n56789 ");
        assertThat(cardNumber.getLength(), is(equalTo(9)));
    }

    @Test(expected = NullPointerException.class)
    public void null_string_throws_NullPointerException() {
        new CardNumber(null);
    }

    @Test(expected = NumberFormatException.class)
    public void empty_string_throws_NumberFormatException() {
        new CardNumber("");
    }

    @Test(expected = NumberFormatException.class)
    public void negative_number_throws_NumberFormatException() {
        new CardNumber("-1");
    }

    @Test(expected = NumberFormatException.class)
    public void non_number_throws_NumberFormatException() {
        new CardNumber("abc");
    }
}
