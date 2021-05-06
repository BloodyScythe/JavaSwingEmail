package com.dhruvmail.email_dbms;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
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
		//String message = "This is a computer generated email, it won't accept replies ";
		//String subject = "This is the subject";
		//String to = "kaithdhruv@gmail.com";
		String from = "carbonatedfiji@gmail.com ";
		
		//JDBC url and user password for databsse connection
		String jdbcURL = "jdbc:postgresql://localhost:5432/email";
		String username = "postgres";
        String password = "dhruvkaith";
       
        Scanner sc= new Scanner(System.in); 

        //specifing where we want to create pdf
       // String filename = "A:\\email\\pdf\\new.pdf";
       // Document document = new Document();
        
        System.out.println("Enter client's name: ");
        String name= sc.nextLine();  
        System.out.println("Enter client's email: ");
        String client_email= sc.nextLine();  
        System.out.println("Enter subject of Email ");
        String subject= sc.nextLine();  
        System.out.println("Enter message of mail ");
        String message= sc.nextLine();  
        
 try {
        	
	    String filename = "A:\\email\\pdf\\invoice.pdf";
		Rectangle pageSize = new Rectangle(780, 525);
		Document document = new Document(pageSize);
		OutputStream file = null;
		try {
			file = new FileOutputStream(new File(filename));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		PdfWriter.getInstance(document, file);
		//PdfWriter.getInstance(document, response.getOutputStream());
		document.open();
		float[] colsWidth1 = {1f, 1f, 1f,1f,1f}; // Code 1
		//Image image = Image.getInstance(path+"employee/payslip/view/fly-hind.jpg");
		PdfPTable table = new PdfPTable(colsWidth1);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(100); // Code 2
		table.setHorizontalAlignment(Element.ALIGN_LEFT);//Code 3
		table.addCell("");
		table.addCell("");
		table.addCell("This is a genaric PDF also");
		table.addCell("");
		table.addCell("");
		document.add(table);
		document.add( Chunk.NEWLINE );
		document.add( Chunk.NEWLINE );
		float[] colsWidth_main = {1f, 1f, 1f}; // Code 1
		table = new PdfPTable(colsWidth_main);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(100); // Code 2
		table.setHorizontalAlignment(Element.ALIGN_LEFT);//Code 3
		table.addCell("");
		try {
			table.addCell(Image.getInstance("A:\\email\\images\\birds.jpg"));
		} catch (BadElementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		table.addCell("");
		document.add(table);
		float[] colsWidth = {1f, 1f, 1f, 1f}; // Code 1
		table = new PdfPTable(colsWidth);
		table.getDefaultCell().setBorder(0);
		table.setWidthPercentage(100); // Code 2
		table.setHorizontalAlignment(Element.ALIGN_LEFT);//Code 3
		//table.addCell("Employee ID");
		//table.addCell("00000");
		table.addCell("Name");
		table.addCell(name);
		table.addCell("Recieved from");
		table.addCell("carbonatedfiji");
		table.addCell("00000");
		table.addCell("00000");
		table.addCell("Email of sender");
		table.addCell("CarbonatedFiji@gmail.com");
		table.addCell("This is a gernaric table heading");
		table.addCell("0000");
		table.addCell("This is a gernaric table heading");
		table.addCell("0000");

		
		document.add(table);
		document.add( Chunk.NEWLINE );
		document.add( Chunk.NEWLINE );
		
		Paragraph p = new Paragraph();
		//p.add("This is a genaric sentance");
		
     //  p.setAlignment(Element.ALIGN_CENTER);
     document.add(p);
     Paragraph p2 = new Paragraph();
     p2.add("The birds on the top are a JPG "); //no alignment
 	document.add( Chunk.NEWLINE );
    
     document.add(p2);
	
		document.close();

            System.out.println("PDF generated");
        } catch (Exception e) {
            e.printStackTrace();
        } 
        
       try {
    	   Connection connection =  DriverManager.getConnection(jdbcURL, username, password);
    	   System.out.println("Connected ");
    	   
    	   String sql = "INSERT INTO record(Name, File, recipient, subject, message) VALUES (?,?,?,?,?)";
    	   
    	   //Statement statement = connection.createStatement();
    	   PreparedStatement pstmt = connection.prepareStatement(sql);
    	   
    	   pstmt.setString(1, name);
    	   FileInputStream fis = new FileInputStream("A:\\email\\pdf\\new.pdf");
    	   pstmt.setBinaryStream(2, fis,fis.available());
    	   pstmt.setString(3, client_email);
    	   pstmt.setString(4, subject);
    	   pstmt.setString(5, message);
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