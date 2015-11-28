package com.baeldung.activitispringdemo;

import java.io.Serializable;
import java.math.BigDecimal;

import org.activiti.engine.delegate.DelegateExecution;

import com.baeldung.activitispringdemo.util.ActivitiFormParser;

public class CreateCedingFormDTO implements Serializable {

    private static final long serialVersionUID = 5313448853795481125L;

    private String facultativeCode;
    private String cedingName;
    private String address;
    private String contactEmail;
    private String contactPhone;
    private CedingApplicationStatus status;
    private float shareInPercent;
    private BigDecimal amount;

    public CreateCedingFormDTO() {
    }

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

    public String getFacultativeCode() {
        return facultativeCode;
    }

    public void setFacultativeCode(String facultativeCode) {
        this.facultativeCode = facultativeCode;
    }

    public String getCedingName() {
        return cedingName;
    }

    public void setCedingName(String cedingName) {
        this.cedingName = cedingName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getContactPhone() {
        return contactPhone;
    }

    public void setContactPhone(String contactPhone) {
        this.contactPhone = contactPhone;
    }

    public CedingApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(CedingApplicationStatus status) {
        this.status = status;
    }

    public float getShareInPercent() {
        return shareInPercent;
    }

    public void setShareInPercent(float shareInPercent) {
        this.shareInPercent = shareInPercent;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "CreateCedingFormDTO [facultativeCode=" + facultativeCode + ", cedingName=" + cedingName + ", address="
                + address + ", contactEmail=" + contactEmail + ", contactPhone=" + contactPhone + ", status=" + status
                + ", shareInPercent=" + shareInPercent + ", amount=" + amount + "]";
    }

}
