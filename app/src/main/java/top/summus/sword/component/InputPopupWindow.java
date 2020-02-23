package top.summus.sword.component;

import android.app.ActionBar;
import android.app.Activity;
import android.view.*;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.textfield.TextInputLayout;

import top.summus.sword.R;

import java.util.Objects;

public class InputPopupWindow extends PopupWindow {
    private Activity parentActivity;
    private View contentView;
    private InputPopupConfirmListener confirmCallback;

    public InputPopupWindow(Activity parentActivity) {
        this.parentActivity = parentActivity;
        contentView = LayoutInflater.from(parentActivity).inflate(R.layout.popup_single_input, null);
        setContentView(contentView);
        setWidth(ActionBar.LayoutParams.WRAP_CONTENT);
        setHeight(ActionBar.LayoutParams.WRAP_CONTENT);
        initDefaultListener();
        setOutsideTouchable(true);
        setFocusable(true);

    }

    public void initDefaultListener() {
        TextView cancelBtn = contentView.findViewById(R.id.popup_single_input_cancel_bt);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        setOnDismissListener(() -> {
            Window window = parentActivity.getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            layoutParams.alpha = 1f;
            window.setAttributes(layoutParams);
        });

        final TextView confirmBtn = contentView.findViewById(R.id.popup_single_input_confirm_bt);
        confirmBtn.setOnClickListener(v -> confirmCallback.inputConfirm(v));

    }

    public void setConfirmBtnOnClickListener(InputPopupConfirmListener listener) {
        confirmCallback = listener;
    }


    public void setHint(String hint) {
        TextInputLayout textInputLayout = contentView.findViewById(R.id.popup_single_input_inputlayout);
        textInputLayout.setHint(hint);
    }

    public String getInputText() {
        EditText editText = contentView.findViewById(R.id.popup_single_input_editview);
        return Objects.requireNonNull(editText.getText()).toString();

    }


    public interface InputPopupConfirmListener {
        void inputConfirm(View v);
    }

    public void focusWindow() {
        Window window = parentActivity.getWindow();
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.alpha = 0.7f;
        window.setAttributes(layoutParams);
        window.addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

}
