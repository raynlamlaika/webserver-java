package com.example.app;

import java.io.*;
import java.net.*;

class ServerInstance {
    int port;
    String serverName;
    String responseBody;
    private ServerSocket serverSocket;
    private volatile boolean running = false;

    ServerInstance(int port) {
        this.port = port;
    }

    @Override
    public String toString()
    {
        return "ServerInstance{" +
                "port=" + port +
                ", serverName='" + serverName + '\'' +
                ", responseBody='" + responseBody + '\'' +
                '}';
    }

    void Runserver()
    {
        System.out.println("[Server starting] Port: " + port + ", Name: " + serverName);
        
        try
        {
            serverSocket = new ServerSocket(port);
            running = true;
            System.out.println("[Server READY] Listening on port " + port);
            
            while (running)
            {
                try
                {
                    Socket clientSocket = serverSocket.accept();
                    handleClient(clientSocket);
                }
                catch (IOException e)
                {
                    if (running)
                    {
                        System.err.println("[Server ERROR] Error accepting connection: " + e.getMessage());
                    }
                }
            }
        }
        catch (IOException e)
        {
            System.err.println("[Server ERROR] Could not start server on port " + port + ": " + e.getMessage());
        }
        finally
        {
            stop();
        }
    }

    private void handleClient(Socket clientSocket)
    {

        // init resources inside the try ( ... ) to help  maange and free resoures avter the block
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        )
        {
            // Read HTTP request
            String HttpRequist = "";
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty())
            {
                System.out.println("[Request] " + line);
                HttpRequist += line;
            }
            
            String p = "------------------------------------------------";
            System.out.println("this is the REQUIST: + \n\n" + p + HttpRequist  + p +  "\n\n\n ");
            // Send HTTP response
            String body = responseBody != null ? responseBody.replace("\"", "") : "Hello from server!";
            
            String response = "HTTP/1.1 200 OK\r\n" +
                             "Content-Type: text/plain\r\n" +
                             "Content-Length: " + body.length() + "\r\n" +
                             "Connection: close\r\n" +
                             "\r\n" +
                             body;
            
            out.print(response);
            out.flush();
            
            System.out.println("[Response sent] " + body.substring(0, Math.min(50, body.length())));
        }
        catch (IOException e)
        {
            System.err.println("[Client ERROR] " + e.getMessage());
        }
        finally
        {
            try
            {
                clientSocket.close();
            }
            catch (IOException e)
            {
                // Ignore
            }
        }
    }

    public void stop()
    {
        running = false;
        if (serverSocket != null && !serverSocket.isClosed())
        {
            try
            {
                serverSocket.close();
                System.out.println("[Server STOPPED] Port " + port);
            }
            catch (IOException e)
            {
                System.err.println("[Server ERROR] Error closing server: " + e.getMessage());
            }
        }
    }
}
