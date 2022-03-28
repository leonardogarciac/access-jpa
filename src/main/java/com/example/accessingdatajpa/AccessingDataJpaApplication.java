package com.example.accessingdatajpa;

import org.aspectj.weaver.ast.Or;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class AccessingDataJpaApplication {

	private static Logger logger = LoggerFactory.getLogger(AccessingDataJpaApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AccessingDataJpaApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(CustomerRepository customerRepository, OrderRepository orderRepository) {
		return args -> {

			// Save customers
			customerRepository.save(new Customer("James", "Ford"));
			customerRepository.save(new Customer("Kate", "Austen"));
			customerRepository.save(new Customer("John", "Locke"));
			customerRepository.save(new Customer("Jack", "Shepard"));
			customerRepository.save(new Customer("Benjamin", "Linus"));
			customerRepository.save(new Customer("Jin-Soo", "Kwon"));
			customerRepository.save(new Customer("Sun-Hwa", "Kwon"));

			// Save Orders
			orderRepository.save(new Order(customerRepository.findById(1L).get(), "Oceanic Six"));
			orderRepository.save(new Order(customerRepository.findById(2L).get(), "Heart of the island"));
			orderRepository.save(new Order(customerRepository.findById(3L).get(), "Oahu, Hawaii"));



			ModelMapper modelMapper = new ModelMapper();

			// find all
			logger.info("------------------------------");
			logger.info("Customers found with findAll()");
			logger.info("------------------------------");
			customerRepository.findAll().forEach(customer -> {
				logger.info(customer.toString());
				// Get orders for this customer
				List<Order> orders = orderRepository.findByCustomer(customer);

				if (orders != null && !orders.isEmpty()) {
					logger.info("++++++++++++++++++++++++++");
					logger.info("Orders of this customer...");
					logger.info("++++++++++++++++++++++++++");
					orders.forEach(order -> {

						OrderDTO orderDTO = modelMapper.map(order, OrderDTO.class);
						logger.info(orderDTO.toString());
						logger.info("");
					});
				}
			});
			logger.info("");

			// Find by id
			Customer customerById = customerRepository.findById(1L).get();
			logger.info("------------------------------");
			logger.info("Customer found with findById()");
			logger.info("------------------------------");
			logger.info(customerById.toString());
			logger.info("");

			// Find by lastName
			List<Customer> customers = customerRepository.findByLastName("Kwon");
			logger.info("------------------------------");
			logger.info("Customers found with findByLastName()");
			logger.info("------------------------------");
			customers.forEach(customerByLastName -> {
				logger.info(customerByLastName.toString());
			});
			logger.info("");

		};
	}

}
