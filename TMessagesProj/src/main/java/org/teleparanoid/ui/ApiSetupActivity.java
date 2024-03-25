package org.teleparanoid.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Build;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextWatcher;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.accessibility.AccessibilityNodeInfo;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.ActionBarMenu;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Components.BulletinFactory;
import org.telegram.ui.Components.EditTextBoldCursor;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.LinkSpanDrawable;
import org.telegram.ui.Components.OutlineTextContainerView;
import org.telegram.ui.Components.RLottieImageView;
import org.telegram.ui.Components.URLSpanNoUnderline;
import org.teleparanoid.TeleParanoidConfig;

import java.util.ArrayList;

public class ApiSetupActivity extends BaseFragment {

    private class PasswordRow {
        public EditTextBoldCursor editText;
        public OutlineTextContainerView outlineView;
        public boolean isInputVisible;

        public PasswordRow(Context context, String text, String contentDescription, OnPasswordEditorDone onEditorDone) {

            editText = createEditText(context, text, contentDescription, onEditorDone);
            ImageView showInputButton = createShowInputButton(context);
            LinearLayout linearLayout = createLinearLayout(context, editText, showInputButton);
            outlineView = createOutlineView(context, contentDescription, editText, linearLayout);
        }


        private LinearLayout createLinearLayout(Context context, EditTextBoldCursor editText, ImageView showInputButton) {
            LinearLayout linearLayout = new LinearLayout(context);
            linearLayout.setOrientation(LinearLayout.HORIZONTAL);

            linearLayout.addView(editText, LayoutHelper.createLinear(0, LayoutHelper.WRAP_CONTENT, 1f));
            linearLayout.addView(showInputButton, LayoutHelper.createLinear(24, 24, Gravity.CENTER_VERTICAL, 0, 0, 16, 0));

            return linearLayout;
        }


        private EditTextBoldCursor createEditText(Context context, String text, String contextDescription, OnPasswordEditorDone onEditorDone) {
            EditTextBoldCursor editText = new EditTextBoldCursor(context);
            editText.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18);
            editText.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteBlackText));
            editText.setHintTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteHintText));
            editText.setBackground(null);
            editText.setSingleLine(true);
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            editText.setTypeface(Typeface.DEFAULT);
            editText.setCursorColor(Theme.getColor(Theme.key_windowBackgroundWhiteInputFieldActivated));
            editText.setCursorWidth(1.5f);
            int padding = AndroidUtilities.dp(16);
            editText.setPadding(padding, padding, padding, padding);
            editText.setOnFocusChangeListener((v, hasFocus) -> outlineView.animateSelection(hasFocus ? 1 : 0));
            editText.setText(text);
            editText.setContentDescription(contextDescription);
            editText.setOnEditorActionListener((textView, i, keyEvent) -> {
                if (i == EditorInfo.IME_ACTION_NEXT || i == EditorInfo.IME_ACTION_DONE) {
                    onEditorDone.execute();
                    return true;
                }
                return false;
            });
            editText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

                @Override
                public void afterTextChanged(Editable s) {
                }
            });

            return editText;
        }


        private ImageView createShowInputButton(Context context) {
            ImageView showInputButton = new ImageView(context) {
                @Override
                public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo info) {
                    super.onInitializeAccessibilityNodeInfo(info);
                    info.setCheckable(true);
                    info.setChecked(editText.getTransformationMethod() == null);
                }
            };
            showInputButton.setImageResource(R.drawable.msg_message);
            showInputButton.setScaleType(ImageView.ScaleType.CENTER);
            showInputButton.setContentDescription(LocaleController.getString(R.string.Show));
            showInputButton.setBackground(Theme.createSelectorDrawable(Theme.getColor(Theme.key_listSelector)));
            showInputButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_messagePanelIcons), PorterDuff.Mode.MULTIPLY));
            AndroidUtilities.updateViewVisibilityAnimated(showInputButton, true, 0.1f, false);

            showInputButton.setOnClickListener(v -> {
                if (editText.getTransformationMethod() == null) {
                    isInputVisible = false;
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    showInputButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_messagePanelIcons), PorterDuff.Mode.MULTIPLY));
                } else {
                    isInputVisible = true;
                    editText.setTransformationMethod(null);
                    showInputButton.setColorFilter(new PorterDuffColorFilter(Theme.getColor(Theme.key_chat_messagePanelSend), PorterDuff.Mode.MULTIPLY));
                }
                editText.setSelection(editText.length());
            });

            return showInputButton;
        }


        private OutlineTextContainerView createOutlineView(Context context, String contentDescription, EditTextBoldCursor editText, LinearLayout linearLayout) {
            OutlineTextContainerView outlineView = new OutlineTextContainerView(context);
            outlineView.setText(contentDescription);
            outlineView.animateSelection(1f, false);
            outlineView.attachEditText(editText);
            outlineView.addView(linearLayout, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT));

            return outlineView;
        }
    }


    @FunctionalInterface
    private interface OnPasswordEditorDone {
        public void execute();
    }

    private View doneButton;
    private final static int done_button = 1;
    private ScrollView scrollView;

    private RLottieImageView lockImageView;

    private int apiId;

    private PasswordRow apiIdPasswordRow;
    ;
    private String apiHash;
    private PasswordRow apiHashPasswordRow;


    public ApiSetupActivity() {
        super();

        int apiId = 0;
        try {
            apiId = TeleParanoidConfig.getApiId();
        } catch (Throwable e) {
            FileLog.e(e);
        }

        String apiHash = "";
        try {
            apiHash = TeleParanoidConfig.getAppHash();
        } catch (Throwable e) {
            FileLog.e(e);
        }


        this.apiId = apiId;
        this.apiHash = apiHash;
    }

    public class LinkSpan extends ClickableSpan {

        private String url;

        public LinkSpan(String value) {
            url = value;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setUnderlineText(false);
        }

        @Override
        public void onClick(View widget) {
            try {
                android.content.ClipboardManager clipboard = (android.content.ClipboardManager) ApplicationLoader.applicationContext.getSystemService(Context.CLIPBOARD_SERVICE);
                android.content.ClipData clip = android.content.ClipData.newPlainText("label", url);
                clipboard.setPrimaryClip(clip);
                if (BulletinFactory.canShowBulletin(ApiSetupActivity.this)) {
                    BulletinFactory.createCopyLinkBulletin(ApiSetupActivity.this).show();
                }
            } catch (Exception e) {
                FileLog.e(e);
            }
        }
    }

    private View createTitleGuideView(Context context) {
        LinkSpanDrawable.LinksTextView linksTextView = new LinkSpanDrawable.LinksTextView(context);
        linksTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
        linksTextView.setTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteGrayText8));
        linksTextView.setGravity(LocaleController.isRTL ? Gravity.RIGHT : Gravity.LEFT);
        linksTextView.setLinkTextColor(Theme.getColor(Theme.key_windowBackgroundWhiteLinkText));
        linksTextView.setHighlightColor(Theme.getColor(Theme.key_windowBackgroundWhiteLinkSelection));
        linksTextView.setPadding(AndroidUtilities.dp(3), 0, AndroidUtilities.dp(3), 0);
        String str = "*" + LocaleController.getString(R.string.ObtainingApiIdGuide) + "*";
        SpannableStringBuilder text = new SpannableStringBuilder(str);
        int index1 = str.indexOf('*');
        int index2 = str.lastIndexOf('*');
        if (index1 != -1 && index2 != -1 && index1 != index2) {
            text.replace(index2, index2 + 1, "");
            text.replace(index1, index1 + 1, "");
            text.setSpan(new URLSpanNoUnderline(LocaleController.getString(R.string.ObtainingApiIdUrl)), index1, index2 - 1, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        linksTextView.setText(text);

        return linksTextView;
    }

    private View createLockImageView(Context context) {

        lockImageView = new RLottieImageView(context);
        lockImageView.setAnimation(R.raw.tsv_setup_intro, 120, 120);
        lockImageView.playAnimation();
        lockImageView.setVisibility(AndroidUtilities.isSmallScreen() || AndroidUtilities.displaySize.x > AndroidUtilities.displaySize.y ? View.GONE : View.VISIBLE);

        return lockImageView;
    }


    private void setupActionBar(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString(R.string.ChangeApiCredentials));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                } else if (id == done_button) {
                    saveChanges();
                }
            }
        });

        ActionBarMenu menu = actionBar.createMenu();
        doneButton = menu.addItemWithWidth(done_button, R.drawable.ic_ab_done, AndroidUtilities.dp(56), LocaleController.getString("Done", R.string.Done));
    }


    private PasswordRow createApiIdRow(Context context) {
        String text = String.valueOf(apiId);
        String contentDescription = LocaleController.getString(R.string.ApiId);
        OnPasswordEditorDone onEditorDone = () -> {
            focusApiHashField(true);
        };

        apiIdPasswordRow = new PasswordRow(context, text, contentDescription, onEditorDone);

        return apiIdPasswordRow;
    }


    private PasswordRow createApiHashRow(Context context) {
        String text = apiHash;
        String contentDescription = LocaleController.getString(R.string.ApiHash);
        OnPasswordEditorDone onEditorDone = () -> {
            doneButton.performClick();
        };

        apiHashPasswordRow = new PasswordRow(context, text, contentDescription, onEditorDone);

        return apiHashPasswordRow;
    }

    @Override
    public View createView(Context context) {
        setupActionBar(context);

        LinearLayout container = new LinearLayout(context);
        fragmentView = container;
        container.setOrientation(LinearLayout.HORIZONTAL);
        container.setBackgroundColor(getThemedColor(Theme.key_windowBackgroundGray));
        container.setOnTouchListener((v, event) -> true);


        LinearLayout scrollViewLinearLayout = new LinearLayout(context) {
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        };
        scrollViewLinearLayout.setOrientation(LinearLayout.VERTICAL);
        scrollViewLinearLayout.setGravity(Gravity.CENTER_HORIZONTAL);


        View lockImageView = createLockImageView(context);
        scrollViewLinearLayout.addView(lockImageView, LayoutHelper.createLinear(120, 120, Gravity.CENTER_HORIZONTAL));


        View titleGuideView = createTitleGuideView(context);
        scrollViewLinearLayout.addView(titleGuideView, LayoutHelper.createLinear(LayoutHelper.WRAP_CONTENT, LayoutHelper.WRAP_CONTENT, Gravity.TOP | Gravity.CENTER_HORIZONTAL));


        apiIdPasswordRow = createApiIdRow(context);
        scrollViewLinearLayout.addView(apiIdPasswordRow.outlineView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 24, 32, 24, 32));


        apiHashPasswordRow = createApiHashRow(context);
        scrollViewLinearLayout.addView(apiHashPasswordRow.outlineView, LayoutHelper.createLinear(LayoutHelper.MATCH_PARENT, LayoutHelper.WRAP_CONTENT, Gravity.TOP | Gravity.CENTER_HORIZONTAL, 24, 32, 24, 32));


        scrollView = new ScrollView(context) {

            private int[] location = new int[2];
            private Rect tempRect = new Rect();
            private boolean isLayoutDirty = true;
            private int scrollingUp;

            @Override
            protected void onScrollChanged(int l, int t, int oldl, int oldt) {
                super.onScrollChanged(l, t, oldl, oldt);
            }

            @Override
            public void scrollToDescendant(View child) {
                child.getDrawingRect(tempRect);
                offsetDescendantRectToMyCoords(child, tempRect);

                tempRect.bottom += AndroidUtilities.dp(120);

                int scrollDelta = computeScrollDeltaToGetChildRectOnScreen(tempRect);
                if (scrollDelta < 0) {
                    scrollDelta -= (scrollingUp = (getMeasuredHeight() - child.getMeasuredHeight()) / 2);
                } else {
                    scrollingUp = 0;
                }
                if (scrollDelta != 0) {
                    smoothScrollBy(0, scrollDelta);
                }
            }

            @Override
            public void requestChildFocus(View child, View focused) {
                if (Build.VERSION.SDK_INT < 29) {
                    if (focused != null && !isLayoutDirty) {
                        scrollToDescendant(focused);
                    }
                }
                super.requestChildFocus(child, focused);
            }

            @Override
            public boolean requestChildRectangleOnScreen(View child, Rect rectangle, boolean immediate) {
                if (Build.VERSION.SDK_INT < 23) {

                    rectangle.bottom += AndroidUtilities.dp(120);

                    if (scrollingUp != 0) {
                        rectangle.top -= scrollingUp;
                        rectangle.bottom -= scrollingUp;
                        scrollingUp = 0;
                    }
                }
                return super.requestChildRectangleOnScreen(child, rectangle, immediate);
            }

            @Override
            public void requestLayout() {
                isLayoutDirty = true;
                super.requestLayout();
            }

            @Override
            protected void onLayout(boolean changed, int l, int t, int r, int b) {
                isLayoutDirty = false;
                super.onLayout(changed, l, t, r, b);
            }
        };
        scrollView.setVerticalScrollBarEnabled(false);
        scrollView.addView(scrollViewLinearLayout, LayoutHelper.createScroll(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT, Gravity.LEFT | Gravity.TOP));
        container.addView(scrollView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));


        AndroidUtilities.runOnUIThread(() -> {
            if (apiHash == null || !apiHash.isEmpty()) {
                focusApiIdField(true);
            }
        }, 40);

        return fragmentView;
    }

    private void focusApiIdField(boolean showKeyboard) {
        if (apiIdPasswordRow != null && apiIdPasswordRow.editText != null) {
            if (!apiIdPasswordRow.editText.isFocused()) {
                apiIdPasswordRow.editText.setSelection(apiIdPasswordRow.editText.length());
            }
            apiIdPasswordRow.editText.requestFocus();
            if (showKeyboard) {
                AndroidUtilities.showKeyboard(apiIdPasswordRow.editText);
            }
        }
    }

    private void focusApiHashField(boolean showKeyboard) {
        if (apiHashPasswordRow != null && apiHashPasswordRow.editText != null) {
            if (!apiHashPasswordRow.editText.isFocused()) {
                apiHashPasswordRow.editText.setSelection(apiHashPasswordRow.editText.length());
            }
            apiHashPasswordRow.editText.requestFocus();
            if (showKeyboard) {
                AndroidUtilities.showKeyboard(apiHashPasswordRow.editText);
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        lockImageView.setVisibility(AndroidUtilities.isSmallScreen() || AndroidUtilities.displaySize.x > AndroidUtilities.displaySize.y ? View.GONE : View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = MessagesController.getGlobalMainSettings();
        boolean animations = preferences.getBoolean("view_animations", true);
        if (!animations) {
            focusApiIdField(false);
        }
    }

    private void saveChanges() {
        if (getParentActivity() == null) {
            return;
        }

        final String newApiId = apiIdPasswordRow.editText.getText().toString();
        int newApiIdInt;
        try {
            newApiIdInt = Integer.parseInt(newApiId);
        } catch (NumberFormatException e) {
            return;
        }
        final String newApiHash = apiHashPasswordRow.editText.getText().toString();

        if (newApiHash.equals(apiHash) && String.valueOf(apiId).equals(newApiId)) {
            finishFragment();
        }

        try {
            TeleParanoidConfig.setApiId(newApiIdInt);
            TeleParanoidConfig.setAppHash(newApiHash);
            finishFragment();
        } catch (Throwable e) {
            FileLog.e(e);
        }
    }

    @Override
    public void onTransitionAnimationEnd(boolean isOpen, boolean backward) {
        if (isOpen) {
            focusApiIdField(false);
        }
    }

    @Override
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> themeDescriptions = new ArrayList<>();

        themeDescriptions.add(new ThemeDescription(fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundWhite));

        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault));
        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));

        themeDescriptions.add(new ThemeDescription(apiHashPasswordRow.editText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        themeDescriptions.add(new ThemeDescription(apiHashPasswordRow.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
        themeDescriptions.add(new ThemeDescription(apiHashPasswordRow.editText, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField));
        themeDescriptions.add(new ThemeDescription(apiHashPasswordRow.editText, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));

        themeDescriptions.add(new ThemeDescription(apiIdPasswordRow.editText, ThemeDescription.FLAG_TEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        themeDescriptions.add(new ThemeDescription(apiIdPasswordRow.editText, ThemeDescription.FLAG_HINTTEXTCOLOR, null, null, null, null, Theme.key_windowBackgroundWhiteHintText));
        themeDescriptions.add(new ThemeDescription(apiIdPasswordRow.editText, ThemeDescription.FLAG_BACKGROUNDFILTER, null, null, null, null, Theme.key_windowBackgroundWhiteInputField));
        themeDescriptions.add(new ThemeDescription(apiIdPasswordRow.editText, ThemeDescription.FLAG_BACKGROUNDFILTER | ThemeDescription.FLAG_DRAWABLESELECTEDSTATE, null, null, null, null, Theme.key_windowBackgroundWhiteInputFieldActivated));

        return themeDescriptions;
    }

    @Override
    public void onBecomeFullyVisible() {
        super.onBecomeFullyVisible();
        if (parentLayout != null && parentLayout.getDrawerLayoutContainer() != null) {
            parentLayout.getDrawerLayoutContainer().setBehindKeyboardColor(getThemedColor(Theme.key_windowBackgroundGray));
        }
    }

    @Override
    public void onBecomeFullyHidden() {
        super.onBecomeFullyHidden();
        if (parentLayout != null && parentLayout.getDrawerLayoutContainer() != null) {
            parentLayout.getDrawerLayoutContainer().setBehindKeyboardColor(Theme.getColor(Theme.key_windowBackgroundWhite));
        }
    }
}
