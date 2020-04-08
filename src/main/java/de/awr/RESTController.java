package de.awr;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.beanutils.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class RESTController<T, ID extends Serializable> {

	private Logger logger = LoggerFactory.getLogger(RESTController.class);

	private CrudRepository<T, ID> repo;

	public RESTController(CrudRepository<T, ID> repo) {
		this.repo = repo;
	}

	@GetMapping
	public @ResponseBody List<T> listAll() {
		Iterable<T> all = this.repo.findAll();
		return Lists.newArrayList(all);
	}

	@PostMapping(consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody Map<String, Object> create(@RequestBody @Valid T json) {
		logger.debug("create() with body {} of type {}", json, json.getClass());

		T created = this.repo.save(json);

		Map<String, Object> m = Maps.newHashMap();
		m.put("success", true);
		m.put("created", created);
		return m;
	}

	@GetMapping("/{id}")
	public @ResponseBody T get(@PathVariable ID id) {
		return this.repo.findById(id).orElse(null);
	}

	@PutMapping(value = "/{id}", consumes = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody Map<String, Object> update(@PathVariable ID id, @RequestBody @Valid T json) {
		logger.debug("update() of id#{} with body {}", id, json);
		logger.debug("T json is of type {}", json.getClass());

		T entity = this.repo.findById(id).orElse(null);
		try {
			BeanUtils.copyProperties(entity, json);
		} catch (Exception e) {
			logger.warn("while copying properties", e);
			throw Throwables.propagate(e);
		}

		logger.debug("merged entity: {}", entity);

		T updated = this.repo.save(entity);
		logger.debug("updated enitity: {}", updated);

		Map<String, Object> m = Maps.newHashMap();
		m.put("success", true);
		m.put("id", id);
		m.put("updated", updated);
		return m;
	}

	@DeleteMapping("/{id}")
	public @ResponseBody Map<String, Object> delete(@PathVariable ID id) {
		this.repo.deleteById(id);
		Map<String, Object> m = Maps.newHashMap();
		m.put("success", true);
		return m;
	}
}