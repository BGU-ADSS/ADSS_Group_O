package DataAccessLayer;

import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import PresentationLayer.Logs;

public class PathForDB {
    public PathForDB(){

    }

    public String getEmployeeDB(){
        String fileName = "ADSS_DB_EMPLOYEE_MODULE.db";
        Logs.debug("ddddddddd");
        
        // Try to get the file path relative to the class location
        try {
            URL resourceUrl =PathForDB.class.getResource("/" + fileName);
            if (resourceUrl != null) {
                Path filePath = Paths.get(resourceUrl.toURI());
                // Get the full path (absolute path) of the file
                Path fullPath = filePath.toAbsolutePath();
                Logs.debug("dddddd");
                // Print the full path
                return fullPath.toString();
            } else {
                System.out.println("File not found in the classpath.");
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        // File dbFile = Files
        // return Paths.get("./ADSS_DB_EMPLOYEE_MODULE.db").toAbsolutePath().toString();
        return null;

    }
}
