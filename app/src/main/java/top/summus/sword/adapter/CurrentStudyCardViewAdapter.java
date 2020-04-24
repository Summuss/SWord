package top.summus.sword.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.eugeneek.smilebar.SmileBar;
import com.willy.ratingbar.RotationRatingBar;

import java.util.List;

import lombok.Getter;
import lombok.Setter;
import top.summus.sword.R;
import top.summus.sword.room.entity.Word;

public class CurrentStudyCardViewAdapter extends RecyclerView.Adapter<CurrentStudyCardViewAdapter.ViewHolder> {

    @Getter
    private final List<Word> mValues;

    @Setter
    private CurrentStudyCardViewAdapter.OnCurrentStudyCardViewAdapterCallback callback;

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
        holder.contentTV.setText(word.getContent());
        holder.phoneticTV.setText(word.getPronunciation());
        holder.priorityBar.setRating(word.getPriority());
        holder.difficultyBar.setRating(word.getDifficulty());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView contentTV;
        TextView phoneticTV;
        SmileBar difficultyBar;
        RotationRatingBar priorityBar;
        ImageView toDetailBt;
        CardView outLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            contentTV = itemView.findViewById(R.id.content_tv);
            phoneticTV = itemView.findViewById(R.id.phonetic_tv);
            difficultyBar = itemView.findViewById(R.id.diffulty_rating);
            priorityBar = itemView.findViewById(R.id.priority_rating);
            toDetailBt = itemView.findViewById(R.id.to_detail_bt);
            outLayout = itemView.findViewById(R.id.out_layout);

            outLayout.setOnClickListener(view -> {
                phoneticTV.setVisibility(View.VISIBLE);
            });
            toDetailBt.setOnClickListener(view -> {
                Word word = mValues.get(getAdapterPosition());
                callback.toDetail(word);

            });
        }

    }

    public interface OnCurrentStudyCardViewAdapterCallback {

        void toDetail(Word word);
    }
}
