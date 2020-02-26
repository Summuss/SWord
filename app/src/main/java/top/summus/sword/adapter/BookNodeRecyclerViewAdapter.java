package top.summus.sword.adapter;

import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import lombok.Getter;
import top.summus.sword.R;
import top.summus.sword.entity.BookNode;

import java.util.List;

import static top.summus.sword.SWordApplication.getContext;


public class BookNodeRecyclerViewAdapter extends RecyclerView.Adapter<BookNodeRecyclerViewAdapter.ViewHolder> {

    @Getter
    private final List<BookNode> bookNodeList;
    private final OnBookNodeItemClicked clickedCallback;
//    private final OnListFragmentInteractionListener mListener;

    public BookNodeRecyclerViewAdapter(OnBookNodeItemClicked clickedCallback, List<BookNode> items) {
        bookNodeList = items;
        this.clickedCallback = clickedCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_booknode_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        BookNode bookNode = bookNodeList.get(position);
        holder.folderNameView.setText(bookNode.getNodeName());
        if (bookNode.getNodeTag() == 0) {
            Glide.with(getContext()).load(R.drawable.ic_folder_black_24dp).into(holder.iconView);
        } else {
            Glide.with(getContext()).load(R.drawable.ic_page).into(holder.iconView);
        }

    }

    @Override
    public int getItemCount() {
        return bookNodeList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        final TextView folderNameView;
        final ImageView iconView;
        final LinearLayout outLayout;


        public ViewHolder(View view) {
            super(view);
            folderNameView = (TextView) view.findViewById(R.id.node_name_view);
            iconView = (ImageView) view.findViewById(R.id.node_icon_view);
            outLayout = (LinearLayout) view.findViewById(R.id.book_node_outer_layout);
            outLayout.setOnClickListener(this);

        }


        @Override
        public void onClick(View v) {

            if (v.getId() == R.id.book_node_outer_layout) {
                clickedCallback.onBookNodeItemClicked(getAdapterPosition(), bookNodeList.get(getAdapterPosition()));
            }

        }
    }

    public interface OnBookNodeItemClicked {
        void onBookNodeItemClicked(int position, BookNode target);
    }
}
