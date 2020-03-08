package top.summus.sword.component;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import top.summus.sword.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class TextInputTreeNode extends TreeNode {
    public static final int MEANING = 1;
    public static final int SENTENCE = 2;
    public static final int SENTENCE_INTERPRETATION = 3;

    private int type;
    private AndroidTreeView treeView;
    private ViewHolder viewHolder;

    public TextInputTreeNode(int type, AndroidTreeView treeView, Context context) {
        super(new Object());
        this.type = type;
        this.treeView = treeView;
        this.viewHolder = new ViewHolder(context);
        setViewHolder(viewHolder);
    }

    private String getHint() {
        switch (type) {
            case MEANING:
                return "解释";
            case SENTENCE:
                return "例句";
            case SENTENCE_INTERPRETATION:
                return "例句翻译";
            default:
                return "null";
        }
    }


    private int getMarginStart() {
        switch (type) {
            case MEANING:
                return 32;
            case SENTENCE:
            case SENTENCE_INTERPRETATION:
                return 64;
            default:
                return 0;
        }
    }

    @Setter
    @Getter
    public class ViewHolder extends TreeNode.BaseNodeViewHolder<Object> {

        private ConstraintLayout outLayout;
        private ImageView arrowImage;
        private ImageView addImage;
        private TextInputLayout inputLayout;
        private TextInputEditText editText;

        public ViewHolder(Context context) {
            super(context);
        }

        @Override
        public View createNodeView(TreeNode node, Object value) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.tree_node_input, null, false);
            arrowImage = view.findViewById(R.id.arrow_image);
            addImage = view.findViewById(R.id.addImage);
            inputLayout = view.findViewById(R.id.textInputLayout);
            editText = view.findViewById(R.id.textInputEditText);
            outLayout = view.findViewById(R.id.out_layout);


            switch (type) {
                case MEANING:
                    inputLayout.setHint("解释");
                    editText.setTextSize(19);
                    break;
                case SENTENCE:
                    inputLayout.setHint("例句");
                    editText.setTextSize(14);
                    break;
                case SENTENCE_INTERPRETATION:
                    inputLayout.setHint("翻译");
                    editText.setTextSize(14);
                    addImage.setVisibility(View.INVISIBLE);
                    arrowImage.setVisibility(View.INVISIBLE);
                    break;
                default:

            }


            arrowImage.setOnClickListener(view1 -> {
                if (isExpanded()) {
                    treeView.collapseNode(node);
                } else {
                    treeView.expandNode(node);
                }
            });

            if (getChildren().isEmpty()) {
                arrowImage.setVisibility(View.INVISIBLE);
            } else {
                arrowImage.setVisibility(View.VISIBLE);
            }

            addImage.setOnClickListener(view1 -> {
                if (type == SENTENCE) {
                    if (getChildren().isEmpty()) {
                        addImage.setVisibility(View.INVISIBLE);
                        Log.i("testtt", "createNodeView: ");
                    }
                }
                TextInputTreeNode treeNode = null;
                switch (type) {
                    case MEANING:
                        treeNode = new TextInputTreeNode(SENTENCE, treeView, context);
                        break;
                    case SENTENCE:
                        treeNode = new TextInputTreeNode(SENTENCE_INTERPRETATION, treeView, context);
                        break;
                    default:
                        throw new UnsupportedOperationException();
                }
                node.addChild(treeNode);
                if (arrowImage.getVisibility() == View.INVISIBLE) {
                    arrowImage.setVisibility(View.VISIBLE);
                }
                treeView.expandNode(node);
            });


            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            lp.setMarginStart(getMarginStart());
            outLayout.setLayoutParams(lp);


            ViewGroup.LayoutParams rootParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            view.setLayoutParams(rootParams);  ///////*********set layout params to your view

            return view;
        }
    }
}
