package com.raf.rafvodic;


import com.raf.rafvodic.repositories.user.UserRepository;
import com.raf.rafvodic.repositories.user.UserRepositoryImpl;
import com.raf.rafvodic.services.UserService;
import org.glassfish.jersey.internal.inject.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

import javax.inject.Singleton;
import javax.ws.rs.ApplicationPath;


@ApplicationPath("/api")
public class HelloApplication extends ResourceConfig {

    public HelloApplication() {
        // Ukljucujemo validaciju
        property(ServerProperties.BV_SEND_ERROR_IN_RESPONSE, true);
        System.out.println("HelloApplication");
        // Definisemo implementacije u dependency container-u
        AbstractBinder binder = new AbstractBinder() {
            @Override
            protected void configure() {
//                this.bind(MySqlSubjectRepository.class).to(SubjectRepository.class).in(Singleton.class);
                this.bind(UserRepositoryImpl.class).to(UserRepository.class).in(Singleton.class);

//                this.bindAsContract(SubjectService.class);
                this.bindAsContract(UserService.class);
            }
        };
        register(binder);

        System.out.println("registerBinder");
        // Ucitavamo resurse
        packages("com.raf.rafvodic");
        System.out.println("registerPackages");
        // Additional logging to confirm the registration
        getClasses().forEach(clazz -> System.out.println("Registered class: " + clazz.getName()));

    }



}