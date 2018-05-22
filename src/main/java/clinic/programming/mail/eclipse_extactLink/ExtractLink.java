package clinic.programming.mail.eclipse_extactLink;



import java.io.*;
import java.net.URL;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.*;
import javax.mail.internet.MimeMessage;


public class ExtractLink {
	public static void recieveMail(String userName,String Password,String Searcheremail,String Subject) throws Exception {
		try {
			Properties properties =new Properties();
			properties.setProperty("mail.store.protocol","imaps");
			Session emailSession = Session.getDefaultInstance(properties);
			Store emailStore = emailSession.getStore("imaps");
			//String[] Host = userName.split("@");
			String host = "imap.gmail.com";
			 emailStore.connect(host,userName,Password);
			 Folder emailFolder = emailStore.getFolder("INBOX");
			 emailFolder.open(Folder.READ_ONLY);
			 Message[] messages = emailFolder.getMessages();			 
			 for(int i=messages.length-1;i>messages.length-500;i--) {
				 MimeMessage message =(MimeMessage) messages[i];
				    String Docu = message.getFrom()[0].toString();
				    String[] ema = Docu.split("<");
				    if(ema.length == 2) {
				    String[] emil = ema[1].split(">");
				    String  email = emil[0];
				    
				     if(email.equals(Searcheremail)) {
					    if( message.getSubject().equals(Subject)) {
						    message.getContentType();
						    if (message.isMimeType("text/plain")) {
					        
					         System.out.println((String) message.getContent());
					         String sand = (String) message.getContent();
					         extractlink(sand);				         
					                                        } 
						else if(message.isMimeType("multipart/*")) {
				    	Multipart mp= (Multipart) message.getContent();
				    	int count = mp.getCount();
				         for (int j = 0; j < count; j++) {
				        	 Part p = mp.getBodyPart(j);
				        	 if (p.isMimeType("text/plain")) {
                             System.out.println((String) p.getContent());
                             String sand = (String) p.getContent();
                             extractlink(sand);                             
	                }
				      }
				         }				       
        			      break;
					      }
			            			 
			  }
			 }
				}
			 emailFolder.close(false);
			 emailStore.close();
		}catch(MessagingException me) {
			me.printStackTrace();
		}
	}

	

  public static void main(String args[]) throws Exception {
	  URL input = new ExtractLink().getClass().getClassLoader().getResource("config.txt");
		String  file = input.getFile(); 
		//System.out.println("ok");// path to the config file
		BufferedReader br = null;
		FileReader fr = null;
		String sCurrentLine;
		String [] arrOfStr ;
		String em = null;
		String pass = null;
		String Searcheremail = null;
		String Subject;
		try{
			fr = new FileReader(file);
			br =  new BufferedReader(fr);
			sCurrentLine = br.readLine();  
			System.out.println(sCurrentLine);// reading from first line
			arrOfStr = sCurrentLine.split(":", 2);
			em = arrOfStr[1];                                                                         //getting email id from config file
			sCurrentLine = br.readLine();
			arrOfStr = sCurrentLine.split(":", 2);
			pass = arrOfStr[1];                                                                      //getting password from config file
			sCurrentLine = br.readLine();
			arrOfStr = sCurrentLine.split(":", 2);
			Searcheremail = arrOfStr[1];
			sCurrentLine = br.readLine();
			arrOfStr = sCurrentLine.split(":", 2);
			 Subject = arrOfStr[1];
			br.close();
			fr.close();
			  recieveMail(em,pass,Searcheremail,Subject);
		}catch (IOException e) {
			e.printStackTrace();
		                  }
		
           
    
  }
public static void extractlink(String sCurrentLine) {
	
	
	        String urlRegex = "((https?|ftp|gopher|telnet|file):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)"; /*regex expression*/
            Pattern pattern = Pattern.compile(urlRegex, Pattern.CASE_INSENSITIVE);	
			Matcher urlMatcher = pattern.matcher(sCurrentLine);
            System.out.println("The extracted Links are:");
			while (urlMatcher.find()) {       
				
			System.out.println(sCurrentLine.substring(urlMatcher.start(0),urlMatcher.end(0)));
			
		}}
		                                    
	
}
