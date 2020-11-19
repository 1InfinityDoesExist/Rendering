package com.template.render.model.response;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.template.render.model.DefaultData;

import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TemplateCreateResponse {

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

	public TemplateCreateResponse(String id, String name, Date createdAt, Date modifiedOn, boolean isActive,
			List<String> tags, String template, Map<String, Object> sampleData,
			Map<String, Object> additionalProperties, DefaultData defaultData) {
		super();
		this.id = id;
		this.name = name;
		this.createdAt = createdAt;
		this.modifiedOn = modifiedOn;
		this.isActive = isActive;
		this.tags = tags;
		this.template = template;
		this.sampleData = sampleData;
		this.additionalProperties = additionalProperties;
		this.defaultData = defaultData;
	}

}
