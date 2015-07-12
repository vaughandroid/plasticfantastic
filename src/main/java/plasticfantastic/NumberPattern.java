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

/**
 * Common interface for classes used to match {@link CardNumber}s.
 */
interface NumberPattern {

    /**
     * Check whether a given card number matches the pattern.
     *
     * @param cardNumber to check
     * @return true if the card number is a match for the pattern
     */
    boolean isMatch(CardNumber cardNumber);

    /**
     * @return the number of digits used for a match with this pattern
     */
    int getLength();
}
