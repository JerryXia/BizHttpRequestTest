/**
 * 
 */
package com.github.jerryxia.devhelper.util;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Administrator
 *
 */
public class MapTest {

    @Test
    public void testCloneIsOk() {
        String k1 = "k1";
        String[] v1 = { "1", "2", "3" };

        String k2 = new String("k2");
        String[] v2 = { "3", "4", "5" };

        Map<String, String[]> originMap = new LinkedHashMap<String, String[]>();
        originMap.put(k1, v1);
        originMap.put(k2, Arrays.copyOf(v2, v2.length));

        k2 = null;
        System.gc();

        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(originMap);
        System.out.println(originMap.keySet());
        Iterator<String> iterator = originMap.keySet().iterator();
        String lastKey = null;
        while (iterator.hasNext()) {
            lastKey = iterator.next();
        }
        Assert.assertTrue("k2" != lastKey);
        Assert.assertTrue("k2" == lastKey.intern());

        v2[0] = "123";
        System.out.println(originMap.get(lastKey)[0]);
    }
    
    @Test
    public void testPutAllIsOk() {
        HashMap<String, Object> origin = new HashMap<String, Object>();
        String a0 = new String("a0");
        String a1 = new String("a1");
        String a2 = new String("a2");
        origin.put("a0", a0);
        origin.put("a1", a1);
        origin.put("a2", a2);
        HashMap<String, Object> target = new HashMap<String, Object>();
        target.put("a1", a1);
        target.put("a2", "asdfdas");

        target.putAll(origin);
        Assert.assertTrue(target.get("a1") == a1);
        Assert.assertTrue(target.get("a2") == a2);
    }
}
