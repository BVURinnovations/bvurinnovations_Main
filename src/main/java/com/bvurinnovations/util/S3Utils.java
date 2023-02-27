package com.bvurinnovations.util;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.DefaultAWSCredentialsProviderChain;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.Region;

public class S3Utils {

    public static AmazonS3Client getS3ClientWithCredentials(String accessKey, String secretKey){
        AWSCredentials awsCreds = new BasicAWSCredentials(accessKey, secretKey);
        //regionS3.
        AmazonS3Client s3Client = new AmazonS3Client(awsCreds);
        return s3Client;
    }

    public static AWSCredentials getAwsProviderCredentials() {
        return new DefaultAWSCredentialsProviderChain().getCredentials();
    }
}
