package PresentationLayer;

import DTOs.LocalDateAdapter;
import DTOs.Response;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.time.LocalDate;

public class ResponseManager {
    public boolean hasErrorOccured;
    public String errorMessage;
    public Object value;
    private Gson gson = new GsonBuilder().registerTypeAdapter(LocalDate.class, new LocalDateAdapter()).create();
    
    public ResponseManager(String response){
        Response res = gson.fromJson(response, Response.class);
        hasErrorOccured = res.isErrorOccured();
        errorMessage = res.getErrorMessage();
        value = res.getReturnValue();
    }

    private int getLastIndexOfValue(String response, int i) {
        int open = 1;
        int retVal = 0;
        int currIndex = retVal+i;
        while(open>0){
            char c = response.charAt(currIndex);
            currIndex++;
            if(c=='{') open++;
            if(c=='}') open--;
        }
        return retVal;
    }
}
