package com.app.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.app.model.Item;
import com.app.repo.ItemRepository;
import com.app.service.IItemService;

@Service
public class ItemServiceImpl implements IItemService {

	@Autowired
	private ItemRepository repo;
	
	@Transactional
	public Integer saveItem(Item item) {
		return repo.save(item).getItemId();
	}

	@Transactional
	public void updateItem(Item item) {
		repo.save(item).getItemId();

	}

	@Transactional
	public void deleteItem(Integer id) {
		repo.deleteById(id);

	}

	@Transactional(readOnly=true)
	public Item getItemById(Integer id) {
		Optional<Item> list = repo.findById(id);
		
		return list.isPresent()?list.get():null;
	}

	@Transactional(readOnly=true)
	public List<Item> getAllItems() {
		return repo.findAll();
	}

	
	@Override
	public Map<Integer, String> getItemIdNameCode() {
		List<Object[]> list = repo.findByItemIdAndItemCode();
		
		return list.stream()
						.collect(
								Collectors.toMap(ob->(Integer)ob[0],ob->(String)ob[1])
						);
	}
	
}
