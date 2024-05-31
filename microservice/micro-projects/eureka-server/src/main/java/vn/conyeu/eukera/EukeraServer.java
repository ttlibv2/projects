package vn.conyeu.eukera;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class EukeraServer {

	public static void main(String[] args) {
		SpringApplication.run(EukeraServer.class, args);
	}

}