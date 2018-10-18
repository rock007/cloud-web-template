package org.cloud.webtemplate;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.rest.RepositoryRestMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages ={ "org.cloud.webtemplate"
        ,"org.cloud.db.sys.service","org.cloud.core"})
@MapperScan("org.cloud.webtemplate.db.mapper")
@EnableAutoConfiguration(exclude={RepositoryRestMvcAutoConfiguration.class})
public class WebStarterApplication {

    public static void main(String[] args) {
    	
        SpringApplication.run(WebStarterApplication.class, args);
    }

}
