package com.liftofftech.falcon.core.reporting;

import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 * TestNG listener that sends Allure report via email after test suite execution completes.
 */
public class AllureReportListener implements ISuiteListener {

    @Override
    public void onStart(ISuite suite) {
        System.out.println("=== AllureReportListener: Suite started ===");
    }

    @Override
    public void onFinish(ISuite suite) {
        System.out.println("=== AllureReportListener: Suite finished, triggering email service ===");
        // Send email report after suite completes
        EmailService.sendAllureReport();
    }
}

