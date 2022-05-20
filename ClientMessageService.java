package qqclient.service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


/**
 * @author Tang
 * @version 1.0
 * 用于处理客户端的消息发送与接收
 */
public class ClientMessageService {
    SimpleDateFormat sdf = new SimpleDateFormat();  //用于格式化时间
    /**
     *
     * @param content   要发送的消息内容
     * @param sender    消息发送方
     * @param getter    消息接收方
     */
    public void sendMessageToFriend(String content, String sender, String getter){
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_COM_MESS);
        message.setContent(content);
        message.setSender(sender);
        message.setReceiver(getter);
        message.setSendTime(sdf.format(new Date()).toString());

        System.out.println(sender + "对" + getter + "发送了" + content);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageCCSThread.getCCSThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 群发消息
     * @param content   内容
     * @param sender    发送方
     */
    public void sendMessageToOther(String content, String sender){

        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_ToALL_MESSAGE);
        message.setSender(sender);
        message.setSendTime(sdf.format(new Date()).toString());
        message.setContent(content);

        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageCCSThread.getCCSThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
            System.out.println(sender + "对大家说 : " + content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
