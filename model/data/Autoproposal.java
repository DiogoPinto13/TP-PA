package pt.isec.pa.apoio_poe.model.data;

public class Autoproposal extends Proposal{

    public Autoproposal(String id, String title, Long numStudent){
        super(id, title, "", numStudent);
    }

    @Override
    public String toCSV(){
        return String.format("T3,%s,%s,%d",getId(),getTitle(),getNumStudent());
    }

    @Override
    public Autoproposal clone(){
        return new Autoproposal(getId(),getTitle(),getNumStudent());
    }
    @Override 
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Autoproposal (%-10s) with title: %-10s and student number: %-10d",super.getId(),super.getTitle(),super.getNumStudent()));
        return sb.toString();
    }
}
