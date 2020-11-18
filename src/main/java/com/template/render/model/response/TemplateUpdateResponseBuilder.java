package com.template.render.model.response;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.template.render.model.DefaultData;

public class TemplateUpdateResponseBuilder {
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

	public TemplateUpdateResponseBuilder setId(String id) {
		this.id = id;
		return this;
	}

	public TemplateUpdateResponseBuilder setName(String name) {
		this.name = name;
		return this;
	}

	public TemplateUpdateResponseBuilder setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
		return this;
	}

	public TemplateUpdateResponseBuilder setModifiedOn(Date modifiedOn) {
		this.modifiedOn = modifiedOn;
		return this;
	}

	public TemplateUpdateResponseBuilder setActive(boolean isActive) {
		this.isActive = isActive;
		return this;
	}

	public TemplateUpdateResponseBuilder setTags(List<String> tags) {
		this.tags = tags;
		return this;
	}

	public TemplateUpdateResponseBuilder setTemplate(String template) {
		this.template = template;
		return this;
	}

	public TemplateUpdateResponseBuilder setSampleData(Map<String, Object> sampleData) {
		this.sampleData = sampleData;
		return this;
	}

	public TemplateUpdateResponseBuilder setAdditionalProperties(Map<String, Object> additionalProperties) {
		this.additionalProperties = additionalProperties;
		return this;
	}

	public TemplateUpdateResponseBuilder setDefaultDate(DefaultData defaultDate) {
		this.defaultDate = defaultDate;
		return this;
	}

	public TemplateUpdateResponse getTemplateupdateResponse() {
		return new TemplateUpdateResponse(id, name, createdAt, modifiedOn, isActive, tags, template, sampleData,
				additionalProperties, defaultDate);
	}

}
