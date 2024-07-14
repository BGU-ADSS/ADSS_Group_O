package ServiceLayer;

public class Response {
    private Object returnedValue;
    private String errorOccurred;
    public Response(String errorOccurred){
        this.errorOccurred = errorOccurred;
    }
    public Response(Object returnedValue){
        this.returnedValue = returnedValue;
    }
}
