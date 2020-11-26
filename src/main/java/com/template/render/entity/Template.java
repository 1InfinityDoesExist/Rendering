package com.template.render.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.template.render.model.DefaultData;

import lombok.EqualsAndHashCode;

@lombok.Data
@Document(collection = "render")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "template", type = "render", shards = 1, replicas = 0)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Template implements Serializable {
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
	private DefaultData defaultData;
}
