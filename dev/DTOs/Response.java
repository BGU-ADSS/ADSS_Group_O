package DTOs;

public class Response {
    private String errorMessage;
    private Object returnValue;

    /**
     * Gets or sets the error message associated with the method call.
     */
    public boolean isErrorOccured() {
        return errorMessage != null;
    }

    /**
     * Initializes a new instance of the Response class.
     */
    public Response() {}

    public Response(String errorMessage, Object returnValue) {
        this.errorMessage = errorMessage;
        this.returnValue = returnValue;
    }

    /**
     * Initializes a new instance of the Response class with an error message.
     * @param msg The error message to be associated with the Response.
     */
    public Response(String msg) {
        this.errorMessage = msg;
    }

    public Response(Object returnValue) {
        this.returnValue = returnValue;
    }

    // Getters and Setters
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public Object getReturnValue() {
        return returnValue;
    }

    public void setReturnValue(Object returnValue) {
        this.returnValue = returnValue;
    }
}
