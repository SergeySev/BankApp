package com.example.telproject.config;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {
    Config config = new Config();
    @Bean
    public AmazonS3 s3 () {
        AWSCredentials awsCredentials = new BasicAWSCredentials(
          config.getAccessKey(),
                config.getSecretKey()
        );
        return AmazonS3ClientBuilder.standard().
                withCredentials(new AWSStaticCredentialsProvider(awsCredentials)).withRegion(config.getRegion()).build();
    }
}
