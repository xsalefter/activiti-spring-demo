package com.baeldung.activitispringdemo;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.hamcrest.CoreMatchers.*;
import static com.baeldung.activitispringdemo.activiti.ConstantActivitiProcesses.*;

public class ReInsuranceProcessV2IntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(ReInsuranceProcessV2IntegrationTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();

    private TaskService taskService;
    private RuntimeService runtimeService;
    private IdentityService identityService;

    private void initActivitiObjects() {
        this.taskService = this.activitiRule.getTaskService();
        this.runtimeService = this.activitiRule.getRuntimeService();
        this.identityService = this.activitiRule.getIdentityService();
    }

    private void initUsersAndGroups() {
        final Group reIndoAdmin = this.identityService.newGroup("reindo-admin");
        reIndoAdmin.setName("Re-Indo Admin");
        reIndoAdmin.setType("assignment");
        this.identityService.saveGroup(reIndoAdmin);

        final Group underwriter = this.identityService.newGroup("reindo-underwriter");
        underwriter.setName("Re-Indo Underwriter");
        underwriter.setType("assignment");
        this.identityService.saveGroup(underwriter);

        final User admin1 = this.identityService.newUser("admin1");
        admin1.setFirstName("admin");
        admin1.setLastName("1");
        admin1.setEmail("admin1reindo@mailinator.com");
        admin1.setPassword("admin1");
        this.identityService.saveUser(admin1);

        final User underwriter1 = this.identityService.newUser("underwriter1");
        underwriter1.setFirstName("underwriter");
        underwriter1.setLastName("1");
        underwriter1.setEmail("uw1reindo@mailinator.com");
        underwriter1.setPassword("underwriter1");
        this.identityService.saveUser(underwriter1);

        this.identityService.createMembership("admin1", "reindo-admin");
        this.identityService.createMembership("underwriter1", "reindo-underwriter");
    }

    @Test
    @Deployment(resources={"reinsurance_process_v2.bpmn20.xml"})
    public void test1() {
        this.initActivitiObjects();
        this.initUsersAndGroups();

        final ProcessInstance processInstance = this.runtimeService.startProcessInstanceByKey(REINSURANCE_PROCESS);
        assertThat(processInstance, is(notNullValue()));

        this.receiveAnOffers(BusinessClassified.Others, "F004.25102015.0001");
        this.checkForFacultativeCodeAndBusinessClass();
        this.inputCedingData(new CreateCedingFormDTO(
                "F004.25102015.0002", 
                "Some Other Ceding", 
                "Rasuna Said street, Central Jakarta, Indonesia", 
                "someotherceding@mailinator.com", 
                "+628123456789", 
                CedingApplicationStatus.P11_Offer_In_Progress, 
                0, 
                BigDecimal.ZERO));
        this.saveCedingApplication();
        
    }


    private void receiveAnOffers(final BusinessClassified businessClassified, final String facultativeCode) {
        final Task task = this.getTask();
        final String taskId = task.getId();
        this.taskService.claim(taskId, "admin1");

        final Map<String, Object> variables = new HashMap<>();
        variables.put("business_classified", businessClassified);
        variables.put("facultative_code", facultativeCode);

        this.taskService.complete(taskId, variables);
    }


    private void checkForFacultativeCodeAndBusinessClass() {
        final Task task = this.getTask();
        final String taskId = task.getId();
        this.taskService.complete(taskId);
    }


    private void inputCedingData(CreateCedingFormDTO createCedingFormDTO) {
        final Task task = this.getTask();
        final String taskId = task.getId();

        final Map<String, Object> variables = new HashMap<>();
        variables.put("facultative_code", createCedingFormDTO.getFacultativeCode());
        variables.put("ceding_name", createCedingFormDTO.getCedingName());
        variables.put("ceding_address", createCedingFormDTO.getAddress());
        variables.put("ceding_contact_email", createCedingFormDTO.getContactEmail());
        variables.put("ceding_contact_phone", createCedingFormDTO.getContactPhone());
        variables.put("application_status" , createCedingFormDTO.getStatus());
        variables.put("share_in_percent", createCedingFormDTO.getShareInPercent());
        variables.put("application_amount", createCedingFormDTO.getAmount().longValue());

        this.taskService.complete(taskId, variables);
    }


    private void saveCedingApplication() {
        final Task task = this.getTask();
        final String taskId = task.getId();
        this.taskService.complete(taskId);
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
