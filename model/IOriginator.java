package pt.isec.pa.apoio_poe.model;

import pt.isec.pa.apoio_poe.model.IMemento;

public interface IOriginator {
    IMemento save();
    void restore(IMemento memento);
}
