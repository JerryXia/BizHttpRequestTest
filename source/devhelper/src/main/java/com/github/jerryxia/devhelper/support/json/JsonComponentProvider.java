/**
 * 
 */
package com.github.jerryxia.devhelper.support.json;

/**
 * @author guqk
 *
 */
public interface JsonComponentProvider {
    String toJson(Object object);

    <T> T fromJson(String jsonString, Class<T> clazz);
}
