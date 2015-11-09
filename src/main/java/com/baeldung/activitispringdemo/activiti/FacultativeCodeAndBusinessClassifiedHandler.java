package com.baeldung.activitispringdemo.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baeldung.activitispringdemo.BusinessClassified;
import com.baeldung.activitispringdemo.util.StringUtils;
import static com.baeldung.activitispringdemo.activiti.ConstantActivitiAttributes.*;

public class FacultativeCodeAndBusinessClassifiedHandler
implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(FacultativeCodeAndBusinessClassifiedHandler.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.info(">>> execute() in FacultativeCodeAndBusinessClassifiedHandler. Variables={}", execution.getVariables());

        final String businessClassifiedAsString = execution.getVariable("business_classified").toString();
        final BusinessClassified businessClassified = BusinessClassified.valueOf(businessClassifiedAsString);
        final String facultativeCode = (String) execution.getVariable("facultative_code");

        if (businessClassified.equals(BusinessClassified.Others)) {
            if ( !StringUtils.nullOrEmpty(facultativeCode) ) {
                logger.info(">>> Would looking at master database for facultative code.");
                logger.info(">>> If found, activiti variable expression 'facultative_code_and_business_class_valid' would set to true.");
                logger.info(">>> If not found, activiti variable expression 'facultative_code_and_business_class_valid' would set to false.");

                // Just true at the moment.
                execution.setVariable(FACULTATIVE_CODE_AND_BUSINESS_CLASS_VALID, true);
            } else {
                logger.error(">>> Error! BusinessClassified 'others' should contains valid FacultativeCode!");
                logger.info(">>> Activiti variable expression 'facultative_code_and_business_class_valid' would set to false.");

                execution.setVariable(FACULTATIVE_CODE_AND_BUSINESS_CLASS_VALID, false);
            }
        } else {
            logger.info(">>> Activiti variable expression 'facultative_code_and_business_class_valid' would set to true.");

            execution.setVariable(FACULTATIVE_CODE_AND_BUSINESS_CLASS_VALID, true);
        }

    }
}
