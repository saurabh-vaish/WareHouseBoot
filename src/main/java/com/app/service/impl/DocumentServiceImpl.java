package com.app.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.excetion.DocumentNotFoundException;
import com.app.model.Document;
import com.app.repo.DocumentRepository;
import com.app.service.IDocumentService;

@Service
public class DocumentServiceImpl implements IDocumentService {

	
	@Autowired
	private DocumentRepository repo;
	
	@Transactional
	public Integer saveDocument(Document doc) {
		return repo.save(doc).getFileId();
	}
	
	@Transactional(readOnly=true)
	public List<Object[]> getFileIdAndName() {
		return repo.findByFileIdAndFileName();
	}
	
	@Transactional(readOnly=true)
	public Document getDocumentById(Integer id) {
		Optional<Document> doc = repo.findById(id);
		if(doc.isPresent())
		{
			return doc.get();
		}
		else throw new DocumentNotFoundException("No document found of given id");
	}
	
}
