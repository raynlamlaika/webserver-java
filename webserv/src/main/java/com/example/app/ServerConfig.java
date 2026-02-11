package com.example.app;

import java.util.*;

public class ServerConfig 
{
    public List<String> listen = new ArrayList<>();
    public String server_name = "";
    public String root = "";
    public String index = "";
    public String error_page = "";
    public int client_max_body_size = 0;
    public List<String> try_files = new ArrayList<>();
    public boolean autoindex = false;
    public List<LocationConfig> locations = new ArrayList<>();

    public String getDocumentRoot() {
        return root.isEmpty() ? "./public" : root;
    }
}