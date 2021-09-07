package com.pengxh.app.multilib.utils;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 将Log日志写入文件中
 */
public class LogToFile {
    private static final String TAG = "LogToFile";

    public static File initLog(Context context) {
        File documentDir = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "");
        String timeStamp = new SimpleDateFormat("yyyyMMdd", Locale.CHINA).format(new Date());
        File logFile = new File(documentDir.toString() + File.separator + "Log_" + timeStamp + ".txt");
        if (!logFile.exists()) {
            try {
                logFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Log.d(TAG, "initLog: " + logFile.getAbsolutePath());
        return logFile;
    }

    /**
     * @param log 待写入的内容
     */
    public static void write(Context context, String log) {
        try {
            FileWriter fileWriter = new FileWriter(initLog(context), true);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.write(log);
            writer.newLine(); //换行
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String read(File file) {
        StringBuilder builder;
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String line = bufferedReader.readLine();
            builder = new StringBuilder();
            while (line != null) {
                builder.append(line);
                builder.append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }
}