package edu.hm.cs.vadere.seating.datacollection;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.os.Environment;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import edu.hm.cs.vadere.seating.datacollection.db.MySQLiteOpenHelper;

public class DatabaseExportTask extends AsyncTask<Void, String, Boolean> {
    private final Context context;
    private final ProgressDialog progressDialog;

    public DatabaseExportTask(Context context, ProgressDialog progressDialog) {
        this.context = context;
        this.progressDialog = progressDialog;
    }

    @Override
    protected void onPreExecute() {
        progressDialog.show();
    }

    @Override
    protected void onProgressUpdate(String... values) {
        progressDialog.setMessage(values[0]);
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
//        progressDialog.set... or show dialog?
        progressDialog.setMessage("Finished");
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            exportAllTables();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void exportAllTables() throws IOException {
        if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED)
            throw new IOException("external storage must be mounted writable");

        File directory = Environment.getExternalStorageDirectory();
        directory = new File(directory, "SeatingDataCollection");
        directory.mkdir();

        // Open db
        SQLiteOpenHelper helper = new MySQLiteOpenHelper(context, SeatingDataCollectionApp.DATABASE_NAME, null, SeatingDataCollectionApp.DATABASE_VERSION);
        SQLiteDatabase db = helper.getWritableDatabase();

        // For each table
        for (String tableName : getTableList(db)) {
            publishProgress("Exporting table " + tableName);
            exportTable(db, tableName, directory);
        }
    }

    private List<String> getTableList(SQLiteDatabase db) {
        List<String> result = new LinkedList<>();
        try (Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table'", null)) {
            while (!c.isAfterLast()) {
                result.add(c.getString(0));
                c.moveToNext();
            }
        }
        return result;
    }

    private void exportTable(SQLiteDatabase db, String tableName, File directory) throws IOException {
        final File exportFile = new File(directory, tableName + ".csv");
        try (CSVWriter writer = new CSVWriter(new FileWriter(exportFile), ',')) {
            final Cursor cursor = queryTable(db, tableName);
            while (!cursor.isAfterLast()) {
                final String[] fields = getFieldsAsStringArray(cursor);
                writer.writeNext(fields);
                cursor.moveToNext();
            }
        }
    }

    private Cursor queryTable(SQLiteDatabase db, String tableName) {
        return db.rawQuery("SELECT * FROM ?", new String[] {tableName});
    }

    private String[] getFieldsAsStringArray(Cursor cursor) {
        final int columnCount = cursor.getColumnCount();
        final String[] result = new String[columnCount];
        for (int i = 0; i < columnCount; i++) {
            result[i] = cursor.getString(i);
        }
        return result;
    }

}
