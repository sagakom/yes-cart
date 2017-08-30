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

package org.yes.cart.search.dto.impl;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * User: denispavlov
 * Date: 26/11/2014
 * Time: 00:35
 */
public class LuceneNavigationContextImplTest {


    @Test
    public void testCategoriesCanBeNull() throws Exception {

        assertNull(new LuceneNavigationContextImpl(10L, 10L, null, false, Collections.EMPTY_MAP, null, null).getCategories());

    }

    @Test
    public void testParametersCanBeNull() throws Exception {

        assertNotNull(new LuceneNavigationContextImpl(10L, 10L, null, false, null, null, null).getFilterParameters());

    }

    @Test
    public void testEqualsNull() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, null, false, Collections.EMPTY_MAP, null, null);
        assertFalse(ctx1.equals(null));

    }

    @Test
    public void testEqualsByShopId() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, null, false, Collections.EMPTY_MAP, null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, null, false, Collections.EMPTY_MAP, null, null);
        assertTrue(ctx1.equals(ctx2));
        assertTrue(ctx2.equals(ctx1));
        assertEquals(ctx1.hashCode(), ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);
    }

    @Test
    public void testNotEqualsByShopId() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, null, false, Collections.EMPTY_MAP, null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(11L, 11L, null, false, Collections.EMPTY_MAP, null, null);
        assertFalse(ctx1.equals(ctx2));
        assertFalse(ctx2.equals(ctx1));
        assertFalse(ctx1.hashCode() == ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);
    }


    @Test
    public void testNotEqualsBySubShopId() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, null, false, Collections.EMPTY_MAP, null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 1010L, null, false, Collections.EMPTY_MAP, null, null);
        assertFalse(ctx1.equals(ctx2));
        assertFalse(ctx2.equals(ctx1));
        assertFalse(ctx1.hashCode() == ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);
    }

    @Test
    public void testNotEqualsByInclusion() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, null, false, Collections.EMPTY_MAP, null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, null, true, Collections.EMPTY_MAP, null, null);
        assertFalse(ctx1.equals(ctx2));
        assertFalse(ctx2.equals(ctx1));
        assertFalse(ctx1.hashCode() == ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);
    }

    @Test
    public void testEqualsByCategories1() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, new ArrayList<Long>(Arrays.asList(10L)), false, Collections.EMPTY_MAP, null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, new ArrayList<Long>(Arrays.asList(10L)), false, Collections.EMPTY_MAP, null, null);
        assertTrue(ctx1.equals(ctx2));
        assertTrue(ctx2.equals(ctx1));
        assertEquals(ctx1.hashCode(), ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);
    }

    @Test
    public void testEqualsByCategories2() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, new ArrayList<Long>(Arrays.asList(10L, 11L)), false, Collections.EMPTY_MAP, null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, new ArrayList<Long>(Arrays.asList(10L, 11L)), false, Collections.EMPTY_MAP, null, null);
        assertTrue(ctx1.equals(ctx2));
        assertTrue(ctx2.equals(ctx1));
        assertEquals(ctx1.hashCode(), ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);
    }

    @Test
    public void testNotEqualsByCategories1() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, new ArrayList<Long>(Arrays.asList(10L)), false, Collections.EMPTY_MAP, null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, new ArrayList<Long>(Arrays.asList(11L)), false, Collections.EMPTY_MAP, null, null);
        assertFalse(ctx1.equals(ctx2));
        assertFalse(ctx2.equals(ctx1));
        assertFalse(ctx1.hashCode() == ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);
    }

    @Test
    public void testNotEqualsByCategories2() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, new ArrayList<Long>(Arrays.asList(10L)), false, Collections.EMPTY_MAP, null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, null, false, Collections.EMPTY_MAP, null, null);
        assertFalse(ctx1.equals(ctx2));
        assertFalse(ctx2.equals(ctx1));
        assertFalse(ctx1.hashCode() == ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);
    }


    @Test
    public void testNotEqualsByCategories3() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, null, false, Collections.EMPTY_MAP, null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, new ArrayList<Long>(Arrays.asList(10L)), false, Collections.EMPTY_MAP, null, null);
        assertFalse(ctx1.equals(ctx2));
        assertFalse(ctx2.equals(ctx1));
        assertFalse(ctx1.hashCode() == ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);
    }

    @Test
    public void testNotEqualsByCategories4() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, new ArrayList<Long>(Arrays.asList(10L)), false, Collections.EMPTY_MAP, null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, new ArrayList<Long>(Arrays.asList(10L, 11L)), false, Collections.EMPTY_MAP, null, null);
        assertFalse(ctx1.equals(ctx2));
        assertFalse(ctx2.equals(ctx1));
        assertFalse(ctx1.hashCode() == ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);
    }

    @Test
    public void testNotEqualsByCategories5() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, new ArrayList<Long>(Arrays.asList(10L, 11L)), false, Collections.EMPTY_MAP, null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, new ArrayList<Long>(Arrays.asList(10L)), false, Collections.EMPTY_MAP, null, null);
        assertFalse(ctx1.equals(ctx2));
        assertFalse(ctx2.equals(ctx1));
        assertFalse(ctx1.hashCode() == ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);
    }


    @Test
    public void testEqualsByParams1() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, null, false, (Map) Collections.singletonMap("p1", null), null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, null, false, (Map) Collections.singletonMap("p1", null), null, null);
        assertTrue(ctx1.equals(ctx2));
        assertTrue(ctx2.equals(ctx1));
        assertEquals(ctx1.hashCode(), ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);

    }

    @Test
    public void testEqualsByParams2() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, null, false,
                (Map) Collections.singletonMap("p1", new ArrayList<String>(Arrays.asList("1", "2"))), null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, null, false,
                (Map) Collections.singletonMap("p1", new ArrayList<String>(Arrays.asList("1", "2"))), null, null);
        assertTrue(ctx1.equals(ctx2));
        assertTrue(ctx2.equals(ctx1));
        assertEquals(ctx1.hashCode(), ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);

    }

    @Test
    public void testEqualsByParams3() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, null, false,
                (Map) new HashMap() {{
                    put("p1", new ArrayList<String>(Arrays.asList("1", "2")));
                    put("p2", new ArrayList<String>(Arrays.asList("3", "4")));
                }}, null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, null, false,
                (Map)  new HashMap() {{
                    put("p1", new ArrayList<String>(Arrays.asList("1", "2")));
                    put("p2", new ArrayList<String>(Arrays.asList("3", "4")));
                }}, null, null);
        assertTrue(ctx1.equals(ctx2));
        assertTrue(ctx2.equals(ctx1));
        assertEquals(ctx1.hashCode(), ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);

    }

    @Test
    public void testNotEqualsByParams1() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, null, false,
                Collections.EMPTY_MAP, null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, null, false,
                (Map) Collections.singletonMap("p1", new ArrayList<String>(Arrays.asList("1", "2"))), null, null);
        assertFalse(ctx1.equals(ctx2));
        assertFalse(ctx2.equals(ctx1));
        assertFalse(ctx1.hashCode() == ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);

    }

    @Test
    public void testNotEqualsByParams2() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, null, false,
                (Map) Collections.singletonMap("p1", new ArrayList<String>(Arrays.asList("1", "2"))), null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, null, false,
                Collections.EMPTY_MAP, null, null);
        assertFalse(ctx1.equals(ctx2));
        assertFalse(ctx2.equals(ctx1));
        assertFalse(ctx1.hashCode() == ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);

    }


    @Test
    public void testNotEqualsByParams3() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, null, false,
                (Map) new HashMap() {{
                    put("p1", new ArrayList<String>(Arrays.asList("1", "2")));
                }}, null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, null, false,
                (Map)  new HashMap() {{
                    put("p1", new ArrayList<String>(Arrays.asList("1", "3")));
                }}, null, null);
        assertFalse(ctx1.equals(ctx2));
        assertFalse(ctx2.equals(ctx1));
        assertFalse(ctx1.hashCode() == ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);

    }

    @Test
    public void testEqualsByParams4() throws Exception {

        final LuceneNavigationContextImpl ctx1 = new LuceneNavigationContextImpl(10L, 10L, null, false,
                (Map) new HashMap() {{
                    put("p1", new ArrayList<String>(Arrays.asList("1", "2")));
                    put("p2", new ArrayList<String>(Arrays.asList("4", "3")));
                }}, null, null);
        final LuceneNavigationContextImpl ctx2 = new LuceneNavigationContextImpl(10L, 10L, null, false,
                (Map)  new HashMap() {{
                    put("p1", new ArrayList<String>(Arrays.asList("1", "2")));
                    put("p2", new ArrayList<String>(Arrays.asList("3", "4")));
                }}, null, null);
        assertFalse(ctx1.equals(ctx2));
        assertFalse(ctx2.equals(ctx1));
        assertFalse(ctx1.hashCode() == ctx2.hashCode());
        assertTrue(ctx1.hashCode() != 0L);

    }


}
