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

package org.yes.cart.search.query.impl;

import org.apache.commons.lang.StringUtils;
import org.apache.lucene.search.BooleanClause;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.Query;
import org.yes.cart.constants.Constants;
import org.yes.cart.search.dto.NavigationContext;
import org.yes.cart.search.query.ProductSearchQueryBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * User: denispavlov
 * Date: 15/11/2014
 * Time: 23:16
 */
public class ProductBrandSearchQueryBuilder extends AbstractSearchQueryBuilderImpl implements ProductSearchQueryBuilder<Query> {

    /**
     * {@inheritDoc}
     */
    public List<Query> createQueryChain(final NavigationContext<Query> navigationContext, final String parameter, final Object value) {

        if (value instanceof Collection) {

            final Collection brands = (Collection) value;
            if (brands.size() > 1) {

                final BooleanQuery.Builder aggregatedQuery = new BooleanQuery.Builder();

                boolean hasClause = false;
                for (final Object brand : brands) {

                    final Query clause = createBrandQuery(escapeValue(brand));
                    if (clause != null) {
                        aggregatedQuery.add(clause, BooleanClause.Occur.SHOULD);
                        hasClause = true;
                    }

                }

                if (hasClause) {
                    return Collections.<Query>singletonList(aggregatedQuery.build());
                }

            } else if (brands.size() == 1) {

                final Query clause = createBrandQuery(escapeValue(brands.iterator().next()));
                if (clause != null) {
                    return Collections.<Query>singletonList(clause);
                }

            }
            return null;
        }

        final Query clause = createBrandQuery(escapeValue(value));
        if (clause != null) {
            return Collections.<Query>singletonList(clause);
        }
        return null;

    }

    private Query createBrandQuery(final String value) {

        if (isEmptyValue(value)) {
            return null;
        }

        return createTermQuery(BRAND_FIELD, value.toLowerCase(), 3.5f);

    }



}
