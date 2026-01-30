package com.example.app;
import java.util.ArrayList;
import java.util.List;


public class ServerParams
{
    public List<String> listen = new ArrayList<>();
    public String server_name = "";
    public String root = "";
    public String index = "";
    public List<String> try_files = new ArrayList<>();
    public List<LocationConfig> locations = new ArrayList<>();
}

