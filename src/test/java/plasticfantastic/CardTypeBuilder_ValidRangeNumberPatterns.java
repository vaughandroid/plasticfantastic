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

/**
 * Tests for passing valid arguments to {@link plasticfantastic.CardType.Builder#addRangePatterns(String...)}.
 */
@RunWith(Parameterized.class)
public class CardTypeBuilder_ValidRangeNumberPatterns {

    @Parameterized.Parameters
    public static Iterable<Object[]> buildParameters() {
        return Arrays.asList(new Object[][]{
                {new String[]{"0000-1111"}},
                {new String[]{"1234-5678"}},
                {new String[]{"1234567890-2345678901"}},
                {new String[]{"1234-5678", "5678-9012"}},
                {new String[]{"12-34", "56-78", "90-99"}},
        });
    }

    private final String[] numberPatterns;

    public CardTypeBuilder_ValidRangeNumberPatterns(String[] numberPatterns) {
        this.numberPatterns = numberPatterns;
    }

    @Test
    public void pattern_is_accepted() {
        new CardType.Builder("Type Name").addRangePatterns(numberPatterns);
    }
}
