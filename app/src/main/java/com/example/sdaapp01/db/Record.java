package com.example.sdaapp01.db;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.database.Cursor;

import com.activeandroid.Cache;
import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;
import com.example.sdaapp01.util.L;

@Table(name = "Records")
public class Record extends Model {
	private static SimpleDateFormat mSdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS", Locale.JAPANESE);
	@Column(name = "datetime")
	public String datetime;
	@Column(name = "ax")
	public double ax;
	@Column(name = "ay")
	public double ay;
	@Column(name = "az")
	public double az;

	public Record() {
		super();
	}

	public Record(double ax, double ay, double az) {
		super();
		String datetime = mSdf.format(System.currentTimeMillis());
		this.datetime = datetime;
		this.ax = ax;
		this.ay = ay;
		this.az = az;
	}

	public static List<Record> getAll(int limit) {
		return new Select().from(Record.class).orderBy("datetime DESC").limit(limit).execute();
	}

	public static Cursor fetchResultCursor(int limit) {
		L.i("Cache.isInitialized():"+Cache.isInitialized());
		if (!Cache.isInitialized()) {
			return null;
		}
		String tableName = Cache.getTableInfo(Record.class).getTableName();
		String resultRecords = new Select(tableName + ".*, " + tableName
				+ ".Id as _id").from(Record.class).orderBy("datetime DESC").limit(limit).toSql();
		Cursor resultCursor = Cache.openDatabase()
				.rawQuery(resultRecords, null);
		return resultCursor;
	}
    public static void clear(){
        String tableName = Cache.getTableInfo(Record.class).getTableName();
        Cache.openDatabase().delete(tableName,null,null);
    }
}
