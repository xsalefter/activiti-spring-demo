package com.baeldung.activitispringdemo.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baeldung.activitispringdemo.BusinessClassified;
import com.baeldung.activitispringdemo.util.StringUtils;

public class FacultativeCodeAndBusinessClassifiedHandler
implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(FacultativeCodeAndBusinessClassifiedHandler.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.info(">>> execute() in FacultativeCodeAndBusinessClassifiedHandler.");

        final String businessClassifiedAsString = execution.getVariable("business_classified").toString();
        final BusinessClassified businessClassified = BusinessClassified.valueOf(businessClassifiedAsString);
        final String facultativeCode = (String) execution.getVariable("facultative_code");

        if (businessClassified.equals(BusinessClassified.Others)) {
            if ( !StringUtils.nullOrEmpty(facultativeCode) ) {
                logger.info(">>> Would looking at master database for facultative code.");
                logger.info(">>> If found, activiti variable expression 'is_valid_combination' would set to true.");
                logger.info(">>> If not found, activiti variable expression 'is_valid_combination' would set to false.");

                // Just true at the moment.
                execution.setVariable("is_valid_combination", true);
            } else {
                logger.error(">>> Error! BusinessClassified 'others' should contains valid FacultativeCode!");
                logger.info(">>> Activiti variable expression 'is_valid_combination' would set to false.");

                execution.setVariable("is_valid_combination", false);
            }
        } else {
            logger.info(">>> Activiti variable expression 'is_valid_combination' would set to true.");

            execution.setVariable("is_valid_combination", true);
        }

    }
}
