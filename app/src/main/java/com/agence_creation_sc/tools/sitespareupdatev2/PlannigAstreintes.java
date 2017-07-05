package com.agence_creation_sc.tools.sitespareupdatev2;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;

/**
 * Created by Stéf on 31/01/2016.
 */
public class PlannigAstreintes extends Fragment {

    public static PlannigAstreintes newInstance() {
        PlannigAstreintes fragment = new PlannigAstreintes();
        return fragment;
    }

    public PlannigAstreintes() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View setView = inflater.inflate(R.layout.activity_main, container, false);
            downloadplanning();

        return setView;
    }


    public void downloadplanning() {
        Calendar calendar = Calendar.getInstance();
        int week =  calendar.get(Calendar.WEEK_OF_YEAR);
        new DownloadFile().execute("http://tools.agence-creation-sc.com/CHRONOPOST/PlanningSemaine"+week+".pdf", "PlanningSemaine"+week+".pdf");


        File pdfFile = new File(Environment.getExternalStorageDirectory() + "/testthreepdf/" + "PlanningSemaine"+week+".pdf");  // -> filename = maven.pdf
        Uri path = Uri.fromFile(pdfFile);
        Intent pdfIntent = new Intent(Intent.ACTION_VIEW);
        pdfIntent.setDataAndType(path, "application/pdf");
        pdfIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        try {
            startActivity(pdfIntent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No Application available to view PDF", Toast.LENGTH_SHORT).show();
        }
    }

    private class DownloadFile extends AsyncTask<String, Void, Void> {

        @Override
        protected Void doInBackground(String... strings) {
            String fileUrl = strings[0];   // -> http://maven.apache.org/maven-1.x/maven.pdf
            String fileName = strings[1];  // -> maven.pdf
            String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
            File folder = new File(extStorageDirectory, "testthreepdf");
            folder.mkdir();

            File pdfFile = new File(folder, fileName);

            try {
                pdfFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            FileDownloader.downloadFile(fileUrl, pdfFile);
            return null;
        }
    }

}
