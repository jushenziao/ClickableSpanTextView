package com.aicc.myapplication;

import android.content.Context;
import android.text.Layout;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatTextView;

public class ClickableSpanTextView extends AppCompatTextView {
    private BackgroundColorSpan backgroundColorSpan;
    private boolean hasSpan;

    public ClickableSpanTextView(Context context) {
        super(context);
        init();
    }

    public ClickableSpanTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ClickableSpanTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setMovementMethod(LinkMovementMethod.getInstance());
        backgroundColorSpan = new BackgroundColorSpan(getContext().getResources().getColor(R.color.colorPrimary));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        boolean handled = super.onTouchEvent(event);
        int action = event.getAction();

        if (!(getText() instanceof Spannable)) {
            return handled;
        }

        Spannable spannable = (Spannable) getText();
        if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            x -= getTotalPaddingLeft();
            y -= getTotalPaddingTop();
            x += getScrollX();
            y += getScrollY();
            Layout layout = getLayout();
            int line = layout.getLineForVertical(y);
            int off = layout.getOffsetForHorizontal(line, x);
            if (off >= getText().length()) {
                int off1 = layout.getOffsetForHorizontal(line, x - getTextSize());
                if (off == off1) {
                    return handled;
                }
            }

            ClickableSpan[] links = spannable.getSpans(off, off, ClickableSpan.class);
            if (links.length > 0) {
                ClickableSpan clickableSpan = links[0];
                int start = spannable.getSpanStart(clickableSpan);
                int end = spannable.getSpanEnd(clickableSpan);

                if (action == MotionEvent.ACTION_DOWN && !hasSpan) {
                    spannable.setSpan(backgroundColorSpan, start, end, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                    hasSpan = true;
                } else if (hasSpan) {
                    spannable.removeSpan(backgroundColorSpan);
                    hasSpan = false;
                }
            }
            return links.length != 0;
        } else {
            if (hasSpan && action != MotionEvent.ACTION_MOVE) {
                spannable.removeSpan(backgroundColorSpan);
                hasSpan = false;
            }
        }
        return handled;
    }
}
