package com.baeldung.activitispringdemo.util;

/**
 * Transform non java friendly enum value from activiti form to valid enum by 
 * transforming its value in {@link #transform(String)} method.
 */
public interface TransformableEnum {
    String transform(String activitiValue);
}
