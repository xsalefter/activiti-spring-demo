package com.baeldung.activitispringdemo.activiti;

import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.DelegateExecution;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baeldung.activitispringdemo.CreateCedingFormDTO;

public class CedingApplicationCreator implements JavaDelegate {

    private static final Logger logger = LoggerFactory.getLogger(CedingApplicationCreator.class);

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        final TaskService taskService = execution.getEngineServices().getTaskService();
        final Task task = taskService.createTaskQuery().singleResult();
        logger.info(">>> execute() method on CedingApplicationCreator. Task ID={} Name={}", task.getId(), task.getName());
        logger.info(">>> Data from Create Ceding Application form: {}", new CreateCedingFormDTO(execution));
    }

}
