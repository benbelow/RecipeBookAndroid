package com.example.ben.recipebook.fetching;

import android.os.AsyncTask;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class ImageUploadTask extends AsyncTask<PutObjectRequest, Integer, Long> {

    //ToDo: CONFIG!
    private static final String MY_ACCESS_KEY_ID = "AKIAJACHSQOJRBRFTTWQ";
    private static final String MY_SECRET_KEY = "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";

    @Override
    protected Long doInBackground(PutObjectRequest... putObjectRequests) {
        AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(MY_ACCESS_KEY_ID, MY_SECRET_KEY));
        s3Client.putObject(putObjectRequests[0]);
        return null;
    }
}
