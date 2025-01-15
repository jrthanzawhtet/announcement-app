package com.jdc.spring.model.repo;

import com.jdc.spring.model.BaseRepository;
import com.jdc.spring.model.entity.Account;

public interface AccountRepo extends BaseRepository<Account, String>{

	long countByLoginId(String loginId);

}
