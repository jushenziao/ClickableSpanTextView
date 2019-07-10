package com.aicc.myapplication;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ColorInt;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.text);
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("这是一个提供ClickSpan点击和TextView整体点击不一样事件的TextView");
        spannableStringBuilder.setSpan(new LinkClickSpan(getResources().getColor(R.color.colorAccent),
                this), 6, 11, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        textView.setText(spannableStringBuilder);
    }

    @Override
    public void onClick(View view) {
        Toast.makeText(this,"click span is clicked",Toast.LENGTH_SHORT).show();
    }

    public static class LinkClickSpan extends ClickableSpan {
        @ColorInt
        int color;

        @Nullable
        View.OnClickListener onClickListener;

        public LinkClickSpan(@ColorInt int color, @Nullable View.OnClickListener onClickListener) {
            super();
            this.color = color;
            this.onClickListener = onClickListener;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            ds.setColor(color);
        }

        @Override
        public void onClick(View widget) {
            if (onClickListener != null) {
                onClickListener.onClick(widget);
            }
        }
    }
}
