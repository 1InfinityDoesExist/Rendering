package com.template.render.model.response;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.template.render.model.DefaultData;

public class TemplateCreateResponseBuilder {
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

	public TemplateCreateResponseBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public TemplateCreateResponseBuilder setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public TemplateCreateResponseBuilder setTemplate(String template) {
		this.template = template;
		return this;
	}

	public TemplateCreateResponseBuilder setSampleData(Map<String, Object> sampleData) {
		this.sampleData = sampleData;
		return this;
	}

	public TemplateCreateResponseBuilder setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
		return this;
	}

	public TemplateCreateResponseBuilder setDefaultDate(DefaultData defaultDate) {
		this.defaultDate = defaultDate;
		return this;
	}

	public TemplateCreateResponseBuilder setId(String id) {
		this.id = id;
		return this;
	}

	public TemplateCreateResponseBuilder setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public TemplateCreateResponseBuilder setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
		return this;
	}

	public TemplateCreateResponseBuilder setActive(boolean isActive) {
		this.isActive = isActive;
		return this;
	}

	public TemplateCreateResponse getTemplateCreateResponse() {
		return new TemplateCreateResponse(id, name, createdAt, modifiedOn, isActive, tags, template, sampleData,
				additionalProperties, defaultDate);
	}

}
