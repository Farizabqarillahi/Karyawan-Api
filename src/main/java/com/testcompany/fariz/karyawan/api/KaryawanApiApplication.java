package com.testcompany.fariz.karyawan.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = "com.testcompany.fariz.karyawan")
public class KaryawanApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(KaryawanApiApplication.class, args);
	}

}
