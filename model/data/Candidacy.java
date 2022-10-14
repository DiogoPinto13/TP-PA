package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;
import java.util.ArrayList;

public class Candidacy implements Serializable {
    
    private long numAluno;
    private ArrayList<String> preferences;
    
    public Candidacy(Long numAluno, ArrayList<String> preferences){
        this.preferences = new ArrayList<String>();
        this.numAluno = numAluno;
        for(String preference : preferences){
            (this.preferences).add(preference);
        }
    }

    public ArrayList<String> getPreferences(){
        return preferences;
    }

    public long getNumStudent() {
        return numAluno;
    }

    public String toCSV(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%d,",getNumStudent()));
        for(String preference : getPreferences()){
            if(getPreferences().indexOf(preference) == getPreferences().size()-1){
                sb.append(String.format("%s",preference));
                break;
            }
            sb.append(String.format("%s,",preference));
        }

        return sb.toString();
    }
}
