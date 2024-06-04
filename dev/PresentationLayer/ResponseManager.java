package PresentationLayer;

public class ResponseManager {
    public boolean hasErrorOccured;
    public String errorMessage;
    public String value;
    
    public ResponseManager(String response){
        StringBuilder builder = new StringBuilder();
        String key=null;
        for(int i =0;i<response.length();i++){
            if(response.charAt(i)=='='){
                key=builder.toString();
                builder = new StringBuilder();
                if(key.equals("value") && response.charAt(i+2)=='{'){
                    int lastIndex = getLastIndexOfValue(response,i+3);
                    value = response.substring(i, lastIndex+1);
                    key=null;
                }
            }else if(response.charAt(i)==',' || response.charAt(i)=='}'){
                switch (key) {
                    case "errorOccured":
                        hasErrorOccured = builder.toString()=="true";
                        break;
                    case "errorMessage":
                        errorMessage= builder.toString();
                        break;
                    case "value":
                        value= builder.toString();
                        break;
                    default:
                        break;
                }
                builder = new StringBuilder();
                key=null;
                
            }
        }

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
