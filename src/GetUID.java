

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 * Servlet implementation class GetUIDProxy
 */
@WebServlet("/GetUID")
public class GetUID extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static final Logger log = Logger.getLogger(GetUID.class);
	String url ="";
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetUID() {
        super();
        // TODO Auto-generated constructor stub               
                                
        log.info("GetUID...");     
        
        log.info("Loading a properties file from class path...");
     	Properties prop = new Properties();
    	InputStream input = null;

    	try {
    		String filename = "config.properties";
    		input = GetUID.class.getClassLoader().getResourceAsStream(filename);
    		
    		if(input == null){
    			log.error("Sorry, unable to find " + filename);
    		    return;
    		}

    		// Load a properties file from class path
    		prop.load(input);

            // Get the property value, JDBC URL, user and password of MySQL server
    		url = prop.getProperty("url");
    		    		
            log.info("url : " + url);            

    	} catch (IOException e) {
    		e.printStackTrace();
        } finally {
        	if(input != null){
        		try {
        			input.close();
        		} catch (IOException e) {
        			e.printStackTrace();
        		}
        	}
        }     	
    	
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// Create Get request dynamically to remote server		
		log.info("GetUID-doGet...");	
		
		// response.setContentType("application/json"); 		
		// response.setCharacterEncoding("UTF-8");
        // response.setHeader("Cache-Control", "no-cache"); 				
		
		URL ApUrl = null;
	    BufferedReader reader = null;
	    ServletOutputStream sout = null;
	    StringBuilder respData = null;
	    
	    String desiredUrl = url + request.getQueryString();          
        log.info("Sending 'GET' request to URL : " + desiredUrl);

	    try {
	    	// Create the HttpURLConnection
	    	ApUrl = new URL(desiredUrl);
	    	HttpURLConnection conn = (HttpURLConnection) ApUrl.openConnection();
	      
	    	// Just want to do an HTTP GET here
	    	conn.setRequestMethod("GET");
	      
	    	// Give it 15 seconds to respond
	    	conn.setReadTimeout(15*1000);
	    	conn.connect();
	      
	    	int respCode = conn.getResponseCode();     
	    		      
	    	if (respCode == 200) 
	    		log.info("Response code : " + respCode);	    		
	    	else
	    		log.error("Response code : " + respCode);
	    		
	    	// Read the output from the server
	    	reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));	    	    		      
	    	respData = new StringBuilder();
	    	sout = response.getOutputStream();

	    	String line = null;
	    	while ((line = reader.readLine()) != null) {
	    		respData.append(line + "\n");
	    		sout.write(line.getBytes());	    		
	    	}
	      
	    	log.info("Response data : " + respData.toString());	      
	      
	    } catch (Exception e) {
	    	e.printStackTrace();
	    	throw e;
	    } finally {	    		    	 
	    	// Close the reader; this can throw an exception too, so wrap it in another try/catch block.
	    	if (reader != null) {
	    		try {
	    			reader.close();
	    			sout.flush();
	    		} catch (IOException ioe) {
	    			ioe.printStackTrace();
	    		}
	    	}
	    }		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub		
		log.info("GetUID-doPost...");	
		doGet(request, response);		
	}

}
