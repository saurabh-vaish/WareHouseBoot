package com.app.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.hazelcast.config.Config;
import com.hazelcast.config.EvictionPolicy;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MaxSizeConfig;
import com.hazelcast.config.MaxSizeConfig.MaxSizePolicy;

@Configuration
public class wareHouseConfig {

	// creating cache for application
	
		public Config chacheConfig()
		{
			return new Config()			// for creating cache
					.setInstanceName("hazelcast-cahe")   // setting cache name 
					.addMapConfig(						// Setting MapConfig  module
							new MapConfig()
							.setName("warehouse-cache")			// MapConfig Name
							.setTimeToLiveSeconds(2000)     //  max time for object to present in memory if no activity dne
							.setMaxSizeConfig(new MaxSizeConfig(200,MaxSizePolicy.FREE_HEAP_SIZE))   // size of objects in MapConfig
							.setEvictionPolicy(EvictionPolicy.LRU) 
						)
					;
		}
		
		
		
	// password encoder
	@Bean
	public BCryptPasswordEncoder encoder()
	{
		return new BCryptPasswordEncoder();
	}
	
}
