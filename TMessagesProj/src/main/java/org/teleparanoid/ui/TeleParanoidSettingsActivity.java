/*
 * This is the source code of Telegram for Android v. 5.x.x.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 *
 * Copyright Nikolai Kudashov, 2013-2018.
 */

package org.teleparanoid.ui;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Components.VerticalPositionAutoAnimator;

public class TeleParanoidSettingsActivity extends BaseFragment implements View.OnClickListener {

    public TeleParanoidSettingsActivity() {
        super();
    }

    public TeleParanoidSettingsActivity(Bundle args) {
        super(args);
    }

    @Override
    public boolean onFragmentCreate() {
        return super.onFragmentCreate();
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle("My Custom string :3");

        fragmentView = new ViewGroup(context) {

            private VerticalPositionAutoAnimator verticalPositionAutoAnimator;

//            @Override
//            public void onViewAdded(View child) {
//                if (child == floatingButton && verticalPositionAutoAnimator == null) {
//                    verticalPositionAutoAnimator = VerticalPositionAutoAnimator.attach(child);
//                }
//            }

//            @Override
//            protected void onAttachedToWindow() {
//                super.onAttachedToWindow();
//                if (verticalPositionAutoAnimator != null) {
//                    verticalPositionAutoAnimator.ignoreNextLayout();
//                }
//            }

//            @Override
//            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//                int width = MeasureSpec.getSize(widthMeasureSpec);
//                int height = MeasureSpec.getSize(heightMeasureSpec);
//                setMeasuredDimension(width, height);
//                if (AndroidUtilities.isTablet() || height > width) {
//                    maxSize = AndroidUtilities.dp(144);
//                } else {
//                    maxSize = AndroidUtilities.dp(56);
//                }
//
//                scrollView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(maxSize, MeasureSpec.AT_MOST));
//                listView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height - scrollView.getMeasuredHeight(), MeasureSpec.EXACTLY));
//                emptyView.measure(MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(height - scrollView.getMeasuredHeight(), MeasureSpec.EXACTLY));
//                if (floatingButton != null) {
//                    int w = AndroidUtilities.dp(Build.VERSION.SDK_INT >= 21 ? 56 : 60);
//                    floatingButton.measure(MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY), MeasureSpec.makeMeasureSpec(w, MeasureSpec.EXACTLY));
//                }
//            }

            @Override
            protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
//                scrollView.layout(0, 0, scrollView.getMeasuredWidth(), scrollView.getMeasuredHeight());
//                listView.layout(0, scrollView.getMeasuredHeight(), listView.getMeasuredWidth(), scrollView.getMeasuredHeight() + listView.getMeasuredHeight());
//                emptyView.layout(0, scrollView.getMeasuredHeight(), emptyView.getMeasuredWidth(), scrollView.getMeasuredHeight() + emptyView.getMeasuredHeight());

//                if (floatingButton != null) {
//                    int l = LocaleController.isRTL ? AndroidUtilities.dp(14) : (right - left) - AndroidUtilities.dp(14) - floatingButton.getMeasuredWidth();
//                    int t = bottom - top - AndroidUtilities.dp(14) - floatingButton.getMeasuredHeight();
//                    floatingButton.layout(l, t, l + floatingButton.getMeasuredWidth(), t + floatingButton.getMeasuredHeight());
//                }
            }

//            @Override
//            protected void dispatchDraw(Canvas canvas) {
//                super.dispatchDraw(canvas);
//                parentLayout.drawHeaderShadow(canvas, Math.min(maxSize, measuredContainerHeight + containerHeight - measuredContainerHeight));
//            }

//            @Override
//            protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
//                if (child == listView) {
//                    canvas.save();
//                    canvas.clipRect(child.getLeft(), Math.min(maxSize, measuredContainerHeight + containerHeight - measuredContainerHeight), child.getRight(), child.getBottom());
//                    boolean result = super.drawChild(canvas, child, drawingTime);
//                    canvas.restore();
//                    return result;
//                } else if (child == scrollView) {
//                    canvas.save();
//                    canvas.clipRect(child.getLeft(), child.getTop(), child.getRight(), Math.min(maxSize, measuredContainerHeight + containerHeight - measuredContainerHeight));
//                    boolean result = super.drawChild(canvas, child, drawingTime);
//                    canvas.restore();
//                    return result;
//                } else {
//                    return super.drawChild(canvas, child, drawingTime);
//                }
//            }
        };

        return fragmentView;
    }

}
