package qqcommon;

/**
 * @author Tang
 * @version 1.0
 * 消息类型
 */
public interface MessageType {
    String MESSAGE_LOGIN_SUCCEED = "1"; //登陆成功
    String MESSAGE_LOGIN_FAIL = "2";    //登录失败
    String MESSAGE_COM_MESS = "3";      //普通消息包
    String MESSAGE_GET_ONLINE_FRIEND = "4"; //拉取在线用户
    String MESSAGE_RETURN_ONLINE_FRIEND = "5"; //返回在线用户
    String MESSAGE_CLIENT_EXIT = "6"; //客户端请求退出
    String MESSAGE_ToALL_MESSAGE = "7"; //群发消息
    String MESSAGE_FILE_MESSAGE = "8"; //文件消息

}
