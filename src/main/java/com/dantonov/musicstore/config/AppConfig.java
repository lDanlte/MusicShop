package com.dantonov.musicstore.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;

/**
 * @author denis.antonov
 * @since 20.03.17.
 */
@Configuration
@ComponentScan(basePackages = {"com.dantonov.musicstore.service"})
public class AppConfig {


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
}
