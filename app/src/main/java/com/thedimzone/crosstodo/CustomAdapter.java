package com.thedimzone.crosstodo;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.CompoundButton;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

class CustomAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private Context contextParent;

    //private ArrayList<String> mData = new ArrayList<String>();
    private List<Todo> TodoList = new ArrayList<>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public CustomAdapter(Context context) {
        contextParent = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String item, Boolean status, Integer id) {
        //mData.add(item);

        Todo temp_todo = new Todo();
        temp_todo.setText(item);
        temp_todo.setId(id);
        temp_todo.setCompleted(status);
        TodoList.add(temp_todo);
        //mIds.add(id);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        //mData.add(item);
        Todo temp_todo = new Todo();
        temp_todo.setText(item);
        temp_todo.setId(-1);
        temp_todo.setCompleted(false);
        TodoList.add(temp_todo);
        //sectionHeader.add(mData.size() - 1);
        sectionHeader.add(TodoList.size() - 1);
        notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        return sectionHeader.contains(position) ? TYPE_SEPARATOR : TYPE_ITEM;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getCount() {
        return TodoList.size();
        //return mData.size();
    }

    @Override
    public String getItem(int position) {
        return TodoList.get(position).getText();
        //return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.snippet_item1, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                    holder.checkBox = (CheckBox) convertView.findViewById(R.id.check);
                    if(TodoList.get(position).isCompleted()) {
                        holder.checkBox.setChecked(true);
                    }
                    holder.checkBox.setTag(position);
                    holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            Integer todoInFocus = (Integer) buttonView.getTag();
                            if (TodoList.get(todoInFocus).isCompleted() == isChecked) return;

                            JsonObject params = new JsonObject();
                            params.addProperty("todo_id", TodoList.get(todoInFocus).getId());
                            //update

                            Ion.with(contextParent)
                                    .load(contextParent.getString(R.string.requestUpdate))
                                    .setHeader("Content-Type", "application/json")
                                    .setJsonObjectBody(params)
                                    .asJsonObject()
                                    .setCallback(new FutureCallback<JsonObject>() {
                                        @Override
                                        public void onCompleted(Exception e, JsonObject result) {
                                            // do stuff with the result or error
                                        }});

                        }
                    });
                    break;
                case TYPE_SEPARATOR:
                    convertView = mInflater.inflate(R.layout.snippet_item2, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.textSeparator);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        //holder.textView.setText(mData.get(position));
        holder.textView.setText(TodoList.get(position).getText());

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
        public CheckBox checkBox;
    }

}