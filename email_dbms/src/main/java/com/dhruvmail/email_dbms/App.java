package com.dhruvmail.email_dbms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Scanner;

//email packages 
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
//database package
import java.sql.*;
//pdf package
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class App {
	public static void main(String[] args) {
		
		System.out.println("preparing to send message ...");
		String message = "This is a computer generated email, it won't accept replies ";
		String subject = "This is the subject";
		//String to = "kaithdhruv@gmail.com";
		String from = "carbonatedfiji@gmail.com ";
		
		//JDBC url and user password for databsse connection
		String jdbcURL = "jdbc:postgresql://localhost:5432/email";
		String username = "postgres";
        String password = "dhruvkaith";
       
        Scanner sc= new Scanner(System.in); 

        //specifing where we want to create pdf
        String filename = "A:\\email\\pdf\\new.pdf";
        Document document = new Document();
        
        System.out.println("Enter client's name: ");
        String name= sc.nextLine();  
        System.out.println("Enter client's email: ");
        String client_email= sc.nextLine();  
       
        
 try {
        	
        	OutputStream file = new FileOutputStream(new File(filename));
            //PdfWriter.getInstance(document, new FileOutputStream(new File(filename)));
            PdfWriter.getInstance(document, file);
            document.open();
         
           Paragraph p = new Paragraph();
            p.add("Name "+ name);
          //  p.setAlignment(Element.ALIGN_CENTER);
            document.add(p);
            Paragraph p2 = new Paragraph();
            p2.add("Your email "+client_email); //no alignment
            p2.add("Recieved from CarbonatedFiji");
            document.add(p2);
            Font f = new Font();
            f.setStyle(Font.BOLD);
            f.setSize(8);
            document.add(new Paragraph("This is my paragraph 3", f));
           // document.add(Image.getInstance("A:\\sample.png"));
            document.close();
            System.out.println("PDF generated");
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
       try {
    	   Connection connection =  DriverManager.getConnection(jdbcURL, username, password);
    	   System.out.println("Connected ");
    	   
    	   String sql = "INSERT INTO record(Name, File, recipient) VALUES (?,?,?)";
    	   
    	   //Statement statement = connection.createStatement();
    	   PreparedStatement pstmt = connection.prepareStatement(sql);
    	   
    	   pstmt.setString(1, name);
    	   FileInputStream fis = new FileInputStream("A:\\email\\pdf\\new.pdf");
    	   pstmt.setBinaryStream(2, fis,fis.available());
    	   pstmt.setString(3, client_email);
    	   pstmt.executeUpdate();
    	 /*  int rows = statement.executeUpdate(sql);
    	   if (rows > 0) {
    		   System.out.print("A new entry is entered");
    	   }*/
    	   connection.close();

       } catch(SQLException | IOException e) {
    	   System.out.println("Error in connecting to databse");
    	   e.printStackTrace();
       }
	
		
		
		//sendEmail(message,subject,to,from);
		sendAttach(message,subject,client_email,from);
	}

	
	
	
	//this is responsible to send the message with attachment
	
	
	private static void sendAttach(String message, String subject, String client_email, String from) {

		//Variable for gmail
		String host="smtp.gmail.com";
		
		//get the system properties
		Properties properties = System.getProperties();
		System.out.println("PROPERTIES "+properties);
		
		//setting important information to properties object
		
		//host set
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port","465");
		properties.put("mail.smtp.ssl.enable","true");
		properties.put("mail.smtp.auth","true");
		
		//Step 1: to get the session object..
		Session session=Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {				
				return new PasswordAuthentication("carbonatedfiji@gmail.com", "f4yeV@lentine");
			}
			
			
			
		});
		
		session.setDebug(true);
		
		//Step 2 : compose the message [text,multi media]
		MimeMessage m = new MimeMessage(session);
		
		try {
		
		//from email
		m.setFrom(from);
		
		//adding recipient to message
		m.addRecipient(Message.RecipientType.TO, new InternetAddress(client_email));
		
		//adding subject to message
		m.setSubject(subject);
	
		
		//attachement..
		
		//attachment path
		String path="A:\\email\\pdf\\new.pdf";
		
		
		MimeMultipart mimeMultipart = new MimeMultipart();
		//text
		//file
		
		MimeBodyPart textMime = new MimeBodyPart();
		
		MimeBodyPart fileMime = new MimeBodyPart();
		
		try {
			
			textMime.setText(message);
			
			File file=new File(path);
			fileMime.attachFile(file);
			
			
			mimeMultipart.addBodyPart(textMime);
			mimeMultipart.addBodyPart(fileMime);
		
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		
		m.setContent(mimeMultipart);
		
		
		//send 
		
		//Step 3 : send the message using Transport class
		Transport.send(m);
		
		
		
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Sent success...................");
		
		
	}

	//this is responsible to send email..
	private static void sendEmail(String message, String subject, String client_email, String from) {
		
		//Variable for gmail
		String host="smtp.gmail.com";
		
		//get the system properties
		Properties properties = System.getProperties();
		System.out.println("PROPERTIES "+properties);
		
		//setting important information to properties object
		
		//host set
		properties.put("mail.smtp.host", host);
		properties.put("mail.smtp.port","465");
		properties.put("mail.smtp.ssl.enable","true");
		properties.put("mail.smtp.auth","true");
		
		//Step 1: to get the session object..
		Session session=Session.getInstance(properties, new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {				
				return new PasswordAuthentication("carbonatedfiji@gmail.com", "f4yeV@lentine");
			}
			
			
			
		});
		
		session.setDebug(true);
		
		//Step 2 : compose the message [text,multi media]
		MimeMessage m = new MimeMessage(session);
		
		try {
		
		//from email
		m.setFrom(from);
		
		//adding recipient to message
		m.addRecipient(Message.RecipientType.TO, new InternetAddress(client_email));
		
		//adding subject to message
		m.setSubject(subject);
	
		
		//adding text to message
		m.setText(message);
		
		//send 
		
		//Step 3 : send the message using Transport class
		Transport.send(m);
		
		System.out.println("Sent success...................");
		
		
		}catch (Exception e) {
			e.printStackTrace();
		}
			
	}
}