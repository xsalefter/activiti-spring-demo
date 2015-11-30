package com.baeldung.activitispringdemo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.delegate.JavaDelegate;
import org.activiti.engine.task.Task;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.baeldung.activitispringdemo.activiti.BusinessClassified;
import com.baeldung.activitispringdemo.activiti.CedingApplicationStatus;
import com.baeldung.activitispringdemo.activiti.CreateCedingFormDTO;
import com.baeldung.activitispringdemo.repository.CedingApplicationRepository;
import com.baeldung.activitispringdemo.spring.ApplicationConfiguration;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes={ApplicationConfiguration.class})
public class ReInsuranceDemoIntegrationTest {

    private static final Logger logger = LoggerFactory.getLogger(ReInsuranceDemoIntegrationTest.class);

    @Autowired 
    private ProcessEngineConfiguration processEngineConfiguration;

    @Autowired 
    private ProcessEngine processEngine;

    @Autowired 
    private CedingApplicationRepository cedingApplicationRepository;

    @Autowired
    @Qualifier("cedingApplicationActivitiValidator")
    private JavaDelegate cedingApplicationActivitiValidator;

    private ReInsuranceDemoWorkFlow workFlow;

    @Before
    public void before() {
        final RuntimeService runtimeService = this.processEngine.getRuntimeService();
        final TaskService taskService = this.processEngine.getTaskService();
        final IdentityService identityService = this.processEngine.getIdentityService();

        this.workFlow = new ReInsuranceDemoWorkFlow(runtimeService, taskService, identityService);
    }

    @Test
    public void testInjectionWorksAsExpected() {
        assertThat(this.processEngineConfiguration, is(notNullValue()));
        assertThat(this.processEngine, is(notNullValue()));
        assertThat(this.cedingApplicationRepository, is(notNullValue()));
        assertThat(this.cedingApplicationActivitiValidator, is(notNullValue()));
    }

    @Test
    public void test() throws InterruptedException {
        try {
            this.workFlow.getIdentityService().setAuthenticatedUserId("admin1");
            this.workFlow.getRuntimeService().startProcessInstanceByKey("re_insurance_demo");

            List<Task> availableTasks = null;
            while (!(availableTasks = this.workFlow.getTaskService().createTaskQuery().list()).isEmpty()) {
                for (final Task currentRunningTask : availableTasks) {
                    final String taskId = currentRunningTask.getId();
                    final String taskName = currentRunningTask.getName();
                    final String executionId = currentRunningTask.getExecutionId();

                    final Map<String, Object> taskProcessVars = currentRunningTask.getProcessVariables();

                    logger.info(">> Processing Task with TaskID: {}, TaskName: {}, ExecutionId: {},  TaskVariables: {}", taskId, executionId, taskName, taskProcessVars);

                    // Receive an offers
                    this.workFlow.receiveAnOffers(currentRunningTask, BusinessClassified.Others, "OF.001");

                    // Input ceding application
                    final String facultativeCode = this.workFlow.getRuntimeService().getVariable(executionId, "facultative_code").toString();
                    final CreateCedingFormDTO createCedingForm = new CreateCedingFormDTO();
                    createCedingForm.setFacultativeCode(facultativeCode);
                    createCedingForm.setCedingName("Aksalife Insurance");
                    createCedingForm.setAddress("Rasuna Said street, Central Jakarta, Indonesia");
                    createCedingForm.setContactEmail("aksalife@mailinator.com");
                    createCedingForm.setContactPhone("+628123456789");
                    createCedingForm.setStatus(CedingApplicationStatus.P11_Offer_In_Progress);
                    createCedingForm.setShareInPercent(0);
                    createCedingForm.setAmount(BigDecimal.ZERO);
                    this.workFlow.inputCedingApplication(currentRunningTask, createCedingForm);

                    // Ceding acceptance.
                    this.workFlow.cedingApplicationAcceptance(currentRunningTask, true);

                    Thread.sleep(1000);
                    logger.info("------------------------------------------");
                }
            }
        } finally {
            this.workFlow.getIdentityService().setAuthenticatedUserId(null);
        }
    }

}
