package com.template.render.repository.mongo;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import com.mongodb.BasicDBObject;
import com.template.render.entity.Template;

@Repository
public interface TemplateRepository extends MongoRepository<Template, String> {

	@Query(value = "{'isActive':true, 'id':?0}")
	public Template findTemplateByName(String name);

	@Query(value = "{'isActive':true, 'id':?0}")
	public Template findTemplateById(String id);

	@Query(value = "{'isActive':true, '$or':?0}")
	public List<Template> findTemplateByTags(List<BasicDBObject> templateQury);

}
