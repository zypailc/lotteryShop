package com.didu.lotteryshop.common.service;

import com.didu.lotteryshop.common.entity.MailCommonProperties;
import com.didu.lotteryshop.common.entity.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.Socket;
import java.util.Base64;

@Service
public class MailService {

    //@Autowired
    //private MailProperties mailProperties;
    @Autowired
    private MailCommonProperties mailProperties;

    private String userName;

    private String password;

    private String host;

    private Socket socket;

    private BufferedReader bufferedReader;

    private PrintWriter printWriter;

    public void sendSimpleMailMember(Member member, String subject, String text) {
        sendSimpleMail(member.getEmail(),subject,text);
    }

    public void sendSimpleMail(String email,String subject,String text){
        try
        {
            host = mailProperties.getHost();
            userName = mailProperties.getUsername();
            password = mailProperties.getPassword();
            Integer port = mailProperties.getPort();
            this.socket = new Socket(host, port);
            bufferedReader = this.getReader(socket);
            printWriter = this.getWriter(socket);


            writeCommandStream(null);
            //按照命令行发送邮件的顺序与smtp服务器进行交流
            writeCommandStream("helo hello");//与smtp服务器进行对话
            writeCommandStream("auth login");//登录命令
            //用户名和密码都是用base64进行编码了的，不是普通的字符串
            writeCommandStream(Base64.getEncoder().encodeToString(userName.getBytes()));//登录用户用户名
            writeCommandStream(Base64.getEncoder().encodeToString(password.getBytes()));//密码

            //登录成功之后，设置发件人
            //writeCommandStream("mail from:<" + mailProperties.getUsername() + ">");//设置发件人，xxxxxx为真实的邮件发送源地址，如xxx@qq.com这种邮箱地址
            writeCommandStream("mail from:<" + mailProperties.getUsername() + ">");
            //设置收件人，可以设置多个，所以采用遍历方式进行设置
            //参数reciver里面装了所有收件人的邮箱地址，多个邮箱用","号分隔，所以我用逗号拆分
            /*for (String oneReciver : reciver.split(","))
            {
                writeCommandStream("rcpt to:" + oneReciver);
            }*/
            //设置邮件发送地址
            writeCommandStream("rcpt to:<"+email+">");
            //开始输入邮件内容
            writeCommandStream("data");//邮件内容，在输入命令data之后开始

            //这个地方就是伪造邮件发件人的时候，from之后的字符串任意填，
            //填了之后，收到邮件的人，会看到以这个名字发送的邮件，但是他不能回复，因为这个是伪造的地址，无效的。
            printWriter.println("from:" + "COLORBALL@COLORBALL.COM");
            //收件人，格式和抄送者一样
            printWriter.println("to:" + email);
            //这是抄送者，同收件人一样，可以设置多个，中间用,号分隔
            //比如：xxx@qq.com,xxxxxx@qq.com,xxxx@qq.com
            printWriter.println("Cc:" + "COLORBALL");

            //设置邮件主题
            printWriter.println("subject:" + subject);
            //设置邮件正文
            //注意下面这个设置类型的，这一句代码是必须的，不然你发的邮件的正文内容是不会存在的
            //笔者最开始没有设置邮件正文类型，发了很多封，但是每一封邮件的正文内容都为空，后来才发现必须加上这个
            printWriter.println("Content-Type:text/html;");//这个是HTML格式的邮件正文，如果是纯文本，用text/plain
            //注意这个空行是必须的，设置好了类型，需要空一行再起一行输入正文内容
            printWriter.println();

            printWriter.println(text);

            /*printWriter.println("Your account number is : " + member.getEmail() + "<br/>");
            printWriter.println("Your password is : "+ text);

            printWriter.println();*/
            //结束邮件发送"."命令
            writeCommandStream(".");
            //关闭
            writeCommandStream("quit");

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            try
            {
                printWriter.close();
                bufferedReader.close();
                socket.close();
            }
            catch (Exception e2)
            {
                e2.printStackTrace();
            }
        }
    }




    private PrintWriter getWriter(Socket socket) throws IOException
    {
        OutputStream socketOut = socket.getOutputStream();
        return new PrintWriter(socketOut, true); //注意设置为true
    }

    private BufferedReader getReader(Socket socket) throws IOException
    {
        InputStream socketIn = socket.getInputStream();
        return new BufferedReader(new InputStreamReader(socketIn));
    }

    private void writeCommandStream(String command) throws IOException
    {
        if (command != null)
        {
            printWriter.println(command);
            printWriter.flush();
            //System.out.println("客户端命令行信息→" + command);
        }
        char[] serviceResponse = new char[1024];

        bufferedReader.read(serviceResponse);
        //System.out.println("服务器响应→" + new String(serviceResponse));
    }

}
