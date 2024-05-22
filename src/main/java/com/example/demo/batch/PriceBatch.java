package com.example.demo.batch;

import com.example.demo.service.PriceService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import java.util.*;
import com.example.demo.entity.Price;

@Configuration
public class PriceBatch {
    @Autowired
    private PriceService priceService;

    @Bean
    public Job PriceJob(JobRepository jobRepository, Step step) {
        return new JobBuilder("PriceJob1", jobRepository).start(step).build();
    }

    @Bean
    Step step(JobRepository jobRepository, Tasklet tasklet, PlatformTransactionManager platformTransactionManager) {
        return new StepBuilder("step1", jobRepository).tasklet(tasklet, platformTransactionManager).build();
    }

    @Bean
    Tasklet tasklet() {
        return ((contribution, chunkContext) -> {
            HashMap<Integer, String> map = new HashMap<>();
            map.put(100, "식량작물");
            map.put(200, "채소류");
            map.put(300, "특용작물");
            map.put(400, "과일류");
            map.put(500, "축산물");
            map.put(600, "수산물");
            List<Price> prices = priceService.fetchPrices("2024-05-22", map);
            return RepeatStatus.FINISHED;
        }
        );
    }

}
