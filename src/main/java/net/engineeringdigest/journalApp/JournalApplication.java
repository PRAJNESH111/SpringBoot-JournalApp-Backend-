package net.engineeringdigest.journalApp;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class JournalApplication {

    public static void main(String[] args) {


        SpringApplication.run(JournalApplication.class, args);

        System.out.println("Spring Boot application");
    }

    @Bean
    public  PlatformTransactionManager add(MongoDatabaseFactory dbFactory){
        return  new MongoTransactionManager(dbFactory);
    }


}