package top.summus.sword.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import lombok.Getter;
import top.summus.sword.R;
import top.summus.sword.room.entity.Word;

public class CurrentStudyCardViewAdapter extends RecyclerView.Adapter<CurrentStudyCardViewAdapter.ViewHolder> {

    @Getter
    private final List<Word> mValues;

    public CurrentStudyCardViewAdapter(List<Word> words) {
        mValues = words;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.current_study_word_card_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Word word = mValues.get(position);
        holder.textView.setText(word.getContent());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.word_tx);
        }
    }
}
