////
//import com.chaoxiang.base.utils.SDGson;
//import com.chaoxiang.base.utils.SDLogUtil;
//import com.google.gson.reflect.TypeToken;
//import com.utils.FlowUtils;
//
//import org.junit.Test;
//
//import java.util.List;
//import java.util.Map;
//
//import static junit.framework.Assert.assertEquals;
//import static org.junit.Assert.assertEquals;
///**
// * Created by injoy-pc on 2016/5/20.
// */
//public class MyTest {
//
//    private String TAG = getClass().getSimpleName();
//
//    @Test
//    public void test1(){
//        String s = "11";
//        String[] ss = s.split("\\.");
//        System.out.println(ss.length);
//    }
//
//    @Test
//    public void test2(){
//        String s = "11";
//        assertEquals(s.contains("."),false);
//    }
//
//    @Test
//    public void test3(){
//        String s1 = "{\n" +
//                "        \"dp_ck\": \"\", \n" +
//                "        \"dp_cw\": \"\", \n" +
//                "        \"dp_ds\": 3\n" +
//                "    }";
//        Map<String,String> map = new SDGson().fromJson(s1,new TypeToken<Map<String,String>>(){}.getType());
//        for (String key : map.keySet()){
//            System.out.println("key-------->>"+key);
//            System.out.println("value-------->>"+map.get(key));
//        }
//    }
//
//    @Test
//    public void test4(){
//        String s1 = "[\n" +
//                "        {\n" +
//                "            \"dp_bm\": \"1\"\n" +
//                "        }, \n" +
//                "        {\n" +
//                "            \"dp_cw\": \"3\"\n" +
//                "        }, \n" +
//                "        {\n" +
//                "            \"dp_ck\": \"\"\n" +
//                "        }\n" +
//                "    ]";
//        List<Map<String,String>> mapList = new SDGson().fromJson(s1,new TypeToken<List<Map<String,String>>>(){}.getType());
//        System.out.println(s1);
//        List<FlowUtils.FlowBean> flowBeanList = FlowUtils.buildFlowList(mapList);
//        System.out.println(new SDGson().getGson().toJson(flowBeanList));
//    }
//}
