package com.laboratory.sudoku;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

/**
 * Created by Li on 2016/11/30.
 */
public class FileController {
    private File m_RecordFile;

    public void WriteToFile(String filepath, ArrayList<String> data){
        try {
            m_RecordFile = new File(filepath, "save.pcm");
            OutputStream outputStream = new FileOutputStream(m_RecordFile);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
            DataOutputStream dataOutputStream = new DataOutputStream(bufferedOutputStream);

            int n = 0;
            while (n < data.size()) {
                dataOutputStream.writeChars(data.get(n)); // stream data to file
                n++;
            }
            dataOutputStream.close();
        } catch (IOException e) {
            Log.d("File Writer", "Cannot write puzzle data to file");
            e.printStackTrace();
        }
    }

    public boolean ReadFromFile(String filepath, ArrayList<String> data){
        String path = filepath + File.separator + "save.pcm";

        try {
            DataInputStream fileData = new DataInputStream(new BufferedInputStream(new FileInputStream(path)));

            int n = 0;
            while (fileData.available() > 0) {
                data.add(n, ((Character)fileData.readChar()).toString());
                n++;
            }
            fileData.close();
        } catch (IOException e) {
            Log.d("File Writer", "Cannot write sound to file");
            return false;
            //e.printStackTrace();
        }

        return true;
    }

}
