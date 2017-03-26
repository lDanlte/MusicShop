package com.dantonov.musicstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * @author denis.antonov
 * @since 20.03.17.
 */
@Configuration
@ComponentScan(basePackages = {"com.dantonov.musicstore.service"})
@EnableScheduling
public class AppConfig implements SchedulingConfigurer {


    @Bean
    public DecimalFormat decimalFormat() {
        final DecimalFormat decimalFormat = new DecimalFormat();

        decimalFormat.setMaximumFractionDigits(2);
        decimalFormat.setMinimumFractionDigits(2);
        decimalFormat.setGroupingUsed(false);

        return decimalFormat;
    }

    @Bean
    public SimpleDateFormat dateFormat() {
        return new SimpleDateFormat("dd.MM.yyyy");
    }

    @Override
    public void configureTasks(final ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.setScheduler(taskExecutor());
    }

    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService taskExecutor() {
        return Executors.newSingleThreadScheduledExecutor();
    }
}
