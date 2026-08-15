package com.sky.config;

import io.minio.MinioClient;
import io.minio.SetBucketPolicyArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Configuration
@Slf4j
public class MinIOBucketConfig {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private MinioConfiguration minioConfig;

    /**
     * 项目启动时自动执行：确保桶存在，并设置为公开
     */
    @PostConstruct
    public void initBucket() {
        try {
            String bucketName = minioConfig.getBucketName();
            // 1. 检查桶是否存在
            boolean found = minioClient.bucketExists(
                    io.minio.BucketExistsArgs.builder().bucket(bucketName).build()
            );
            if (!found) {
                // 2. 如果不存在，创建桶
                minioClient.makeBucket(
                        io.minio.MakeBucketArgs.builder().bucket(bucketName).build()
                );
                log.info("MinIO 桶 '{}' 创建成功", bucketName);
            } else {
                log.info("MinIO 桶 '{}' 已存在", bucketName);
            }

            // 3. 设置桶策略为公开（允许任何人读取）
            // 这是一个标准的 S3 公开读策略
            String policy = "{\n" +
                    "    \"Version\": \"2012-10-17\",\n" +
                    "    \"Statement\": [\n" +
                    "        {\n" +
                    "            \"Effect\": \"Allow\",\n" +
                    "            \"Principal\": {\n" +
                    "                \"AWS\": [\"*\"]\n" +
                    "            },\n" +
                    "            \"Action\": [\"s3:GetObject\"],\n" +
                    "            \"Resource\": [\n" +
                    "                \"arn:aws:s3:::" + bucketName + "/*\"\n" +
                    "            ]\n" +
                    "        }\n" +
                    "    ]\n" +
                    "}";

            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder()
                            .bucket(bucketName)
                            .config(policy)
                            .build()
            );
            log.info("MinIO 桶 '{}' 访问策略已设置为公开", bucketName);

        } catch (Exception e) {
            log.error("MinIO 桶初始化失败：{}", e.getMessage());
            // 生产环境可以考虑抛出异常阻止启动，开发环境打印日志即可
        }
    }
}