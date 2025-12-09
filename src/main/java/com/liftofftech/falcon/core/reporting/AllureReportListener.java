package com.liftofftech.falcon.core.reporting;

import org.testng.IExecutionListener;
import org.testng.ISuite;
import org.testng.ISuiteListener;

/**
 * TestNG listener that sends Allure report via email after test execution completes.
 * Implements both ISuiteListener (for suite-based execution) and IExecutionListener 
 * (for single test method execution via -Dtest parameter).
 */
public class AllureReportListener implements ISuiteListener, IExecutionListener {

    private static boolean emailSent = false;

    @Override
    public void onStart(ISuite suite) {
        // Listener started
    }

    @Override
    public void onFinish(ISuite suite) {
        sendEmailIfNotSent();
    }

    @Override
    public void onExecutionStart() {
        emailSent = false;
    }

    @Override
    public void onExecutionFinish() {
        sendEmailIfNotSent();
    }

    private synchronized void sendEmailIfNotSent() {
        if (!emailSent) {
            try {
        EmailService.sendAllureReport();
                emailSent = true;
            } catch (Exception e) {
                System.err.println("Failed to send email report: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}

