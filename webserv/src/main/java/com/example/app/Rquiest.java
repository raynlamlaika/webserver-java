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
    public static Request parseRequest(String request)
    {
        Request req = new Request();

        // Split headers and body
        String[] parts = request.split("\r\n\r\n", 2);
        String headerPart = parts[0];
        req.body = (parts.length == 2) ? parts[1] : "";
        System.out.print("BODYY______________________________"+req.body + "\n");

        
        // method      = "GET"
        // path        = "/"
        // version     = "HTTP/1.1"

        // headers = {
        // "accept-encoding": "gzip, deflate, br",
        // "accept": "*/*",
        // "user-agent": "Thunder Client (https://www.thunderclient.com)",
        // "content-type": "application/json",
        // "host": "127.0.0.1:8181",
        // "connection": "close"
        // }

        // host = "127.0.0.1"
        // port = 8181
        // body = ""
        
    

        // Split lines
        String[] lines = headerPart.split("\r\n");

        // ---- Parse request line ----
        String[] startLine = lines[0].split(" ");
        req.method = startLine[0];
        req.path = startLine[1];
        req.version = startLine[2];
        System.out.print(" methos______________" + req.method + "\n");
        System.out.print(" mapth______________" + req.path +"\n");
        System.out.print(" version________________" + req.version + "\n");

        // ---- Parse headers ----
        // ---- Parse headers ----
        for (int i = 1; i < lines.length; i++)
        {
            System.out.println("LINE[" + i + "]: [" + lines[i] + "]");

            // Empty line = end of headers
            if (lines[i].isEmpty())
                break;

            int colonIndex = lines[i].indexOf(':');
            if (colonIndex == -1)
                continue;

            String key = lines[i].substring(0, colonIndex).trim().toLowerCase();
            String value = lines[i].substring(colonIndex + 1).trim();

            System.out.println("KEY = " + key + " | VALUE = " + value);

            req.headers.put(key, value);
        }
        // for (int i = 1; i < lines.length; i++)
        // {
        //     System.out.print("_____________________________"+ lines+"\n");
        //     int colonIndex = lines[i].indexOf(":");
        //     if (colonIndex == -1)
        //         continue;

        //     String key = lines[i].substring(0, colonIndex).trim();
        //     String value = lines[i].substring(colonIndex + 1).trim();
        //     System.out.print("KEY " +key+ " _________________________VALU _____"+ value + "\n");
        //     req.headers.put(key, value);

        // }

        return req;
    }

    // ===== Getters =====
    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getVersion() { return version; }
    public Map<String, String> getHeaders() { return headers; }
    public String getBody() { return body; }
}