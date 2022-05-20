package main;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import java.io.File;
import java.util.Properties;
import java.util.Scanner;

public class EmailSender {
	public void sendEmail(final String username,final String password)
	{
        try {

			Scanner in = new Scanner(System.in);
			System.out.println("Eneter your recipient");
			String recipient = in.nextLine();
			System.out.println("Eneter subject");
			String subject = in.nextLine();
			System.out.println("Enter email text");
			String text = in.nextLine();
			
			
			Properties prop = new Properties();
			prop.put("mail.smtp.host", "smtp.gmail.com");
	        prop.put("mail.smtp.port", "465");
	        prop.put("mail.smtp.auth", "true");
	        prop.put("mail.smtp.socketFactory.port", "465");
	        prop.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	        
	        Session session = Session.getInstance(prop,
	                new javax.mail.Authenticator() {
	                    protected PasswordAuthentication getPasswordAuthentication() {
	                        return new PasswordAuthentication(username, password);
	                    }
	                });

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("pentruvasilii@gmail.com"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(recipient)
            );
            message.setSubject(subject);
            
            BodyPart messageBodyPart = new MimeBodyPart(); 
            messageBodyPart.setText(text);
            
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);
            
            System.out.println("Doriti sa adaugati attachment?");
            String s = in.nextLine();
    		if ("Y".equals(s) || "y".equals(s)) 
    		{
    			System.out.println("Path:");
    			 s = in.nextLine();
    			 MimeBodyPart attachmentPart = new MimeBodyPart();
    			 attachmentPart.attachFile(new File(s));
    			 multipart.addBodyPart(attachmentPart);
    		}                     
            
            message.setContent(multipart);

            Transport.send(message);

            System.out.println("Done");

        } catch (Exception e) {
            e.printStackTrace();
        }
	}
	

}
