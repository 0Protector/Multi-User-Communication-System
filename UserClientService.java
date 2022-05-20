package qqclient.service;

import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

/**
 * @author Tang
 * @version 1.0
 * 该类完成客户端的基本操作
 */
@SuppressWarnings("all")
public class UserClientService {
    //因为用户信息可能在其他地方也会被用到，所以这里考虑做成成员属性
    private User user = new User();
    //同上，socket也应该被作为成员属性
    private Socket socket;
    //检查用户是否合法
    public boolean checkUser(String userId, String psw){
        boolean b = false;
        //创建user对象

        user.setUserId(userId);
        user.setPassword(psw);

        try {
            socket = new  Socket(InetAddress.getLocalHost(), 9999);
            //得到ObjectOutputStream对象，并发送user对象
            ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
            oos.writeObject(user);
            //接收从服务端的消息(是否登录成功)
            ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
            Message message = (Message)ois.readObject();

            if (message.getMesType().equals(MessageType.MESSAGE_LOGIN_SUCCEED)){

                /**
                 * 登录成功
                 * 此时需要创建一个持有socket 的线程来维持客户端和服务端的这次通信
                 * 然后将该线程添加至客户端管理该线程集合中
                 */
                CCSThread ccst = new CCSThread(socket);
                ccst.start();
                ManageCCSThread.addCCSThread(userId, ccst);
                b = true;
            }else{
                //如果登录失败，则不创建线程，并且关闭socket
                socket.close();
//                b = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return b;
   }
   //拉取在线用户列表
    public void onlineFriendList(){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_GET_ONLINE_FRIEND);
        message.setSender(user.getUserId());
        try {
            ObjectOutputStream oos =
                    new ObjectOutputStream(ManageCCSThread.getCCSThread(user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //客户端登出
    public void logout(){
        //新建message用于通知服务端结束与该客户端相关的socket进程
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_CLIENT_EXIT);
        message.setSender(user.getUserId());    //指明客户端Id

        //获取输出流
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageCCSThread.getCCSThread(user.getUserId()).getSocket().getOutputStream());
            oos.writeObject(message);
            System.exit(0); //合法结束进程（其实System.exit(0)结束的是整个java虚拟机，即所有线程）
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
