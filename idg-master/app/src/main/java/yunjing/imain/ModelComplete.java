package yunjing.imain;

import yunjing.adapter.YunJingMainAdapter;
import yunjing.view.DialogNomalView;

/**
 * Created by tu2460 on 2017/7/24.
 * 业务逻辑完成后接口回掉管理
 */

public class ModelComplete {

    /**
     * 设置适配器完成
     */
    public interface SetAdapterComplete {
        void setAdapterComplete(YunJingMainAdapter yjMainAdapter);
    }


    /**
     * 初始化dialogs属性完成
     */
    public interface DialogComplete {
        void initDialogComplete(DialogNomalView dialogNomalView);
    }
}
