package com.example.app;



class Response
{
    static void getMethod(Request req, ServerConfig cfg)
    {
        String url = req.getPath();
        System.out.println("GET URL: " + url);
        if (url.equals("/"))
        {
            System.out.println("Serving index.html");
            // Serve index.html from the configured document root
            String documentRoot = cfg.getDocumentRoot();
        }
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
            getMethod(req, cfg);
        }
        else if (method.equals("POST"))
        {
            return version + " 200 OK\r\nContent-Type: text/plain\r\n\r\nReceived POST data!";
        }
        else
        {
            return version + " 405 Method Not Allowed\r\nContent-Type: text/plain\r\n\r\nMethod Not Allowed";
        }
    }

}
