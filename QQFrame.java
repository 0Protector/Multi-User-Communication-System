package qqframe;

import qqserver.service.QQServer;

/**
 * @author Tang
 * @version 1.0
 * 该类创建QQServer，后台启动客户端
 */
public class QQFrame {
    public static void main(String[] args) {
        new QQServer();
    }
}
