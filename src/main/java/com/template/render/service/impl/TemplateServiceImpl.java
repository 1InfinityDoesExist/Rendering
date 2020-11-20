package com.template.render.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.template.render.entity.Template;
import com.template.render.exception.InvalidInputException;
import com.template.render.exception.TemplateAlreadyExistException;
import com.template.render.exception.TemplateNotFoundException;
import com.template.render.model.DefaultData;
import com.template.render.model.URLKeyValue;
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

	@Autowired
	private RestTemplate restTemplate;

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
		log.info(":::::Template Service Class, getAllKeys method:::::");
		List<String> keys = getKeys("", data, new ArrayList<String>());
		log.info(":::::keys {}", keys);
		return keys;
	}

	private List<String> getKeys(String parentKey, Map<String, Object> data, ArrayList<String> keys) {
		data.forEach((key, value) -> {
			if (value instanceof Map) {
				@SuppressWarnings("unchecked")
				Map<String, Object> map = (Map<String, Object>) value;
				getKeys(parentKey + key + ".", map, keys);
			} else if (value instanceof List) { // remove else if you want all the keys
				List list = (List) value;
				if (list.size() > 0) {
					fillListData(parentKey + key, list, keys);
				}
			} else if (parentKey.isEmpty()) { // remove the else if you want all the keys
				// keys.add(key); // uncomment this if you want all the keys
			} else {
				keys.add(parentKey + key);
			}
		});
		return keys;
	}

	@SuppressWarnings("unchecked")
	private void fillListData(String parentKey, List list, ArrayList<String> keys) {
		for (int iter = 0; iter < list.size(); iter++) {
			if (list.get(iter) instanceof List) {
				List l = (List) (list.get(iter));
				fillListData(parentKey + "[" + iter + "]" + ".", l, keys);
			}
			if (list.get(iter) instanceof Map) {
				Map<String, Object> map = (Map<String, Object>) list.get(iter);
				getKeys(parentKey + "[" + iter + "]" + ".", map, keys);
			}
		}
		return;
	}

	/*
	 * Prepare final data for processing
	 */
	@Override
	public JsonNode getFinalDataForProcessing(Template template, DefaultData input) {

		Map<String, Object> dataForProcessing = null;
		if (!ObjectUtils.isEmpty(input) && !ObjectUtils.isEmpty(input.getDefData())) {
			dataForProcessing = input.getDefData();
		} else if (!ObjectUtils.isEmpty(template.getDefaultData())
				&& !ObjectUtils.isEmpty(template.getDefaultData().getDefData())) {
			dataForProcessing = template.getDefaultData().getDefData();
		}
		if (dataForProcessing == null) {
			dataForProcessing = new HashMap<String, Object>();
		}
		ObjectNode finalNode = new ObjectMapper().valueToTree(dataForProcessing);

		/*
		 * Fill finalNode data with value obtained from url.
		 */

		if (!ObjectUtils.isEmpty(input) && !ObjectUtils.isEmpty(input.getUrlKeyValue())) {
			fillFinalData(finalNode, input.getUrlKeyValue());
		} else if (!ObjectUtils.isEmpty(template.getDefaultData())
				&& !ObjectUtils.isEmpty(template.getDefaultData().getUrlKeyValue())) {
			fillFinalData(finalNode, template.getDefaultData().getUrlKeyValue());
		}
		log.info(":::::FinalNode from main service method {}", finalNode);
		return finalNode;
	}

	/*
	 * 
	 * Fetch data from the given url and send inserting data into the respective key
	 */
	private void fillFinalData(ObjectNode finalNode, List<URLKeyValue> urlKeyValue) {
		for (URLKeyValue urlKV : urlKeyValue) {
			String key = urlKV.getKey();
			String url = urlKV.getValue();
			MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
			HttpEntity entity = new HttpEntity<>(headers);
			ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
			String urlValue = response.getBody();
			log.info(":::::urlValue {}", urlValue);
			try {
				setAndGetFinalNode(key, finalNode, new ObjectMapper().readTree(urlValue));
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
		}
	}

	/*
	 * Set the value obtained form the link provided
	 */
	@SuppressWarnings("deprecation")
	private void setAndGetFinalNode(String key, ObjectNode finalNode, JsonNode readTree) {
		if (!StringUtils.isEmpty(key)) {
			String[] nodes = key.split("\\.");
			ObjectNode currentNode = finalNode;
			/*
			 * Creates a new one
			 */
			if (nodes.length > 0 && currentNode.get(nodes[0]) == null) { // to be updated p.x.y.z payload has only x.y.k
				for (int iter = 0; iter < nodes.length - 1; iter++) {
					String node = nodes[iter];
					if (!currentNode.isNull() && !currentNode.isEmpty() && currentNode.get(node) != null) {
						currentNode = (ObjectNode) currentNode.get(node);
					} else {
						currentNode.set(node, new ObjectMapper().createObjectNode());
						currentNode = (ObjectNode) currentNode.get(node);
					}
				}
				currentNode.set(nodes[nodes.length - 1], readTree);
				/*
				 * Updates in the existing value
				 */
			} else { // to be updated x.y.z.k payload x.y
				for (int iter = 0; iter < nodes.length - 1; iter++) {
					String node = nodes[iter];
					if (!currentNode.isNull() && !currentNode.isEmpty() && !currentNode.get(node).isNull()) {
						currentNode = (ObjectNode) currentNode.get(node);
					} else {
						currentNode.set(node, new ObjectMapper().createObjectNode());
						currentNode = (ObjectNode) currentNode.get(node);
					}
				}
				currentNode.set(nodes[nodes.length - 1], readTree);
			}

		}
		log.info(":::::finalNode {}", finalNode);
	}
}
