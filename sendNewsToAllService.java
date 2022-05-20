package qqserver.service;

import qqcommon.Message;
import qqcommon.MessageType;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

/**
 * @author Tang
 * @version 1.0
 */
public class sendNewsToAllService extends Thread {
    private Scanner scanner = new Scanner(System.in);

    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat();  //用于格式化时间
        while(true) {
            System.out.println("请输入要推送的新闻(不再推送新闻，请输入 exit):");
            String news = scanner.next();
            if (news.equals("exit")){
                break;
            }
            Message message = new Message();
            message.setMesType(MessageType.MESSAGE_ToALL_MESSAGE);
            message.setSender("服务器");
            message.setContent(news);
            message.setSendTime(sdf.format(new Date()).toString());
            System.out.println("服务器推送消息给所有人说:" + news);

            Iterator<String> iterator = ManageSCCT.getHashMap().keySet().iterator();
            while (iterator.hasNext()) {
                String getter = iterator.next();
                try {
                    ObjectOutputStream oos = new ObjectOutputStream(ManageSCCT.getSCCT(getter).getSocket().getOutputStream());
                    oos.writeObject(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
