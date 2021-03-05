package me.kjgleh.localstack.config

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.testcontainers.containers.localstack.LocalStackContainer
import org.testcontainers.utility.DockerImageName

@TestConfiguration
class LocalStackS3Config {

    private val localstackImage =
        DockerImageName.parse("localstack/localstack:latest")

    @Bean
    fun localStackContainer(): LocalStackContainer {
        return LocalStackContainer(localstackImage).withServices(
            LocalStackContainer.Service.S3
        )
    }

    @Bean
    fun amazonS3(localStackContainer: LocalStackContainer): AmazonS3 {
        return AmazonS3ClientBuilder.standard()
            .withEndpointConfiguration(
                localStackContainer.getEndpointConfiguration(
                    LocalStackContainer.Service.S3
                )
            )
            .withCredentials(localStackContainer.defaultCredentialsProvider)
            .build()
    }
}