package com.example.listview;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Collections;
import java.util.List;

public class Activity_ListView extends AppCompatActivity {

	ListView my_listview;
	DownloadTask downloadTask;
	SharedPreferences myPrefs;
	List<BikeData> bikes;
	SharedPreferences.OnSharedPreferenceChangeListener prefListener;
	CustomAdapter customAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Change title to indicate sort by
		setTitle(getString(R.string.app_title));

		//listview that you will operate on
		my_listview = (ListView)findViewById(R.id.lv);
		myPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		//toolbar
		Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);
		android.support.v7.app.ActionBar actionBar = getSupportActionBar();

		//Checks the device's network connection
		//Alerts user if not connected
		//Otherwise starts a thread to get JSON data
		if(ConnectivityCheck.isNetworkReachableAlertUserIfNot(this)){
			String url = myPrefs.getString(getString(R.string.json_website), "");
			if(url == null){
				url = getString(R.string.defenderURL);
			}
			downloadTask = new DownloadTask(this);
			downloadTask.execute(url);
		}

		//Creates and populates spinner within the toolbar
		setupSimpleSpinner();

		//set the listview onclick listener
		setupListViewOnClickListener();

		//Preference listener which is called whenever a change to the preferences is made by the user
		prefListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
			@Override
			public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
				String url = myPrefs.getString(key, "");
				downloadTask = new DownloadTask(Activity_ListView.this);
				downloadTask.execute(url);
			}
		};
		myPrefs.registerOnSharedPreferenceChangeListener(prefListener);
	}

	/*
		The listView's onClickListener
		Will display an alert dialog for the selected bike in the listView
		The alert dialog will display info about the selected bike
	 */
	private void setupListViewOnClickListener() {
		//Shows an alert dialog with information about the bike selected in the listview
		my_listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
									long id) {
				BikeData current = bikes.get(position);

				AlertDialog.Builder adBuilder = new AlertDialog.Builder(Activity_ListView.this);
				adBuilder.setMessage(current.toString());
				adBuilder.setNeutralButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
				adBuilder.show();
			}
		});
	}

	/**
	 * Takes the string of bikes, parses it using JSONHelper
	 * Sets the adapter with this list using a custom row layout and an instance of the CustomAdapter
	 * binds the adapter to the Listview using setAdapter
	 *
	 * @param: JSONString,  complete string of all bikes
	 */
	public void bindData(String JSONString) {
		bikes = JSONHelper.parseAll(JSONString);

		customAdapter = new CustomAdapter(this, bikes);

		my_listview.setAdapter(customAdapter);
	}

	Spinner spinner;
	/**
	 * create a data adapter to fill above spinner with choices(Company, Location, Model and Price),
	 * bind it to the spinner
	 * Also create a OnItemSelectedListener for this spinner so
	 * when a user clicks the spinner the list of bikes is resorted according to selection
	 */
	private void setupSimpleSpinner() {
		spinner = (Spinner)findViewById(R.id.spinner);
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.sortable_fields, R.layout.spinner_item);
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		spinner.setAdapter(adapter);

		spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				if(bikes != null) {
					Collections.sort(bikes, new ComparatorModel(position));
					customAdapter.notifyDataSetChanged();
				}
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	/**
	 * Listener for the menuItems in the toolbar
	 * @param: MenuItem item, the MenuItem selected by the user
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.action_settings:
				Intent myIntent = new Intent(this, activityPreference.class);
				startActivity(myIntent);
				return true;
			case R.id.refresh:
				//Refreshes back to original state
				spinner.setSelection(0);
				bikes.clear();
				new DownloadTask(this).execute(myPrefs.getString(getString(R.string.json_website), ""));
			default:
				return super.onOptionsItemSelected(item);
		}
	}
}
