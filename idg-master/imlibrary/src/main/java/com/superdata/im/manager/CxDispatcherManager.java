package com.superdata.im.manager;

import com.superdata.im.constants.CxIMMessageType;
import com.superdata.im.processor.CxIProcessor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zjh
 * @version v1.0.0
 * @date 2016/1/4
 * @desc 分发管理器, 用于注册处理器进行分发
 */
public class CxDispatcherManager
{
    private CxDispatcherManager()
    {
    }

    private static CxDispatcherManager manager;
    private static Map<CxIMMessageType, CxIProcessor> map = new LinkedHashMap<>();
    
    public static CxDispatcherManager getInstance()
    {
        if (manager == null)
        {
            manager = new CxDispatcherManager();
        }
        return manager;
    }

    /**
     * 注册处理器
     *
     * @param type
     * @param manager
     */
    public void registerManager(CxIMMessageType type, CxIProcessor manager)
    {
        map.put(type, manager);
    }

    /**
     * 反注册manager
     *
     * @param type
     */
    public void unregisterManager(CxIMMessageType type)
    {
        map.remove(type);
    }


    public Map<CxIMMessageType, CxIProcessor> getMap()
    {
        return map;
    }
}
