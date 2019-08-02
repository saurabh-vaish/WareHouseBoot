package com.app.util;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public interface AppCollectionUtil {

	public static Map<Integer,String> toMap(List<Object[]> list) {

		
		Map<Integer,String> map = list.stream().collect(
				Collectors.toMap(
				ob->(Integer)ob[0],   
				ob->(String)ob[1]    
				)
		);
		
		System.out.println(map);
		
		return	map;
			}

	}
