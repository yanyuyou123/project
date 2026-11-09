package com.study.service;

import java.util.List;
import java.util.Map;

public interface LogService {
    public void record(String method, String path, String user, long cost, int status);
    public List<Map<String, Object>> list();
}
