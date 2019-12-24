package com.utils.Observer;



import com.chaoxiang.base.utils.SDLogUtil;
import com.cxgz.activity.home.adapter.CommonAdapter;
import com.entity.SDDraftEntity;

import java.util.Observable;
import java.util.Observer;

/**
 * @author zjh
 * @date 2015-7-20
 * @desc 草稿箱数据观察者, 用于观察草稿箱数据是否改变
 */
public class DraftObserver implements Observer {
    private CommonAdapter<SDDraftEntity> adapter;
    private int identificatify;

    public DraftObserver(CommonAdapter<SDDraftEntity> adapter) {
        this.adapter = adapter;
    }

    @Override
    public void update(Observable observable, Object data) {
        SDDraftEntity draftEntity = null;
        if (data != null) {
            draftEntity = (SDDraftEntity) data;
            if(draftEntity == null){
                return;
            }
        }else {
            return;
        }
        switch (((DraftObservable) observable).getChangeType()) {
            case DraftObservable.ADD_TYPE:
                if (adapter.getAll() != null) {
                    adapter.getAll().add(0,draftEntity);
                    adapter.notifyDataSetChanged();
                }
                break;
            case DraftObservable.DEL_TYPE:
                if (adapter.getAll() != null && !adapter.getAll().isEmpty()) {
                    for (int i = 0; i < adapter.getAll().size(); i++) {
                        SDDraftEntity draft = adapter.getAll().get(i);
                        if (draft.getId() == draftEntity.getId()) {
                            adapter.getAll().remove(i);
                            break;
                        }
                    }
                    adapter.notifyDataSetChanged();
                }
                break;
            case DraftObservable.UPDATE_TYPE:
                if (draftEntity != null) {
                    SDLogUtil.debug("update====" + draftEntity.toString());
                    if (adapter.getAll() != null && !adapter.getAll().isEmpty()) {
                        for (int i = 0; i < adapter.getAll().size(); i++) {
                            SDDraftEntity draft = adapter.getAll().get(i);
                            if (draft.getId() == draftEntity.getId()) {
                                adapter.getAll().remove(i);
                                adapter.getAll().add(i, draftEntity);
                                break;
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }
                }
                break;

            default:
                break;
        }
    }

    public int getIdentificatify() {
        return identificatify;
    }

    public void setIdentificatify(int identificatify) {
        this.identificatify = identificatify;
    }

}
