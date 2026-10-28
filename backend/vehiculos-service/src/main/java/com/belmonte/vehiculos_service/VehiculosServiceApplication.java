package com.belmonte.vehiculos_service;

import com.belmonte.vehiculos_service.model.Vehiculo;
import com.belmonte.vehiculos_service.repository.VehiculoRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class VehiculosServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehiculosServiceApplication.class, args);
	}

	@Bean
	CommandLineRunner initDatabase(VehiculoRepository repository) {
		return args -> {
			if (repository.count() == 0) {
				repository.save(new Vehiculo("Toyota", "Corolla", "ABCD-12", 4, true));
				repository.save(new Vehiculo("Hyundai", "Staria", "EFGH-34", 8, true));
				repository.save(new Vehiculo("Mercedes-Benz", "Sprinter", "IJKL-56", 16, true));
			}
		};
	}
}