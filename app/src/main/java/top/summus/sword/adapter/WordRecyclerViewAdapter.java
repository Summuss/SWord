package top.summus.sword.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.white.progressview.HorizontalProgressView;

import top.summus.sword.R;
import top.summus.sword.room.entity.Word;

import java.util.List;


public class WordRecyclerViewAdapter extends RecyclerView.Adapter<WordRecyclerViewAdapter.ViewHolder> {

    private final List<Word> mValues;

    public WordRecyclerViewAdapter(List<Word> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_word_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Word word = mValues.get(position);
        holder.contentTV.setText(word.getContent());
        holder.prounTV.setText(word.getPronunciation());
        holder.proficiencyProgress.setProgress(word.getProficiency());
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
        TextView prounTV;
        HorizontalProgressView proficiencyProgress;

        public ViewHolder(View view) {
            super(view);
            contentTV = view.findViewById(R.id.tv_word_content);
            prounTV = view.findViewById(R.id.tv_word_pronun);
            proficiencyProgress = view.findViewById(R.id.progress_proficiency);
        }

    }
}
