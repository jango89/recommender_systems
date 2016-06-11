package com.system.core.repository.nosql;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import com.system.core.entity.nosql.DataIndexer;

@Repository
public interface DataIndexerRepository extends ElasticsearchRepository<DataIndexer, String> {

}
