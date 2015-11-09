package com.baeldung.activitispringdemo;

import java.io.Serializable;
import java.math.BigDecimal;

import org.activiti.engine.delegate.DelegateExecution;

import com.baeldung.activitispringdemo.util.ActivitiFormParser;

public class CreateCedingFormDTO implements Serializable {

    private static final long serialVersionUID = 5313448853795481125L;

    private final String facultativeCode;
    private final String cedingName;
    private final String address;
    private final String contactEmail;
    private final String contactPhone;
    private final CedingApplicationStatus status;
    private final float shareInPercent;
    private final BigDecimal amount;

    public CreateCedingFormDTO(final DelegateExecution execution) {
        final ActivitiFormParser parser = new ActivitiFormParser(execution);

        this.facultativeCode = parser.getString("facultative_code");
        this.cedingName = parser.getString("ceding_name");
        this.address = parser.getString("ceding_address");
        this.contactEmail = parser.getString("ceding_contact_email");
        this.contactPhone = parser.getString("ceding_contact_phone");
        this.status = parser.getEnum(CedingApplicationStatus.class, "application_status");
        this.shareInPercent = 0f;
        this.amount = BigDecimal.ZERO;
    }

    public CreateCedingFormDTO(String facultativeCode, String cedingName, String address, String contactEmail,
            String contactPhone, CedingApplicationStatus status, float shareInPercent, BigDecimal amount) {
        super();
        this.facultativeCode = facultativeCode;
        this.cedingName = cedingName;
        this.address = address;
        this.contactEmail = contactEmail;
        this.contactPhone = contactPhone;
        this.status = status;
        this.shareInPercent = shareInPercent;
        this.amount = amount;
    }

    public String getFacultativeCode() {
        return facultativeCode;
    }

    public String getCedingName() {
        return cedingName;
    }

    public String getAddress() {
        return address;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public CedingApplicationStatus getStatus() {
        return status;
    }

    public float getShareInPercent() {
        return shareInPercent;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    @Override
    public String toString() {
        return "CreateCedingFormDTO [facultativeCode=" + facultativeCode + ", cedingName=" + cedingName + ", address="
                + address + ", contactEmail=" + contactEmail + ", contactPhone=" + contactPhone + ", status=" + status
                + ", shareInPercent=" + shareInPercent + ", amount=" + amount + "]";
    }


}
