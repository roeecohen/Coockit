package com.example.coockit.Search;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coockit.R;

import java.util.ArrayList;
import java.util.List;

public class IngredientsOptionsAdapter extends RecyclerView.Adapter<IngredientsOptionsAdapter.IngOptionsViewHolder> {

    private Context mContext;
    private int checkOptionsCounter;
    private static ArrayList<String> mCheckBoxMarkedOptions;

    public IngredientsOptionsAdapter(Context context,View view) {
        checkOptionsCounter=0;
        mContext = context;
        mCheckBoxMarkedOptions = new ArrayList<String>();
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
        List<String> optionsTemp = new ArrayList<String>();
        optionsTemp.addAll(Results.getmCheckBoxOptions());
        holder.checkBox.setText(optionsTemp.get(position));

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,boolean isChecked) {
                if(buttonView.isChecked()) {
                    ++checkOptionsCounter;
                    if(checkOptionsCounter>0)
                        SearchFragment.getmCheckAroundBtn().setVisibility(View.VISIBLE);

                    buttonView.setBackgroundResource(R.drawable.rounded_button);
                    buttonView.setTextColor(Color.parseColor("#FFFFFF"));
                    mCheckBoxMarkedOptions.add(buttonView.getText().toString().trim());
                }
                else {
                    --checkOptionsCounter;
                    if(checkOptionsCounter==0)
                        SearchFragment.getmCheckAroundBtn().setVisibility(View.GONE);

                    buttonView.setBackgroundResource(R.drawable.rounded_button_gray);
                    buttonView.setTextColor(Color.parseColor("#6A6A6A"));
                    mCheckBoxMarkedOptions.remove(buttonView.getText().toString().trim());
                }
            }
        }
        );
    }

    @Override
    public int getItemCount() {
        return Results.getmCheckBoxOptions().size();
    }

    public class IngOptionsViewHolder extends RecyclerView.ViewHolder {
        private CheckBox checkBox;

        public IngOptionsViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = (CheckBox) itemView.findViewById(R.id.ingredient_check_box);

        }
    }

    public static ArrayList<String> getmCheckBoxMarkedOptions() {
        return mCheckBoxMarkedOptions;
    }
}



