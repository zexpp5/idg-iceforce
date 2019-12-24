package com.cxgz.activity.cxim;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.injoy.idg.R;
import com.cc.emojilibrary.EmojiAdapter;
import com.cc.emojilibrary.emoji.Emojicon;

/**
 * Created by winbo on 15/12/28.
 */
public class GolfEmojiPageFragment extends Fragment
{
    private static final String TAG = "GolfEmojiPageFragment";
    private static final String EMOJICONS_KEY = "emojicons";
    private static final String NUMCOLUMNS_KEY = "numColumns";
    private Emojicon[] mData;
    private OnEmojiconClickedListener mOnEmojiconClickedListener;

    public static GolfEmojiPageFragment getInstance(Emojicon[] emojicons, int numColumns)
    {
        GolfEmojiPageFragment golfEmojiPageFragment = new GolfEmojiPageFragment();
        Bundle args = new Bundle();
        args.putParcelableArray(EMOJICONS_KEY, emojicons);
        args.putInt(NUMCOLUMNS_KEY, numColumns);
        golfEmojiPageFragment.setArguments(args);
        return golfEmojiPageFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState)
    {
        return inflater.inflate(R.layout.fragment_emoji_page_gridview, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);

        GridView gridView = (GridView) view.findViewById(R.id.grid_view);
        Bundle bundle = getArguments();
        Parcelable[] parcels = bundle.getParcelableArray(EMOJICONS_KEY);
        mData = new Emojicon[parcels.length];
        for (int i = 0; i < parcels.length; i++)
        {
            mData[i] = (Emojicon) parcels[i];
        }

        gridView.setNumColumns(bundle.getInt(NUMCOLUMNS_KEY));
        gridView.setAdapter(new EmojiAdapter(view.getContext(), mData, false));
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if (mOnEmojiconClickedListener != null)
                {
                    mOnEmojiconClickedListener.onEmojiconClicked((Emojicon) parent.getItemAtPosition(position));
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        super.onSaveInstanceState(outState);
        outState.putParcelableArray(EMOJICONS_KEY, mData);
    }

    @Override
    public void onDetach()
    {

        super.onDetach();
    }

    public void setOnEmojiconClickedListener(OnEmojiconClickedListener mOnEmojiconClickedListener)
    {
        this.mOnEmojiconClickedListener = mOnEmojiconClickedListener;
    }

    public interface OnEmojiconClickedListener
    {
        void onEmojiconClicked(Emojicon emojicon);
    }
}
