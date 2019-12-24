package com.cxgz.activity.cx.msg.email;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

/**
 * 接收邮件
 * @author xiaoli
 */
public class MailReceiver {


    /**
     * 保存附件
     */
    public String saveAttchMent(Part part,String path,Map<String,String> map) throws MessagingException, IOException {
        String ids = "";
        String filename = "";
        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++) {
                String contentId = "";
                BodyPart mpart = mp.getBodyPart(i);
                String dispostion = mpart.getDisposition();
                String[] contentIds = mpart.getHeader("Content-ID");
                if(contentIds!=null){
                    contentId = contentIds[0];
                }
                if ((dispostion != null)
                        && (dispostion.equals(Part.ATTACHMENT)
                        ||dispostion.equals(Part.INLINE))) {
                    filename = mpart.getFileName();
                    if (filename != null) {
                        filename = MimeUtility.decodeText(filename);
                        ids+=","+saveFile(path,contentId,filename, mpart.getInputStream(),map);
                    }
                }else if (mpart.isMimeType("multipart/*")) {
                    saveAttchMent(mpart,path,map);
                }else {
                    filename = mpart.getFileName();
                    if (filename != null) {
                        filename = MimeUtility.decodeText(filename);
                        ids+=","+saveFile(path,contentId,filename, mpart.getInputStream(),map);
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            saveAttchMent((Part) part.getContent(),path,map);
        }
        return ids;
    }
    /**
     * 保存文件内容
     */
    private String saveFile(String path,String contentId,String filename,InputStream inputStream,Map<String,String> map)
            throws IOException {
        String srcName = filename;

        int i = 1;
        File filePath = new File(path);
        if(!filePath.exists()){
            filePath.mkdirs();
        }
        String dirPath = path+filename;
        File dirFile = new File(dirPath);
        if(dirFile.exists()){
            while(true){
                srcName = "("+i+")"+filename;
                dirPath = path+srcName;
                dirFile = new File(dirPath);
                if(dirFile.exists()){
                    i++;
                }else{
                    dirFile.createNewFile();
                    break;
                }
            }
        }else{
            dirFile.createNewFile();
        }

        if(filename!=null&&!filename.equals("")&&contentId!=null&&!contentId.equals("")){
            contentId = contentId.substring(1,contentId.length()-1);
            map.put(contentId, srcName);
        }

        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;

        try {
            bos = new BufferedOutputStream(new FileOutputStream(dirFile));
            bis = new BufferedInputStream(inputStream);
            int c;
            while ((c = bis.read()) != -1) {
                bos.write(c);
                bos.flush();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            bos.close();
            bis.close();
        }
        return dirPath.substring(dirPath.lastIndexOf("/")+1,dirPath.length());
    }

    /**
     * 判断是是否包含附件
     */
    public boolean isContainAttch(Part part) throws MessagingException,
            IOException {
        boolean flag = false;
        if (part.isMimeType("multipart/*")) {
            Multipart multipart = (Multipart) part.getContent();
            int count = multipart.getCount();
            for (int i = 0; i < count; i++) {
                BodyPart bodypart = multipart.getBodyPart(i);
                String dispostion = bodypart.getDisposition();
                if ((dispostion != null)
                        && (dispostion.equals(Part.ATTACHMENT) || dispostion
                        .equals(Part.INLINE))) {
                    flag = true;
                } else if (bodypart.isMimeType("multipart/*")) {
                    flag = isContainAttch(bodypart);
                } else {
                    String conType = bodypart.getContentType();
                    if (conType.toLowerCase().indexOf("appliaction") != -1) {
                        flag = true;
                    }
                    if (conType.toLowerCase().indexOf("name") != -1) {
                        flag = true;
                    }
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            flag = isContainAttch((Part) part.getContent());
        }

        return flag;
    }
    /**
     * POP3协议此方法无效
     * 判断此邮件是否已读，如果未读则返回false，已读返回true
     */
    public boolean isNew(MimeMessage msg) throws MessagingException {
        boolean isnew = false;

        Flags flags = msg.getFlags();
        Flags.Flag[] flag = flags.getSystemFlags();
        //System.out.println("flags's length:" + flag.length);
        for (int i = 0; i < flag.length; i++) {
            if (flag[i] == Flags.Flag.SEEN) {
                isnew = true;
                break;
            }
        }

        return isnew;
    }
    /**
     * 获取此邮件的message-id
     */
    public String getMessageId(MimeMessage msg) throws MessagingException {
        return msg.getMessageID();
    }
    /**
     * 判断邮件是否需要回执，如需回执返回true，否则返回false
     */
    public boolean getReplySign(MimeMessage msg) throws MessagingException {
        boolean replySign = false;
        String needreply[] = msg.getHeader("Disposition-Notification-TO");
        if (needreply != null) {
            replySign = true;
        }
        return replySign;
    }

    /**
     * 获取邮件发送日期
     */
    public String getSendDate(MimeMessage msg,String format) throws MessagingException {
        Date sendDate = msg.getSentDate();
        SimpleDateFormat smd = new SimpleDateFormat(format);
        return smd.format(sendDate);
    }
    /**MimeMessageMimeMessage
     * 获取邮件主题
     */
    public String getSubject(MimeMessage msg) throws UnsupportedEncodingException,
            MessagingException {
        String subject = "";
        try{
            subject = MimeUtility.decodeText(msg.getSubject());
            if (subject == null) { //MimeUtility.decodeText(MimeUtility.unfold(rawvalue));
                subject = "";
            }
        } catch (Exception exce) {
            exce.printStackTrace();
        }
        return subject;
    }


    /**
     * 解析邮件 主要根据MimeType的不同执行不同的操作，一步一步的解析
     * 将得到的邮件内容保存到一个stringBuffer对象中.
     */
    public void getMailContent(Part part,StringBuffer htmlContent) throws Exception {
        String contentType = part.getContentType();
        int nameIndex = contentType.indexOf("name");
        Object obc = part.getContent();

        if (part.isMimeType("text/html") && nameIndex==-1) {
            if(obc instanceof String){
                htmlContent.append((String)part.getContent());
            }
        } else if (part.isMimeType("multipart/*")) {
            if(obc instanceof Multipart) {
                Multipart multipart = (Multipart) part.getContent();
                int counts = multipart.getCount();
                for (int i = 0; i < counts; i++) {
                    getMailContent(multipart.getBodyPart(i), htmlContent);
                }
            }
        } else if (part.isMimeType("message/rfc822")) {
            getMailContent((Part) part.getContent(),htmlContent);
        }
    }

    /**
     * 根据所传递的参数获取邮件收件人，抄送，密送的地址和信息。
     * type 参数如下：
     * "to"获取收件人
     * "cc"获取抄送人地址
     * "bcc"获取密送地址
     */
    public String getMailAddress(MimeMessage msg,String type) throws MessagingException,
            UnsupportedEncodingException {
        String mailaddr = "";
        String addrType = type.toUpperCase();
        InternetAddress[] address = null;

        if (addrType.equals("TO") || addrType.equals("CC")
                || addrType.equals("BCC")) {
            if (addrType.equals("TO")) {
                address = (InternetAddress[]) msg
                        .getRecipients(Message.RecipientType.TO);
            }
            if (addrType.equals("CC")) {
                address = (InternetAddress[]) msg
                        .getRecipients(Message.RecipientType.CC);
            }
            if (addrType.equals("BCC")) {
                address = (InternetAddress[]) msg
                        .getRecipients(Message.RecipientType.BCC);
            }

            if (address != null) {
                for (int i = 0; i < address.length; i++) {
                    String mail = address[i].getAddress();
                    if (mail == null) {
                        mail = "";
                    } else {
                        mail = MimeUtility.decodeText(mail);
                    }
                    String personal = address[i].getPersonal();
                    if (personal == null) {
                        personal = "";
                    } else {
                        personal = MimeUtility.decodeText(personal);
                    }
                    String compositeto = personal + "<" + mail + ">";
                    mailaddr += "," + compositeto;
                }
                mailaddr = mailaddr.substring(1);
            }
        } else {
            throw new RuntimeException("Error email Type!");
        }
        return mailaddr;
    }
    /**
     * 获取发送邮件者信息
     */
    public String getFrom(MimeMessage msg) throws MessagingException {
        InternetAddress[] address = (InternetAddress[]) msg.getFrom();
        String from = address[0].getAddress();
        if (from == null) {
            from = "";
        }
        String personal = address[0].getPersonal();
        if (personal == null) {
            personal = "";
        }
        String fromaddr = personal + "<" + from + ">";
        return fromaddr;
    }
}
