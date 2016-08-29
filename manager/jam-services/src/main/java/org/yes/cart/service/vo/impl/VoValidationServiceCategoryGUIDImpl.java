/*
 * Copyright 2009 - 2016 Denys Pavlov, Igor Azarnyi
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

package org.yes.cart.service.vo.impl;

import org.yes.cart.domain.vo.VoValidationRequest;
import org.yes.cart.domain.vo.VoValidationResult;
import org.yes.cart.service.domain.CategoryService;
import org.yes.cart.service.vo.VoValidationService;

import java.util.regex.Pattern;

/**
 * User: denispavlov
 * Date: 29/08/2016
 * Time: 15:31
 */
public class VoValidationServiceCategoryGUIDImpl implements VoValidationService {

    private final Pattern VALID_URI = Pattern.compile("^[A-Za-z0-9\\-_]+$");
    private final CategoryService categoryService;

    public VoValidationServiceCategoryGUIDImpl(final CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * {@inheritDoc}
     */
    public VoValidationResult validate(final VoValidationRequest request) {

        final String guid = request.getValue();
        if (guid == null) {
            return new VoValidationResult(request, 0L, null);
        }

        if (VALID_URI.matcher(guid).matches()) {

            final Long catId = this.categoryService.findCategoryIdByGUID(guid);
            if (catId == null || catId.equals(request.getSubjectId())) {
                return new VoValidationResult(request);
            }

            return new VoValidationResult(request, catId, "DUPLICATE");

        }
        return new VoValidationResult(request, 0L, "INVALID_URI");
    }
}
