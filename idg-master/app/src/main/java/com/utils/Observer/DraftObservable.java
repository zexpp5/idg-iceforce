package com.utils.Observer;

import android.content.Context;

import com.chaoxiang.base.utils.SDLogUtil;
import com.entity.SDDraftEntity;
import com.lidroid.xutils.DbUtils;
import com.lidroid.xutils.db.sqlite.Selector;
import com.lidroid.xutils.exception.DbException;
import com.utils.SPUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

/**
 * @author zjh
 * @date 2015-7-16
 * @desc 草稿箱被观察者
 */
public class DraftObservable extends Observable {
	public static DraftObservable instance;
	private DraftObservable(){}
	public static final int ADD_TYPE = 1;
	public static final int DEL_TYPE = 2;
	public static final int UPDATE_TYPE = 3;
	
	private int changeType = -1;
	
	public synchronized static DraftObservable getInstance(){
		if(instance == null){
			instance = new DraftObservable();
		}
		return instance;
	}
	
	public synchronized void addData(Context context, DbUtils mDbUtils, SDDraftEntity draftEntity){
		SDLogUtil.syso("========addData=========");
		if(draftEntity == null){
			return;
		}
		try {
			SDLogUtil.debug("draftEntity_tickets="+draftEntity.getTickets());
			changeType = ADD_TYPE;
			draftEntity.setUserId((String) SPUtils.get(context, SPUtils.USER_ID, "-1"));
			draftEntity.setCompanyId((String) SPUtils.get(context, SPUtils.COMPANY_ID, "-1"));
			if(draftEntity.getId() == 0){
				mDbUtils.save(draftEntity);
			}else{
				int count = (int) mDbUtils.count(Selector.from(SDDraftEntity.class).where("tickets","=",draftEntity.getTickets()));
				if(count == 0){
					mDbUtils.save(draftEntity);
				}
			}
			setChanged();
			notifyObservers(draftEntity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void updateData(Context context, DbUtils mDbUtils, SDDraftEntity draftEntity){
		SDLogUtil.syso("========updateData=========");
		if(draftEntity == null){
			return;
		}
		draftEntity.setUserId((String)SPUtils.get(context, SPUtils.USER_ID, "-1"));
		draftEntity.setCompanyId((String) SPUtils.get(context, SPUtils.COMPANY_ID, "-1"));
		try {
			changeType = UPDATE_TYPE;
			mDbUtils.update(draftEntity);
			setChanged();
			notifyObservers(draftEntity);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	public synchronized void deleteData(DbUtils mDbUtils, SDDraftEntity draftEntity){
		SDLogUtil.syso("========deleteData=========");
		if(draftEntity == null){
			return;
		}
		try {
			changeType = DEL_TYPE;
			mDbUtils.delete(draftEntity);
			setChanged();
			notifyObservers(draftEntity);
		} catch (DbException e) {
			e.printStackTrace();
		}
	}
	
	public void deleteAllData(DbUtils mDbUtils){
		SDLogUtil.syso("========deleteAllData=========");
		try {
			changeType = DEL_TYPE;
			mDbUtils.deleteAll(SDDraftEntity.class);
			setChanged();
			notifyObservers();
		} catch (DbException e) {
			e.printStackTrace();
		}
	}

	public int getChangeType() {
		return changeType;
	}

	public void setChangeType(int changeType) {
		this.changeType = changeType;
	}
	
	@SuppressWarnings("unchecked")
	public List<Observer> getObserver(){
		List<Observer> observers = null;
		Field fields[] = Observable.class.getDeclaredFields();
		Field field=null;
		for(int i = 0; i<fields.length; i++){
			field = fields[i];
			field.setAccessible(true);
			if(field.getName().equals("observers")){
				try {
					observers = (List<Observer>) field.get(this);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return observers;
	}
}
