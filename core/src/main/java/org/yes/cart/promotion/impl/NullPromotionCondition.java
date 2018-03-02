/*
 * Copyright 2009 Denys Pavlov, Igor Azarnyi
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */

package org.yes.cart.promotion.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yes.cart.promotion.PromotionCondition;

import java.util.Map;

/**
 * This is a null object to prevent promotion engine from crashing.
 * Condition is always false and every time it is executed a log
 * message is created.
 *
 * User: denispavlov
 * Date: 13-10-28
 * Time: 9:36 AM
 */
public class NullPromotionCondition implements PromotionCondition {

    private static final Logger LOG = LoggerFactory.getLogger(NullPromotionCondition.class);

    private final long promotionId;
    private final String promotionCode;

    public NullPromotionCondition(final long promotionId,
                                  final String promotionCode) {
        this.promotionId = promotionId;
        this.promotionCode = promotionCode;
    }

    /** {@inheritDoc} */
    @Override
    public long getPromotionId() {
        return promotionId;
    }

    /** {@inheritDoc} */
    @Override
    public String getPromotionCode() {
        return promotionCode;
    }

    /** {@inheritDoc} */
    @Override
    public boolean isEligible(final Map<String, Object> context) {
        LOG.warn("Null condition invoked for promo: {}", promotionCode);
        return false;
    }
}
