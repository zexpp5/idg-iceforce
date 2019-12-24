package yunjing.processor;


import com.cxgz.activity.db.entity.UnreadEntity;

import java.util.Observable;
import java.util.Observer;

/**
 * @desc 推送未读数据被观察者
 */
public class UnreadObserver implements Observer
{
    private UnreadChangeListener changeListener;

    public UnreadObserver()
    {
    }

    public UnreadObserver(UnreadChangeListener changeListener)
    {
        this.changeListener = changeListener;
    }

    @Override
    public void update(Observable observable, Object data)
    {
        if (data != null)
        {
            if (data instanceof UnreadEntity)
            {
                if (changeListener != null)
                {
                    changeListener.unreadChange((UnreadEntity) data);
                }
            }
        }
    }

    public UnreadChangeListener getChangeListener()
    {
        return changeListener;
    }

    public void setChangeListener(UnreadChangeListener changeListener)
    {
        this.changeListener = changeListener;
    }

    public interface UnreadChangeListener
    {
        void unreadChange(UnreadEntity unreadEntity);
    }
}
