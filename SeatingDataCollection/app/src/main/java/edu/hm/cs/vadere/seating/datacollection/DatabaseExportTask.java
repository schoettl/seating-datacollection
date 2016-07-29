package edu.hm.cs.vadere.seating.datacollection;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import edu.hm.cs.vadere.seating.datacollection.db.MySQLiteOpenHelper;
import edu.hm.cs.vadere.seating.datacollection.model.LogEvent;
import edu.hm.cs.vadere.seating.datacollection.model.Person;
import edu.hm.cs.vadere.seating.datacollection.model.Survey;

public class DatabaseExportTask extends AsyncTask<Void, String, Boolean> {
    private static final String TAG = "DatabaseExport";

    private final Activity activity;
    private final ProgressDialog progressDialog;

    public DatabaseExportTask(Activity activity, ProgressDialog progressDialog) {
        this.activity = activity;
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
    protected void onPostExecute(Boolean success) {
        progressDialog.dismiss();
        if (success) {
            UiHelper.showInfoDialog(activity, R.string.dialog_info_export_success);
        } else {
            UiHelper.showInfoDialog(activity, R.string.dialog_info_export_fail);
        }
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        try {
            exportAllTables();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "error while exporting: " + e.getLocalizedMessage());
            return false;
        }
        return true;
    }

    private void exportAllTables() throws IOException {
        // This throws an exception with no real external SD card!
        //if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED)
        //    throw new IOException("external storage must be mounted writable");

        File directory = Environment.getExternalStorageDirectory();
        //File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS); // as alternative
        logDirExist(directory);
        directory = new File(directory, "SeatingDataCollection");
        directory.mkdirs();
        logDirExist(directory);

        // Open db
        SQLiteOpenHelper helper = new MySQLiteOpenHelper(activity, SeatingDataCollectionApp.DATABASE_NAME, null, SeatingDataCollectionApp.DATABASE_VERSION);
        try (SQLiteDatabase db = helper.getWritableDatabase()) {

            List<String> tableNames = getTableList();
            Log.d(TAG, "exporting " + tableNames.size() + " tables");

            for (String tableName : tableNames) {
                Log.d(TAG, "exporting table " + tableName);
                publishProgress("Exporting table " + tableName);
                exportTable(db, tableName, directory);
            }
        }

        triggerMediaScan();
    }

    /** Trigger media scan to make new files visible for MTP clients. */
    private void triggerMediaScan() {
        // http://muzso.hu/2014/05/11/manually-running-a-media-scan-on-android
        Intent intent = new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + Environment.getExternalStorageState()));
        // not tested
    }

    private void logDirExist(File directory) {
        Log.d(TAG, "dir exists? " + directory.toString() + " -> " + directory.isDirectory());
    }

    private List<String> getTableList() {
        List<String> result = new LinkedList<>();
        result.add(Utils.toSugarTableName(Survey.class));
        result.add(Utils.toSugarTableName(Person.class));
        result.add(Utils.toSugarTableName(LogEvent.class));
        return result;
    }

    private void exportTable(SQLiteDatabase db, String tableName, File directory) throws IOException {
        final File exportFile = new File(directory, tableName + ".csv");
        try (CSVWriter writer = new CSVWriter(new FileWriter(exportFile), ',')) {
            final Cursor cursor = queryTable(db, tableName);
            // Write headers
            final String[] columnNames = cursor.getColumnNames();
            Log.d(TAG, "column names: " + Arrays.asList(columnNames));
            writer.writeNext(columnNames);
            // Write rows
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                final String[] fields = getFieldsAsStringArray(cursor);
                writer.writeNext(fields);
                cursor.moveToNext();
            }
        }
    }

    private Cursor queryTable(SQLiteDatabase db, String tableName) {
        // Trust caller that it provides only good table names
        return db.rawQuery("SELECT * FROM " + tableName, null);
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
