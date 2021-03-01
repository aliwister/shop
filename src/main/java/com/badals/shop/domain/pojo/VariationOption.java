package com.badals.shop.domain.pojo;

import lombok.Data;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Data
public class VariationOption implements Serializable {
    private static final long serialVersionUID = -5573648649418161369L;
    String name;
    String label;
    List<String> values = new ArrayList<>();

    public VariationOption() {
    }

    public VariationOption(String name, String label, List<String> values) {
        this.name = name;
        this.label = label;
        this.values = values;
    }

    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {
        name = aInputStream.readUTF();
        label = aInputStream.readUTF();
        values = (ArrayList) aInputStream.readObject();
    }

    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
        aOutputStream.writeUTF(name);
        aOutputStream.writeUTF(label);
        aOutputStream.writeObject(values);
    }
}
