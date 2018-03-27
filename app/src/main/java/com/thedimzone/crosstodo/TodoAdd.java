package com.thedimzone.crosstodo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import android.util.Log;

public class TodoAdd extends Activity {
    private String projectName;
    private ArrayList<Integer> projectIds;
    private Integer projectId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo_add);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );


        Bundle b = this.getIntent().getExtras();
        ArrayList<String> titlesArray = b.getStringArrayList("titles");
        projectIds = b.getIntegerArrayList("ids");

        if(projectIds.size() == 0) {
            projectName = "Нет проектов";
            projectId = 0;
        } else {
            projectName = titlesArray.get(0);
            projectId = projectIds.get(0);
        }
        ((TextView) findViewById(R.id.categoryProject)).setText(projectName);

        ListView listView = (ListView) findViewById(R.id.projectsList);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.projectlist_cell, titlesArray);

        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View itemClicked, int position,
                                    long id) {
                projectId = projectIds.get(position);
                projectName = (((TextView) itemClicked).getText().toString());
                ((TextView) findViewById(R.id.categoryProject)).setText(projectName);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //case R.id.cancel:
             //   finish();
            //    return true;
            case R.id.add:
                EditText editText = (EditText) findViewById(R.id.editText);
                String text = String.valueOf(editText.getText());

                JsonObject params = new JsonObject();
                params.addProperty("text", text);
                params.addProperty("project_id", projectId);

                Ion.with(this)
                        .load(getString(R.string.requestCreate))
                        .setHeader("Content-Type", "application/json")
                        .setJsonObjectBody(params)
                        .asJsonObject()
                        .setCallback(new FutureCallback<JsonObject>() {
                            @Override
                            public void onCompleted(Exception e, JsonObject result) {
                                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                                startActivity(intent);
                                // do stuff with the result or error
                            }});
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
