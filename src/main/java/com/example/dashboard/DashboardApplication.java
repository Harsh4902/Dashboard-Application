//usr/bin/env jbang "$0" "$@" ; exit $?
//DEPS org.springframework.boot:spring-boot-starter-web:3.1.0
//DEPS org.springframework.boot:spring-boot-starter-test:3.1.0
//DEPS org.springframework.boot:spring-boot-starter-thymeleaf:3.1.0
//DEPS org.springframework.boot:spring-boot-devtools:3.1.0

//SOURCES ./MessageItem.java
//SOURCES ./Chat.java

//FILES ../../resources/templates/index.html=index.html
//FILES resources/templates/fragments.html=fragments.html
//FILES resources/templates/main.css=main.css

package com.example.dashboard;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@Controller
@RequestMapping("/")
public class DashboardApplication {

  @Bean
  public WebClient webClient(){
    return WebClient.builder().build();
  }

  public static void main(String[] args) {
    SpringApplication.run(DashboardApplication.class, args);
  }


}
