package kr.co.sist.e_learning;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@ComponentScan("kr.co.sist.e_learning")
public class ELearningPrjApplication {

	public static void main(String[] args) {
		SpringApplication.run(ELearningPrjApplication.class, args);
	}

}
