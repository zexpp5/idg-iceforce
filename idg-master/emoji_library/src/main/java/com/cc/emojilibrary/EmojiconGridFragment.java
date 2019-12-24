package com.cc.emojilibrary;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.cc.emojilibrary.emoji.Emojicon;
import com.cc.emojilibrary.emoji.People;

/**
 * Created by winbo on 15/12/14.
 */
public class EmojiconGridFragment extends Fragment implements AdapterView.OnItemClickListener
{

    private static final String USE_SYSTEM_DEFAULT_KEY = "useSystemDefaults";
    private static final String EMOJICONS_KEY = "emojicons";

    private OnEmojiconClickedListener mOnEmojiconClickedListener;
    private EmojiconRecents mRecents;
    private Emojicon[] mData;
    private boolean mUseSystemDefault = false;

    public static EmojiconGridFragment getInstance(Emojicon[] emojicons, EmojiconRecents recents)
    {
        return newInstance(emojicons, recents, false);
    }

    public static EmojiconGridFragment newInstance(Emojicon[] emojicons, EmojiconRecents recents, boolean useSystemDefault)
    {
        EmojiconGridFragment emojiGridFragment = new EmojiconGridFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(EMOJICONS_KEY, emojicons);
        args.putBoolean(USE_SYSTEM_DEFAULT_KEY, useSystemDefault);
        emojiGridFragment.setArguments(args);
        emojiGridFragment.setRecents(recents);
        return emojiGridFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState)
    {
        return inflater.inflate(R.layout.emojicon_grid, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        GridView gridView = (GridView) view.findViewById(R.id.Emoji_GridView);
        Bundle bundle = getArguments();
        if (bundle == null)
        {
            mData = People.DATA;
            mUseSystemDefault = false;
        } else
        {
            Parcelable[] parcels = bundle.getParcelableArray(EMOJICONS_KEY);
            mData = new Emojicon[parcels.length];
            for (int i = 0; i < parcels.length; i++)
            {
                mData[i] = (Emojicon) parcels[i];
            }
            mUseSystemDefault = bundle.getBoolean(USE_SYSTEM_DEFAULT_KEY);
        }
        gridView.setAdapter(new EmojiAdapter(view.getContext(), mData, mUseSystemDefault));
        gridView.setOnItemClickListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(EMOJICONS_KEY, mData);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        if (activity instanceof OnEmojiconClickedListener)
        {
            mOnEmojiconClickedListener = (OnEmojiconClickedListener) activity;
        } else if (getParentFragment() instanceof OnEmojiconClickedListener)
        {
            mOnEmojiconClickedListener = (OnEmojiconClickedListener) getParentFragment();
        } else
        {
            throw new IllegalArgumentException(activity + " must implement interface " + OnEmojiconClickedListener.class.getSimpleName());
        }
    }

    @Override
    public void onDetach()
    {
        mOnEmojiconClickedListener = null;
        super.onDetach();
    }

    private void setRecents(EmojiconRecents recents)
    {
        mRecents = recents;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        if (mOnEmojiconClickedListener != null)
        {
            mOnEmojiconClickedListener.onEmojiconClicked((Emojicon) parent.getItemAtPosition(position));
        }
        if (mRecents != null)
        {
            mRecents.addRecentEmoji(view.getContext(), ((Emojicon) parent
                    .getItemAtPosition(position)));
        }
    }


    public interface OnEmojiconClickedListener
    {
        void onEmojiconClicked(Emojicon emojicon);
    }
}
