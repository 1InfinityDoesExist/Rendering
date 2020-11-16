package com.template.render.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.template.render.entity.Template;

@Repository
public interface TemplateRepository extends MongoRepository<Template, String> {

}
