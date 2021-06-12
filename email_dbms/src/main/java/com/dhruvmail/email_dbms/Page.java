package com.dhruvmail.email_dbms;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
//database package
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.JTextArea;
import javax.swing.JTextField;

//pdf package
import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class Page implements ActionListener{
	

	
	JFrame frame = new JFrame();
	JButton sendBtn = new JButton("send");
	JButton resetButton = new JButton("Reset");
	JTextField clientNameTxt = new JTextField();
	JTextField clientEmailTxt = new JTextField();
	JTextArea messageBoxTxt = new JTextArea();
	JTextField subjectTxt = new JTextField();
	JLabel clinetNamelabel = new JLabel("To:");
	JLabel clinetEmaillabel = new JLabel("Email :");
	JLabel subjectLabel	= new JLabel("Subject: ");
	JLabel messageLabel = new JLabel("Message: ");
	JProgressBar bar = new JProgressBar();
	
	
	String jdbcURL = "jdbc:postgresql://localhost:5432/email";
	String username = "postgres";
    String password = "dhruvkaith";
    
    
	Page(){
		
		bar.setString("IDLE");
		bar.setBounds(60,520,320,25);
		bar.setStringPainted(true);
		bar.setFont(new Font("MV Boli",Font.BOLD,25));
		bar.setForeground(Color.red);
		//bar.setBackground(Color.red);
		
		clinetNamelabel.setBounds(4,50,20,25);
		clinetNamelabel.setFont(new Font(null,Font.BOLD,10));
		
		clinetEmaillabel.setBounds(4,100,40,30);
		clinetEmaillabel.setFont(new Font(null,Font.BOLD,10));
		
		subjectLabel.setBounds(4,150,50,30);
		clinetEmaillabel.setFont(new Font(null,Font.BOLD,5));
		
		messageLabel.setBounds(4,200,70,30);
		clinetEmaillabel.setFont(new Font(null,Font.BOLD,10));
		
		clientNameTxt.setBounds(60,50,200,25);
		
		clientEmailTxt.setBounds(60,100,200,25);
		
		
		subjectTxt.setBounds(60,150,450,25);
		
		messageBoxTxt.setBounds(60,200,450,300);
		
		
		
		sendBtn.setBounds(425,550,100,25);
		sendBtn.setFocusable(false);
		//loginButton.addActionListener(this);
		
		resetButton.setBounds(530,550,75,25);
		resetButton.setFocusable(false);
		resetButton.addActionListener(this);
		sendBtn.addActionListener(this);
		frame.add(bar);
		frame.add(subjectLabel);
		frame.add(messageLabel);
		frame.add(clinetEmaillabel);
		frame.add(clinetNamelabel);
		frame.add(messageBoxTxt);
		frame.add(subjectTxt);
		frame.add(clientEmailTxt);
		frame.add(clientNameTxt);
		frame.add(sendBtn);
		frame.add(resetButton);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(620,620);
		frame.setLayout(null);
		frame.setVisible(true);
		frame.setResizable(false);
		int counter = 0;
		while(counter<=100) {
			bar.setForeground(Color.green);
			bar.setBackground(Color.red);
			bar.setString("..Connecting..");
			bar.setValue(counter);
			try {
				Thread.sleep(20);
				
			}catch(InterruptedException e3) {
				e3.printStackTrace();
			}
			counter +=1;
		}
		
		bar.setString("ACTIVE!");
		bar.setBackground(Color.green);
	}
	
	
	@Override
	public void actionPerformed(ActionEvent e) {

		if(e.getSource()==resetButton) {
			clientNameTxt.setText("");
			clientEmailTxt.setText("");
			messageBoxTxt.setText("");
			subjectTxt.setText("");
			bar.setString("");
		}
		if(e.getSource()==sendBtn) {
		/*	
			clientName.setText("");
			clientEmail.setText("");
			messageBox.setText("");
			subject.setText("");
			bar.setString("ACTIVE!");
		*/
			
			String clientmail= clientEmailTxt.getText();
			String []ArrEmail = clientmail.split(",");
			String Clinetname= clientNameTxt.getText();
			String messageBox = messageBoxTxt.getText();
			String subject = subjectTxt.getText();
			String from = "Carbonatedfiji@gmail.com";
			bar.setBackground(Color.green);
			
			/*int counter = 0;
			while(counter<=100) {
				bar.setValue(counter);
				try {
					Thread.sleep(50);
				}catch(InterruptedException e3) {
					e3.printStackTrace();
				}
				counter +=1;
			}*/
			try {
		        //save pdf as	
			    String filename = "A:\\email\\pdf\\invoice.pdf";
				Rectangle pageSize = new Rectangle(780, 525);
				Document document = new Document(pageSize);
				OutputStream file = null;
				try {
					file = new FileOutputStream(new File(filename));
				} catch (FileNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				PdfWriter.getInstance(document, file);
				//PdfWriter.getInstance(document, response.getOutputStream());
				document.open();
				float[] colsWidth1 = {1f, 1f, 1f,1f,1f}; // Code 1
			
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
				} catch (BadElementException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
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
				table.addCell(Clinetname);
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
		           
		        } catch (Exception e1) {
		            e1.printStackTrace();
		        } 
		        
		       try {
		    	   Connection connection =  DriverManager.getConnection(jdbcURL, username, password);
		    	   System.out.println("Connected ");
		    	   bar.setString("Active!");
		    	   String sql = "INSERT INTO record(Name, File, recipient, subject, message) VALUES (?,?,?,?,?)";
		    	   
		    	   //Statement statement = connection.createStatement();
		    	   PreparedStatement pstmt = connection.prepareStatement(sql);
		    	   
		    	   pstmt.setString(1, Clinetname);
		    	   FileInputStream fis = new FileInputStream("A:\\email\\pdf\\invoice.pdf");
		    	   pstmt.setBinaryStream(2, fis,fis.available());
		    	   pstmt.setString(3, clientmail);
		    	   pstmt.setString(4, subject);
		    	   pstmt.setString(5, messageBox);
		    	   pstmt.executeUpdate();
		    	 /*  int rows = statement.executeUpdate(sql);
		    	   if (rows > 0) {
		    		   System.out.print("A new entry is entered");
		    	   }*/
		    	   connection.close();

		       } catch(SQLException | IOException e1) {
		    	   System.out.println("Error in connecting to databse");
		    	   bar.setString("Error in connecting to databse");
		    	   e1.printStackTrace();
		       }
			
				
				
				//sendEmail(message,subject,to,from);
				sendAttach(messageBox,subject,clientmail,from, ArrEmail);
				
				bar.setBackground(Color.green);
				bar.setString("Sent!");
			}
	}
		//this is responsible to send the message with attachment
		
		
		private static void sendAttach(String message, String subject, String client_email, String from, String[] ArrEmail) {
			for(String singleEmail:ArrEmail){
				
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
			
			//Step 1: to get the session object
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
			//changed to add bulk feature
			//adding recipient to message
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(singleEmail));
			//m.addRecipient(Message.RecipientType.TO, new InternetAddress(client_email));
			
			//adding subject to message
			m.setSubject(subject);
		
			
			//attachement..
			
			//attachment path
			String path="A:\\email\\pdf\\invoice.pdf";
			
			
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
			
			System.out.println("Sent success");
			
			}	
			
		}
	}
