package com.beyond.basic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

//ComponentScan에 의해 Application파일 하위 경로의 요소들이 싱글톤객체로 Scan
@SpringBootApplication
//스케쥴러 사용시 필요한 어노테이션
@EnableScheduling
public class BasicApplication {
	public static void main(String[] args) {
		SpringApplication.run(BasicApplication.class, args);
	}
}
