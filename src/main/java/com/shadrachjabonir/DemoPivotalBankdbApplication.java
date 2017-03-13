package com.shadrachjabonir;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestMvcConfiguration;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.*;
import java.io.Serializable;

@SpringBootApplication
@EnableDiscoveryClient
@EnableAutoConfiguration
public class DemoPivotalBankdbApplication {

	@Autowired
	AccountDao accountDao;

	@PostConstruct
	void init(){
		Account a = new Account("Shadrach","0001",100D);
		accountDao.save(a);
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoPivotalBankdbApplication.class, args);
	}
}


@RepositoryRestResource(collectionResourceRel = "account", path = "account")
@Transactional(readOnly = true)
interface AccountDao extends CrudRepository<Account,Long>{
	Account findByNumber(@Param("number")String number);
}

@Entity
class Account implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private String name;
	@Column(unique=true)
	private String number;
	private Double amount;

	public Account() {
	}

	public Account(String name, String number, Double amount) {
		this.name = name;
		this.number = number;
		this.amount = amount;
	}

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}
}

@Configuration
class RepositoryConfig extends
		RepositoryRestMvcConfiguration {

	@Override
	protected void configureRepositoryRestConfiguration(
			RepositoryRestConfiguration config) {
		config.exposeIdsFor(Account.class);
	}
}