/**
 * Copyright (C) 2014 NTT DOCOMO, INC. All Rights Reserved.
 */

package com.example.sdaapp01;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import com.example.sdaapp01.util.L;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;

/**
 * コンテントプロバイダー.
 */

public class FileProvider extends ContentProvider {

        public static final String AUTHORITY = Define.OWN_PACKAGE;
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY);

        @Override
        public boolean onCreate() {
        	L.e("@@@@@ onCreate()");
            return false;
        }

        @Override
        public String getType(Uri uri) {
        	L.e("@@@@@ getType()");
            L.d("getType, " + uri.toString());
            return null;
        }

        @Override
        public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
            L.d("open file" + uri.toString());
        	L.e("@@@@@ open file" + uri.toString());

            File root = new File(Environment.getExternalStorageDirectory(), "/sdadriver/");
            String state = Environment.getExternalStorageState();
            if (!Environment.MEDIA_MOUNTED.equals(state)) {
                root = new File("/storage/emulated/legacy", "/sdadriver/");
            }
            root.mkdirs();
            File path = new File(root, uri.getEncodedPath());

            int imode = 0;
            if (mode.contains("w")) {
                    imode |= ParcelFileDescriptor.MODE_WRITE_ONLY;
                    if (!path.exists()) {
                        try {
                            path.createNewFile();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
            }
            if (mode.contains("r")) imode |= ParcelFileDescriptor.MODE_READ_ONLY;
            if (mode.contains("+")) imode |= ParcelFileDescriptor.MODE_APPEND;        

            return ParcelFileDescriptor.open(path, imode);        }

        @Override
        public int delete(Uri uri, String selection, String[] selectionArgs) {
        	L.e("@@@@@ delete()");
            return 0;
        }

        @Override
        public Uri insert(Uri uri, ContentValues values) {
        	L.e("@@@@@ insert()");
            return null;
        }

        @Override
        public Cursor query(Uri uri, String[] projection, String selection,
                String[] selectionArgs, String sortOrder) {
        	L.e("@@@@@ query()");
            return null;
        }

        @Override
        public int update(Uri uri, ContentValues values, String selection,
                String[] selectionArgs) {
        	L.e("@@@@@ update()");
            return 0;
        }
}
