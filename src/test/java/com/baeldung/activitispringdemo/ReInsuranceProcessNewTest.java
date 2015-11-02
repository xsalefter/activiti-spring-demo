package com.baeldung.activitispringdemo;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.*;

public class ReInsuranceProcessNewTest {

    private static final Logger logger = LoggerFactory.getLogger(ReInsuranceProcessNewTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    private TaskService taskService;
    private RuntimeService runtimeService;

    private void initActivitiObjects() {
        this.taskService = this.activitiRule.getTaskService();
        this.runtimeService = this.activitiRule.getRuntimeService();
    }

    @Test
    @Deployment(resources={"reinsurance_process_new.bpmn20.xml"})
    public void test() {
        this.initActivitiObjects();
        final ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey("reinsurance_process_new");
        assertThat(processInstance, is(notNullValue()));

        this.receiveAnOffers(BusinessClassified.Others, "F004.25102015.0001");
        this.checkForCombination();
        this.inputCedingData(new CreateCedingFormDTO(
                "F004.25102015.0002", 
                "Some Other Ceding", 
                "Rasuna Said street, Central Jakarta, Indonesia", 
                "someotherceding@mailinator.com", 
                "+628123456789", 
                CedingApplicationStatus.P11_Offer_In_Progress, 
                0, 
                BigDecimal.ZERO));
    }


    private void receiveAnOffers(final BusinessClassified businessClassified, final String facultativeCode) {
        final Task task = this.getTask();
        final String taskId = task.getId();

        final Map<String, Object> variables = new HashMap<>();
        variables.put("business_classified", businessClassified);
        variables.put("facultative_code", facultativeCode);

        this.taskService.complete(taskId, variables);
    }


    private void checkForCombination() {
        final Task task = this.getTask();
        final String taskId = task.getId();
        this.taskService.complete(taskId);
    }


    private void inputCedingData(CreateCedingFormDTO createCedingFormDTO) {
        //this.taskService.getV
    }


    private Task getTask() {
        final Task task = this.taskService.createTaskQuery().singleResult();
        final String taskId = task.getId();
        final String taskExecutionId = task.getExecutionId();
        final String taskProcessDefId = task.getProcessDefinitionId();
        final String taskProcessInstanceId = task.getProcessInstanceId();
        final String taskDefKey = task.getTaskDefinitionKey();
        final String taskName = task.getName();
        final String taskCategory = task.getCategory();
        final String taskAssignee = task.getAssignee();
        final String taskOwner = task.getOwner();
        final String taskFormKey = task.getFormKey();
        final Map<String, Object> taskProcessVariables = task.getProcessVariables();
        final Map<String, Object> taskLocalVariables = task.getTaskLocalVariables();

        final Object[] logParams = {
            taskId, taskExecutionId, 
            taskProcessDefId, taskProcessInstanceId, taskDefKey,
            taskName, taskCategory, 
            taskAssignee, taskOwner, 
            taskFormKey, taskProcessVariables, taskLocalVariables
        };
        logger.info(">>> Task => Id:{}, ExecutionId:{}, ProcessDefId:{}, ProcessInstanceId:{}, ProcessDefKey:{}, "
                + "Name:{}, Category:{}, Assignee:{}, Owner:{}, FormKey:{}, TaskProcessVariables:{}, TaskLocalVariables:{}", logParams);

        return task;
    }
}
