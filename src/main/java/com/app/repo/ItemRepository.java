package com.app.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.app.model.Item;

public interface ItemRepository extends JpaRepository<Item, Integer>{

	@Query("select i.itemId,i.itemCode from com.app.model.Item as i")
	public List<Object[]> findByItemIdAndItemCode();
	
	
}
