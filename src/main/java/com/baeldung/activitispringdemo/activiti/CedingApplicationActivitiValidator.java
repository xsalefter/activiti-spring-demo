package com.baeldung.activitispringdemo.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baeldung.activitispringdemo.entity.CedingApplication;
import com.baeldung.activitispringdemo.repository.CedingApplicationRepository;
import com.baeldung.activitispringdemo.spring.ApplicationConfiguration;
import com.baeldung.activitispringdemo.util.StringUtils;
import static com.baeldung.activitispringdemo.activiti.ConstantActivitiAttributes.*;

@Service
public class CedingApplicationActivitiValidator implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(CedingApplicationActivitiValidator.class);

    @Autowired
    private CedingApplicationRepository cedingApplicationRepository;

    @Transactional
    public void execute(DelegateExecution execution) throws Exception {
        logger.info(">>> execute() in FacultativeCodeAndBusinessClassifiedHandler. Variables={}", execution.getVariables());
        logger.info(">>> cedingApplicationRepository: {}", this.cedingApplicationRepository);

        final String businessClassifiedAsString = execution.getVariable("business_classified").toString();
        final BusinessClassified businessClassified = BusinessClassified.valueOf(businessClassifiedAsString);
        final String facultativeCode = execution.getVariable("facultative_code").toString();

        if (businessClassified.equals(BusinessClassified.Others)) {
            if ( !StringUtils.nullOrEmpty(facultativeCode) ) {
                logger.info(">>> Would looking at master database for facultative code.");

                CedingApplication application = null;
                if (this.cedingApplicationRepository == null) {
                    try (final AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext(ApplicationConfiguration.class)) {
                        this.cedingApplicationRepository = appContext.getBean(CedingApplicationRepository.class);
                        application = this.cedingApplicationRepository.findByFacultativeCode(facultativeCode);
                    }
                }

                logger.info(">>> If found, activiti variable expression 'facultative_code_and_business_class_valid' would set to true.");
                logger.info(">>> If not found, activiti variable expression 'facultative_code_and_business_class_valid' would set to false.");

                execution.setVariable(FACULTATIVE_CODE_AND_BUSINESS_CLASS_VALID, application != null);
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
