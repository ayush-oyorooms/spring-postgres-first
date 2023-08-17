package com.example.springpostgres;

import com.example.springpostgres.respositories.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import redis.clients.jedis.JedisPooled;

@SpringBootApplication
@EnableCaching
public class SpringPostgresApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringPostgresApplication.class, args);
	}
}
