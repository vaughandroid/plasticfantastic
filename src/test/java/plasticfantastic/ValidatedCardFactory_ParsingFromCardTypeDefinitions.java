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

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Tests for parsing a {@link ValidatedCardFactory} from a list of {@link CardTypeDefinition}s.
 */
public class ValidatedCardFactory_ParsingFromCardTypeDefinitions {

    @Test(expected = NullPointerException.class)
    public void fromJsonString_with_null_throws_NullPointerException() {
        ValidatedCardFactory.fromCardTypeDefinitions((CardTypeDefinition) null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromJsonString_with_empty_list_throws_NullPointerException() {
        ValidatedCardFactory.fromCardTypeDefinitions(new CardTypeDefinition[0]);
    }

    @Test
    public void fromJsonString_with_valid_single_number_patterns_returns_expected() throws IOException {
        CardTypeDefinition definition = new CardTypeDefinition();
        definition.name = "Def 1";
        definition.numberPatterns = new String[]{"123", "456"};
        definition.validLengths = new int[]{14, 15};

        Assert.assertThat(
                ValidatedCardFactory.fromCardTypeDefinitions(definition).toString(),
                is(equalTo("{cardTypes:[{name:\"Def 1\", patterns:[123, 456], lengths:[14, 15]}]}")));
    }

    @Test
    public void fromJsonString_with_valid_range_patterns_returns_expected() throws IOException {
        CardTypeDefinition definition = new CardTypeDefinition();
        definition.name = "Def 1";
        definition.numberPatterns = new String[]{"123-456", "78-90"};
        definition.validLengths = new int[]{14, 15};

        Assert.assertThat(
                ValidatedCardFactory.fromCardTypeDefinitions(definition).toString(),
                is(equalTo("{cardTypes:[{name:\"Def 1\", patterns:[123-456, 78-90], lengths:[14, 15]}]}")));
    }

    @Test
    public void fromJsonString_with_multiple_card_types_returns_expected() throws IOException {
        CardTypeDefinition definition1 = new CardTypeDefinition();
        definition1.name = "Def 1";
        definition1.numberPatterns = new String[]{"123", "45-67"};
        definition1.validLengths = new int[]{14, 15};
        CardTypeDefinition definition2 = new CardTypeDefinition();
        definition2.name = "Def 2";
        definition2.numberPatterns = new String[]{"8-9", "0"};
        definition2.validLengths = new int[]{10};

        Assert.assertThat(
                ValidatedCardFactory.fromCardTypeDefinitions(definition1, definition2).toString(),
                is(equalTo("{cardTypes:[{name:\"Def 1\", patterns:[123, 45-67], lengths:[14, 15]}, {name:\"Def 2\", patterns:[8-9, 0], lengths:[10]}]}")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromJsonString_with_missing_name_throws_IllegalArgumentException() throws IOException {
        CardTypeDefinition definition = new CardTypeDefinition();
        definition.numberPatterns = new String[]{"123", "456"};
        definition.validLengths = new int[]{14, 15};

        ValidatedCardFactory.fromCardTypeDefinitions(definition);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromJsonString_with_missing_numberPatterns_throws_IllegalArgumentException() throws IOException {
        CardTypeDefinition definition = new CardTypeDefinition();
        definition.name = "Def 1";
        definition.validLengths = new int[]{14, 15};

        ValidatedCardFactory.fromCardTypeDefinitions(definition);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromJsonString_with_empty_numberPatterns_throws_IllegalArgumentException() throws IOException {
        CardTypeDefinition definition = new CardTypeDefinition();
        definition.name = "Def 1";
        definition.numberPatterns = new String[0];
        definition.validLengths = new int[]{14, 15};

        ValidatedCardFactory.fromCardTypeDefinitions(definition);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromJsonString_with_empty_validLengths_throws_IllegalArgumentException() throws IOException {
        CardTypeDefinition definition = new CardTypeDefinition();
        definition.name = "Def 1";
        definition.numberPatterns = new String[]{"123", "456"};
        definition.validLengths = new int[0];

        ValidatedCardFactory.fromCardTypeDefinitions(definition);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromJsonString_with_invalid_numberPattern_throws_IllegalArgumentException() throws IOException {
        CardTypeDefinition definition = new CardTypeDefinition();
        definition.name = "Def 1";
        definition.numberPatterns = new String[]{"abc"};
        definition.validLengths = new int[]{14, 15};

        ValidatedCardFactory.fromCardTypeDefinitions(definition);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fromJsonString_with_invalid_validLength_throws_IllegalArgumentException() throws IOException {
        CardTypeDefinition definition = new CardTypeDefinition();
        definition.name = "Def 1";
        definition.numberPatterns = new String[]{"123"};
        definition.validLengths = new int[]{-14};

        ValidatedCardFactory.fromCardTypeDefinitions(definition);
    }
}
