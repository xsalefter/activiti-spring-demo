package com.baeldung.activitispringdemo;

import org.junit.Rule;
import org.junit.Test;

import com.icegreen.greenmail.junit.GreenMailRule;
import com.icegreen.greenmail.util.GreenMailUtil;
import com.icegreen.greenmail.util.ServerSetupTest;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.*;

public class GreenMailTest {

    @Rule
    public GreenMailRule greenMailRule = new GreenMailRule(ServerSetupTest.ALL);

    @Test
    public void testSendEmail() {
        GreenMailUtil.sendTextEmailTest("from@localhost", "to@localhost", "Testing Email Title", "Testing email body.");

        assertThat(greenMailRule.getReceivedMessages(), is(notNullValue()));

        final int emailSize = greenMailRule.getReceivedMessages().length;
        assertThat(emailSize, is(1));
    }
}
