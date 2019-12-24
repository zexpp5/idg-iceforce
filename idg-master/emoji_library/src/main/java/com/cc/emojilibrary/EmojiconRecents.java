package com.cc.emojilibrary;

import android.content.Context;

import com.cc.emojilibrary.emoji.Emojicon;

/**
 * 添加为最近使用
 * Created by winbo on 15/12/14.
 */
public interface EmojiconRecents {
    public void addRecentEmoji(Context context, Emojicon emojicon);
}
