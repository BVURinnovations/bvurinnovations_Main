package com.bvurinnovations.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.Bucket;
import com.amazonaws.services.s3.model.Region;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

public class S3Utils {
    AmazonS3 amazonS3;
    private static AWSCredentials credentialProvider(String accessKey, String secretKey) {
        return new BasicAWSCredentials(accessKey, secretKey);
    }
    public static AmazonS3 getS3Client() {
        AWSCredentials credentials = credentialProvider(Constants.accessKey, Constants.secretKey);

        return AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion("ap-south-1")
                .build();
        //List<Bucket> buckets = s3Client.listBuckets();
        //AmazonS3Client s3Client = new AmazonS3Client(awsCreds).withRegion(Regions.valueOf("ap-south-1"));
//        s3Client.putObject(
//                "ap-sounth-1-dev-furrcrew",
//                "Test/img1.PNG",
//                new File("./src/main/resources/img1.PNG")
//        );
    }

    public static AWSCredentials getAwsProviderCredentials() {
        return new DefaultAWSCredentialsProviderChain().getCredentials();
    }
}
