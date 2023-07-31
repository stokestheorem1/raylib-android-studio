package com.example.raylibtutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NativeActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.raylibtutorial.databinding.ActivityMainBinding;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class MainActivity extends NativeActivity {

    // Used to load the 'raylibtutorial' library on application startup.
    static {
        System.loadLibrary("main");
    }
    private void unzipFromRawResource(int rawResourceId, String destinationPath) {
        try {
            InputStream inputStream = getResources().openRawResource(rawResourceId);
            ZipInputStream zipInputStream = new ZipInputStream(inputStream);
            ZipEntry zipEntry;

            byte[] buffer = new byte[4096];
            int count;

            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String fileName = zipEntry.getName();
                File outputFile = new File(destinationPath, fileName);

                if (zipEntry.isDirectory()) {
                    // Create directories if the entry is a directory
                    outputFile.mkdirs();
                } else {
                    // Create parent directories for the file
                    outputFile.getParentFile().mkdirs();

                    // Write the file content to the internal storage
                    FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
                    while ((count = zipInputStream.read(buffer)) != -1) {
                        fileOutputStream.write(buffer, 0, count);
                    }
                    fileOutputStream.close();
                }

                zipInputStream.closeEntry();
            }

            zipInputStream.close();
            inputStream.close();
        } catch (IOException e) {
            Log.e("Unzip", "Error unzipping the file", e);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unzipFromRawResource(R.raw.resources, getFilesDir().getPath());
    }

}