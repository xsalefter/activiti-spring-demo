package com.baeldung.activitispringdemo;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.task.Task;
import org.activiti.engine.test.ActivitiRule;
import org.activiti.engine.test.Deployment;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ReInsuranceDemoIntegrationTest {

    private static final Logger LOG = LoggerFactory.getLogger(ReInsuranceDemoIntegrationTest.class);

    @Rule
    public ActivitiRule activitiRule = new ActivitiRule();
    private ReInsuranceDemoWorkFlow workFlow;

    @Before
    public void before() {
        final RuntimeService runtimeService = this.activitiRule.getRuntimeService();
        final TaskService taskService = this.activitiRule.getTaskService();
        final IdentityService identityService = this.activitiRule.getIdentityService();

        this.workFlow = new ReInsuranceDemoWorkFlow(runtimeService, taskService, identityService);
    }

    @Test
    @Deployment(resources={"reinsurance-demo.bpmn20.xml"})
    public void test() throws InterruptedException {
        try {
            this.workFlow.getIdentityService().setAuthenticatedUserId("admin1");
            this.workFlow.getRuntimeService().startProcessInstanceByKey("re_insurance_demo");

            List<Task> availableTasks = null;
            while (!(availableTasks = this.workFlow.getTaskService().createTaskQuery().list()).isEmpty()) {
                for (final Task currentRunningTask : availableTasks) {
                    final String taskId = currentRunningTask.getId();
                    final String taskName = currentRunningTask.getName();
                    final Map<String, Object> taskProcessVars = currentRunningTask.getProcessVariables();

                    LOG.info(">> Processing Task with TaskID: {}, TaskName: {}, TaskVariables: {}", taskId, taskName, taskProcessVars);

                    // Receive an offers
                    this.workFlow.receiveAnOffers(currentRunningTask, BusinessClassified.Others, "F001.2015.12.01");

                    // Input ceding application
                    final String facultativeCode = this.workFlow.getRuntimeService()
                        .getVariable(currentRunningTask.getExecutionId(), "facultatice_code")
                        .toString();
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
                    LOG.info("------------------------------------------");
                }
            }
        } finally {
            this.workFlow.getIdentityService().setAuthenticatedUserId(null);
        }
    }

}
