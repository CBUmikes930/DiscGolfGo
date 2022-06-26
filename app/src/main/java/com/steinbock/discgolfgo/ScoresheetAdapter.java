package com.steinbock.discgolfgo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ScoresheetAdapter extends RecyclerView.Adapter<ScoresheetAdapter.ScoresheetViewHolder> {

    List<ScoresheetModel> mList;

    public ScoresheetAdapter(List<ScoresheetModel> mList) {
        this.mList = mList;
    }

    @NonNull
    @Override
    public ScoresheetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scoresheet_row, parent, false);
        return new ScoresheetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoresheetViewHolder holder, int position) {
        ScoresheetModel model = mList.get(position);
        holder.nameField.setText(model.getName());
        for (int i = 0; i < model.getScores().length; i++) {
            holder.scoreFields[i].setText(String.valueOf(model.getScore(i)));
        }
        holder.totalField.setText(String.valueOf(model.getTotal()));
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class ScoresheetViewHolder extends RecyclerView.ViewHolder {

        private TextView nameField;
        private EditText[] scoreFields;
        private TextView totalField;

        public ScoresheetViewHolder(@NonNull View scoresheetView) {
            super(scoresheetView);

            nameField = scoresheetView.findViewById(R.id.scoresheet_name);

            scoreFields = new EditText[9];
            for (int i = 1; i <= 9; i++) {
                int id = scoresheetView.getResources().getIdentifier("scoresheet_hole" + i,
                        "id", scoresheetView.getContext().getPackageName());
                System.out.println(id);
                scoreFields[i - 1] = scoresheetView.findViewById(id);
            }
            totalField = scoresheetView.findViewById(R.id.scoresheet_total);
        }
    }
}
