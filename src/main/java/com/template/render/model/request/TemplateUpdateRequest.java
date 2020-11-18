package com.template.render.model.request;

import java.util.List;
import java.util.Map;
import com.template.render.model.DefaultData;

@lombok.Data
public class TemplateUpdateRequest {
    private String name;
    private List<String> tags;
    private String template;
    private Map<String, Object> sampleData;
    private Map<String, Object> additionalProperties;
    private DefaultData defaultDate;
}
