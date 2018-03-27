package com.thedimzone.crosstodo;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.ListView;
import android.view.View;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;
import android.content.Intent;
import com.google.gson.Gson;
import java.util.ArrayList;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;


public class MainActivity extends Activity {
    private CustomAdapter mAdapter;

    public class JsonTodo{

        Integer id;
        String text;
        Integer project_id;
        Boolean isCompleted;

    }

    public class JsonProject{

        String title;
        Integer id;

    }

    //для передачи в другой активити
    private ArrayList<String> projectTitles = new ArrayList<String>();
    private ArrayList<Integer> projectIds = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/OpenSans-Light.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        mAdapter = new CustomAdapter(this);

        Ion.with(this)
                .load(getString(R.string.requestIndex))
                .setHeader("Accept", "application/json")
                .asJsonObject()
                .setCallback(new FutureCallback<JsonObject>() {
                    @Override
                    public void onCompleted(Exception e, JsonObject result) {
                        if(result != null) {
                            JsonProject[] project_list;
                            project_list = new Gson().fromJson(result.getAsJsonArray("projects"), JsonProject[].class);

                            JsonTodo[] todo_list;
                            todo_list = new Gson().fromJson(result.getAsJsonArray("todo"), JsonTodo[].class);

                            for(JsonProject project : project_list) {
                                projectTitles.add(project.title);
                                projectIds.add(project.id);

                                mAdapter.addSectionHeaderItem(project.title);
                                for(JsonTodo todo : todo_list) {
                                    if(project.id.equals(todo.project_id)) {
                                        mAdapter.addItem(todo.text, todo.isCompleted, todo.id);
                                    }
                                }

                            }

                            ListView view = (ListView) findViewById(R.id.list);
                            view.setAdapter(mAdapter);

                        }
                        // do stuff with the result or error
                    }
                });


    }

    public void onAddClick(View v) {
        Bundle b = new Bundle();
        b.putStringArrayList("titles", projectTitles);
        b.putIntegerArrayList("ids", projectIds);
        Intent i = new Intent(this, TodoAdd.class);
        i.putExtras(b);
        this.startActivity(i);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}