package qqcommon;

import java.io.Serializable;

/**
 * @author Tang
 * @version 1.0
 * 通讯信息类
 */
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;
    private String sender;      //发送方
    private String receiver;    //接收方
    private String content;     //发送信息
    private String sendTime;    //发送时间
    private String mesType;     //信息类型[可以在接口中定义消息类型]

    //进行扩展和文件相关的字段
    private byte[] fileBytes;     //字节数组存放文件
    private int fileLen = 0;        //文件长度
    private String sendFilePath;    //文件发送方的文件地址
    private String receiveFilePath; //文件接收方的文件地址

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public byte[] getFileBytes() {
        return fileBytes;
    }

    public void setFileBytes(byte[] fileBytes) {
        this.fileBytes = fileBytes;
    }

    public int getFileLen() {
        return fileLen;
    }

    public void setFileLen(int fileLen) {
        this.fileLen = fileLen;
    }

    public String getSendFilePath() {
        return sendFilePath;
    }

    public void setSendFilePath(String sendFilePath) {
        this.sendFilePath = sendFilePath;
    }

    public String getReceiveFilePath() {
        return receiveFilePath;
    }

    public void setReceiveFilePath(String receiveFilePath) {
        this.receiveFilePath = receiveFilePath;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSendTime() {
        return sendTime;
    }

    public void setSendTime(String sendTime) {
        this.sendTime = sendTime;
    }

    public String getMesType() {
        return mesType;
    }

    public void setMesType(String mesType) {
        this.mesType = mesType;
    }
}
