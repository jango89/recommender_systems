package com.system.core.crawler.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.system.core.entity.nosql.DataIndexer;
import com.system.core.repository.nosql.DataIndexerRepository;

@Service
public class DataIndexerService {

	private final DataIndexerRepository dataIndexerRepository;

	@Autowired
	public DataIndexerService(DataIndexerRepository dataIndexerRepository) {
		this.dataIndexerRepository = dataIndexerRepository;
	}

	public DataIndexer saveData(DataIndexer dataIndexer) {
		return dataIndexerRepository.save(dataIndexer);
	}

	public Iterable<DataIndexer> getAll() {
		return dataIndexerRepository.findAll();
	}

	public void resetAllData() {
		dataIndexerRepository.deleteAll();
	}
}
