package com.system.core.repository.nosql;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.system.core.entity.nosql.UserInfo;

@Repository
public interface UserInfoRepository extends ElasticsearchRepository<UserInfo, String> {

}
