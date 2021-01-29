package server.dao;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class S3DAO {

    private AmazonS3 s3 = AmazonS3ClientBuilder.standard().withRegion("us-west-2").build();
    private String bucket = "coreydev-twitter-user-images";

    public String saveImage(byte[] imageBytes, String alias) throws IOException {
        InputStream stream = new ByteArrayInputStream(imageBytes);
        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(imageBytes.length);
        metadata.setContentType("image/png");
        String fileName = alias + ".png";

        s3.putObject(new PutObjectRequest(bucket, fileName, stream, metadata)
            .withCannedAcl(CannedAccessControlList.PublicRead));

        stream.close();

        return "https://" + bucket + ".s3-us-west-2.amazonaws.com/" + fileName;
    }
}
