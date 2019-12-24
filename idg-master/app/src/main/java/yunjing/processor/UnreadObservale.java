package yunjing.processor;

import android.content.Context;


import com.cxgz.activity.db.dao.PushUnreadDao;
import com.cxgz.activity.db.entity.UnreadEntity;
import com.utils.SPUtils;


import java.util.Observable;

/**
 * @author kc
 * @date 2016-7-01
 * @desc 推送未读数据被观察者
 */
public class UnreadObservale extends Observable {
    private PushUnreadDao unreadDao;

    private UnreadObservale() {
    }

    private static UnreadObservale observale;

    public static UnreadObservale getInstance() {
        if (observale == null) {
            observale = new UnreadObservale();
        }
        return observale;
    }

    /**
     * 待处理工作未查看条数发生改变
     */
    public void unReaderChange(Context context, int total, int type,int placeIndex,int workIndex) {

        String userId = (String) SPUtils.get(context, SPUtils.USER_ID, "");
        unreadDao = new PushUnreadDao(context);
        UnreadEntity unreadEntity;
        unreadEntity = unreadDao.findByUserId(type);
        if (unreadEntity == null) {
            unreadEntity = new UnreadEntity(type, total, userId,false,placeIndex,workIndex);
        } else {
            unreadEntity.setType(type);
            unreadEntity.setUnreadCount(total);
            unreadEntity.setRead(false);
            unreadEntity.setPlaceIndex(placeIndex);
            unreadEntity.setWorkMenu(workIndex);
        }
        unreadDao.save(unreadEntity);//保存具体模块的
        obersverChange(unreadEntity);
    }


    private void obersverChange(UnreadEntity unreadEntity) {
        //该方法必须调用，否则观察者无效
        setChanged();
        //触发该方法通知观察者更新UI
        notifyObservers(unreadEntity);
    }
}
