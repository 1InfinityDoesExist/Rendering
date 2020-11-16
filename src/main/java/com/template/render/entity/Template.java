package com.template.render.entity;

import java.util.Date;
import java.util.List;
import java.util.Map;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.template.render.model.DefaultData;

@lombok.Data
@Document(collection = "template")
public class Template {
    @Id
    private String id;
    private String name;
    private Date createdAt;
    private Date modifiedOn;
    private boolean isActive;
    private List<String> tags;
    private String template;
    private Map<String, Object> sampleData;
    private Map<String, Object> additionalProperties;
    private DefaultData defaultDate;
}
