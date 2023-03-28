package com.example.batch_web.job;

import com.example.batch_web.CustomService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.step.tasklet.MethodInvokingTaskletAdapter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Log4j2
//배치를 만들기 위한 어노테이션
@EnableBatchProcessing
@Configuration
@RequiredArgsConstructor
public class TaskJobConfig {
    //Job을 생성하기 위한 Builder 객체
    private final JobBuilderFactory jobBuilderFactory;
    //Step을 생성하기 위한 Builder 객체
    private final StepBuilderFactory stepBuilderFactory;

    //스텝 생성
    @Bean
    public Step TaskStep(){
        return stepBuilderFactory.get("LambdaStep")
                .tasklet((contribution, chunkContext) -> {
                    //비지니스 로직 호출
                    for(int idx = 0; idx<10; idx++){
                        log.info("[idx]:" + idx);
                    }
                    return RepeatStatus.FINISHED;
                }).build();
    }

    //Job 생성
    @Bean
    public Job TaskletJob(){
        Job customJob = jobBuilderFactory.get("taskletjob")
                .start(TaskStep())
                .build();
        return customJob;
    }

    //Step으로 수행할 객체
    private final CustomService customService;

    @Bean
    public Job makeJob(){
        return jobBuilderFactory.get("methodInvokingJob")
                .start(makeStep())
                .build();
    }

    @Bean
    public Step makeStep(){
        return stepBuilderFactory.get("makeMethodInvokingTasklet")
                .tasklet(makeMethodInvokingTasklet())
                .build();
    }

    //Step에 사용할 메서드를 등록해주는 Bean
    @Bean
    @StepScope
    public MethodInvokingTaskletAdapter makeMethodInvokingTasklet(){
        MethodInvokingTaskletAdapter methodInvokingTaskletAdapter =
                new MethodInvokingTaskletAdapter();
        methodInvokingTaskletAdapter.setTargetObject(customService);
        methodInvokingTaskletAdapter.setTargetMethod("businessLogic");
        //매개변수 대입
        //methodInvokingTaskletAdapter.setArguments(//매개변수를 배열로 등록);
        return methodInvokingTaskletAdapter;
    }
}








