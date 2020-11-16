package com.template.render.model;

import java.util.List;
import java.util.Map;

@lombok.Data
public class DefaultData {
    private Map<String, Object> defData;
    private List<URLKeyValue> urlKeyValue;
}
