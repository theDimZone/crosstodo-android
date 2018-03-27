package com.thedimzone.crosstodo;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.CompoundButton;
import com.google.gson.JsonObject;
import com.koushikdutta.ion.Ion;
import android.graphics.Paint;

class CustomAdapter extends BaseAdapter {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_SEPARATOR = 1;
    private Context contextParent;

    private List<Todo> TodoList = new ArrayList<>();
    private TreeSet<Integer> sectionHeader = new TreeSet<Integer>();

    private LayoutInflater mInflater;

    public CustomAdapter(Context context) {
        contextParent = context;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void addItem(final String item, Boolean status, Integer id) {
        Todo temp_todo = new Todo();
        temp_todo.setText(item);
        temp_todo.setId(id);
        temp_todo.setCompleted(status);
        TodoList.add(temp_todo);
        notifyDataSetChanged();
    }

    public void addSectionHeaderItem(final String item) {
        //mData.add(item);
        Todo temp_todo = new Todo();
        temp_todo.setText(item);
        temp_todo.setId(-1);
        temp_todo.setCompleted(false);
        TodoList.add(temp_todo);
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
    }

    @Override
    public Todo getItem(int position) {
        return TodoList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        int rowType = getItemViewType(position);

        if (convertView == null) {
            holder = new ViewHolder();
            switch (rowType) {
                case TYPE_ITEM:
                    convertView = mInflater.inflate(R.layout.snippet_item1, null);
                    holder.textView = (TextView) convertView.findViewById(R.id.text);
                    holder.checkBox = (CheckBox) convertView.findViewById(R.id.check);
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

        holder.textView.setText(TodoList.get(position).getText());
        if(rowType == TYPE_ITEM) {
            if(TodoList.get(position).isCompleted()) {
                holder.textView.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            } else  {
                holder.textView.setPaintFlags(Paint.ANTI_ALIAS_FLAG);
            }

            holder.checkBox.setChecked(TodoList.get(position).isCompleted());
            holder.checkBox.setTag(holder.textView);
            holder.textView.setTag(holder.checkBox);


            holder.checkBox.setOnClickListener(new CheckBox.OnClickListener(){

                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox)v).isChecked();
                    TodoList.get(position).changeCompleted();

                    if(checked) {
                        ((TextView) v.getTag()).setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        ((TextView) v.getTag()).setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                    }
                    JsonObject params = new JsonObject();
                    params.addProperty("todo_id", TodoList.get(position).getId());

                    Ion.with(contextParent)
                            .load(contextParent.getString(R.string.requestUpdate))
                            .setHeader("Content-Type", "application/json")
                            .setJsonObjectBody(params)
                            .asJsonObject();
                }

            });

            holder.textView.setOnClickListener(new CheckBox.OnClickListener(){

                @Override
                public void onClick(View v) {
                    TodoList.get(position).changeCompleted();

                    //boolean isChecked = ((CheckBox) v.findViewById(R.id.check)).isChecked();
                    //((CheckBox) v.findViewById(R.id.check)).setChecked(!isChecked);
                    boolean isChecked = ((CheckBox) v.getTag()).isChecked();
                    ((CheckBox) v.getTag()).setChecked(!isChecked);

                    if(((CheckBox) v.getTag()).isChecked()) {
                        ((TextView) v).setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
                    } else {
                        ((TextView) v).setPaintFlags(Paint.ANTI_ALIAS_FLAG);
                    }


                    JsonObject params = new JsonObject();
                    //params.addProperty("todo_id", ((Todo) v.getTag()).getId());
                    params.addProperty("todo_id", TodoList.get(position).getId());

                    Ion.with(contextParent)
                            .load(contextParent.getString(R.string.requestUpdate))
                            .setHeader("Content-Type", "application/json")
                            .setJsonObjectBody(params)
                            .asJsonObject();
                }

            });

        }

        return convertView;
    }

    public static class ViewHolder {
        public TextView textView;
        public CheckBox checkBox;
    }

}