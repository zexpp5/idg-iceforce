package com.chaoxiang.base.other;

import com.chaoxiang.base.utils.StringUtils;

/**
 * User: Selson
 * Date: 2017-05-23
 * FIXME
 */
public class GroupUtils
{
    //不可退出和加好友入群
    public static final String IM_GROUP_BUSINESS = "1";
    //不可加好友入群，推广群退群要调接口
    public static final String IM_GROUP_POPULARIZE = "2";
    /**
     * 判断是否为商务和行业群，两个为不可退出群。
     *
     * @param groupString
     * @return
     */
    public static String checkGroup(String groupString)
    {
        String isGroup = "0";
        if (StringUtils.notEmpty(groupString))
        {
            //默认群群主=还有商务群
            if (groupString.equals("10000000000") || groupString.equals("10000000001") ||
                    groupString.equals("10000000002") || groupString.equals("10000000003")
                    )
            {
                isGroup = "1";
            }
            //erp行业群
            else if (groupString.equals("10000000007") || groupString.equals("10000000008") ||
                    groupString.equals("10000000009") || groupString.equals("10000000010") ||
                    groupString.equals("10000000011") || groupString.equals("10000000012"))
            {
                isGroup = "1";
            }
            //crm行业群
            else if (groupString.equals("10000000013") || groupString.equals("10000000014") ||
                    groupString.equals("10000000015") || groupString.equals("10000000015") ||
                    groupString.equals("10000000017") || groupString.equals("10000000018"))
            {
                isGroup = "1";
            }
            //lsz行业群
            else if (groupString.equals("10000000019") || groupString.equals("10000000020") ||
                    groupString.equals("10000000021") || groupString.equals("10000000022") ||
                    groupString.equals("10000000023") || groupString.equals("10000000024"))
            {
                isGroup = "1";
            }
            //推广群
            else if (groupString.equals("10000000004") ||
                    groupString.equals("10000000005") || groupString.equals("10000000006")
                    )
            {
                isGroup = "2";
            } else
            {
                isGroup = "0";
            }
        } else
        {
            isGroup = "0";
        }

        return isGroup;
    }


}