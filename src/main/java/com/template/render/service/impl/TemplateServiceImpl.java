package com.template.render.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
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
		template.setDefaultData(templateCreateRequest.getDefaultData());
		template.setModifiedOn(new Date());
		template.setName(templateCreateRequest.getName());
		template.setSampleData(templateCreateRequest.getSampleData());
		template.setTags(templateCreateRequest.getTags());
		template.setTemplate(templateCreateRequest.getTemplate());
		templateRepository.save(template);
		log.info("Using builder design pattern to generate response");
		// Builder Design Pattern
		TemplateCreateResponse response = new TemplateCreateResponseBuilder().setId(template.getId())
				.setName(template.getName()).setActive(template.isActive()).getTemplateCreateResponse();
		return response;
	}

	@Override
	public Template getTemplateById(String id) throws Exception {
		if (!ObjectUtils.isEmpty(id)) {
			Template template = templateRepository.findTemplateById(id);
			if (ObjectUtils.isEmpty(template)) {
				throw new TemplateNotFoundException(String.format("Template with id : %s does not exist.", id));
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
				throw new TemplateNotFoundException(String.format("Template with id : %s does not exist.", id));
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
		log.info(":::::TemplateServiceImpl Class, updateTemplate method:::::");
		if (!ObjectUtils.isEmpty(id)) {
			Template template = templateRepository.findTemplateById(id);
			if (ObjectUtils.isEmpty(template)) {
				throw new TemplateNotFoundException(String.format("Template with id : %s does not exist.", id));
			} else {
				JSONObject templateFromDB = (JSONObject) new JSONParser()
						.parse(new ObjectMapper().writeValueAsString(template));
				JSONObject templateFromPayload = (JSONObject) new JSONParser()
						.parse(new ObjectMapper().writeValueAsString(templateUpdateRequest));
				log.info(":::::db {}", templateFromDB);
				log.info(":::payload {}", templateFromPayload);
				for (Object object : templateFromPayload.keySet()) {
					String param = (String) object;
					log.info("::::::param {}", param);
					templateFromDB.put(param, templateFromPayload.get(param));
				}
				Template newTemplate = new ObjectMapper().readValue(templateFromDB.toJSONString(), Template.class);
				newTemplate.setModifiedOn(new Date());
				templateRepository.save(newTemplate);
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

	@Override
	public List<String> getAllKeys(Map<String, Object> data) {
		List<String> keys = getKeys("", data, new ArrayList<String>());
		return keys;
	}

	private List<String> getKeys(String parentKey, Map<String, Object> data, ArrayList<String> keys) {
		log.info(":::::data : {} , parentKey : {}, keys : {}", data, parentKey, keys);
		data.forEach((key, value) -> {
			if (value instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) value;
				getKeys(parentKey + key + ".", map, keys);
			}
			if (value instanceof List) {
				List list = (List) value;
				if (list.size() > 0) {
					fillListData(parentKey, list, keys);
				}
			}
			if (!parentKey.isEmpty()) {
				keys.add(parentKey);
				log.info("::parentKey is not empty:::keys {}", keys);
			} else {
				keys.add(parentKey + key);
				log.info("::::parentKey is empty ::::keys {}", keys);
			}
		});
		return keys;
	}

	private void fillListData(String parentKey, List list, ArrayList<String> keys) {

	}

}
