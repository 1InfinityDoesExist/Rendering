package com.template.render.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.template.render.entity.Template;
import com.template.render.exception.InvalidInputException;
import com.template.render.exception.TemplateAlreadyExistException;
import com.template.render.exception.TemplateNotFoundException;
import com.template.render.model.request.TemplateCreateRequest;
import com.template.render.model.request.TemplateUpdateRequest;
import com.template.render.model.response.TemplateCreateResponse;
import com.template.render.model.response.TemplateUpdateResponse;
import com.template.render.service.TemplateService;
import com.template.render.util.Constants;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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
			return ResponseEntity.status(HttpStatus.CONFLICT)
					.body(new ModelMap().addAttribute(Constants.ERROR_MESSAGE, ex.getMessage()));
		}
	}

	@ApiImplicitParams({ @ApiImplicitParam(name = "id", paramType = "header") })
	@GetMapping(value = "/get/{id}", produces = "application/json")
	public ResponseEntity<ModelMap> getTemplateById(@PathVariable(value = "id", required = true) String id)
			throws Exception {
		try {
			Template template = templateService.getTemplateById(id);
			return ResponseEntity.status(HttpStatus.OK).body(new ModelMap().addAttribute(Constants.RESPONSE, template));
		} catch (final InvalidInputException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ModelMap().addAttribute(Constants.ERROR_MESSAGE, ex.getMessage()));
		} catch (final TemplateNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ModelMap().addAttribute(Constants.ERROR_MESSAGE, ex.getMessage()));
		}
	}

	@GetMapping(value = "/getAll")
	public ResponseEntity<ModelMap> getAllTemplates() {
		List<Template> listOfTemplate = templateService.getAllTemplate();
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ModelMap().addAttribute(Constants.RESPONSE, listOfTemplate));
	}

	@ApiImplicitParams({ @ApiImplicitParam(name = "id", paramType = "header") })
	@DeleteMapping(value = "/delete/{id}")
	public ResponseEntity<ModelMap> deleteTemplateById(@PathVariable(value = "id", required = true) String id)
			throws Exception {
		try {
			String response = templateService.deleteTemplate(id);
			return ResponseEntity.status(HttpStatus.OK).body(new ModelMap().addAttribute(Constants.RESPONSE, response));
		} catch (final InvalidInputException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ModelMap().addAttribute(Constants.ERROR_MESSAGE, ex.getMessage()));
		} catch (final TemplateNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ModelMap().addAttribute(Constants.ERROR_MESSAGE, ex.getMessage()));
		}
	}

	@ApiImplicitParams({ @ApiImplicitParam(name = "id", paramType = "header") })
	@PutMapping(value = "/update/{id}")
	public ResponseEntity<ModelMap> updateTemplateById(@RequestBody TemplateUpdateRequest templateUpdateRequest,
			@PathVariable(value = "id", required = true) String id) throws Exception {
		try {
			TemplateUpdateResponse templateUpdateResponse = templateService.updateTemplate(id, templateUpdateRequest);
			return ResponseEntity.status(HttpStatus.OK)
					.body(new ModelMap().addAttribute(Constants.MESSAGE, "Successfully updated")
							.addAttribute(Constants.RESPONSE, templateUpdateResponse));
		} catch (final InvalidInputException ex) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body(new ModelMap().addAttribute(Constants.ERROR_MESSAGE, ex.getMessage()));
		} catch (final TemplateNotFoundException ex) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND)
					.body(new ModelMap().addAttribute(Constants.ERROR_MESSAGE, ex.getMessage()));
		}
	}
}
