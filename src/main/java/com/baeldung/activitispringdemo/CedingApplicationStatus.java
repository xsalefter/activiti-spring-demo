package com.baeldung.activitispringdemo;

import com.baeldung.activitispringdemo.util.TransformableEnum;

public enum CedingApplicationStatus implements TransformableEnum {
    P11_Offer_In_Progress,
    A01_Accept,
    D01_Decline,
    P21_Slip_In_Process,
    P31_Slip_Processed;

    @Override
    public String transform(final String activitiValue) {
        switch (activitiValue) {
            case "P11 - Offer In Process": return P11_Offer_In_Progress.name();
            case "A01 - Accept": return A01_Accept.name();
            case "D01 - Decline": return D01_Decline.name();
            case "P21 - Slip In Process": return P21_Slip_In_Process.name();
            case "P31 - Slip Processed": return P31_Slip_Processed.name();
            default: return null;
        }
    }
}
