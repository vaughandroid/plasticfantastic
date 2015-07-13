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

import org.apache.commons.io.FileUtils;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Tests for parsing a {@link ValidatedCardFactory} from JSON data.
 */
public class ValidatedCardFactory_JsonParsing {

    private static String getResourceAsString(String filename) throws IOException {
        filename = ValidatedCardFactory_JsonParsing.class.getClassLoader().getResource(filename).getFile();
        return FileUtils.readFileToString(new File(filename));
    }

    @Test(expected = NullPointerException.class)
    public void fromJson_with_null_throws_NullPointerException() {
        ValidatedCardFactory.fromJson(null);
    }

    @Test
    public void fromJson_with_valid_one_single_number_type_returns_expected() throws IOException {
        String json = getResourceAsString("valid_one_single_number_type.json");
        Assert.assertThat(
                ValidatedCardFactory.fromJson(json).toString(),
                is(equalTo("{cardTypes:[{patterns:[123, 456], lengths:[14, 15]}]}")));
    }

    @Test
    public void fromJson_with_valid_multiple_single_number_types_returns_expected() throws IOException {
        String json = getResourceAsString("valid_multiple_single_number_types.json");
        Assert.assertThat(
                ValidatedCardFactory.fromJson(json).toString(),
                is(equalTo("{cardTypes:[{patterns:[123, 456], lengths:[14, 15]}, {patterns:[7890, 98], lengths:[10]}, {patterns:[7], lengths:[10]}]}")));
    }

    // TODO: range patterns
    // TODO: missing data
    // TODO: invalid data
}
