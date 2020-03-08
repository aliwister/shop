package com.badals.shop.domain.pojo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Variation implements Serializable {
    long ref;
    List<Attribute> variationAttributes;

    public Variation() {
    }

    public Variation(long ref, List<Attribute> variationAttributes) {
        this.ref = ref;
        this.variationAttributes = variationAttributes;
    }

    public long getRef() {
        return ref;
    }

    public void setRef(long ref) {
        this.ref = ref;
    }

    public List<Attribute> getVariationAttributes() {
        return variationAttributes;
    }

    public void setVariationAttributes(List<Attribute> variationAttributes) {
        this.variationAttributes = variationAttributes;
    }

    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {
        ref = aInputStream.readLong();
        variationAttributes = (ArrayList) aInputStream.readObject();
    }

    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
        aOutputStream.writeLong(ref);
        aOutputStream.writeObject(variationAttributes);
    }
}
