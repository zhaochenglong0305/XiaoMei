package utils.DataBaseUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Adminis on 2018/1/26.
 */

public class CityDbHandler {
    /**
     * 数据库名称
     */
    private String dbName = "citycode.db";

    /**
     * 数据库保存路径
     */
    private String dbPath = "/data/data/";

    public SQLiteDatabase getDataBase(Context context) {
        dbPath += context.getPackageName() + "/";
        File path = new File(dbPath);
        if (!path.exists())
            path.mkdirs();

        File dbFile = new File(dbPath + dbName);


        if (!dbFile.exists()) {
            try {
                InputStream is = context.getAssets().open("city.db");
                FileOutputStream fos = new FileOutputStream(dbFile);

                byte[] buffer = new byte[1024];
                int c = 0;
                while ((c = is.read(buffer)) >= 0) {
                    fos.write(buffer, 0, c);
                }

                fos.flush();
                fos.close();
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return SQLiteDatabase.openDatabase(dbPath + dbName, null, SQLiteDatabase.OPEN_READWRITE);
    }

}
