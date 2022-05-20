package qqserver.service;

import qqcommon.Message;
import qqcommon.MessageType;
import qqcommon.User;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Tang
 * @version 1.0
 * 创建SCCT线程类(Server Content Client Thread),和某个客户端保持通信
 */
@SuppressWarnings("all")
public class SCCT extends Thread{
    private Socket socket;
    private String userId;

    public SCCT(Socket socket, String userId) {
        this.socket = socket;
        this.userId = userId;
    }

    public Socket getSocket() {
        return socket;
    }

    public String getUserId() {
        return userId;
    }

    @Override
    public void run() {
        while(true){
            try {
                System.out.println("服务端和客户端"+userId+"保持通信");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message)ois.readObject();
                //针对Message类型，做相应的业务处理
                if(message.getMesType().equals(MessageType.MESSAGE_GET_ONLINE_FRIEND)){ //拉取在线列表
                    System.out.println(message.getSender() + "需要拉取在线列表");
                    String onlineFriendList = ManageSCCT.getOnlineFriendList();
                    //创建一个新的Message对象用于返回
                    Message message2 = new Message();
                    message2.setMesType(MessageType.MESSAGE_RETURN_ONLINE_FRIEND);
                    message2.setContent(onlineFriendList);
                    message2.setReceiver(message.getSender());
                    //返回给客户端
                    ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
                    oos.writeObject(message2);
                }else if(message.getMesType().equals(MessageType.MESSAGE_COM_MESS)){    //客户端之间通信
                    ObjectOutputStream oos = new ObjectOutputStream(ManageSCCT.getSCCT(message.getReceiver()).getSocket().getOutputStream());
                    oos.writeObject(message);   //转发，如果客户不线，可以保存到数据库以实现离线留言，这里不做这一步
                }
                else if(message.getMesType().equals(MessageType.MESSAGE_CLIENT_EXIT)){ //客户端退出
                    System.out.println(message.getSender() + "退出");
                    ManageSCCT.removeSCCT(message.getSender());

                    socket.close(); //关闭该通道的socket
                    break;
                }else if(message.getMesType().equals(MessageType.MESSAGE_ToALL_MESSAGE)){
                    Set<String> strings = ManageSCCT.getHashMap().keySet();
                    Iterator<String> iterator = strings.iterator();
                    while (iterator.hasNext()) {
                        String sender = iterator.next();
                        if (!(sender.equals(message.getSender()))){
                            ObjectOutputStream oos = new ObjectOutputStream(ManageSCCT.getSCCT(sender).getSocket().getOutputStream());
                            oos.writeObject(message);
                        }
                    }

                }else if(message.getMesType().equals(MessageType.MESSAGE_FILE_MESSAGE)){
                    ObjectOutputStream oos = new ObjectOutputStream(ManageSCCT.getSCCT(message.getReceiver()).getSocket().getOutputStream());
                    oos.writeObject(message);

                }else{
                    System.out.println("其他类型message暂时不做处理");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
