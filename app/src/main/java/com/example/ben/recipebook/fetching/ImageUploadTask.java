package com.example.ben.recipebook.fetching;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;

public class ImageUploadTask extends AsyncTask<PutObjectRequest, Integer, Boolean> {

    //ToDo: CONFIG!
    private static final String MY_ACCESS_KEY_ID = "AKIAJACHSQOJRBRFTTWQ";
    private static final String MY_SECRET_KEY = "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";
    private Context context;

    public ImageUploadTask(Context context){
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(PutObjectRequest... putObjectRequests) {
        try{
            AmazonS3Client s3Client = new AmazonS3Client(new BasicAWSCredentials(MY_ACCESS_KEY_ID, MY_SECRET_KEY));
            s3Client.putObject(putObjectRequests[0]);
            return true;
        } catch (Exception e){
            Log.e("Upload failed: ", e.getMessage());
            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        String status = result ? "succeeded." : "failed.";
        Toast.makeText(context, "Image upload " + status, Toast.LENGTH_SHORT).show();
    }
}
