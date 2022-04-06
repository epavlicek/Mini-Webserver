/*--------------------------------------------------------
Checklist Question 1:

MIME-types are essentially labels that will tell the browser what data is coming.
They allow browsers to recognize what type of file the file is so that the browser
can then properly process and display the file.

Checklist Question 2:

You would return it as follows:
HTTP/1.1 200 OK
Content-Length: 700
Content-Type: text/html

Checklist Question 3:

You would return it as follows:
HTTP/1.1 200 OK
Content-Length: 700
Content-Type: text/plain


1. Name / Date: Elizabeth Pavlicek 10/06/20121


2. I used amazon-corretto-11 version of Java.

3.Command-line compilation instructions:
javac MiniWebserver.java
java MiniWebserver

4.Instructions to run the program:
1) In the terminal, run java MiniWebserver.
2) Open Firefox browser
3) Open WebAdd.html in Firefox browser
4) Input your name and two numbers to get your result.




5.Files you need to run the program:
MiniWebserver.java
AddNums.html

6. Notes:
After opening the WebAdd.html in Firefox, there may be an error in the terminal
however, it does not affect the results in the browser.


----------------------------------------------------------*/

import java.io.*;  
import java.net.*; 

class Worker extends Thread {    // Defines the class.
  Socket sock;                   
  Worker (Socket s) {sock = s;} // Worker constructor 
  public void run(){
    PrintStream out = null;   //the printstream will be refered to as out
    BufferedReader in = null; //the BufferedReader will be reffered to as in
    try {
    	//Creating a new PrintStream here and this will be sent to addTheNums() 
      out = new PrintStream(sock.getOutputStream());
      //Creating a new bufferedreader here
      in = new BufferedReader
        (new InputStreamReader(sock.getInputStream()));
      //reading what was sent through the web and assigning it to string submitted
      String submitted = in.readLine();
      //getting only the part of the string that we need and want to send to addTheNums
      //by only getting the contents between the specified indexes. 
      String info = submitted.substring(4, submitted.length()-9); //assigning it to a new string 'info'
      //sending the substring 'info' to addTheNums to add the numbers together.
      addTheNums(info,out);                                    
    
	
      sock.close(); // closing up the socket
    } catch (IOException x) {
      System.out.println("Error!!!"); //catching an error that has occured
    }
  }
  // This will add the numbers together and send the result out, along with a new html form.
  public void addTheNums(String info, PrintStream out) {
	  String name; //holds the user input name
	  //int firstNum;
	  String firstNum; //hold num1 for now
	  //int secondNum;
	  String secondNum;//hold num2 for now
	  int sum; //where the sum of num1 and num2 will be held
	  String [] input = info.split("[=&]");//Splitting string by two delimiters to get only what is inside '=' and '&'.
	  name = input[1]; //name of the person is the first split
	  firstNum = input[3]; //first number is the result of the second split
	  secondNum = input[5];//second number is equal to the third section of the split
	  int num1= Integer.parseInt(firstNum); //parsing the firstNum from string to integer
	  int num2=Integer.parseInt(secondNum); //parsing the secondNum from string to integer
	  sum = num1 + num2; //calculating the sum to be put into the response
	  //Giving the user their sum after they press the 'submit' button.
	  String HTMLoutput = "<html> <h1> Hi " + name + ", the sum of " + firstNum + " and " + secondNum + " equals: " + sum +
			      "</h1> <p><p> <hr> <p>";
	  out.println("HTTP/1.1 200 OK");
      out.println("Connection: close"); //Left this alone
      out.println("Content-Length: 700"); // Had to adjust from 400 to 700 to allow the full new form to be seen
      out.println("Content-Type: text/html \r\n\r\n"); //Says what type of data is coming
	  out.println(HTMLoutput); //Giving the user their response
	  //New AddNums form so that user can continiously submit and get results without having to reload the page.
	  out.println("<HTML>\n" + 
	  		"<BODY>\n" + 
	  		"<H1> WebAdd </H1>\n" + 
	  		"\n" + 
	  		"<FORM method=\"GET\" action=\"http://localhost:2540/WebAdd.fake-cgi\">\n" + 
	  		"\n" + 
	  		"Enter your name and two numbers. My program will return the sum:<p>\n" + 
	  		"\n" + 
	  		"<INPUT TYPE=\"text\" NAME=\"person\" size=20 value=\"YourName\"><P>\n" + 
	  		"\n" + 
	  		"<INPUT TYPE=\"text\" NAME=\"num1\" size=5 value=\"4\"> <br>\n" + 
	  		"<INPUT TYPE=\"text\" NAME=\"num2\" size=5 value=\"5\"> <p>\n" + 
	  		"\n" + 
	  		"<INPUT TYPE=\"submit\" VALUE=\"Submit Numbers\">\n" + 
	  		"\n" + 
	  		"</FORM> </BODY></HTML>");

  }
}

public class MiniWebserver {

  static int i = 0;

  public static void main(String a[]) throws IOException {
    int q_len = 6; 
    int port = 2540; //This will be the port at which we connect to 
    Socket sock; // the socket will be reffered to as sock

    ServerSocket servsock = new ServerSocket(port, q_len); //Creating a new serversocket
  //printing this out when the MiniWebserver starts
    System.out.println("Elizabeth Pavlicek's Mini Webserver running at 2540.");
    //Letting you know that you have to open the html file in Firefox
    System.out.println("Open WebAdd.html in Firefox browser.\n");
    while (true) { //will continiously loop
      sock = servsock.accept(); //socket being accepted here
      new Worker (sock).start(); //The worker is being started here
    }
  }
}
