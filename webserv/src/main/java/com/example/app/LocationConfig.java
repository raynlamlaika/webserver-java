package com.example.app;

import java.util.*;

public class LocationConfig {
    public String path = "";
    public String root = "";
    public List<String> _return = new ArrayList<>();
    public String methods = "";
    public String cgi_extension = "";
    public String cgi_path = "";
    public String index = "";
    public boolean autoindex = false;
    public List<LocationConfig> locations = new ArrayList<>();

    public String upload_store = "";
    public Map<String, String> cgi = new HashMap<>();
    public String redirection = "";
}
