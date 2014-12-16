package com.example.sdaapp01;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.Configuration;
import com.activeandroid.Configuration.Builder;
import com.activeandroid.app.Application;

public class MyApplication extends Application {

	  @Override
	  public void onCreate() {
	    super.onCreate();
	    Builder builder = new Configuration.Builder(getBaseContext());
	    builder.setCacheSize(1024*1024*4);
	    builder.setDatabaseName("data.db");
	    builder.setDatabaseVersion(1);
	    ActiveAndroid.initialize(builder.create(), true);
	  }

	  @Override
	  public void onTerminate() {
	    super.onTerminate();
	    ActiveAndroid.dispose();
	  }
}