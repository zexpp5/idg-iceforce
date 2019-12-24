package com.receiver;

import android.view.View;

import com.cxgz.activity.db.dao.SDUserEntity;
import com.entity.VoiceEntity;

import java.io.File;
import java.util.List;


/**
 * @author zjh
 */
public interface SendMsgCallback {
   void onSelectedImg(List<String> imgPaths);

    /**
     * 地理位置
     *
     * @param latitude  纬度
     * @param longitude 经度
     * @param address
     */
    void onClickLocationFunction(double latitude, double longitude, String address);

    /**
     * 抄送范围
     *
     * @param userEntities
     */
    void onClickSendRange(List<SDUserEntity> userEntities);

//    /**
//     * 选择工作圈
//     *
//     * @param circleListEntities
//     */
//    void onClickSelectedWorkCircle(List<SDWorkCircleListEntity> circleListEntities);

    /**
     * 选择了点评人或指派人的回调
     *
     * @param userEntities
     */
    void onClickSendPerson(List<SDUserEntity> userEntities);

//    /**
//     * 关联联系人回调
//     *
//     * @param customerContactsEntities
//     */
//    void onClickRelContacts(List<SDCustomerContactsEntity> customerContactsEntities);
//
//    void onClickRelCustomer(List<SDCustomerCompanyEntity> companyEntities);
//
//    void onClickRelTopic(List<SDTopicEntity> topicEntities);

//    /**
//     * @param atUser
//     * @人回调
//     */
//    void onClickAtUser(List<AtEntity> atUser);

    /**
     * 点击工具栏的附件获取文件的回调
     *
     * @param pickerFile
     */
    void onClickAttach(List<File> pickerFile);

    /**
     * 删除附件item，需自己判断附件类型通过v.getTag得到一个type(int)
     *
     * @param v
     */
    void onDelAttachItem(View v);

    /**
     * 语音回调
     *
     * @param voiceEntity
     */
    void onClickVoice(VoiceEntity voiceEntity);

    /**
     * 获取草稿箱数据类型
     *
     * @return
     */
    int getDraftDataType();

 /**
  * 資金附件
  * @param moneyString
  */
    void onClickMoneyFile(String moneyString);
}
