package com.baeldung.activitispringdemo;

import java.util.HashMap;
import java.util.Map;

import org.activiti.engine.IdentityService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.Group;
import org.activiti.engine.identity.User;
import org.activiti.engine.task.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.baeldung.activitispringdemo.activiti.BusinessClassified;
import com.baeldung.activitispringdemo.activiti.CreateCedingFormDTO;

public final class ReInsuranceDemoWorkFlow {

    private static final Logger logger = LoggerFactory.getLogger(ReInsuranceDemoWorkFlow.class);

    private final RuntimeService runtimeService;
    private final IdentityService identityService;
    private final TaskService taskService;

    public ReInsuranceDemoWorkFlow(final RuntimeService runtimeService, final TaskService taskService, final IdentityService identityService) {
        this.runtimeService = runtimeService;
        this.taskService = taskService;
        this.identityService = identityService;

        this.initUsersAndGroups();
    }

    protected void initUsersAndGroups() {
        Group reIndoAdmin = this.identityService.createGroupQuery()
            .groupId("reindo-admin").singleResult();
        if (reIndoAdmin == null) {
            reIndoAdmin = this.identityService.newGroup("reindo-admin");
            reIndoAdmin.setName("Re-Indo Admin");
            reIndoAdmin.setType("assignment");
            this.identityService.saveGroup(reIndoAdmin);
        }

        Group underwriter = this.identityService
            .createGroupQuery()
            .groupId("reindo-underwriter").singleResult();
        if (underwriter == null) {
            underwriter = this.identityService.newGroup("reindo-underwriter");
            underwriter.setName("Re-Indo Underwriter");
            underwriter.setType("assignment");
            this.identityService.saveGroup(underwriter);
        }

        User admin1 = this.identityService.createUserQuery()
            .userId("admin1").singleResult();
        if (admin1 == null) {
            admin1 = this.identityService.newUser("admin1");
            admin1.setFirstName("admin");
            admin1.setLastName("1");
            admin1.setEmail("admin1reindo@mailinator.com");
            admin1.setPassword("admin1");
            this.identityService.saveUser(admin1);
        }

        User underwriter1 = this.identityService.createUserQuery()
            .userId("underwriter1").singleResult();
        if (underwriter1 == null) {
            underwriter1 = this.identityService.newUser("underwriter1");
            underwriter1.setFirstName("underwriter");
            underwriter1.setLastName("1");
            underwriter1.setEmail("uw1reindo@mailinator.com");
            underwriter1.setPassword("underwriter1");
            this.identityService.saveUser(underwriter1);
        }

        try {
            this.identityService.createMembership("admin1", "reindo-admin");
            this.identityService.createMembership("underwriter1", "reindo-underwriter");
        } catch (RuntimeException e) {
            logger.warn(">>> Memberships: {} and or {} already exist.", "admin1:reindo-admin", "underwriter1:reindo-underwriter");
        }
    }

    public RuntimeService getRuntimeService() {
        return runtimeService;
    }

    public IdentityService getIdentityService() {
        return identityService;
    }

    public TaskService getTaskService() {
        return taskService;
    }

    public void receiveAnOffers(final Task task, final BusinessClassified businessClassified, final String facultativeCode) {
        final String taskName = task.getName() == null ? "" : task.getName();

        if (taskName.equals("Receive an Offers")) {
            this.taskService.claim(task.getId(), "admin1");

            final Map<String, Object> variables = new HashMap<>();
            variables.put("business_classified", businessClassified);
            variables.put("facultative_code", facultativeCode);

            logger.info(">>> ReceiveAnOffers. TaskService: {}, TaskID: {}", this.taskService, task.getId());
            this.taskService.complete(task.getId(), variables);
        }
    }

    public void inputCedingApplication(final Task task, final CreateCedingFormDTO createCedingFormDTO) {
        final String taskName = task.getName() == null ? "" : task.getName();

        if (taskName.equals("Input ceding application")) {
            this.taskService.claim(task.getId(), "admin1");
            final Map<String, Object> variables = new HashMap<>();
            variables.put("facultative_code", createCedingFormDTO.getFacultativeCode());
            variables.put("ceding_name", createCedingFormDTO.getCedingName());
            variables.put("ceding_address", createCedingFormDTO.getAddress());
            variables.put("ceding_contact_email", createCedingFormDTO.getContactEmail());
            variables.put("ceding_contact_phone", createCedingFormDTO.getContactPhone());
            variables.put("application_status" , createCedingFormDTO.getStatus());
            variables.put("share_in_percent", createCedingFormDTO.getShareInPercent());
            variables.put("application_amount", createCedingFormDTO.getAmount().longValue());

            this.taskService.complete(task.getId(), variables);
        }
    }

    public void cedingApplicationAcceptance(final Task task, final boolean accept) {
        final String taskName = task.getName() == null ? "" : task.getName();

        if (taskName.equals("Ceding Application Acceptance")) {
            this.taskService.claim(task.getId(), "underwriter1");
            final Map<String, Object> variables = new HashMap<>();
            variables.put("ceding_application_accepted", accept);

            this.taskService.complete(task.getId(), variables);
        }
    }
}
