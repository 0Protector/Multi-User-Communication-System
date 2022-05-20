package qqserver.service;

import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tang
 * @version 1.0
 * 服务器，监听9999端口，等待客户端连接
 */
public class QQServer {
    ServerSocket serverSocket = null;
    ManageSCCT manageSCCT;

    //创建一个HashMap(代替DB)用来存放默认合法的用户
    //也可以使用ConcurrentHashMap，并且这种HashMap可以处理并发 的 集合，没有线程安全问题
    private static ConcurrentHashMap<String, User> validUsers = new ConcurrentHashMap<>();


    static {    //添加默认用户
        validUsers.put("100", new User("100", "123456"));
        validUsers.put("200", new User("200", "123123"));
        validUsers.put("至尊宝", new User("至尊宝", "123321"));
        validUsers.put("紫霞仙子", new User("紫霞仙子", "112233"));
        validUsers.put("菩提老祖", new User("菩提老祖", "332211"));
    }

    //判断用户是否合法
    public boolean checkUser(String userId, String pwd){
        User user = validUsers.get(userId);
        if (user == null){  //HashMap中并没有该用户
            return false;
        }
        if (!user.getPassword().equals(pwd)){   //用户存在但是密码错误
            return false;
        }
        return true;
    }

    public QQServer() {
        try {

            //后面可以考虑将在端口的位置设置成配置文件
            System.out.println("服务器在9999端口进行监听");
            serverSocket = new ServerSocket(9999);

            //启动推送新闻的线程
            new Thread(new sendNewsToAllService()).start();

            while(true){     //考虑到一个客户端可能不止一个数据通道的情况
                Socket socket = serverSocket.accept();
                //通过socket获取对象输入流、输出流
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                User user = (User)ois.readObject(); //先考虑第一次接收到的对象为User对象
                Message message = new Message();
                if (checkUser(user.getUserId(), user.getPassword())){
                    message.setMesType(MessageType.MESSAGE_LOGIN_SUCCEED);  //登录成功
                    oos.writeObject(message);
                    //创建持有socket的线程
                    SCCT scct = new SCCT(socket, user.getUserId());
                    scct.start();
                    manageSCCT.addSCCT(user.getUserId(), scct);

                }else{
                    message.setMesType(MessageType.MESSAGE_LOGIN_FAIL); //登录失败
                    oos.writeObject(message);
                    System.out.println("用户ID：" + user.getUserId()+ "验证失败");
                    socket.close();
                }

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
