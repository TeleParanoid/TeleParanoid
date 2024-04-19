package org.teleparanoid.ui;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.IntDef;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.BuildVars;
import org.telegram.messenger.FileLog;
import org.telegram.messenger.LocaleController;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.messenger.SharedConfig;
import org.telegram.ui.ActionBar.ActionBar;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.ActionBar.ThemeDescription;
import org.telegram.ui.Cells.HeaderCell;
import org.telegram.ui.Cells.TextCheckCell;
import org.telegram.ui.Cells.TextInfoPrivacyCell;
import org.telegram.ui.Cells.TextSettingsCell;
import org.telegram.ui.Components.LayoutHelper;
import org.telegram.ui.Components.RecyclerListView;
import org.teleparanoid.TeleParanoidConfig;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;

public class TeleParanoidSettingsActivity extends BaseFragment implements NotificationCenter.NotificationCenterDelegate {

    private ListAdapter listAdapter;
    private RecyclerListView listView;
    @SuppressWarnings("FieldCanBeLocal")
    private LinearLayoutManager layoutManager;

    private int securitySectionRow;
    private int obtainingApiIdRow;


    private int secretMapRow;
    private int allowCaptureScreenRow;
    private int sendReadPacketsRow;
    private int sendOnlinePacketsRow;
    private int sendUploadProgressRow;
    private int markReadAfterSendRow;
    private int shouldHideChatJoiningMessagesRow;
    private int shouldHideRecommendationsCellRow;
    private int buildInfoRow;
    private int rowCount;
    private boolean secretMapUpdate;

    @Override
    public boolean onFragmentCreate() {
        super.onFragmentCreate();

        updateRows();

        return true;
    }

    @Override
    public void onFragmentDestroy() {
        super.onFragmentDestroy();
    }

    @Override
    public View createView(Context context) {
        actionBar.setBackButtonImage(R.drawable.ic_ab_back);
        actionBar.setAllowOverlayTitle(true);
        actionBar.setTitle(LocaleController.getString(R.string.TeleParanoidSettings));
        actionBar.setActionBarMenuOnItemClick(new ActionBar.ActionBarMenuOnItemClick() {
            @Override
            public void onItemClick(int id) {
                if (id == -1) {
                    finishFragment();
                }
            }
        });

        listAdapter = new ListAdapter(context);

        fragmentView = new FrameLayout(context);
        FrameLayout frameLayout = (FrameLayout) fragmentView;
        frameLayout.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundGray));

        listView = new RecyclerListView(context);
        listView.setLayoutManager(layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false) {
            @Override
            public boolean supportsPredictiveItemAnimations() {
                return false;
            }
        });
        listView.setVerticalScrollBarEnabled(false);
        listView.setLayoutAnimation(null);
        listView.setItemAnimator(null);
        frameLayout.addView(listView, LayoutHelper.createFrame(LayoutHelper.MATCH_PARENT, LayoutHelper.MATCH_PARENT));
        listView.setAdapter(listAdapter);
        listView.setOnItemClickListener((view, position) -> {
            if (!view.isEnabled()) {
                return;
            }
            if (position == obtainingApiIdRow) {
                ApiSetupActivity fragment = new ApiSetupActivity();
                presentFragment(fragment);
            } else if (position == allowCaptureScreenRow) {

                TextCheckCell textCheckCell = ((TextCheckCell) view);
                final boolean isChecked = textCheckCell.getCheckBox().isChecked();

                try {
                    TeleParanoidConfig tpConfig = TeleParanoidConfig.getInstance(currentAccount);
                    tpConfig.isCaptureScreenAllowed = !isChecked;
                    tpConfig.saveConfig();
                    textCheckCell.setChecked(!isChecked);
                }
                catch (Throwable e){
                    FileLog.e(e);
                }
            } else if (position == sendReadPacketsRow) {

                TextCheckCell textCheckCell = ((TextCheckCell) view);
                final boolean isChecked = textCheckCell.getCheckBox().isChecked();

                try {
                    TeleParanoidConfig tpConfig = TeleParanoidConfig.getInstance(currentAccount);
                    tpConfig.shouldIgnoreReadPackets = !isChecked;
                    tpConfig.saveConfig();
                    textCheckCell.setChecked(!isChecked);
                }
                catch (Throwable e){
                    FileLog.e(e);
                }
            } else if (position == sendOnlinePacketsRow) {

                TextCheckCell textCheckCell = ((TextCheckCell) view);
                final boolean isChecked = textCheckCell.getCheckBox().isChecked();

                try {
                    TeleParanoidConfig tpConfig = TeleParanoidConfig.getInstance(currentAccount);
                    tpConfig.shouldSetOfflineInUpdatePackets = !isChecked;
                    tpConfig.saveConfig();
                    textCheckCell.setChecked(!isChecked);
                }
                catch (Throwable e){
                    FileLog.e(e);
                }
            } else if (position == sendUploadProgressRow) {

                TextCheckCell textCheckCell = ((TextCheckCell) view);
                final boolean isChecked = textCheckCell.getCheckBox().isChecked();

                try {
                    TeleParanoidConfig tpConfig = TeleParanoidConfig.getInstance(currentAccount);
                    tpConfig.shouldIgnoreSendTypingPackets = !isChecked;
                    tpConfig.saveConfig();
                    textCheckCell.setChecked(!isChecked);
                }
                catch (Throwable e){
                    FileLog.e(e);
                }
            } else if (position == markReadAfterSendRow) {

                TextCheckCell textCheckCell = ((TextCheckCell) view);
                final boolean isChecked = textCheckCell.getCheckBox().isChecked();

                try {
                    TeleParanoidConfig tpConfig = TeleParanoidConfig.getInstance(currentAccount);
                    tpConfig.shouldMarkReadAfterSend = !isChecked;
                    tpConfig.saveConfig();
                    textCheckCell.setChecked(!isChecked);
                }
                catch (Throwable e){
                    FileLog.e(e);
                }
            } else if (position == shouldHideChatJoiningMessagesRow) {

                TextCheckCell textCheckCell = ((TextCheckCell) view);
                final boolean isChecked = textCheckCell.getCheckBox().isChecked();

                try {
                    TeleParanoidConfig tpConfig = TeleParanoidConfig.getInstance(currentAccount);
                    tpConfig.shouldHideChatJoiningMessages = !isChecked;
                    tpConfig.saveConfig();
                    textCheckCell.setChecked(!isChecked);
                }
                catch (Throwable e){
                    FileLog.e(e);
                }
            } else if (position == shouldHideRecommendationsCellRow) {

                TextCheckCell textCheckCell = ((TextCheckCell) view);
                final boolean isChecked = textCheckCell.getCheckBox().isChecked();

                try {
                    TeleParanoidConfig tpConfig = TeleParanoidConfig.getInstance(currentAccount);
                    tpConfig.shouldHideRecommendationsCell = !isChecked;
                    tpConfig.saveConfig();
                    textCheckCell.setChecked(!isChecked);
                }
                catch (Throwable e){
                    FileLog.e(e);
                }
            } else if (position == secretMapRow) {
//                AlertsCreator.showSecretLocationAlert(getParentActivity(), currentAccount, () -> {
//                    listAdapter.notifyDataSetChanged();
//                    secretMapUpdate = true;
//                }, false, null);
            }
        });

        return fragmentView;
    }

    @Override
    public void didReceivedNotification(int id, int account, Object... args) {

    }

    private void updateRows() {
        updateRows(true);
    }

    private void updateRows(boolean notify) {
        rowCount = 0;

        securitySectionRow = rowCount++;
        obtainingApiIdRow = rowCount++;
        allowCaptureScreenRow = rowCount++;
        sendReadPacketsRow = rowCount++;
        sendOnlinePacketsRow = rowCount++;
        sendUploadProgressRow = rowCount++;
        markReadAfterSendRow = rowCount++;
        shouldHideChatJoiningMessagesRow = rowCount++;
        shouldHideRecommendationsCellRow = rowCount++;

        secretMapRow = rowCount++;
        buildInfoRow = rowCount++;

        if (listAdapter != null && notify) {
            listAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listAdapter != null) {
            listAdapter.notifyDataSetChanged();
        }
    }


    public final static int TEXT_SETTINGS_CELL = 0,
            TEXT_INFO_PRIVACY_CELL = 1,
            HEADER_CELL = 2,
            TEXT_CHECK_CELL = 3;

    @Retention(RetentionPolicy.SOURCE)
    @IntDef({
            TEXT_SETTINGS_CELL,
            TEXT_INFO_PRIVACY_CELL,
            HEADER_CELL,
            TEXT_CHECK_CELL
    })
    public @interface ViewType {
    }


    private class ListAdapter extends RecyclerListView.SelectionAdapter {

        private Context mContext;

        public ListAdapter(Context context) {
            mContext = context;
        }

        @Override
        public boolean isEnabled(RecyclerView.ViewHolder holder) {
            int position = holder.getAdapterPosition();
            return position == allowCaptureScreenRow
                    || position == secretMapRow
                    || position == sendReadPacketsRow
                    || position == sendOnlinePacketsRow
                    || position == sendUploadProgressRow
                    || position == markReadAfterSendRow
                    || position == shouldHideChatJoiningMessagesRow
                    || position == shouldHideRecommendationsCellRow
                    || position == obtainingApiIdRow;
        }

        @Override
        public int getItemCount() {
            return rowCount;
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, @ViewType int viewType) {
            View view;
            switch (viewType) {
                case TEXT_SETTINGS_CELL:
                    view = new TextSettingsCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case TEXT_INFO_PRIVACY_CELL:
                    view = new TextInfoPrivacyCell(mContext);
                    break;
                case HEADER_CELL:
                    view = new HeaderCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
                case TEXT_CHECK_CELL:
                default:
                    view = new TextCheckCell(mContext);
                    view.setBackgroundColor(Theme.getColor(Theme.key_windowBackgroundWhite));
                    break;
            }
            return new RecyclerListView.Holder(view);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            @ViewType final int viewType = getItemViewType(position);

            switch (viewType) {
                case TEXT_SETTINGS_CELL:
                    String value = null;
                    holder.itemView.setTag(position);
                    TextSettingsCell textCell = (TextSettingsCell) holder.itemView;
                    if (position == secretMapRow) {
                        switch (SharedConfig.mapPreviewType) {
                            case 0:
                                value = LocaleController.getString("MapPreviewProviderTelegram", R.string.MapPreviewProviderTelegram);
                                break;
                            case 1:
                                value = LocaleController.getString("MapPreviewProviderGoogle", R.string.MapPreviewProviderGoogle);
                                break;
                            case 2:
                                value = LocaleController.getString("MapPreviewProviderNobody", R.string.MapPreviewProviderNobody);
                                break;
                            case 3:
                            default:
                                value = LocaleController.getString("MapPreviewProviderYandex", R.string.MapPreviewProviderYandex);
                                break;
                        }
                        textCell.setTextAndValue(LocaleController.getString("MapPreviewProvider", R.string.MapPreviewProvider), value, secretMapUpdate, true);
                        secretMapUpdate = false;
                    } else if (position == obtainingApiIdRow) {
                        textCell.setText(LocaleController.getString(R.string.ChangeApiCredentials), true);
                    }
                    break;
                case TEXT_INFO_PRIVACY_CELL:
                    TextInfoPrivacyCell privacyCell = (TextInfoPrivacyCell) holder.itemView;
                    boolean isLast = position == getItemCount() - 1;
                    privacyCell.setBackground(Theme.getThemedDrawableByKey(mContext, isLast ? R.drawable.greydivider_bottom : R.drawable.greydivider, Theme.key_windowBackgroundGrayShadow));

                    if (position == buildInfoRow) {
                        privacyCell.setText("TeleParanoid v" + BuildVars.TELEPARANOID_BUILD_VERSION_STRING);
                    }
                    break;
                case HEADER_CELL:
                    HeaderCell headerCell = (HeaderCell) holder.itemView;
                    if (position == securitySectionRow) {
                        headerCell.setText(LocaleController.getString("SecurityTitle", R.string.SecurityTitle));
                    }
                    break;
                case TEXT_CHECK_CELL:
                    TextCheckCell textCheckCell = (TextCheckCell) holder.itemView;
                    if (position == allowCaptureScreenRow) {

                        TeleParanoidConfig tpConfig = TeleParanoidConfig.getInstance(currentAccount);

                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.AllowCaptureScreen), tpConfig.isCaptureScreenAllowed, false);
                    } else if (position == sendReadPacketsRow) {

                        TeleParanoidConfig tpConfig = TeleParanoidConfig.getInstance(currentAccount);

                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.IgnoreReadPackets), tpConfig.shouldIgnoreReadPackets, false);
                    } else if (position == sendOnlinePacketsRow) {

                        TeleParanoidConfig tpConfig = TeleParanoidConfig.getInstance(currentAccount);

                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.SetOfflineInUpdate), tpConfig.shouldSetOfflineInUpdatePackets, false);
                    } else if (position == sendUploadProgressRow) {

                        TeleParanoidConfig tpConfig = TeleParanoidConfig.getInstance(currentAccount);

                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.IgnoreSendTypingPackets), tpConfig.shouldIgnoreSendTypingPackets, false);
                    } else if (position == markReadAfterSendRow) {

                        TeleParanoidConfig tpConfig = TeleParanoidConfig.getInstance(currentAccount);

                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.SendReadAfterReply), tpConfig.shouldMarkReadAfterSend, true);
                    } else if (position == shouldHideChatJoiningMessagesRow) {

                        TeleParanoidConfig tpConfig = TeleParanoidConfig.getInstance(currentAccount);

                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.ShouldHideChatJoiningMessages), tpConfig.shouldHideChatJoiningMessages, false);
                    }else if (position == shouldHideRecommendationsCellRow) {

                        TeleParanoidConfig tpConfig = TeleParanoidConfig.getInstance(currentAccount);

                        textCheckCell.setTextAndCheck(LocaleController.getString(R.string.ShouldHideRecommendationsCell), tpConfig.shouldHideRecommendationsCell, false);
                    }
                    break;
            }
        }

        @Override
        public @ViewType int getItemViewType(int position) {
            if (position == secretMapRow || position == obtainingApiIdRow) {
                return TEXT_SETTINGS_CELL;
            } else if (position == buildInfoRow) {
                return TEXT_INFO_PRIVACY_CELL;
            } else if (position == securitySectionRow) {
                return HEADER_CELL;
            } else if (position == allowCaptureScreenRow
                    || position == sendReadPacketsRow
                    || position == sendOnlinePacketsRow
                    || position == sendUploadProgressRow
                    || position == markReadAfterSendRow
                    || position == shouldHideChatJoiningMessagesRow
                    || position == shouldHideRecommendationsCellRow
            ) {
                return TEXT_CHECK_CELL;
            }

            return TEXT_CHECK_CELL;
        }
    }

    @Override
    public ArrayList<ThemeDescription> getThemeDescriptions() {
        ArrayList<ThemeDescription> themeDescriptions = new ArrayList<>();

        themeDescriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_CELLBACKGROUNDCOLOR, new Class[]{TextSettingsCell.class, HeaderCell.class, TextCheckCell.class}, null, null, null, Theme.key_windowBackgroundWhite));
        themeDescriptions.add(new ThemeDescription(fragmentView, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_windowBackgroundGray));

        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_BACKGROUND, null, null, null, null, Theme.key_actionBarDefault));
        themeDescriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_LISTGLOWCOLOR, null, null, null, null, Theme.key_actionBarDefault));
        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_ITEMSCOLOR, null, null, null, null, Theme.key_actionBarDefaultIcon));
        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_TITLECOLOR, null, null, null, null, Theme.key_actionBarDefaultTitle));
        themeDescriptions.add(new ThemeDescription(actionBar, ThemeDescription.FLAG_AB_SELECTORCOLOR, null, null, null, null, Theme.key_actionBarDefaultSelector));

        themeDescriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_SELECTOR, null, null, null, null, Theme.key_listSelector));

        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{View.class}, Theme.dividerPaint, null, null, Theme.key_divider));

        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{TextSettingsCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteValueText));

        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{HeaderCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlueHeader));

        themeDescriptions.add(new ThemeDescription(listView, ThemeDescription.FLAG_BACKGROUNDFILTER, new Class[]{TextInfoPrivacyCell.class}, null, null, null, Theme.key_windowBackgroundGrayShadow));
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{TextInfoPrivacyCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText4));

        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class}, new String[]{"textView"}, null, null, null, Theme.key_windowBackgroundWhiteBlackText));
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class}, new String[]{"valueTextView"}, null, null, null, Theme.key_windowBackgroundWhiteGrayText2));
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrack));
        themeDescriptions.add(new ThemeDescription(listView, 0, new Class[]{TextCheckCell.class}, new String[]{"checkBox"}, null, null, null, Theme.key_switchTrackChecked));

        return themeDescriptions;
    }
}
