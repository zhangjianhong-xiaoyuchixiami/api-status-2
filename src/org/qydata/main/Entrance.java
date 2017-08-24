package org.qydata.main;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.qydata.po.Api;
import org.qydata.tools.SendEmail;

import java.io.InputStream;
import java.util.List;

/**
 * Created by jonhn on 2017/4/19.
 */
public class Entrance {

    private static String [] to  = {"laoluo@qianyandata.com","zhangjianhong@qianyandata.com"};
    //private static String [] to  = {"zhangjianhong@qianyandata.com"};
    public static void main(String[] args) {

        String resource = "mybatis.xml";
        InputStream is = Entrance.class.getClassLoader().getResourceAsStream(resource);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is);
        SqlSession session = sessionFactory.openSession();

        String queryApiStatus = "org.qydata.mapper.ApiBanMapper.queryApiStatus";
        List<Api> apiList =  session.selectList(queryApiStatus);
        if (apiList != null && apiList.size() > 0){
            for (int i = 0; i < apiList.size() ; i++) {
                Api api = apiList.get(i);
                if (api != null){
                    if (api.getApiTypeName() != null){
                        if (api.getVendorName() != null){
                            api.setApiTypeName(api.getApiTypeName() + "@" + api.getVendorName());
                        }
                    }
                    String apiName = api.getApiTypeName();
                    String title = apiName + "被系统禁用提醒";
                    String content = apiName +"已被系统自动禁用，禁用时间："+ api.getTimeStamp();
                    try {
                        SendEmail.sendMail(to,title,content);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        session.close();
    }
}
