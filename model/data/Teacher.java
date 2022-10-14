package pt.isec.pa.apoio_poe.model.data;

import java.io.Serializable;

public class Teacher implements Serializable{
    
    private String email;
    private String name;

    public Teacher(String name, String email){
        this.email = email;
        this.name = name;
    }

    //GETTERS AND SETTERS
    public void setEmail(String email){
        this.email = email;
    }
    public String getEmail(){
        return email;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getName(){
        return name;
    }

    public String toCSV(){
        return String.format("%s,%s",name,email);
    }

    @Override
    public boolean equals(Object o){
        if(o == null){
            return false;
        }
        
        if(this == o){
            return true;
        }

        if(this.getClass() != o.getClass()){
            return false;
        }

        Teacher teacher = (Teacher) o;

        if(teacher.getEmail() == this.getEmail()){
            return true;
        }
        return false;
    }

    @Override
    public int hashCode(){
        return email.hashCode();
    }

    @Override 
    public String toString(){
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Name:%-10s\tEmail:%-10s\n", name,email));
        return sb.toString();
    }

    @Override
    public Teacher clone(){
        return new Teacher(name,email);
    }
}
