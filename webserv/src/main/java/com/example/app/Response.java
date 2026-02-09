package com.example.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

class Response
{
    private static String getMimeType(String filePath) {
        if (filePath.endsWith(".html") || filePath.endsWith(".htm")) return "text/html";
        if (filePath.endsWith(".css")) return "text/css";
        if (filePath.endsWith(".js")) return "application/javascript";
        if (filePath.endsWith(".png")) return "image/png";
        if (filePath.endsWith(".jpg") || filePath.endsWith(".jpeg")) return "image/jpeg";
        return "text/plain";
    }
    
    static String getMethod(Request req, ServerConfig cfg)
    {
        String url = req.getPath();
        System.out.println("GET URL: " + url);
        if (url.equals("/"))
        {
            System.out.println("Serving index.html");
            String documentRoot = cfg.getDocumentRoot();
            url = "/index.html";
        }
        
        try {
            String filePath = cfg.getDocumentRoot() + url;
            System.out.println("Document Root: " + cfg.getDocumentRoot());
            System.out.println("Requested URL: " + url);
            System.out.println("Full File Path: " + filePath);
            File file = new File(filePath);
            System.out.println("File exists: " + file.exists());
            System.out.println("Is file: " + file.isFile());
            System.out.println("Absolute path: " + file.getAbsolutePath());
            
            if (file.exists() && file.isFile()) {
                // Read file content
                String content = Files.readString(file.toPath());
                String mimeType = getMimeType(filePath);
                
                // Return HTTP response
                return req.getVersion() + " 200 OK\r\n" +
                    "Content-Type: " + mimeType + "\r\n" +
                    "Content-Length: " + content.length() + "\r\n\r\n" +
                    content;
            } else {
                // File not found
                return req.getVersion() + " 404 Not Found\r\n" +
                    "Content-Type: text/plain\r\n\r\n" +
                    "File not found";
            }
        } catch (IOException e) {
            return req.getVersion() + " 500 Internal Server Error\r\n" +
                "Content-Type: text/plain\r\n\r\n" +
                "Server error";
        }
    }


    static String postmethod(Request req, ServerConfig cfg)
    {
        String url = req.getPath();
        System.out.println("POST URL: " + url);


        return req.getVersion() + " 200 OK\r\n" +
            "Content-Type: text/plain\r\n\r\n" +
            "POST request received for " + url;
    }
    
    static public String response(Request req, ServerConfig cfg)
    {
        String method = req.getMethod();
        String path = req.getPath();
        String version = req.getVersion();
        String body = req.getBody();
        String bodyFilePath = req.getBodyFilePath();
        System.out.println("=== Response ===");
        System.out.println("Method: " + method);
        System.out.println("Path: " + path);
        System.out.println("Version: " + version);
        System.out.println("Body: " + (body != null ? body.substring(0, Math.min(100, body.length())) + "..." : "null"));
        System.out.println("Body File Path: " + bodyFilePath);
        if (method.equals("GET"))
        {
            return getMethod(req, cfg);
        }
        else if (method.equals("POST"))
        {
            return postmethod(req, cfg);
        }
        else
        {
            return version + " 405 Method Not Allowed\r\nContent-Type: text/plain\r\n\r\nMethod Not Allowed";
        }
    }
}
