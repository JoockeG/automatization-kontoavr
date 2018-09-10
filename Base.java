
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class Base {
	
static Properties props = new Properties();
	
public static void main(String[] args) {
	
	
try {
		
File configFile = new File("config.properties");
FileReader reader = new FileReader(configFile);
props.load(reader);
		
} catch (IOException e) 
{
	e.printStackTrace();
}
	
hejsandddddgit sta		
/*
		
try {

URL url = new URL("https://api.helpscout.net/v2/conversations?query=(mailbox=b53038937547378a) AND (subject:\\\"klientkontoavräkning\\\"");
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
conn.setRequestMethod("GET");
conn.setRequestProperty("Accept", "application/json");
conn.setRequestProperty ("Authorization", props.getProperty("helpscout-accesstoken"));

if (conn.getResponseCode() != 200) 
{
	throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
}

BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
String output;
System.out.println("Output from Server .... \n");

while ((output = br.readLine()) != null) 
{
	System.out.println(output);
}

conn.disconnect();

} catch (MalformedURLException e) 
{
	e.printStackTrace();
} catch (IOException e) 
{
	e.printStackTrace();
}
}
	
	
*/


	
String orgNr = props.getProperty("example-orgnr");
String invoiceNr = props.getProperty("example-invoicenr");
String result = getOcrNr(invoiceNr, getAccessToken(orgNr));
}
	
	
public static String getAccessToken(String orgNr) {
		
String username = props.getProperty("db-username");
String password = props.getProperty("db-password");
String dbUrl = props.getProperty("db-url");
String clientId = "";
String accessToken = "";
		
Connection conn;
	    
try {
			
conn = DriverManager.getConnection(dbUrl, username, password);
PreparedStatement stmt = conn.prepareStatement("SELECT clientid FROM client WHERE organization_number=?");   // fixa hantering av med/utan bindestreck
stmt.setString(1, orgNr);
ResultSet queryResult1 = stmt.executeQuery();
ResultSetMetaData metaData = queryResult1.getMetaData();
int columnsNumber = metaData.getColumnCount();
		
while (queryResult1.next())
{
	clientId = queryResult1.getString(1);
}
		
stmt = conn.prepareStatement("SELECT externaltoken FROM external_token WHERE clientid=?");
stmt.setString(1, clientId);
ResultSet queryResult2 = stmt.executeQuery();
		
while (queryResult2.next()) 
{
	accessToken = queryResult2.getString(1);
}
		
} catch (SQLException e) 
{
	e.printStackTrace();
}
		
return accessToken;
}

	
	
	
public static String getOcrNr(String invoiceNr, String accesstoken) {
	
String inputLine = "";
String OcrNr = "";

try {
			
URL url = new URL("https://api.fortnox.se/3/noxfinansinvoices/" + invoiceNr);
HttpURLConnection con;
con = (HttpURLConnection) url.openConnection();
con.setRequestMethod("GET");
con.setRequestProperty("Content-Type", "application/json");
con.setRequestProperty("Accept", "application/json");
con.setRequestProperty("Client-Secret", props.getProperty("noxbox-clientsecret"));
con.setRequestProperty("Access-Token", accesstoken);
		
BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
StringBuffer content = new StringBuffer();

while ((inputLine = in.readLine()) != null) 
{
	content.append(inputLine);
	System.out.print(inputLine);
}

in.close();
con.disconnect();
		
} catch (IOException e) 
{
	e.printStackTrace();
}

return inputLine;   // OcrNr;
}







}



// URL url = new URL("https://api.helpscout.net/v2/conversations?query=(mailbox=b53038937547378a) AND (subject:\\\"klientkontoavräkning\\\" AND (subject:\\\"1\\\" OR subject:\\\"2\\\" OR subject:\\\"3\\\" OR subject:\\\"4\\\" OR subject:\\\"5\\\" OR subject:\\\"6\\\" OR subject:\\\"7\\\" OR subject:\\\"8\\\" OR subject:\\\"9\\\"))");

