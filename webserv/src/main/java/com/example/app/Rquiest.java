package com.example.app;

import java.util.HashMap;
import java.util.Map;

class Request
{
    // ===== Request Line =====
    private String method;
    private String path;
    private String version;

    // ===== Headers =====
    private Map<String, String> headers = new HashMap<String, String>();

    // ===== Body =====
    private String body;
    private String bodyFilePath; // Path to file containing large body data

    private static void headersPartParcer(Request req, String headerPart)
    {
        // Split lines by \r\n
        String[] lines = headerPart.split("\r\n");

        // ---- Parse request line ----
        if (lines.length > 0) {
            String[] startLine = lines[0].split(" ");
            if (startLine.length >= 3) {
                req.method = startLine[0];
                req.path = startLine[1];
                req.version = startLine[2];
                System.out.println("✓ method    : " + req.method);
                System.out.println("✓ path      : " + req.path);
                System.out.println("✓ version   : " + req.version);
            } else {
                System.err.println("✗ Invalid request line: " + lines[0]);
            }
        }

        // ---- Parse headers ----
        for (int i = 1; i < lines.length; i++)
        {
            String line = lines[i].trim();
            System.out.println("LINE[" + i + "]: [" + line + "]");
            if (line.isEmpty())
                continue;  // Changed from break to continue

            int colonIndex = line.indexOf(':');
            if (colonIndex == -1)
                continue;

            String key = line.substring(0, colonIndex).trim().toLowerCase();
            String value = line.substring(colonIndex + 1).trim();

            System.out.println("KEY = " + key + " | VALUE = " + value);

            req.headers.put(key, value);
        }
        
        System.out.println("=== All Headers ===");
        req.headers.forEach((key, value) -> System.out.println(key + " : " + value));
        System.out.println("=== All Headers ===");

    }
    private static void bodyPartParcer(Request req, String bodyPart)
    {
        System.out.println("=== Body Part ===");
        System.out.println(bodyPart);
        System.out.println("=== Body Part ===");
    }


    public static Request parseRequest(String request)
    {
        return parseRequest(request, null);
    }

    // Overloaded method to handle file-based body data for large uploads
    public static Request parseRequest(String request, String bodyFilePath)
    {
        Request req = new Request();

        String[] parts = request.split("\r\n\r\n", 2);
        
        System.out.println("✓ Parts found: " + parts.length);
        if (parts.length > 0) {
            System.out.println("✓ Headers part exists");
        }
        if (parts.length > 1) {
            System.out.println("✓ Body part exists");
        }
        
        String headerPart = parts[0];
        headersPartParcer(req, headerPart);
        
        if (bodyFilePath != null) {
            req.bodyFilePath = bodyFilePath;
            req.body = "[Large body data in file: " + bodyFilePath + "]";
            System.out.println("✓ Large body data stored in file: " + bodyFilePath);
        } else if (parts.length > 1) {
            // Small body data - handle normally
            req.body = parts[1];
            bodyPartParcer(req, req.body);
        } else {
            req.body = "";
        }
            
        return req;
    }

    // ===== Getters =====
    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getVersion() { return version; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }
    public String getBodyFilePath() { return bodyFilePath; }
    
    public boolean hasBodyInFile() { return bodyFilePath != null; }
}