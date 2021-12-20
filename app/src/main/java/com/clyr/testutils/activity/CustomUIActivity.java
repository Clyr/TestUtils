package com.clyr.testutils.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.clyr.testutils.R;
import com.clyr.testutils.base.BaseActivity;
import com.clyr.view.UnReadView;

public class CustomUIActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_uiactivity);
    }

    @Override
    protected void initView() {
        initBar();
        UnReadView unReadView = findViewById(R.id.unreadview);
        EditText editText = findViewById(R.id.edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                unReadView.setText(s.toString());
            }
        });
    }
}