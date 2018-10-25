package amazon;

import java.io.File;

import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.Upload;

import utils.Persist;

public class AmazonUtilities {
	
	
	public static int uploadImagesAmazonAPI(File toUpload) {
		String awsAccessKeyId = System.getenv(Persist.AWS_ACCESS_KEY_ID);
		String awsSecretAccessKey = System.getenv(Persist.AWS_SECRET_KEY_ID);
		BasicAWSCredentials awsCreds = new BasicAWSCredentials(awsAccessKeyId, awsSecretAccessKey);
		AmazonS3 s3Client = AmazonS3ClientBuilder.standard()
		                        .withCredentials(new AWSStaticCredentialsProvider(awsCreds))
		                        .withRegion(Regions.EU_CENTRAL_1)
		                        .build();
		
		TransferManager tm = new TransferManager(s3Client);
		
        //TransferManager tm = new TransferManager(new ProfileCredentialsProvider());
        // TransferManager processes all transfers asynchronously, 
        // so this call will return immediately.
        System.out.println("Trying to upload " + toUpload.getAbsolutePath());
        
        PutObjectRequest por = new PutObjectRequest(Persist.AMAZON_S3_BUCKET_NAME, toUpload.getName(), toUpload);
        por.setCannedAcl(CannedAccessControlList.PublicRead);
        Upload upload = tm.upload(por);
        //Upload upload = tm.upload(Persist.AMAZON_S3_BUCKET_NAME, toUpload.getName(), toUpload);
        System.out.println("----------");

        try {
        	// Or you can block and wait for the upload to finish
        	upload.waitForCompletion();
        	System.out.println("Upload complete.");
        	return Persist.SUCCESS;
        } catch (AmazonClientException amazonClientException) {
        	System.out.println("Caught an AmazonClienException, which"
					+ "	means the client encountered an internal error"
					+ " while trying to communicate with S3, such as not"
					+ " being able to access the network.");
			System.out.println("Error Message: " + amazonClientException.getMessage());
        	amazonClientException.printStackTrace();
        	return Persist.ERROR_AMAZON_S3_CLIENT;
        } catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return Persist.ERROR;
	}

}
