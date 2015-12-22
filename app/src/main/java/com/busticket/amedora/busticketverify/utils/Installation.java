package com.busticket.amedora.busticketverify.utils;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

/**
 * Created by Amedora on 7/13/2015.
 */
public class Installation {
    private static String APP_ID = null;
    private static final String INSTALLATION = "INSTALLATION";

    public synchronized static String appId(Context context){
        if(APP_ID == null){
            File installation = new File(context.getFilesDir(),INSTALLATION);
            try{
                if(!installation.exists())
                    writeInstallationFile(installation);
                APP_ID = readInstallationFile(installation);
            }catch (Exception e){
                throw new RuntimeException(e);
            }
        }
        return APP_ID;
    }

    public static String readInstallationFile(File installation) {
        try{
            RandomAccessFile f = new RandomAccessFile(installation,"r");
            byte[] bytes = new byte[(int) f.length()];
            f.readFully(bytes);
            f.close();
            return new String(bytes);
        }catch ( IOException e){
            return null;
        }

    }

    private static void writeInstallationFile(File installation) throws IOException {
        FileOutputStream out = new FileOutputStream(installation);
        String AppId = UUID.randomUUID().toString();
        out.write(AppId.getBytes());
        out.close();
    }

    public static void deleteInstallionFile(Context context)throws IOException {
        File file = new File(context.getFilesDir(),INSTALLATION);
        if(!file.exists())
            file.delete();
    }
}
