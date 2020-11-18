package com.template.render.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.template.render.exception.InvalidInputException;
import com.template.render.exception.TemplateAlreadyExistException;
import com.template.render.model.request.TemplateCreateRequest;
import com.template.render.model.response.TemplateCreateResponse;
import com.template.render.service.TemplateService;
import com.template.render.util.Constants;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "v1/template")
@Slf4j
public class TemplateController {

	@Autowired
	private TemplateService templateService;

	@PostMapping(value = "/create", produces = "application/json", consumes = "application/json")
	public ResponseEntity<ModelMap> persistTemplate(@Valid @RequestBody TemplateCreateRequest templateCreateRequest)
			throws Exception {
		log.info("::::::Inside TemplateController Class, persistTemplate method:::::");
		try {
			TemplateCreateResponse response = templateService.createTemplate(templateCreateRequest);
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(new ModelMap().addAttribute(Constants.MESSAGE, "Successfully created")
							.addAttribute(Constants.RESPONSE, response));
		} catch (final InvalidInputException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ModelMap().addAttribute(Constants.ERROR_MESSAGE, ex.getMessage()));
		} catch (final TemplateAlreadyExistException ex) {
			return ResponseEntity.status(HttpStatus.ALREADY_REPORTED)
					.body(new ModelMap().addAttribute(Constants.ERROR_MESSAGE, ex.getMessage()));
		}
	}
}
