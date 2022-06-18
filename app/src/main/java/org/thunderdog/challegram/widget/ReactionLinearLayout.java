package org.thunderdog.challegram.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import org.drinkless.td.libcore.telegram.TdApi;
import org.thunderdog.challegram.component.sticker.StickerSmallView;
import org.thunderdog.challegram.component.sticker.TGStickerObj;
import org.thunderdog.challegram.telegram.Tdlib;
import org.thunderdog.challegram.tool.Screen;

import java.util.ArrayList;

public class ReactionLinearLayout extends HorizontalScrollView {

    private ArrayList<TdApi.Reaction> totalReactions = new ArrayList<>();
    private Tdlib tdlib;
    private LinearLayout linearLayout;

    public ReactionLinearLayout(Context context, Tdlib tdlib, String[] availableReactions) {
        super(context);
        linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(LinearLayout.HORIZONTAL);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, Screen.dp(54f)));
        addView(linearLayout);
        this.tdlib = tdlib;
        sortReaction(tdlib.getSupportedReactions(), availableReactions);
        initReactionList();
        setHorizontalScrollBarEnabled(false);
    }

    private void sortReaction(TdApi.Reaction[] reactions, String[] availableReactions) {
        totalReactions.clear();
        for (String reaction : availableReactions) {
            for (TdApi.Reaction reactionCurrent : reactions) {
                if (reactionCurrent.reaction.equals(reaction)) {
                    totalReactions.add(reactionCurrent);
                    break;
                }
            }
        }
    }

    private void initReactionList() {
        for (TdApi.Reaction reaction : totalReactions) {
            StickerSmallView stickerSmallView = new StickerSmallView(getContext());
            stickerSmallView.setSticker(new TGStickerObj(tdlib, reaction.activateAnimation, "", reaction.activateAnimation.type));
            stickerSmallView.setLayoutParams(new LinearLayout.LayoutParams(Screen.dp(54f), Screen.dp(54f)));
            linearLayout.addView(stickerSmallView);
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
