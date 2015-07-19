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
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

/**
 * Miscellaneous tests for {@link ValidatedCardFactory#withDefaultCardTypes()}.
 */
@RunWith(Parameterized.class)
public class ValidatedCardFactory_WithDefaultCardTypes {

    private ValidatedCardFactory validatedCardFactory;

    @Parameterized.Parameters
    public static Iterable<Object[]> buildParameters() {
        return Arrays.asList(new Object[][]{
                {"34000 00000 00000", "American Express"},
                {"6200 0000 0000 0000", "China UnionPay"},
                {"3000 000000 0000", "Diners Club International"},
                {"5400 0000 0000 0000", "Diners Club US & Canada"},
                {"6221 2600 0000 0000", "Discover"},
                {"6360 0000 0000 0000", "InterPayment"},
                {"6370 0000 0000 0000", "InstaPayment"},
                {"3528 0000 0000 0000", "JCB"},
                {"5018 0000 0000", "Maestro"},
                {"5019 0000 0000 0000", "Dankort"},
                {"5100 0000 0000 0000", "Mastercard"},
                {"4000 0000 0000 0", "Visa"},
                {"10000 00000 00000", "UATP"},
        });
    }

    private final String cardNumberString;
    private final String expectedCardTypeName;

    public ValidatedCardFactory_WithDefaultCardTypes(String cardNumberString, String expectedCardTypeName)
            throws IOException {
        this.cardNumberString = cardNumberString;
        this.expectedCardTypeName = expectedCardTypeName;
        validatedCardFactory = ValidatedCardFactory.withDefaultCardTypes();
    }

    @Test
    public void identifies_a_card_type() {
        Assert.assertThat(
                validatedCardFactory.create(cardNumberString),
                is(notNullValue()));
    }

    @Test
    public void identifies_expected_card_type() {
        Assert.assertThat(
                validatedCardFactory.create(cardNumberString).getType().getName(),
                is(equalTo(expectedCardTypeName)));
    }
}
