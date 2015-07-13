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

import com.google.gson.JsonParseException;
import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

/**
 * Tests for parsing a {@link ValidatedCardFactory} from JSON data.
 */
public class ValidatedCardFactory_ParsingFromJson {

    public static final String EXPECTED_STRING_VALID =
            "{cardTypes:[{patterns:[123, 456], lengths:[14, 15]}, {patterns:[70-80, 90], lengths:[10]}, {patterns:[101-202], lengths:[10]}]}";

    private static String getResourcePath(String filename) {
        return ValidatedCardFactory_ParsingFromJson.class.getClassLoader().getResource(filename).getFile();
    }

    private static String getResourceAsString(String filename) throws IOException {
        return FileUtils.readFileToString(new File(getResourcePath(filename)));
    }

    @Test(expected = NullPointerException.class)
    public void fromJsonString_with_null_throws_NullPointerException() {
        ValidatedCardFactory.fromJsonString(null);
    }

    @Test
    public void fromJsonString_with_valid_data_returns_expected() throws IOException {
        String json = getResourceAsString("valid.json");
        Assert.assertThat(
                ValidatedCardFactory.fromJsonString(json).toString(),
                is(equalTo(EXPECTED_STRING_VALID)));
    }

    @Test(expected = JsonParseException.class)
    public void fromJsonString_with_invalid_syntax_throws_JsonParseException() throws IOException {
        ValidatedCardFactory.fromJsonString(getResourceAsString("invalid_syntax.json"));
    }

    @Test(expected = JsonParseException.class)
    public void fromJsonString_with_invalid_data_throws_JsonParseException() throws IOException {
        ValidatedCardFactory.fromJsonString(getResourceAsString("invalid_data.json"));
    }

    @Test(expected = JsonParseException.class)
    public void fromJsonString_with_missing_data_throws_JsonParseException() throws IOException {
        ValidatedCardFactory.fromJsonString(getResourceAsString("missing_data.json"));
    }

    @Test
    public void fromFile_with_valid_data_returns_expected() throws IOException {
        Assert.assertThat(
                ValidatedCardFactory.fromFile(new File(getResourcePath("valid.json"))).toString(),
                is(equalTo(EXPECTED_STRING_VALID)));
    }

    // TODO: invalid

    @Test
    public void fromReader_with_valid_data_returns_expected() throws IOException {
        FileReader reader = new FileReader(getResourcePath("valid.json"));
        try {
            Assert.assertThat(
                    ValidatedCardFactory.fromReader(reader).toString(),
                    is(equalTo(EXPECTED_STRING_VALID)));
        } finally {
            reader.close();
        }
    }

    // TODO: invalid
}
