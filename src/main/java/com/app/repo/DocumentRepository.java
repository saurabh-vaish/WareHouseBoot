package com.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.model.Document;

public interface DocumentRepository extends JpaRepository<Document, Integer>{

	@Query("select d.fileId,d.fileName from com.app.model.Document as d")
	List<Object[]> findByFileIdAndFileName();
	
}
