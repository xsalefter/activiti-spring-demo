package com.baeldung.activitispringdemo.util;

import org.activiti.engine.delegate.DelegateExecution;
import org.junit.Test;

import com.baeldung.activitispringdemo.activiti.CedingApplicationStatus;
import com.baeldung.activitispringdemo.util.ActivitiFormParser;

import static org.mockito.Mockito.*;

import java.util.Date;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ActivitiFormParserTest {

    private DelegateExecution execution = mock(DelegateExecution.class);

    @Test
    public void whenGetStringCalled_ThenResultShouldBeValid() {
        when(this.execution.getVariable("company_name")).thenReturn("Some Company");

        ActivitiFormParser parser = new ActivitiFormParser(this.execution);
        final String companyName = parser.getString("company_name");
        assertThat(companyName, is("Some Company"));
    }

    @Test
    public void whenGetStringCalledAndActualValueIsNull_ThenShouldReturnNull() {
        when(this.execution.getVariable("company_name")).thenReturn(null);

        ActivitiFormParser parser = new ActivitiFormParser(this.execution);
        final String companyName = parser.getString("company_name");
        assertThat(companyName, is(nullValue()));
    }

    @Test
    public void whenGetStringCalledAndUseDefaultValueParams_ThenShouldReturnDefaultValue() {
        when(this.execution.getVariable("company_name")).thenReturn(null);

        ActivitiFormParser parser = new ActivitiFormParser(this.execution);
        final String companyName = parser.getString("company_name", "Some Other Company");
        assertThat(companyName, is("Some Other Company"));
    }

    @Test
    public void whenGetLongCalled_ThenResultShouldBeValid() {
        when(this.execution.getVariable("company_phone_number")).thenReturn(123456L);

        ActivitiFormParser parser = new ActivitiFormParser(this.execution);
        final Long phoneNumber = parser.getLong("company_phone_number");
        assertThat(phoneNumber, is(123456L));
    }

    @Test
    public void whenGetDateCalled_ThenResultShouldBeValid() {
        final Date toMock = new Date();
        when(this.execution.getVariable("company_whatever_date")).thenReturn(toMock);

        final ActivitiFormParser parser = new ActivitiFormParser(this.execution);
        final Date value = parser.getDate("company_whatever_date");
        assertThat(value, is(toMock));
    }

    @Test
    public void whenGetEnumCalledOfferInProcess_ThenResultShouldBeValid() {
        when(this.execution.getVariable("application_status")).thenReturn("P11 - Offer In Process");

        final String applicationStatus = this.execution.getVariable("application_status").toString();
        assertThat(applicationStatus, is("P11 - Offer In Process"));

        final ActivitiFormParser parser = new ActivitiFormParser(this.execution);
        final CedingApplicationStatus status = parser.getEnum(CedingApplicationStatus.class, "application_status");

        // Cool, huh?
        assertThat(status, is(CedingApplicationStatus.P11_Offer_In_Progress));
    }

    @Test
    public void whenGetEnumCalledAccept_ThenResultShouldBeValid() {
        when(this.execution.getVariable("application_status")).thenReturn("A01 - Accept");

        final String applicationStatus = this.execution.getVariable("application_status").toString();
        assertThat(applicationStatus, is("A01 - Accept"));

        final ActivitiFormParser parser = new ActivitiFormParser(this.execution);
        final CedingApplicationStatus status = parser.getEnum(CedingApplicationStatus.class, "application_status");

        assertThat(status, is(CedingApplicationStatus.A01_Accept));
    }
}
