package com.jdc.spring.model.repo;

import java.util.Optional;

import com.jdc.spring.model.BaseRepository;
import com.jdc.spring.model.entity.Tag;

public interface TagRepo extends BaseRepository<Tag, Long>{

	Optional<Tag> findByName(String tags);

}
