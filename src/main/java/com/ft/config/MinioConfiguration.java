package com.ft.config;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.xmlpull.v1.XmlPullParserException;

import io.minio.MinioClient;
import io.minio.errors.ErrorResponseException;
import io.minio.errors.InsufficientDataException;
import io.minio.errors.InternalException;
import io.minio.errors.InvalidBucketNameException;
import io.minio.errors.InvalidEndpointException;
import io.minio.errors.InvalidPortException;
import io.minio.errors.InvalidResponseException;
import io.minio.errors.NoResponseException;
import io.minio.errors.RegionConflictException;

@ConditionalOnProperty(prefix = "minio", name = { "endpoint", "bucket-name" })
@ConfigurationProperties(prefix = "minio", ignoreUnknownFields = true)
public class MinioConfiguration {

	private static final Logger log = LoggerFactory.getLogger(MinioConfiguration.class);

	private String endpoint;

	private String accessKey;

	private String secretKey;

	private String bucketName;
	
	private String domain;
	
	@Override
	public String toString() {
		return "MinioConfiguration [endpoint=" + endpoint + ", accessKey=" + accessKey + ", secretKey=" + secretKey
				+ ", bucketName=" + bucketName + ", domain=" + domain + "]";
	}

	@Bean
	public MinioClient minioClient()
			throws InvalidEndpointException, InvalidPortException, InvalidKeyException, InvalidBucketNameException,
			NoSuchAlgorithmException, InsufficientDataException, NoResponseException, ErrorResponseException,
			InternalException, InvalidResponseException, IOException, XmlPullParserException, RegionConflictException {
		// Create a minioClient with the MinIO Server name, Port, Access key and Secret
		// key.
		log.info("Starting MINIO Client: {}", this.toString());
		MinioClient minioClient = new MinioClient(endpoint, accessKey, secretKey); // "https://play.min.io",
																					// "Q3AM3UQ867SPQQA43P2F",
																					// "zuf+tfteSlswRu7BJ86wekitnifILbZam1KYY3TG");

		// Check if the bucket already exists.
		boolean isExist = minioClient.bucketExists(bucketName);
		if (isExist) {
			log.debug("Bucket already exists.");
		} else {
			log.debug("Bucket not exists. Create one");
			minioClient.makeBucket(bucketName);
		}
		return minioClient;
	}

	public String getEndpoint() {
		return endpoint;
	}

	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}

	public String getAccessKey() {
		return accessKey;
	}

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public String getBucketName() {
		return bucketName;
	}

	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}

	public String getDomain() {
		return domain;
	}

	public void setDomain(String domain) {
		this.domain = domain;
	}

	

}
