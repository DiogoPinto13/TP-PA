package pt.isec.pa.apoio_poe.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Memento implements IMemento{
    byte[] snapshot;
    public Memento(Object obj) {
        try (ByteArrayOutputStream baos =
                     new ByteArrayOutputStream();
             ObjectOutputStream oos =
                     new ObjectOutputStream(baos)) {
            oos.writeObject(obj);
            snapshot = baos.toByteArray();
        } catch (Exception e) { snapshot = null; }
    }
    @Override
    public Object getSnapshot() {
        if (snapshot == null) return null;
        try (ByteArrayInputStream bais =
                     new ByteArrayInputStream(snapshot);
             ObjectInputStream ois =
                     new ObjectInputStream(bais)) {
            return ois.readObject();
        } catch (Exception e) { return null; }
    }
}
