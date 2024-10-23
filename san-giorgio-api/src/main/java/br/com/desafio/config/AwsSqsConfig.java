package br.com.desafio.config;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import com.amazonaws.services.sqs.model.ListQueuesRequest;
import com.amazonaws.services.sqs.model.ListQueuesResult;
import io.awspring.cloud.messaging.core.QueueMessagingTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static br.com.desafio.util.QueueConstants.*;

@Configuration
@Slf4j
public class AwsSqsConfig {

    @Value("${spring.aws.sqs.endpoint}")
    private String sqsEndpoint;

    @Value("${spring.aws.sqs.region}")
    private String sqsRegion;

    @Value("${spring.aws.sqs.access-key}")
    private String accessKey;

    @Value("${spring.aws.sqs.secret-key}")
    private String secretKey;

    @Bean
    public AmazonSQSAsync amazonSQSAsync() {
        return AmazonSQSAsyncClientBuilder.standard()
                .withEndpointConfiguration(new AwsClientBuilder.EndpointConfiguration(
                        sqsEndpoint,
                        sqsRegion))
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .build();
    }

    @Bean
    public QueueMessagingTemplate queueMessagingTemplate(AmazonSQSAsync amazonSQSAsync) {
        return new QueueMessagingTemplate(amazonSQSAsync);
    }

    @Bean
    public CommandLineRunner createQueues(AmazonSQSAsync amazonSQSAsync) {
        return args -> {
            ListQueuesResult listQueuesResult = amazonSQSAsync.listQueues(new ListQueuesRequest());

            createQueueIfNotExists(amazonSQSAsync, listQueuesResult, PARTIAL_PAYMENTS_QUEUE);
            createQueueIfNotExists(amazonSQSAsync, listQueuesResult, TOTAL_PAYMENTS_QUEUE);
            createQueueIfNotExists(amazonSQSAsync, listQueuesResult, SURPLUS_PAYMENTS_QUEUE);
        };
    }

    private void createQueueIfNotExists(AmazonSQSAsync amazonSQSAsync, ListQueuesResult listQueuesResult, String queueName) {
        boolean queueExists = listQueuesResult.getQueueUrls().stream()
            .anyMatch(url -> url.endsWith("/" + queueName));

        if (queueExists) {
            log.info("[AWS-SQS-CONFIG] Queue '{}' already exists.", queueName);
            return;
        }

        amazonSQSAsync.createQueue(queueName);
        log.info("[AWS-SQS-CONFIG] Queue '{}' created.", queueName);
    }

}
