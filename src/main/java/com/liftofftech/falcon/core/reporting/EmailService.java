package com.liftofftech.falcon.core.reporting;

import com.liftofftech.falcon.core.config.FrameworkConfig;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

/**
 * Service for sending Allure test reports via email.
 */
public final class EmailService {

    private static HttpServer httpServer;
    private static int serverPort = 8080;

    private EmailService() {
        // utility class
    }

    /**
     * Sends Allure report via email after test execution.
     * Generates the report and attaches it to the email.
     */
    public static void sendAllureReport() {
        if (!FrameworkConfig.emailEnabled()) {
            System.out.println("Email notifications are disabled. Skipping email send.");
            return;
        }
        
        System.out.println("=== Email Service: Starting email report generation ===");
        System.out.println("Email enabled: " + FrameworkConfig.emailEnabled());
        System.out.println("SMTP Host: " + FrameworkConfig.emailSmtpHost());
        System.out.println("SMTP Port: " + FrameworkConfig.emailSmtpPort());
        System.out.println("Email From: " + FrameworkConfig.emailFrom());
        System.out.println("Email Recipients: " + FrameworkConfig.emailRecipients());
        
        try {
            String reportPath = generateAllureReport();
            if (reportPath == null) {
                System.err.println("ERROR: Failed to generate Allure report. Email not sent.");
                System.err.println("Please check if Allure results exist in target/allure-results");
                return;
            }
            System.out.println("Report generated successfully at: " + reportPath);
            sendEmailWithLink(reportPath);
            System.out.println("SUCCESS: Email sent to " + FrameworkConfig.emailRecipients());
        } catch (MessagingException e) {
            System.err.println("ERROR: Failed to send email (MessagingException): " + e.getMessage());
            System.err.println("Cause: " + (e.getCause() != null ? e.getCause().getMessage() : "Unknown"));
            e.printStackTrace();
            System.err.println("\nTroubleshooting:");
            System.err.println("1. Check if email.enabled=true in config");
            System.err.println("2. Verify email.username and email.password are correct");
            System.err.println("3. For Gmail, ensure you're using an App Password (not regular password)");
            System.err.println("4. Check SMTP settings: " + FrameworkConfig.emailSmtpHost() + ":" + FrameworkConfig.emailSmtpPort());
        } catch (Exception e) {
            System.err.println("ERROR: Failed to send email: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Generates Allure report and returns the path to the generated report.
     * Tries to use Allure CLI (system or Maven-downloaded) for interactive report,
     * falls back to Maven site page if Allure CLI is not available.
     */
    private static String generateAllureReport() {
        try {
            String resultsDir = System.getProperty("allure.results.directory", "target/allure-results");
            String reportDir = "target/allure-report";
            
            // Check if results exist
            Path resultsPath = Paths.get(resultsDir);
            if (!Files.exists(resultsPath) || !Files.isDirectory(resultsPath)) {
                System.err.println("Allure results directory not found: " + resultsDir);
                return null;
            }

            String projectDir = System.getProperty("user.dir");
            
            // First, try to generate interactive report using Allure CLI
            String allureCmd = findAllureCommand(projectDir);
            if (allureCmd != null) {
                System.out.println("Found Allure CLI: " + allureCmd);
                System.out.println("Generating interactive Allure report...");
                
                if (generateInteractiveReport(allureCmd, resultsDir, reportDir, projectDir)) {
                    Path reportPath = Paths.get(reportDir);
                    Path indexHtml = reportPath.resolve("index.html");
                    if (Files.exists(indexHtml)) {
                        System.out.println("Interactive Allure report generated successfully!");
                        return reportDir;
                    }
                }
            }
            
            // Fallback: Use Maven plugin to generate Maven site page
            System.out.println("Allure CLI not found or failed. Using Maven plugin to generate site page...");
            return generateMavenSiteReport(resultsDir, projectDir);
            
        } catch (Exception e) {
            System.err.println("Error generating Allure report: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * Finds Allure CLI command - checks system PATH first, then Maven's .allure directory.
     */
    private static String findAllureCommand(String projectDir) {
        // First check if 'allure' is in system PATH
        if (isCommandAvailable("allure")) {
            return "allure";
        }
        
        // Check Maven plugin's .allure directory
        Path allureDir = Paths.get(projectDir, ".allure");
        if (Files.exists(allureDir) && Files.isDirectory(allureDir)) {
            try {
                // Find the latest Allure version directory
                java.util.List<Path> versions = Files.list(allureDir)
                    .filter(Files::isDirectory)
                    .filter(p -> p.getFileName().toString().startsWith("allure-"))
                    .sorted((a, b) -> b.getFileName().compareTo(a.getFileName()))
                    .collect(java.util.stream.Collectors.toList());
                
                if (!versions.isEmpty()) {
                    Path latestVersion = versions.get(0);
                    String os = System.getProperty("os.name").toLowerCase();
                    String executable = os.contains("win") ? "allure.bat" : "allure";
                    Path allureExecutable = latestVersion.resolve("bin").resolve(executable);
                    
                    if (Files.exists(allureExecutable) && Files.isRegularFile(allureExecutable)) {
                        return allureExecutable.toAbsolutePath().toString();
                    }
                }
            } catch (Exception e) {
                System.out.println("Could not search .allure directory: " + e.getMessage());
            }
        }
        
        return null;
    }
    
    /**
     * Checks if a command is available in system PATH.
     */
    private static boolean isCommandAvailable(String command) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            String[] checkCommand = os.contains("win") 
                ? new String[]{"cmd", "/c", "where", command}
                : new String[]{"which", command};
            
            Process process = new ProcessBuilder(checkCommand).start();
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Generates interactive Allure report using Allure CLI.
     */
    private static boolean generateInteractiveReport(String allureCmd, String resultsDir, String reportDir, String projectDir) {
        try {
            String os = System.getProperty("os.name").toLowerCase();
            boolean isWindows = os.contains("win");
            
            ProcessBuilder processBuilder;
            if (isWindows && !allureCmd.endsWith(".bat") && !allureCmd.endsWith(".exe")) {
                // On Windows, if path doesn't end with .bat, we need to call it differently
                processBuilder = new ProcessBuilder(
                    "cmd", "/c", allureCmd, "generate", resultsDir, "--clean", "-o", reportDir
                );
            } else {
                processBuilder = new ProcessBuilder(
                    allureCmd, "generate", resultsDir, "--clean", "-o", reportDir
                );
            }
            
            processBuilder.directory(new java.io.File(projectDir));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            
            // Read output
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Allure CLI output: " + line);
            }
            
            int exitCode = process.waitFor();
            return exitCode == 0;
        } catch (Exception e) {
            System.err.println("Failed to generate interactive report: " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Generates Maven site page report as fallback.
     */
    private static String generateMavenSiteReport(String resultsDir, String projectDir) {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder(
                "mvn", "allure:report", "-Dallure.results.directory=" + resultsDir
            );
            processBuilder.directory(new java.io.File(projectDir));
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            
            java.io.BufferedReader reader = new java.io.BufferedReader(
                new java.io.InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println("Maven Allure output: " + line);
            }
            
            int exitCode = process.waitFor();
            if (exitCode == 0) {
                String mavenReportDir = "target/site/allure-maven-plugin";
                if (Files.exists(Paths.get(mavenReportDir))) {
                    System.out.println("Maven site page generated (limited functionality).");
                    return mavenReportDir;
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("Failed to generate Maven site report: " + e.getMessage());
            return null;
        }
    }
    

    /**
     * Sends email with Allure report link.
     */
    private static void sendEmailWithLink(String reportPath) throws Exception {
        // Configure SMTP properties
        Properties props = new Properties();
        String port = FrameworkConfig.emailSmtpPort();
        props.put("mail.smtp.host", FrameworkConfig.emailSmtpHost());
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.ssl.trust", FrameworkConfig.emailSmtpHost());
        
        if ("465".equals(port)) {
            props.put("mail.smtp.socketFactory.port", "465");
            props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
            props.put("mail.smtp.ssl.enable", "true");
        } else {
            props.put("mail.smtp.starttls.enable", "true");
        }

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(
                    FrameworkConfig.emailUsername(),
                    FrameworkConfig.emailPassword()
                );
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FrameworkConfig.emailFrom()));
        
        // Set recipients
        String[] recipients = FrameworkConfig.emailRecipients().split(",");
        InternetAddress[] addresses = new InternetAddress[recipients.length];
        for (int i = 0; i < recipients.length; i++) {
            addresses[i] = new InternetAddress(recipients[i].trim());
        }
        message.setRecipients(Message.RecipientType.TO, addresses);
        
        // Add timestamp to subject to ensure each email is new (not threaded)
        String timestamp = java.time.LocalDateTime.now().format(
            java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        String subject = FrameworkConfig.emailSubject() + " - " + timestamp;
        message.setSubject(subject);
        
        // Set unique Message-ID to prevent threading
        message.setHeader("Message-ID", "<" + System.currentTimeMillis() + "@falcon-automation>");
        message.setHeader("X-Mailer", "Falcon Automation Framework");

        // Start HTTP server to serve the report
        Path reportDirPath = Paths.get(reportPath).toAbsolutePath();
        
        // Check for index.html (interactive report) or allure-maven.html (Maven site report)
        Path indexHtml = reportDirPath.resolve("index.html");
        Path mavenHtml = reportDirPath.resolve("allure-maven.html");
        String htmlFile = Files.exists(indexHtml) ? "index.html" : 
                          Files.exists(mavenHtml) ? "allure-maven.html" : "index.html";
        
        String reportLink = startHttpServer(reportDirPath, htmlFile);
        
        message.setContent(buildEmailBody(reportLink, reportPath), "text/html; charset=utf-8");
        Transport.send(message);
    }

    /**
     * Checks if running in CI environment (GitHub Actions, Jenkins, etc.)
     */
    private static boolean isCIEnvironment() {
        String ci = System.getenv("CI");
        String githubActions = System.getenv("GITHUB_ACTIONS");
        String jenkins = System.getenv("JENKINS_URL");
        return "true".equalsIgnoreCase(ci) || "true".equalsIgnoreCase(githubActions) || jenkins != null;
    }
    
    /**
     * Starts a simple HTTP server to serve the Allure report and returns the URL.
     */
    private static String startHttpServer(Path reportDir, String htmlFile) {
        // In CI environments, skip HTTP server and use file path or artifact info
        if (isCIEnvironment()) {
            System.out.println("Running in CI environment. Skipping HTTP server.");
            String filePath = reportDir.resolve(htmlFile).toAbsolutePath().toString().replace("\\", "/");
            System.out.println("Report file path: " + filePath);
            return "CI_ARTIFACT:" + filePath;
        }
        
        try {
            // Find an available port starting from 8080
            for (int port = 8080; port < 8100; port++) {
                try {
                    httpServer = HttpServer.create(new InetSocketAddress(port), 0);
                    serverPort = port;
                    break;
                } catch (IOException e) {
                    // Port in use, try next
                    continue;
                }
            }
            
            if (httpServer == null) {
                System.err.println("WARNING: Could not start HTTP server. Using file path instead.");
                return "file://" + reportDir.resolve(htmlFile).toAbsolutePath().toString().replace("\\", "/");
            }
            
            // Create context to serve files from the report directory
            httpServer.createContext("/", new StaticFileHandler(reportDir, htmlFile));
            
            // Use a daemon thread pool so server can keep running in background
            httpServer.setExecutor(java.util.concurrent.Executors.newCachedThreadPool());
            httpServer.start();
            
            // Keep server running in background - don't block test completion
            // The server will run until JVM exits or explicitly stopped
            Thread serverThread = new Thread(() -> {
                try {
                    // Keep the server running
                    Thread.sleep(Long.MAX_VALUE);
                } catch (InterruptedException e) {
                    // Server stopped
                }
            });
            serverThread.setDaemon(true);
            serverThread.start();
            
            // Add shutdown hook to stop gracefully
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                if (httpServer != null) {
                    System.out.println("Stopping HTTP server on port " + serverPort + "...");
                    httpServer.stop(0);
                }
            }));
            
            String reportUrl = "http://localhost:" + serverPort + "/" + htmlFile;
            
            // Print instructions
            System.out.println("HTTP server started successfully on port " + serverPort + "!");
            System.out.println("Report accessible at: " + reportUrl);
            System.out.println("");
            System.out.println("NOTE: If the server stops, you can manually start it using:");
            System.out.println("  cd " + reportDir.toAbsolutePath());
            System.out.println("  python3 -m http.server " + serverPort);
            System.out.println("  # Then access: " + reportUrl);
            
            return reportUrl;
        } catch (Exception e) {
            System.err.println("WARNING: Failed to start HTTP server: " + e.getMessage());
            // Fallback to file:// URL
            return "file://" + reportDir.resolve(htmlFile).toAbsolutePath().toString().replace("\\", "/");
        }
    }
    
    /**
     * HTTP handler to serve static files from a directory.
     */
    private static class StaticFileHandler implements HttpHandler {
        private final Path baseDir;
        private final String defaultHtmlFile;
        
        public StaticFileHandler(Path baseDir, String defaultHtmlFile) {
            this.baseDir = baseDir;
            this.defaultHtmlFile = defaultHtmlFile;
        }
        
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String requestPath = exchange.getRequestURI().getPath();
            
            // Handle root path - serve default HTML file
            if (requestPath.equals("/") || requestPath.isEmpty()) {
                requestPath = "/" + defaultHtmlFile;
            }
            
            // Remove leading slash and resolve path relative to base directory
            String relativePath = requestPath.startsWith("/") ? requestPath.substring(1) : requestPath;
            Path filePath = baseDir.resolve(relativePath).normalize();
            
            // Security check: ensure the resolved path is within baseDir
            if (!filePath.startsWith(baseDir.normalize())) {
                send404(exchange, requestPath);
                return;
            }
            
            // Check if file exists and is a regular file
            if (Files.exists(filePath) && Files.isRegularFile(filePath)) {
                serveFile(exchange, filePath);
            } else if (Files.exists(filePath) && Files.isDirectory(filePath)) {
                // If it's a directory, try to serve index.html from that directory
                Path indexInDir = filePath.resolve("index.html");
                if (Files.exists(indexInDir)) {
                    serveFile(exchange, indexInDir);
                } else {
                    // Directory without index.html - redirect to root
                    redirectToRoot(exchange);
                }
            } else {
                // File not found - try fallback
                if (requestPath.endsWith(".html") || requestPath.equals("/")) {
                    Path fallbackPath = baseDir.resolve(defaultHtmlFile);
                    if (Files.exists(fallbackPath)) {
                        serveFile(exchange, fallbackPath);
                    } else {
                        send404(exchange, requestPath);
                    }
                } else {
                    send404(exchange, requestPath);
                }
            }
        }
        
        private void serveFile(HttpExchange exchange, Path filePath) throws IOException {
            try {
                byte[] fileBytes = Files.readAllBytes(filePath);
                String contentType = getContentType(filePath);
                
                // Set headers
                exchange.getResponseHeaders().set("Content-Type", contentType);
                exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
                exchange.getResponseHeaders().set("Cache-Control", "no-cache, no-store, must-revalidate");
                exchange.getResponseHeaders().set("Pragma", "no-cache");
                exchange.getResponseHeaders().set("Expires", "0");
                
                exchange.sendResponseHeaders(200, fileBytes.length);
                
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(fileBytes);
                }
            } catch (IOException e) {
                System.err.println("Error serving file: " + filePath + " - " + e.getMessage());
                send404(exchange, filePath.toString());
            }
        }
        
        private void send404(HttpExchange exchange, String requestPath) throws IOException {
            String response = "404 - File not found: " + requestPath;
            exchange.getResponseHeaders().set("Content-Type", "text/plain");
            exchange.sendResponseHeaders(404, response.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
        
        private void redirectToRoot(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().set("Location", "/" + defaultHtmlFile);
            exchange.sendResponseHeaders(302, 0);
            exchange.close();
        }
        
        private String getContentType(Path filePath) {
            String fileName = filePath.getFileName().toString().toLowerCase();
            if (fileName.endsWith(".html") || fileName.endsWith(".htm")) {
                return "text/html; charset=utf-8";
            }
            if (fileName.endsWith(".css")) {
                return "text/css; charset=utf-8";
            }
            if (fileName.endsWith(".js")) {
                return "application/javascript; charset=utf-8";
            }
            if (fileName.endsWith(".json")) {
                return "application/json; charset=utf-8";
            }
            if (fileName.endsWith(".png")) {
                return "image/png";
            }
            if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg")) {
                return "image/jpeg";
            }
            if (fileName.endsWith(".svg")) {
                return "image/svg+xml";
            }
            if (fileName.endsWith(".ico")) {
                return "image/x-icon";
            }
            if (fileName.endsWith(".woff") || fileName.endsWith(".woff2")) {
                return "font/woff2";
            }
            if (fileName.endsWith(".ttf")) {
                return "font/ttf";
            }
            if (fileName.endsWith(".eot")) {
                return "application/vnd.ms-fontobject";
            }
            // Default to binary for unknown types
            return "application/octet-stream";
        }
    }

    /**
     * Builds the email body with test execution summary and report link.
     */
    private static String buildEmailBody(String reportLink, String reportPath) {
        String environment = FrameworkConfig.environment();
        String timestamp = java.time.LocalDateTime.now().toString();
        
        StringBuilder body = new StringBuilder();
        body.append("<html><body>");
        body.append("<h2>Test Execution Report</h2>");
        body.append("<p><strong>Environment:</strong> ").append(environment).append("</p>");
        body.append("<p><strong>Execution Time:</strong> ").append(timestamp).append("</p>");
        
        // Handle CI environment
        if (reportLink.startsWith("CI_ARTIFACT:")) {
            String artifactPath = reportLink.substring("CI_ARTIFACT:".length());
            body.append("<div style=\"background: #fff3cd; border-left: 4px solid #ffc107; padding: 15px; margin: 15px 0; border-radius: 4px;\">");
            body.append("<p style=\"margin: 0 0 10px 0; color: #856404; font-size: 14px; font-weight: bold;\">");
            body.append("📊 Test Execution Completed in CI Environment</p>");
            body.append("<p style=\"margin: 0 0 15px 0; color: #333; font-size: 13px;\">");
            body.append("The Allure report has been generated and uploaded as an artifact in GitHub Actions. ");
            body.append("Please download the report from the Actions tab → Artifacts section.");
            body.append("</p>");
            body.append("<p style=\"margin: 0; color: #666; font-size: 12px;\">");
            body.append("<strong>Report Path:</strong> <code style=\"background: #f5f5f5; padding: 2px 6px;\">").append(artifactPath).append("</code>");
            body.append("</p>");
            body.append("</div>");
        } else if (!reportLink.isEmpty() && reportLink.startsWith("http://")) {
            body.append("<div style=\"background: #e7f3ff; border-left: 4px solid #2196F3; padding: 15px; margin: 15px 0; border-radius: 4px;\">");
            body.append("<p style=\"margin: 0 0 10px 0; color: #1976D2; font-size: 14px; font-weight: bold;\">");
            body.append("📊 Interactive Allure Report Ready</p>");
            body.append("<p style=\"margin: 0 0 15px 0; color: #333; font-size: 13px;\">");
            body.append("<strong>⚠️ Important:</strong> Use the HTTP link below (not file://) to view the full interactive report. ");
            body.append("Opening the file directly from your folder will show 'Loading...' due to browser security restrictions.");
            body.append("</p>");
            body.append("<p style=\"margin: 0;\">");
            body.append("<a href=\"").append(reportLink).append("\" ");
            body.append("style=\"display: inline-block; background: #2196F3; color: white; padding: 12px 24px; ");
            body.append("text-decoration: none; border-radius: 4px; font-weight: bold; font-size: 16px;\">");
            body.append("👉 Click here to view the Allure report</a>");
            body.append("</p>");
            body.append("</div>");
            
            body.append("<p style=\"color: #666; font-size: 12px; margin-top: 10px;\">");
            body.append("<strong>Report URL:</strong> <code style=\"background: #f5f5f5; padding: 2px 6px;\">").append(reportLink).append("</code>");
            body.append("</p>");
            
            // Add note about Maven site page vs interactive report
            if (reportPath.contains("allure-maven-plugin")) {
                body.append("<div style=\"background: #fff3cd; border-left: 4px solid #ffc107; padding: 10px; margin: 10px 0;\">");
                body.append("<p style=\"margin: 0; color: #856404; font-size: 12px;\">");
                body.append("<strong>Note:</strong> This is a Maven site page. For the full interactive Allure report with detailed test results, ");
                body.append("install Allure CLI and run:<br>");
                body.append("<code style=\"background: #f8f9fa; padding: 4px 8px; display: inline-block; margin-top: 5px;\">");
                body.append("allure generate target/allure-results --clean && allure open</code>");
                body.append("</p></div>");
            }
            
            body.append("<div style=\"background: #f5f5f5; padding: 10px; margin: 10px 0; border-radius: 4px;\">");
            body.append("<p style=\"margin: 0; color: #666; font-size: 11px;\">");
            body.append("<strong>Server Status:</strong> The HTTP server is running on your local machine (port ").append(serverPort).append("). ");
            body.append("</p>");
            body.append("<p style=\"margin: 10px 0 0 0; color: #d32f2f; font-size: 11px; font-weight: bold;\">");
            body.append("⚠️ If the link doesn't work (shows 'try again' or connection error), the server may have stopped.");
            body.append("</p>");
            body.append("<p style=\"margin: 10px 0 0 0; color: #666; font-size: 11px;\">");
            body.append("<strong>Solution:</strong> Manually start a server using one of these methods:<br><br>");
            body.append("<strong>Method 1 - Quick Script:</strong><br>");
            body.append("<code style=\"background: white; padding: 6px 10px; display: inline-block; margin-top: 5px; font-family: monospace; border: 1px solid #ddd;\">");
            body.append("./start-report-server.sh ").append(serverPort);
            body.append("</code><br><br>");
            body.append("<strong>Method 2 - Python Command:</strong><br>");
            body.append("<code style=\"background: white; padding: 6px 10px; display: inline-block; margin-top: 5px; font-family: monospace; border: 1px solid #ddd;\">");
            body.append("cd ").append(reportPath).append(" && python3 -m http.server ").append(serverPort);
            body.append("</code><br><br>");
            body.append("Then click the link again or access: <code style=\"background: white; padding: 2px 4px;\">").append(reportLink).append("</code>");
            body.append("</p></div>");
        } else {
            body.append("<p><strong>Report Location:</strong> ").append(reportPath).append("/index.html</p>");
            if (!reportLink.isEmpty()) {
                body.append("<p style=\"color: #666; font-size: 12px;\">File path: ").append(reportLink).append("</p>");
            }
            body.append("<div style=\"background: #fff3cd; border-left: 4px solid #ffc107; padding: 10px; margin: 10px 0;\">");
            body.append("<p style=\"margin: 0; color: #856404; font-size: 12px;\">");
            body.append("<strong>⚠️ Note:</strong> Opening the file directly (file://) will show 'Loading...' due to browser security restrictions. ");
            body.append("Please use an HTTP server to view the report properly.");
            body.append("</p></div>");
        }
        
        body.append("<p>Best regards,<br>Falcon Automation Framework</p>");
        body.append("</body></html>");
        
        return body.toString();
    }
}

