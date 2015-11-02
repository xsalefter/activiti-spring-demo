package com.baeldung.activitispringdemo.util;

import java.util.Date;

import org.activiti.engine.delegate.DelegateExecution;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ActivitiFormParser {

    private static final Logger logger = LoggerFactory.getLogger(ActivitiFormParser.class);

    private DelegateExecution execution;

    public ActivitiFormParser(final DelegateExecution execution) {
        this.execution = execution;
    }

    /**
     * Get string value from {@link DelegateExecution}. If result obtained from 
     * 'variable' parameter is null, this method would try to return 
     * 'defaultValue[0]'. If default value is not set or null, return null.
     */
    public final String getString(final String variable, final String... defaultValue) {
        logger.info(">>> Get variable named '{}' as String from activiti.", variable);

        final Object var = this.execution.getVariable(variable);
        if (var != null) {
            return var.toString();
        } else {
            return argsParamsIsNullOrEmpty(defaultValue) ? null : defaultValue[0];
        }
    }


    /**
     * Get {@link Long} value from {@link DelegateExecution}. If result obtained 
     * from 'variable' parameter is null, this method would try to return 
     * 'defaultValue[0]'. If default value is not set or null, return null.
     */
    public final Long getLong(final String variable, final Long... defaultValue) {
        logger.info(">>> Get variable named '{}' as Long from activiti.", variable);

        final Object var = this.execution.getVariable(variable);
        if (var != null) {
            return Long.valueOf(var.toString());
        } else {
            return argsParamsIsNullOrEmpty(defaultValue) ? null : defaultValue[0];
        }
    }


    /**
     * Get {@link Date} value from {@link DelegateExecution}. If result obtained 
     * from 'variable' parameter is null, this method would try to return 
     * 'defaultValue[0]'. If default value is not set or null, return null.
     */
    public final Date getDate(final String variable, final Date... defaultValue) {
        logger.info(">>> Get variable named '{}' as java.util.Date from activiti.", variable);

        final Object var = this.execution.getVariable(variable);
        if (var != null) {
            return (Date) var;
        } else {
            return argsParamsIsNullOrEmpty(defaultValue) ? null : defaultValue[0];
        }
    }


    /**
     * <p>Get {@link Enum} value from {@link DelegateExecution}. To match enum 
     * value obtained from Activiti's enum Form Properties, it is recommended 
     * to implements {@link TransformableEnum} in your {@link Enum}'s business 
     * logic.</p>
     * <p>If result obtained from 'variable' parameter is null, this method 
     * return null.</p>
     */
    @SafeVarargs
    public final <T extends Enum<T>> T getEnum(final Class<T> enumClass, final String variable, T... defaultValue) {
        logger.info(">>> Get variable named '{}' as Enum from activiti.", variable);

        final Object variableValue = this.execution.getVariable(variable);
        if (variableValue != null) {
            final String varName = variableValue.toString();
            final String enumName = enumClass.getSimpleName();
            try {
                return Enum.valueOf(enumClass, variableValue.toString());
            } catch (IllegalArgumentException e) {
                logger.info(">>> '{}' is not exist in '{}' enum value. Will try with TransferableEnum.", varName, enumName);
                // Just get 1 sample. Compiler would complains if it not Enum anyway.
                final Enum<T> enumSample = enumClass.getEnumConstants()[0];
                if (enumSample instanceof TransformableEnum) {
                    final TransformableEnum te = (TransformableEnum) enumSample;
                    final String actualEnumValue = te.transform(variableValue.toString());
                    return Enum.valueOf(enumClass, actualEnumValue);
                } else {
                    logger.error(">>> '{}' is not exist in '{}' enum value and not implements TransformableEnum interface.", varName, enumName);
                    throw new IllegalStateException("Referenced enum value not exist and it is not implements TransformableEnum interface.");
                }
            }
        } else {
            return argsParamsIsNullOrEmpty(defaultValue) ? null : defaultValue[0];
        }
    }


    @SuppressWarnings("unchecked")
    private static <T> boolean argsParamsIsNullOrEmpty(T...args) {
        if (args == null) return true;
        return args.length == 0;
    }
}
