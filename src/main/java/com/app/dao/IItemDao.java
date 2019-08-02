package com.app.dao;

import java.util.List;
import java.util.Map;

import com.app.model.Item;

public interface IItemDao {

	public Integer saveItem(Item item);
	public void updateItem(Item item);
	public void deleteItem(Integer id);
	public Item getItemById(Integer id);
	public List<Item> getAllItems();
	
	
	public Map<Integer, String> getItemIdNameCode();

}
