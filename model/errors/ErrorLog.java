package pt.isec.pa.apoio_poe.model.errors;

import java.util.ArrayList;
import java.util.List;

public class ErrorLog {
    private List<ErrorType> errorType;

    public ErrorLog(ErrorType... errorType){
        this.errorType = new ArrayList<ErrorType>();
        for(ErrorType e : errorType){
            this.errorType.add(e);
        }
    }

    public List<ErrorType> getErrorType() {
        return errorType;
    }

    public void addErrorType(ErrorType errorType) {
        this.errorType.add(errorType);
    }
}
