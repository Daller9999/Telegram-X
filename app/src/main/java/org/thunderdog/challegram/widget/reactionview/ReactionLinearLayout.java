package org.thunderdog.challegram.widget.reactionview;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.drinkless.td.libcore.telegram.TdApi;
import org.thunderdog.challegram.component.sticker.StickerSmallView;
import org.thunderdog.challegram.component.sticker.TGStickerObj;
import org.thunderdog.challegram.core.Background;
import org.thunderdog.challegram.data.TGMessage;
import org.thunderdog.challegram.support.RippleSupport;
import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.tool.Screen;

import java.util.ArrayList;

public class ReactionLinearLayout extends HorizontalScrollView {

    public static final int REACTION_SIZE = 15;
    public static final int REACTION_LAYOUT_ID = -1233124351;

    private final ArrayList<TdApi.Reaction> totalReactions = new ArrayList<>();
    private Tdlib tdlib;
    private LinearLayout linearLayout;
    private TGMessage message;

    private ReactionCallBack reactionCallBack = null;

    public ReactionLinearLayout(Context context, TGMessage message, Tdlib tdlib, String[] availableReactions) {
        super(context);
        this.tdlib = tdlib;
        this.message = message;

        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Screen.dp(54f)));
        RippleSupport.setSimpleWhiteBackground(this);
        addView(linearLayout);

        sortReaction(tdlib.getSupportedReactions(), availableReactions);
        initReactionList();
        setHorizontalScrollBarEnabled(false);
    }

    public void setReactionCallBack(ReactionCallBack reactionCallBack) {
        this.reactionCallBack = reactionCallBack;
    }

    private void sortReaction(TdApi.Reaction[] reactions, String[] availableReactions) {
        totalReactions.clear();
        for (String reaction : availableReactions) {
            for (TdApi.Reaction reactionCurrent : reactions) {
                if (reactionCurrent.isActive) {
                    if (reactionCurrent.reaction.equals(reaction)) {
                        totalReactions.add(reactionCurrent);
                        break;
                    }
                }
            }
        }
    }

    private void initReactionList() {
        ArrayList<StickerSmallView> stickerSmallViewList = new ArrayList<>();
        for (TdApi.Reaction reaction : totalReactions) {
            StickerSmallView stickerSmallView = new StickerSmallView(getContext());
            stickerSmallView.setSticker(new TGStickerObj(tdlib, reaction.activateAnimation, "", reaction.activateAnimation.type));
            stickerSmallView.setLayoutParams(new LinearLayout.LayoutParams(Screen.dp(54f), Screen.dp(54f)));
            stickerSmallViewList.add(stickerSmallView);
            linearLayout.addView(stickerSmallView);
            stickerSmallView.setStickerPressEvent(() -> onStickerClickListener(reaction));
        }
        Background.instance().post(() -> {
            for (StickerSmallView view : stickerSmallViewList) {
                view.setAnimation(false);
            }
        }, 2000);
    }

    private void onStickerClickListener(TdApi.Reaction reaction) {
        TdApi.SetMessageReaction messageReaction = new TdApi.SetMessageReaction();
        TdApi.Message message = this.message.getMessage();
        messageReaction.reaction = reaction.reaction;
        messageReaction.messageId = message.id;
        messageReaction.chatId = message.chatId;
        messageReaction.isBig = false;
        tdlib.client().send(messageReaction, object -> {

        });
        if (reactionCallBack != null) {
            reactionCallBack.onReactionSelected();
        }
    }

    public ReactionLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ReactionLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public ReactionLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


}
