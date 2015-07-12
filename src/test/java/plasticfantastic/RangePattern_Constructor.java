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

/**
 * Tests for the {@link RangePattern} constructor.
 */
public class RangePattern_Constructor {

    @Test(expected = NullPointerException.class)
    public void pass_null_throws_NullPointerException() {
        new RangePattern(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void pass_string_with_missing_max_throws_IllegalArgumentException() {
        new RangePattern("123-");
    }

    @Test(expected = IllegalArgumentException.class)
    public void pass_string_with_missing_min_throws_IllegalArgumentException() {
        new RangePattern("-123");
    }

    @Test(expected = IllegalArgumentException.class)
    public void pass_string_with_no_hyphen_throws_IllegalArgumentException() {
        new RangePattern("123456");
    }

    @Test(expected = IllegalArgumentException.class)
    public void pass_string_with_whitespace_throws_IllegalArgumentException() {
        new RangePattern("123 - 456");
    }
}
