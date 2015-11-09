package com.baeldung.activitispringdemo.activiti;

import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CedingApplicationUpdater implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(CedingApplicationUpdater.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        logger.info(">>> execute() in CedingApplicationUpdater. Variables={}", execution.getVariables());
        execution.setVariable("assignee", "me@gmail.com");
    }

}
