package qqclient.view;

import qqclient.service.ClientMessageService;
import qqclient.service.FileClientService;
import qqclient.service.UserClientService;

import java.util.Scanner;

/**
 * @author Tang
 * @version 1.0
 * 客户端菜单界面
 */
@SuppressWarnings("all")
public class QQView {
    private boolean loop = true;    //用于控制是否显示菜单
    private String key = "";        //用于获取用户输入
    private UserClientService userClientService = new UserClientService();  //用于使用客户端基本操作
    private ClientMessageService clientMessageService = new ClientMessageService(); //用于客户端之间的消息通信
    private FileClientService fileClientService = new FileClientService();  //用于客户端之间的文件传输

    public static void main(String[] args) {
        new QQView().mainView();
        System.out.println("客户端退出系统");
    }
    public void mainView(){

        Scanner scanner = new Scanner(System.in);

        while(loop){
            System.out.println("=======欢迎登录多用户通讯系统=======" +
                    "\n\t\t1.登录系统" +
                    "\n\t\t9.退出系统");
            System.out.print("请输入选项：");
            key = scanner.next();

            switch (key){
                case "1":
                    System.out.print("请输入用户名：");
                    String userID = scanner.next();
                    System.out.print("请输入密  码：");
                    String password = scanner.next();
                    /**
                     * 需要将以上信息封装进一个user对象，
                     * 并且传输至服务端来判断，该用户对象是否合法
                     */
                    if (userClientService.checkUser(userID, password)){
                        System.out.println("欢迎用户" + userID + "进入系统");
                        while(loop){
                            System.out.println("=======多用户通信系统二级菜单(用户" +
                                    userID + ")=======" +
                                    "\n\t\t1.显示在线用户列表" +
                                    "\n\t\t2.群发消息" +
                                    "\n\t\t3.私聊消息" +
                                    "\n\t\t4.发送文件" +
                                    "\n\t\t9.退出系统");
                            System.out.println("请输入选项：");
                            key = scanner.next();
                            switch (key){
                                case "1":
//                                    System.out.println("显示在线用户列表");
                                    userClientService.onlineFriendList();
                                    break;
                                case "2":
                                    /**
                                     * 此处默认为给出自身以外的所有在线客户发送消息
                                     * 如果向指定，可以写一个重载方法，并且传入一个包含Id的集合/对象即可
                                     */
                                    System.out.println("群发消息");
                                    System.out.println("请输入要群发的消息");
                                    String content = scanner.next();
                                    clientMessageService.sendMessageToOther(content, userID);
                                    break;
                                case "3":
                                    System.out.println("私聊消息");
                                    System.out.println("请输入消息的接收方(在线)");
                                    String getter = scanner.next();
                                    System.out.println("请输入消息内容");
                                    content = scanner.next();
                                    clientMessageService.sendMessageToFriend(content, userID, getter);
                                    break;
                                case "4":
                                    System.out.println("发送文件");
                                    System.out.print("请输入要把发送文件发送给谁？:");
                                    getter = scanner.next();
                                    System.out.print("请输入发送文件的路径(形式如d:\\xxx.jpg):");
                                    String srcPath = scanner.next();
                                    System.out.print("请输入接收文件的路径(形式如d:\\xxx.jpg):");
                                    String destPath = scanner.next();
                                    fileClientService.sendFileToOne(srcPath, destPath, userID, getter);
                                    break;
                                case "9":
                                    userClientService.logout();
                                    loop = false;
                                    break;
                            }
                        }
                    }else{  //登录服务器失败
                        System.out.println("=======登录失败=======");
                    }
                    break;
                case "9":
                    loop = false;
                    break;
            }
        }
    }
}
