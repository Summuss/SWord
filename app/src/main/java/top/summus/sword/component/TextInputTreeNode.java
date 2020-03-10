package top.summus.sword.component;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.unnamed.b.atv.model.TreeNode;
import com.unnamed.b.atv.view.AndroidTreeView;

import lombok.Getter;
import lombok.Setter;
import top.summus.sword.R;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;
import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

public class TextInputTreeNode extends TreeNode {
    public static final int MEANING = 1;
    public static final int SENTENCE = 2;
    public static final int SENTENCE_INTERPRETATION = 3;
    //
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
                return 64;
            case SENTENCE_INTERPRETATION:
                return 96;
            default:
                return 0;
        }
    }

    private int getTextSize() {
        switch (type) {
            case MEANING:
                return 19;
            case SENTENCE:
            case SENTENCE_INTERPRETATION:
                return 14;
            default:
                return 0;
        }
    }


    @Setter
    @Getter
    public class ViewHolder extends TreeNode.BaseNodeViewHolder<Object> {

        private ConstraintLayout outLayout;
        private FrameLayout arrowImage;
        private FrameLayout addImage;
        private FrameLayout removeImage;
        private TextInputLayout inputLayout;
        private TextInputEditText editText;

        private ViewHolder(Context context) {
            super(context);
            setClickListener((node, value) -> {
                if (isExpanded()) {
                    arrowRotateBack();
                } else {
                    arrowRotate();
                }
            });
        }

        @Override
        public View createNodeView(TreeNode node, Object value) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View view = inflater.inflate(R.layout.tree_node_input, null, false);
            findView(view);
            initViewState();
            arrowImage.setOnClickListener(view1 -> {
                if (isExpanded()) {
                    treeView.collapseNode(node);
                    arrowRotateBack();
                } else {
                    treeView.expandNode(node);
                    arrowRotate();
                }
            });
            removeImage.setOnClickListener(view1 -> {
                TreeNode parent = node.getParent();
                parent.deleteChild(node);
                treeView.collapseNode(parent);
                if (parent.getChildren().isEmpty()) {
                    if (parent instanceof TextInputTreeNode) {
                        TextInputTreeNode parent1 = (TextInputTreeNode) parent;
                        parent1.viewHolder.arrowRotateBack();
                        if (parent1.type == SENTENCE) {
                            parent1.viewHolder.addImage.setVisibility(View.VISIBLE);
                        }
                    }
                    if (parent instanceof WordClassInputTreeNode) {
                        ((WordClassInputTreeNode) parent).arrowRotateBack();
                    }
                } else {
                    treeView.expandNode(parent);
                }
            });

            addImage.setOnClickListener(view1 -> {
                if (type == SENTENCE) {
                    if (getChildren().isEmpty()) {
                        addImage.setVisibility(View.GONE);
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
                if (!isExpanded()) {
                    arrowRotate();
                }

                treeView.expandNode(node);
            });

            ViewGroup.LayoutParams rootParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            view.setLayoutParams(rootParams);  ///////*********set layout params to your view

            return view;
        }

        private void arrowRotate() {
            Animator animator = ObjectAnimator.ofFloat(arrowImage, "rotation", 0, 90);
            animator.setDuration(300);
            animator.start();
        }

        private void arrowRotateBack() {
            Animator animator = ObjectAnimator.ofFloat(arrowImage, "rotation", 90, 0);
            animator.setDuration(300);
            animator.start();
        }

        private void findView(View view) {
            arrowImage = view.findViewById(R.id.arrow_image);
            addImage = view.findViewById(R.id.addImage);
            removeImage = view.findViewById(R.id.remove_image);
            inputLayout = view.findViewById(R.id.textInputLayout);
            editText = view.findViewById(R.id.textInputEditText);
            outLayout = view.findViewById(R.id.out_layout);
        }

        private void initViewState() {
            inputLayout.setHint(getHint());
            editText.setTextSize(getTextSize());

            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(MATCH_PARENT, WRAP_CONTENT);
            lp.setMarginStart(getMarginStart());
            outLayout.setLayoutParams(lp);


            if (getChildren().isEmpty()) {
                arrowImage.setVisibility(View.INVISIBLE);
            } else {
                arrowImage.setVisibility(View.VISIBLE);
            }

            if (type == SENTENCE_INTERPRETATION) {
                addImage.setVisibility(View.GONE);
                arrowImage.setVisibility(View.INVISIBLE);
            }
        }
    }
}
