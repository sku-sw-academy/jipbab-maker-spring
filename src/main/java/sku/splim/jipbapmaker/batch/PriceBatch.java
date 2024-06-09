package sku.splim.jipbapmaker.batch;

import sku.splim.jipbapmaker.service.PriceService;
import org.springframework.batch.core.*;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import sku.splim.jipbapmaker.domain.Price;

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

            LocalDateTime now = LocalDateTime.now();
            LocalDate currentDate;

            // 현재 시간이 7시 이전이면 전날을 사용하고, 7시 이후면 당일을 사용
            if (now.getHour() < 16) {
                currentDate = now.minusDays(1).toLocalDate();
            } else {
                currentDate = now.toLocalDate();
            }

            String regday = currentDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            System.out.println(regday);

            List<Price> prices = priceService.fetchPrices(regday, map);
            return RepeatStatus.FINISHED;
        }
        );
    }

}
