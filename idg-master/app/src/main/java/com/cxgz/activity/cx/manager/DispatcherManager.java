package com.cxgz.activity.cx.manager;

import com.cxgz.activity.cx.handle.DoMsgHandle;
import com.cxgz.activity.cx.processor.IProcessor;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author zjh
 */
public class DispatcherManager
{
    private DispatcherManager()
    {
    }

    private static DispatcherManager manager;
    private static Map<DoMsgHandle.Type, IProcessor> map = new LinkedHashMap<>();

    public static DispatcherManager newInstance()
    {
        if (manager == null)
        {
            manager = new DispatcherManager();
        }
        return manager;
    }

    /**
     * 注册处理器
     *
     * @param type
     * @param manager
     */
    public void registerManager(DoMsgHandle.Type type, IProcessor manager)
    {
        map.put(type, manager);
    }

    /**
     * 反注册manager
     *
     * @param type
     */
    public void unregisterManager(DoMsgHandle.Type type)
    {
        map.remove(type);
    }


    public Map<DoMsgHandle.Type, IProcessor> getMap()
    {
        return map;
    }
}
