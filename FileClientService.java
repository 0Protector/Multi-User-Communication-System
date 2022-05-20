package qqclient.service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author Tang
 * @version 1.0
 * 用于文件处理的服务类
 */
@SuppressWarnings("all")
public class FileClientService {
    /**
     *
     * @param src   源文件路径
     * @param dest  文件目的地
     * @param send  文件发送方
     * @param receiver  文件接收方
     */
    public void sendFileToOne(String src, String dest, String sender, String receiver ){
        /**
         * 该方法有两块
         * 1.获取源文件，并且转换为字节数组
         * 2.将字节数组包装为message对象发送至服务端
         */
        SimpleDateFormat sdf = new SimpleDateFormat(); //用于格式化时间
        Message message = new Message();
        message.setMesType(MessageType.MESSAGE_FILE_MESSAGE);
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setSendFilePath(src);
        message.setReceiveFilePath(dest);
        message.setSendTime(sdf.format(new Date()).toString());

        //文件读取
        FileInputStream fileInputStream = null;
        byte[] fileBytes = new byte[(int)new File(src).length()];
        int bufLen = 0;
        try {
            fileInputStream = new FileInputStream(src);
            fileInputStream.read(fileBytes);    //将src文件读入到fileBytes中
            message.setFileBytes(fileBytes);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (fileInputStream != null){
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        //提示信息
        System.out.println("\n" + sender + "将文件" + src + "发送至" + receiver
                            + "的电脑磁盘目录" + dest);

        //将message 发送到 服务端
        try {
            ObjectOutputStream oos = new ObjectOutputStream(ManageCCSThread.getCCSThread(sender).getSocket().getOutputStream());
            oos.writeObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
