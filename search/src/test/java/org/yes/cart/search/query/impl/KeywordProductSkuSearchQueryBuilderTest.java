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

import org.apache.lucene.search.Query;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * User: denispavlov
 * Date: 04/12/2014
 * Time: 18:43
 */
public class KeywordProductSkuSearchQueryBuilderTest {

    @Test
    public void testCreateStrictQueryNull() throws Exception {

        final Query query = new KeywordProductSkuSearchQueryBuilder().createStrictQuery(10L, 1010L, "query", null);
        assertNull(query);

    }

    @Test
    public void testCreateStrictQueryBlank() throws Exception {

        final Query query = new KeywordProductSkuSearchQueryBuilder().createStrictQuery(10L, 1010L, "query", "   ");
        assertNull(query);

    }

    @Test
    public void testCreateStrictQuerySingle() throws Exception {

        final Query query = new KeywordProductSkuSearchQueryBuilder().createStrictQuery(10L, 1010L, "query", "SearchWord");
        assertNotNull(query);
        assertEquals("((name:searchword~2)^3.0 (displayName:searchword~2)^3.0 (sku.code:searchword~2)^10.0 (sku.manufacturerCode:searchword~2)^10.0 (attribute.attrvalsearchprimary:searchword~2)^15.0 (attribute.attrvalsearchphrase:searchword~2)^4.0)", query.toString());

    }

    @Test
    public void testCreateStrictQueryMulti() throws Exception {

        final Query query = new KeywordProductSkuSearchQueryBuilder().createStrictQuery(10L, 1010L, "query", "Search, Word");
        assertNotNull(query);
        assertEquals("((name:search, word~2)^3.0 (displayName:search, word~2)^3.0 (sku.code:search, word~2)^10.0 (sku.manufacturerCode:search, word~2)^10.0 (attribute.attrvalsearchprimary:search, word~2)^15.0 (attribute.attrvalsearchphrase:search, word~2)^4.0) ((name:search~2)^2.0 (displayName:search~2)^2.0 (sku.code:search~2)^10.0 (sku.manufacturerCode:search~2)^10.0 (attribute.attrvalsearchprimary:search~2)^15.0 (attribute.attrvalsearch:search~2)^4.0) ((name:word~1)^2.0 (displayName:word~1)^2.0 (sku.code:word~1)^10.0 (sku.manufacturerCode:word~1)^10.0 (attribute.attrvalsearchprimary:word~1)^15.0 (attribute.attrvalsearch:word~1)^4.0)", query.toString());

    }


    @Test
    public void testCreateRelaxedQueryNull() throws Exception {

        final Query query = new KeywordProductSkuSearchQueryBuilder().createRelaxedQuery(10L, 1010L, "query", null);
        assertNull(query);

    }

    @Test
    public void testCreateRelaxedQueryBlank() throws Exception {

        final Query query = new KeywordProductSkuSearchQueryBuilder().createRelaxedQuery(10L, 1010L, "query", "   ");
        assertNull(query);

    }


    @Test
    public void testCreateRelaxedQuerySingle() throws Exception {

        final Query query = new KeywordProductSkuSearchQueryBuilder().createRelaxedQuery(10L, 1010L, "query", "SearchWord");
        assertNotNull(query);
        assertEquals("((name:searchword~2)^2.0 (displayName:searchword~2)^2.0 (sku.code:searchword~2)^10.0 (sku.manufacturerCode:searchword~2)^10.0 (sku.code_stem:searchword~1)^1.0 (sku.manufacturerCode_stem:searchword~1)^1.0 (attribute.attrvalsearchprimary:searchword~2)^4.0 (attribute.attrvalsearch:searchword~2)^4.0)", query.toString());

    }

    @Test
    public void testCreateRelaxedQueryMulti() throws Exception {

        final Query query = new KeywordProductSkuSearchQueryBuilder().createRelaxedQuery(10L, 1010L, "query", "Search, Word");
        assertNotNull(query);
        assertEquals("((name:search~2)^2.0 (displayName:search~2)^2.0 (sku.code:search~2)^10.0 (sku.manufacturerCode:search~2)^10.0 (sku.code_stem:search~1)^1.0 (sku.manufacturerCode_stem:search~1)^1.0 (attribute.attrvalsearchprimary:search~2)^4.0 (attribute.attrvalsearch:search~2)^4.0) ((name:word~1)^2.0 (displayName:word~1)^2.0 (sku.code:word~1)^10.0 (sku.manufacturerCode:word~1)^10.0 (sku.code_stem:word~1)^1.0 (sku.manufacturerCode_stem:word~1)^1.0 (attribute.attrvalsearchprimary:word~1)^4.0 (attribute.attrvalsearch:word~1)^4.0)", query.toString());

    }

}
