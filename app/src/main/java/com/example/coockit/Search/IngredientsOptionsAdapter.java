package com.example.coockit.Search;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coockit.R;

import java.util.ArrayList;

public class IngredientsOptionsAdapter extends RecyclerView.Adapter<IngredientsOptionsAdapter.IngOptionsViewHolder> {

    private Context mContext;
    private Activity mActivity;
    private static ArrayList<String> mCheckBoxOptions;

    public IngredientsOptionsAdapter(Context context, Activity act) {
        mContext = context;
        mActivity = act;
        mCheckBoxOptions = new ArrayList<String>();
        mCheckBoxOptions.add("cvc");
        mCheckBoxOptions.add("cvc2");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
        mCheckBoxOptions.add("cvc3");
    }

    @NonNull
    @Override
    public IngOptionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.search_ingredient_option_item, parent, false);
        return new IngOptionsViewHolder(view);
    }



    @Override
    public void onBindViewHolder(@NonNull IngOptionsViewHolder holder, final int position) {
        holder.checkBox.setText(mCheckBoxOptions.get(position));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(buttonView.isChecked()) {

                    buttonView.setBackgroundResource(R.drawable.rounded_button);
                    buttonView.setTextColor(Color.parseColor("#FFFFFF"));
                    mCheckBoxOptions.add(buttonView.getText().toString());
                }
                else {
                    buttonView.setBackgroundResource(R.drawable.rounded_button_gray);
                    buttonView.setTextColor(Color.parseColor("#6A6A6A"));

                }

            }
        }
        );

        if(holder.checkBox.isChecked()) {
            holder.checkBox.setBackgroundColor(Color.parseColor("#FEFEFE"));
            mCheckBoxOptions.add(holder.checkBox.getText().toString());
        }

       // checkBoxTemp.setText("xczx");
    }

    @Override
    public int getItemCount() {
        return mCheckBoxOptions.size();
    }

    public class IngOptionsViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;

        public IngOptionsViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.ingredient_check_box);

        }
    }

    public static ArrayList<String> getmCheckBoxOptions() {
        return mCheckBoxOptions;
    }
}



