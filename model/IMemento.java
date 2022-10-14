package pt.isec.pa.apoio_poe.model;

public interface IMemento {
    default Object getSnapshot(){
        return null;
    }
}
