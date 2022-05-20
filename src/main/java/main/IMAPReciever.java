package main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class IMAPReciever {
	public void recieve(final String username,final String password)
	{
		Scanner in = new Scanner(System.in);
		Folder folder = null;
	    Store store = null;
	    try 
	    {
	    	Properties props = System.getProperties();
	    	
	    	props.setProperty("mail.store.protocol", "imap");
	    	props.put("mail.smtp.host", "smtp.gmail.com");
	        props.put("mail.smtp.port", "465");
	        props.put("mail.smtp.auth", "true");
	        props.put("mail.smtp.socketFactory.port", "465");
	        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

	    	Session session = Session.getDefaultInstance(props, null);
	      
	    	store = session.getStore("imaps");
	    	store.connect("imap.gmail.com", username, password);
	    	folder = store.getFolder("Inbox");
	    	folder.open(Folder.READ_WRITE);
	      
	    	Message messages[] = folder.getMessages();
	    	System.out.println("No of Messages : " + folder.getMessageCount());
	    	System.out.println("No of Unread Messages : " + folder.getUnreadMessageCount());
	      
	    	for (int i=0; i < messages.length; ++i) 
	    	{
	    		System.out.println("MESSAGE #" + (i + 1) + ":");
	    		Message msg = messages[i];
	    		System.out.println(msg.getSubject() + " from "+ msg.getFrom()[0]);
	    		saveParts(msg.getContent(),"C:\\Users\\strio\\Desktop\\IMAP\\" + msg.getSubject());
	    		
	    		System.out.print("Do you want to reply [y/n] : ");
	    		String s = in.nextLine();
	    		if ("Y".equals(s) || "y".equals(s)) 
	    		{
	                  Message replyMessage = new MimeMessage(session);
	                  replyMessage = (MimeMessage) msg.reply(false);
	                  String to = InternetAddress.toString(msg.getRecipients(Message.RecipientType.TO));
	                  replyMessage.setFrom(new InternetAddress(to));
	                  System.out.print("Write your answer:");
	                  s = in.nextLine();
	                  BodyPart messageBodyPart = new MimeBodyPart(); 
	                  messageBodyPart.setText(s);
	                  
	                  Multipart multipart = new MimeMultipart();
	                  multipart.addBodyPart(messageBodyPart);
	                  
	                  System.out.println("Doriti sa adaugati attachment?");
	                  s = in.nextLine();
	                  if ("Y".equals(s) || "y".equals(s)) 
	                  {
	                	 System.out.println("Path:");
	          			 s = in.nextLine();
	          			 MimeBodyPart attachmentPart = new MimeBodyPart();
	          			 attachmentPart.attachFile(new File(s));
	          			 multipart.addBodyPart(attachmentPart);
	          		  }    
	                  
	                  replyMessage.setReplyTo(msg.getReplyTo());
	                  replyMessage.setContent(multipart);

	                  Transport t = session.getTransport("smtp");

	                  t.connect(username, password);
	                  t.sendMessage(replyMessage, replyMessage.getAllRecipients());
	                  
	                  t.close();
	        
	                  System.out.println("message replied successfully ....");
	    		}
	    	}
	    	folder.close();
	    	store.close();
	    }
	    catch(Exception e)
	    {
	    	e.printStackTrace();
	    }
	}
	public static void saveParts(Object content, String filename)throws Exception
	{
		OutputStream out = null;
		InputStream in = null;
		try 
		{
			if(content instanceof Multipart) 
			{
			    Multipart multi = ((Multipart)content);
			    int parts = multi.getCount();
			    
			    for (int j=0; j < parts; ++j) 
			    {
			    	MimeBodyPart part = (MimeBodyPart)multi.getBodyPart(j);
			    	if(part.getContent() instanceof Multipart) 
			    	{
			    		saveParts(part.getContent(), filename);
			    	}
			    	else 
			    	{
			    		String extension = "";
			        
				        if (part.isMimeType("text/html")) 
				        {
				          extension = "html";
				        }
				        else 
				        {
				          if (part.isMimeType("text/plain")) 
				          {
				            extension = "txt";
				          }
				          else 
				          {
				            extension = part.getDataHandler().getName();
				          }
				          
				          filename = filename + "." + extension;
				              
				              out = new FileOutputStream(new File(filename));
				              in = part.getInputStream();
				              
				              int k;
				              
				              while ((k = in.read()) != -1) 
				              {
				                out.write(k);
				              }
				        }
			        }
			    }
			}
		}
		finally 
		{
			if (in != null) 
			{ 
				in.close(); 
			}
			if(out != null)
			{ 
				out.flush(); 
				out.close(); 
			}
		}
	}
}
