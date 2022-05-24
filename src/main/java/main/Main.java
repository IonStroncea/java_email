package main;

import java.util.*;
import javax.mail.*;

public class Main {
	public static void main(String args[])
	{
		Scanner in = new Scanner(System.in);
		String username = "***";
		String password = "***";
		String path = "";
		String s = "";
		
		while(!s.equals("exit"))
		{
			System.out.println("1. Send email");
			System.out.println("2. Read emails pop3");
			System.out.println("3. Read email imap");

			s = in.nextLine();
			
			if(s.equals("1"))
			{
				EmailSender sender = new EmailSender();
				sender.sendEmail(username,password);
			}
			else if(s.equals("2"))
			{
				POP3Reciever reciever = new POP3Reciever();
				reciever.recieve(username, password, path);
			}
			else if(s.equals("3"))
			{
				IMAPReciever reciever = new IMAPReciever();
				reciever.recieve(username, password, path);
			}
		}

	}

}
