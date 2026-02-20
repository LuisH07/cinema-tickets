package com.es.cinema.tickets.bootstrap;

import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Profile("dev")
@RequiredArgsConstructor
public class DatabaseSeeder implements ApplicationRunner {

    private final List<SeederInterface> seeders;

    @Value("${app.seed.enabled:false}")
    private boolean enabled;

    @Override
    public void run(@NonNull ApplicationArguments args) {
        if (!enabled) return;
        seeders.forEach(SeederInterface::seed);
    }
}
