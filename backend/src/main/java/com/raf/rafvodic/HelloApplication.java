package com.raf.rafvodic;


import com.raf.rafvodic.repositories.activities.ActivityRepository;
import com.raf.rafvodic.repositories.activities.ActivityRepositoryImpl;
import com.raf.rafvodic.repositories.article.ArticleRepository;
import com.raf.rafvodic.repositories.article.ArticleRepositoryImpl;
import com.raf.rafvodic.repositories.comments.CommentRepository;
import com.raf.rafvodic.repositories.comments.CommentRepositoryImpl;
import com.raf.rafvodic.repositories.destination.DestinationRepository;
import com.raf.rafvodic.repositories.destination.DestinationRepositoryImpl;
import com.raf.rafvodic.repositories.user.UserRepository;
import com.raf.rafvodic.repositories.user.UserRepositoryImpl;
import com.raf.rafvodic.services.*;
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
                this.bind(UserRepositoryImpl.class).to(UserRepository.class).in(Singleton.class);
                this.bind(DestinationRepositoryImpl.class).to(DestinationRepository.class).in(Singleton.class);
                this.bind(ArticleRepositoryImpl.class).to(ArticleRepository.class).in(Singleton.class);
                this.bind(ActivityRepositoryImpl.class).to(ActivityRepository.class).in(Singleton.class);
                this.bind(CommentRepositoryImpl.class).to(CommentRepository.class).in(Singleton.class);

                this.bindAsContract(UserService.class);
                this.bindAsContract(DestinationService.class);
                this.bindAsContract(ArticleService.class);
                this.bindAsContract(ActivityService.class);
                this.bindAsContract(CommentService.class);
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