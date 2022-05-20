package qqclient.service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.text.SimpleDateFormat;

/**
 * @author Tang
 * @version 1.0
 * 用于维持通信的线程类(Client Content Server)
 */
public class CCSThread extends Thread{
    private Socket socket;

    public CCSThread(Socket socket) {
        this.socket = socket;
    }

    public Socket getSocket() {
        return socket;
    }

    @Override
    public void run() {
        //因为需要保持通信状态，使用while循环

        while(true){
            try {
                System.out.println("客户端线程，等待从服务端接收消息");
                ObjectInputStream ois = new ObjectInputStream(socket.getInputStream());
                Message message = (Message)ois.readObject();
                //对接收到的message进行处理
                if (message.getMesType().equals(MessageType.MESSAGE_RETURN_ONLINE_FRIEND)){ //返回在线用户列表
                    String[] onlineUser = message.getContent().split(" ");  //默认返回的用户列表用空格" "隔开
                    System.out.println("\n======在线用户列表======");
                    for (int i = 0; i < onlineUser.length; i++) {
                        System.out.println("用户 :" + onlineUser[i]);
                    }
                }else if(message.getMesType().equals(MessageType.MESSAGE_COM_MESS)){    //普通聊天信息
                    System.out.println("\n"+ message.getSendTime() + "\n\t" + message.getSender() + "对" + message.getReceiver()
                            + "说：" + message.getContent());   //展示收到的消息内容
                }else if (message.getMesType().equals(MessageType.MESSAGE_ToALL_MESSAGE)){  //群发消息
                    System.out.println("\n"+ message.getSendTime() + "\n\t" + message.getSender() + "对大家说：" + message.getContent());   //展示收到的消息内容
                }else if(message.getMesType().equals(MessageType.MESSAGE_FILE_MESSAGE)){
                    System.out.println("\n" + message.getSender() + "向我方电脑发送了一个文件："
                            + message.getSendFilePath() + "到" + message.getReceiveFilePath());
                    FileOutputStream fileOutputStream = new FileOutputStream(message.getReceiveFilePath());
                    fileOutputStream.write(message.getFileBytes());
                    fileOutputStream.close();

                    System.out.println("\n保存文件成功");
                } else{
                    System.out.println("其他消息类型，暂不处理");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
