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
        String body = req.getBody();
        String bodyFilePath = req.getBodyFilePath();
        
        System.out.println("=== GET Request Processing ===");
        System.out.println("GET URL: " + url);
        System.out.println("Headers: " + req.getHeaders());
        
        // Handle GET request body (for 42 webserv compliance)
        if (body != null && !body.trim().isEmpty()) {
            System.out.println("WARNING: GET request contains body data: " + 
                              body.substring(0, Math.min(100, body.length())));
        }
        
        if (bodyFilePath != null && !bodyFilePath.isEmpty()) {
            System.out.println("WARNING: GET request has body file: " + bodyFilePath);
            try {
                String fileBody = Files.readString(new File(bodyFilePath).toPath());
                System.out.println("Body from file preview: " + 
                                  fileBody.substring(0, Math.min(100, fileBody.length())));
            } catch (IOException e) {
                System.err.println("Error reading body file: " + e.getMessage());
            }
        }
        
        // Handle root path
        if (url.equals("/")) {
            System.out.println("Serving index.html for root request");
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
            System.out.println("Is directory: " + file.isDirectory());
            System.out.println("Absolute path: " + file.getAbsolutePath());
            System.out.println("Can read: " + file.canRead());
            
            if (file.exists() && file.isFile()) {
                // Read file content
                String content = Files.readString(file.toPath());
                String mimeType = getMimeType(filePath);
                
                System.out.println("✓ File served successfully");
                System.out.println("File size: " + content.length() + " bytes");
                System.out.println("MIME type: " + mimeType);
                
                // Return HTTP response with proper headers
                return req.getVersion() + " 200 OK\r\n" +
                    "Content-Type: " + mimeType + "\r\n" +
                    "Content-Length: " + content.length() + "\r\n" +
                    "Cache-Control: public, max-age=3600\r\n" +
                    "Connection: keep-alive\r\n\r\n" +
                    content;
                    
            } else if (file.exists() && file.isDirectory()) {
                // Directory listing or index file handling
                System.out.println("Directory access attempted: " + filePath);
                
                // Try to serve index.html from directory
                File indexFile = new File(file, "index.html");
                if (indexFile.exists() && indexFile.isFile()) {
                    String content = Files.readString(indexFile.toPath());
                    System.out.println("✓ Serving index.html from directory");
                    
                    return req.getVersion() + " 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + content.length() + "\r\n\r\n" +
                        content;
                } else {
                    // Directory access forbidden
                    String errorMsg = "403 Forbidden - Directory access denied";
                    return req.getVersion() + " 403 Forbidden\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Content-Length: " + errorMsg.length() + "\r\n\r\n" +
                        errorMsg;
                }
            } else {
                // File not found
                System.out.println("✗ File not found: " + filePath);
                String errorMsg = "404 Not Found - The requested resource was not found on this server.";
                
                return req.getVersion() + " 404 Not Found\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: " + errorMsg.length() + "\r\n\r\n" +
                    errorMsg;
            }
            
        } catch (IOException e) {
            System.err.println("✗ IOException serving file: " + e.getMessage());
            e.printStackTrace();
            
            String errorMsg = "500 Internal Server Error - Unable to read file: " + e.getMessage();
            return req.getVersion() + " 500 Internal Server Error\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + errorMsg.length() + "\r\n\r\n" +
                errorMsg;
        }
    }


    static String postmethod(Request req, ServerConfig cfg)
    {
        String url = req.getPath();
        String body = req.getBody();
        String bodyFilePath = req.getBodyFilePath();
        
        System.out.println("POST URL: " + url);
        
        try {
            // Handle body content
            String requestBody = "";
            
            if (bodyFilePath != null && !bodyFilePath.isEmpty()) {
                // Read body from file if it was stored there due to large size
                System.out.println("Reading body from file: " + bodyFilePath);
                File bodyFile = new File(bodyFilePath);
                if (bodyFile.exists()) {
                    requestBody = Files.readString(bodyFile.toPath());
                }
            } else if (body != null && !body.isEmpty()) {
                // Use body directly
                requestBody = body;
            }
            
            System.out.println("POST Body length: " + requestBody.length());
            if (!requestBody.isEmpty()) {
                System.out.println("POST Body preview: " + requestBody.substring(0, Math.min(100, requestBody.length())));
            }
            
            // Handle different POST endpoints
            if (url.equals("/upload") || url.startsWith("/upload/")) {
                return handleFileUpload(req, cfg, requestBody);
            } else if (url.equals("/form") || url.startsWith("/form/")) {
                return handleFormSubmission(req, cfg, requestBody);
            } else {
                // Generic POST response
                String responseBody = "POST request processed successfully\n" +
                    "Path: " + url + "\n" +
                    "Body length: " + requestBody.length() + "\n" +
                    "Body content: " + (requestBody.length() > 0 ? 
                        requestBody.substring(0, Math.min(200, requestBody.length())) + 
                        (requestBody.length() > 200 ? "..." : "") : "empty");
                
                return req.getVersion() + " 200 OK\r\n" +
                    "Content-Type: text/plain\r\n" +
                    "Content-Length: " + responseBody.length() + "\r\n\r\n" +
                    responseBody;
            }
            
        } catch (IOException e) {
            System.err.println("Error processing POST request: " + e.getMessage());
            return req.getVersion() + " 500 Internal Server Error\r\n" +
                "Content-Type: text/plain\r\n\r\n" +
                "Error processing POST request: " + e.getMessage();
        }
    }
    
    private static String handleFileUpload(Request req, ServerConfig cfg, String body) {
        try {
            // Create uploads directory if it doesn't exist
            String uploadDir = cfg.getDocumentRoot() + "/uploads";
            File uploadDirFile = new File(uploadDir);
            if (!uploadDirFile.exists()) {
                uploadDirFile.mkdirs();
            }
            
            // For now, just save the body content to a file
            String fileName = "upload_" + System.currentTimeMillis() + ".txt";
            File uploadFile = new File(uploadDir, fileName);
            Files.writeString(uploadFile.toPath(), body);
            
            String response = "File uploaded successfully: " + fileName + 
                "\nSize: " + body.length() + " bytes";
            
            return req.getVersion() + " 200 OK\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + response.length() + "\r\n\r\n" +
                response;
                
        } catch (IOException e) {
            return req.getVersion() + " 500 Internal Server Error\r\n" +
                "Content-Type: text/plain\r\n\r\n" +
                "Upload failed: " + e.getMessage();
        }
    }
    
    private static String handleFormSubmission(Request req, ServerConfig cfg, String body) {
        // Parse form data (application/x-www-form-urlencoded)
        String[] pairs = body.split("&");
        StringBuilder formData = new StringBuilder("Form data received:\n");
        
        for (String pair : pairs) {
            String[] keyValue = pair.split("=", 2);
            if (keyValue.length == 2) {
                formData.append(keyValue[0]).append(" = ").append(keyValue[1]).append("\n");
            }
        }
        
        String response = formData.toString();
        return req.getVersion() + " 200 OK\r\n" +
            "Content-Type: text/plain\r\n" +
            "Content-Length: " + response.length() + "\r\n\r\n" +
            response;
    }
    
    static public String response(Request req, ServerConfig cfg)
    {
        String method = req.getMethod();
        String path = req.getPath();
        String version = req.getVersion();
        String body = req.getBody();
        String bodyFilePath = req.getBodyFilePath();
        
        System.out.println("=== HTTP Request Processing ===");
        System.out.println("Method: " + (method != null ? method : "NULL"));
        System.out.println("Path: " + (path != null ? path : "NULL"));
        System.out.println("Version: " + (version != null ? version : "NULL"));
        System.out.println("Body: " + (body != null ? body.substring(0, Math.min(100, body.length())) + "..." : "null"));
        System.out.println("Body File Path: " + (bodyFilePath != null ? bodyFilePath : "null"));
        System.out.println("Headers: " + req.getHeaders());
        
        // Validate request components
        if (method == null || method.isEmpty()) {
            String errorMsg = "400 Bad Request - Missing HTTP method";
            return (version != null ? version : "HTTP/1.1") + " 400 Bad Request\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + errorMsg.length() + "\r\n\r\n" +
                errorMsg;
        }
        
        if (path == null || path.isEmpty()) {
            String errorMsg = "400 Bad Request - Missing request path";
            return (version != null ? version : "HTTP/1.1") + " 400 Bad Request\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + errorMsg.length() + "\r\n\r\n" +
                errorMsg;
        }
        
        if (version == null || version.isEmpty()) {
            version = "HTTP/1.1"; // Default to HTTP/1.1 if missing
            System.out.println("WARNING: Missing HTTP version, defaulting to HTTP/1.1");
        }
        
        // Handle different HTTP methods
        try {
            switch (method.toUpperCase()) {
                case "GET":
                    System.out.println("✓ Processing GET request");
                    return getMethod(req, cfg);
                    
                case "POST":
                    System.out.println("✓ Processing POST request");
                    return postmethod(req, cfg);
                    
                case "PUT":
                    System.out.println("✓ Processing PUT request");
                    return handlePutMethod(req, cfg);
                    
                case "DELETE":
                    System.out.println("✓ Processing DELETE request");
                    return handleDeleteMethod(req, cfg);
                    
                case "HEAD":
                    System.out.println("✓ Processing HEAD request");
                    return handleHeadMethod(req, cfg);
                    
                case "OPTIONS":
                    System.out.println("✓ Processing OPTIONS request");
                    return handleOptionsMethod(req, cfg);
                    
                default:
                    System.out.println("✗ Unsupported method: " + method);
                    String errorMsg = "405 Method Not Allowed - Method '" + method + "' is not supported";
                    return version + " 405 Method Not Allowed\r\n" +
                        "Content-Type: text/plain\r\n" +
                        "Allow: GET, POST, PUT, DELETE, HEAD, OPTIONS\r\n" +
                        "Content-Length: " + errorMsg.length() + "\r\n\r\n" +
                        errorMsg;
            }
        } catch (Exception e) {
            System.err.println("✗ Error processing request: " + e.getMessage());
            e.printStackTrace();
            
            String errorMsg = "500 Internal Server Error - Request processing failed: " + e.getMessage();
            return version + " 500 Internal Server Error\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content-Length: " + errorMsg.length() + "\r\n\r\n" +
                errorMsg;
        }
    }
    
    private static String handlePutMethod(Request req, ServerConfig cfg) {
        String responseBody = "PUT method processed for: " + req.getPath();
        return req.getVersion() + " 200 OK\r\n" +
            "Content-Type: text/plain\r\n" +
            "Content-Length: " + responseBody.length() + "\r\n\r\n" +
            responseBody;
    }
    
    private static String handleDeleteMethod(Request req, ServerConfig cfg) {
        String responseBody = "DELETE method processed for: " + req.getPath();
        return req.getVersion() + " 200 OK\r\n" +
            "Content-Type: text/plain\r\n" +
            "Content-Length: " + responseBody.length() + "\r\n\r\n" +
            responseBody;
    }
    
    private static String handleHeadMethod(Request req, ServerConfig cfg) {
        // HEAD should return same headers as GET but without body
        String response = getMethod(req, cfg);
        int bodyStart = response.indexOf("\r\n\r\n");
        if (bodyStart != -1) {
            return response.substring(0, bodyStart + 4); // Include the \r\n\r\n but not the body
        }
        return response;
    }
    
    private static String handleOptionsMethod(Request req, ServerConfig cfg) {
        return req.getVersion() + " 200 OK\r\n" +
            "Allow: GET, POST, PUT, DELETE, HEAD, OPTIONS\r\n" +
            "Content-Type: text/plain\r\n" +
            "Content-Length: 0\r\n\r\n";
    }
}
