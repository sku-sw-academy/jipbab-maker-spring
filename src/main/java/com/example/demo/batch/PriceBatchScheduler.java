package com.example.demo.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.explore.JobExplorer;
import org.springframework.batch.core.launch.JobExecutionNotRunningException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.launch.NoSuchJobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
public class PriceBatchScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @Scheduled(cron = "0/10 * * * * *")
    public void runJob() throws Exception{
        System.out.println("Running job on thread: " + Thread.currentThread().getName());
        JobParameters jobParameters = new JobParametersBuilder().addString("jobName", "PriceJob1"+ System.currentTimeMillis()).toJobParameters();
        jobLauncher.run(job, jobParameters);
    }

}
