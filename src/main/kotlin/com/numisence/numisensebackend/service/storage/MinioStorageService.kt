package com.numisence.numisensebackend.service.storage

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import software.amazon.awssdk.services.s3.presigner.S3Presigner
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest
import java.net.URI
import java.time.Duration
import java.util.UUID

@Service
class MinioStorageService(
    @Value("\${minio.endpoint:http://localhost:9000}") private val endpoint: String,
    @Value("\${minio.access-key:numiterra_admin}") private val accessKey: String,
    @Value("\${minio.secret-key:numiterra_password}") private val secretKey: String,
    @Value("\${minio.bucket-name:diagnostics}") private val bucketName: String
) {
    private val log = LoggerFactory.getLogger(javaClass)

    private val presigner: S3Presigner = S3Presigner.builder()
        .endpointOverride(URI.create(endpoint))
        .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(accessKey, secretKey)))
        .region(Region.US_EAST_1) // MinIO ignores region, but SDK requires it
        .build()

    /**
     * Generates a short-lived URL that the KMP mobile app can use to upload the crop image directly.
     */
    fun generatePreSignedUploadUrl(extension: String = "jpg"): Map<String, String> {
        val objectKey = "images/${UUID.randomUUID()}.$extension"

        val putObjectRequest = PutObjectRequest.builder()
            .bucket(bucketName)
            .key(objectKey)
            .contentType("image/$extension")
            .build()

        val presignRequest = PutObjectPresignRequest.builder()
            .signatureDuration(Duration.ofMinutes(15))
            .putObjectRequest(putObjectRequest)
            .build()

        val presignedUrl = presigner.presignPutObject(presignRequest).url().toString()

        log.info("Generated Pre-signed URL for key: {}", objectKey)

        return mapOf(
            "uploadUrl" to presignedUrl,
            "objectKey" to objectKey
        )
    }
}