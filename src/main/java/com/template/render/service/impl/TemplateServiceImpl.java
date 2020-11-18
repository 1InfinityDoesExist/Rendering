package com.template.render.service.impl;

import java.util.Date;
import java.util.List;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.template.render.entity.Template;
import com.template.render.exception.InvalidInputException;
import com.template.render.exception.TemplateAlreadyExistException;
import com.template.render.exception.TemplateNotFoundException;
import com.template.render.model.request.TemplateCreateRequest;
import com.template.render.model.request.TemplateUpdateRequest;
import com.template.render.model.response.TemplateCreateResponse;
import com.template.render.model.response.TemplateCreateResponseBuilder;
import com.template.render.model.response.TemplateUpdateResponse;
import com.template.render.model.response.TemplateUpdateResponseBuilder;
import com.template.render.repository.TemplateRepository;
import com.template.render.service.TemplateService;

@Service
public class TemplateServiceImpl implements TemplateService {

	@Autowired
	private TemplateRepository templateRepository;

	@Override
	public TemplateCreateResponse createTemplate(TemplateCreateRequest templateCreateRequest) throws Exception {
		Template template;
		if (!ObjectUtils.isEmpty(templateCreateRequest)) {
			if (!ObjectUtils.isEmpty(templateCreateRequest.getName())) {
				template = templateRepository.findTemplateByName(templateCreateRequest.getName());
				if (!ObjectUtils.isEmpty(template)) {
					throw new TemplateAlreadyExistException(
							String.format("Template with name : %s already exist.", templateCreateRequest.getName()));
				}
			} else {
				throw new InvalidInputException("Name field must not be null or empty.");
			}
		}
		template = new Template();
		template.setActive(true);
		template.setAdditionalProperties(templateCreateRequest.getAdditionalProperties());
		template.setCreatedAt(new Date());
		template.setDefaultDate(templateCreateRequest.getDefaultDate());
		template.setModifiedOn(new Date());
		template.setName(templateCreateRequest.getName());
		template.setSampleData(templateCreateRequest.getSampleData());
		template.setTags(templateCreateRequest.getTags());
		template.setTemplate(templateCreateRequest.getTemplate());
		templateRepository.save(template);

		// Builder Design Pattern
		TemplateCreateResponse response = new TemplateCreateResponseBuilder().setId(template.getId())
				.setName(template.getName()).getTemplateCreateResponse();
		return response;
	}

	@Override
	public Template getTemplateById(String id) throws Exception {
		if (!ObjectUtils.isEmpty(id)) {
			Template template = templateRepository.findTemplateById(id);
			if (ObjectUtils.isEmpty(template)) {
				throw new TemplateNotFoundException(String.format("Template with id : %s already exist.", id));
			} else {
				return template;
			}
		} else {
			throw new InvalidInputException("Name field must not be null or empty.");
		}
	}

	@Override
	public List<Template> getAllTemplate() {
		return templateRepository.findAll();
	}

	@Override
	public String deleteTemplate(String id) throws Exception {
		if (!ObjectUtils.isEmpty(id)) {
			Template template = templateRepository.findTemplateById(id);
			if (ObjectUtils.isEmpty(template)) {
				throw new TemplateNotFoundException(String.format("Template with id : %s already exist.", id));
			} else {
				templateRepository.delete(template);
				return "Successfully deleted";
			}
		} else {
			throw new InvalidInputException("Name field must not be null or empty.");
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public TemplateUpdateResponse updateTemplate(String id, TemplateUpdateRequest templateUpdateRequest)
			throws Exception {
		if (!ObjectUtils.isEmpty(id)) {
			Template template = templateRepository.findTemplateById(id);
			if (ObjectUtils.isEmpty(template)) {
				throw new TemplateNotFoundException(String.format("Template with id : %s already exist.", id));
			} else {
				JSONObject templateFromDB = (JSONObject) new JSONParser()
						.parse(new ObjectMapper().writeValueAsString(template));
				JSONObject templateFromPayload = (JSONObject) new JSONParser()
						.parse(new ObjectMapper().writeValueAsString(templateUpdateRequest));
				for (Object object : templateFromPayload.keySet()) {
					String param = (String) object;
					templateFromDB.put(param, templateFromPayload.get(param));
				}
				template = new ObjectMapper().readValue(templateFromDB.toJSONString(), Template.class);
				templateRepository.save(template);
				TemplateUpdateResponse response = new TemplateUpdateResponseBuilder().setId(template.getId())
						.setModifiedOn(template.getModifiedOn()).getTemplateupdateResponse();
				return response;
			}
		} else {
			throw new InvalidInputException("Name field must not be null or empty.");
		}
	}

	@Override
	public void processTemplate(String templateId) {
		// TODO Auto-generated method stub

	}

}
