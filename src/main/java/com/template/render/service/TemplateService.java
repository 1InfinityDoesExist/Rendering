package com.template.render.service;

import java.util.List;
import org.springframework.stereotype.Component;
import com.template.render.entity.Template;
import com.template.render.model.request.TemplateCreateRequest;
import com.template.render.model.request.TemplateUpdateRequest;
import com.template.render.model.response.TemplateCreateResponse;
import com.template.render.model.response.TemplateUpdateResponse;

@Component
public interface TemplateService {

	public TemplateCreateResponse createTemplate(TemplateCreateRequest templateCreateRequest) throws Exception;

	public Template getTemplateById(String id) throws Exception;

	public List<Template> getAllTemplate();

	public String deleteTemplate(String templateId) throws Exception;

	public TemplateUpdateResponse updateTemplate(String id, TemplateUpdateRequest templateUpdateRequest)
			throws Exception;

	public void processTemplate(String templateId);

}
