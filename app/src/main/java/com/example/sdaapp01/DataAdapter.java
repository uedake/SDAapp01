package com.example.sdaapp01;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DataAdapter extends CursorAdapter {
	  public DataAdapter(Context context, Cursor cursor) {
	      super(context, cursor, 0);
	  }

	  @Override
	  public View newView(Context context, Cursor cursor, ViewGroup parent) {
	      return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
	  }

	  @Override
	  public void bindView(View view, Context context, Cursor cursor) {
	      TextView tvDatetime = (TextView) view.findViewById(R.id.textDatetime);
	      TextView tvData1 = (TextView) view.findViewById(R.id.textData1);
	      TextView tvData2 = (TextView) view.findViewById(R.id.textData2);
	      TextView tvData3 = (TextView) view.findViewById(R.id.textData3);

	      String datetime = cursor.getString(cursor.getColumnIndexOrThrow("datetime"));
          double data1 = cursor.getDouble(cursor.getColumnIndexOrThrow("ax"));
          double data2 = cursor.getDouble(cursor.getColumnIndexOrThrow("ay"));
	      double data3 = cursor.getDouble(cursor.getColumnIndexOrThrow("az"));

	      tvDatetime.setText(datetime);
	      tvData1.setText(String.valueOf((int)data1));
	      tvData2.setText(String.valueOf((int)data2));
	      tvData3.setText(String.valueOf((int)data3));
	  }
	}