package top.summus.sword.component;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.textfield.TextInputEditText;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.List;

import es.dmoral.toasty.Toasty;
import lombok.Getter;
import top.summus.sword.R;

public class WordClassInputTreeNode extends TreeNode {
    private List<String> items = Arrays.asList("动词", "名词", "形容词");
    private AndroidTreeView treeView;
    @Getter
    private ViewHolder viewHolder;

    public WordClassInputTreeNode(Context context, AndroidTreeView treeView) {
        super(new Object());
        this.treeView = treeView;
        viewHolder = new ViewHolder(context);
        setViewHolder(viewHolder);

    }

    public int spinerSelectedIndex() {
        return viewHolder.niceSpinner.getSelectedIndex();
    }


    public class ViewHolder extends TreeNode.BaseNodeViewHolder<Object> {
        private NiceSpinner niceSpinner;
        private ImageView addIamge;
        private ImageView arrowImage;

        public ViewHolder(Context context) {
            super(context);
            setClickListener((node, value) -> {
                Toasty.info(context, "click", Toasty.LENGTH_SHORT).show();
            });
        }


        @Override
        public View createNodeView(TreeNode node, Object value) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.tree_node_word_class, null, false);
            niceSpinner = view.findViewById(R.id.niceSpinner);
            addIamge = view.findViewById(R.id.add_image);
            arrowImage = view.findViewById(R.id.arrow_image);
            niceSpinner.attachDataSource(WordClassInputTreeNode.this.items);

            if (getChildren().isEmpty()) {
                arrowImage.setVisibility(View.GONE);
            }

            arrowImage.setOnClickListener(view1 -> {
                if (isExpanded()) {
                    treeView.collapseNode(node);
                } else {
                    treeView.expandNode(node);
                }
            });

            addIamge.setOnClickListener(view1 -> {
                TextInputTreeNode treeNode = new TextInputTreeNode(TextInputTreeNode.MEANING, treeView, context);
                node.addChild(treeNode);
                if (arrowImage.getVisibility() != View.VISIBLE) {
                    arrowImage.setVisibility(View.VISIBLE);
                }
                treeView.expandNode(node);
            });

            ViewGroup.LayoutParams rootParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            view.setLayoutParams(rootParams);  ///////*********set layout params to your view

            return view;
        }
    }
}
