package qqclient.service;

import java.util.HashMap;

/**
 * @author Tang
 * @version 1.0
 * 该类管理CCS(Client Content Server)的线程
 * 使用HashMap
 */
@SuppressWarnings("all")
public class ManageCCSThread {
    //key -> userId, value -> CCSThread
    private static HashMap<String, CCSThread> hashMap = new HashMap<>();

    //提供添加键值对的方法
    public static void addCCSThread(String userId, CCSThread ccsThread){
        hashMap.put(userId, ccsThread);
    }

    /**提供根据key返回value的方法
     *
     * @param userId 用户名
     * @return 返回CCST（Client Content Server Thread)
     * 这里用userId作为key,是否有可能导致同一个客户端下只能存在一个CCST
     *      因为HashMap的存储机制，是按照key值来进行的，
     *      同一个客户端添加的第二个CCST是否会进行替换而不是添加？
     */
    public static CCSThread getCCSThread(String userId){
        return hashMap.get(userId);
    }
}
