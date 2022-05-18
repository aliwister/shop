package com.badals.shop.domain.pojo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * A DTO for the {@link com.badals.shop.domain.Product} entity.
 */
@Data
public class AttributesLang implements Serializable {

    String lang;
    List<Attribute> attributeList;

/*    private void readObject(ObjectInputStream aInputStream) throws ClassNotFoundException, IOException
    {
        title = aInputStream.readUTF();
        brand = aInputStream.readUTF();
        group = aInputStream.readUTF();
        model = aInputStream.readUTF();
        lang = aInputStream.readUTF();
        description = aInputStream.readUTF();

        features = (ArrayList) aInputStream.readObject();
        attributes = (ArrayList) aInputStream.readObject();
    }

    private void writeObject(ObjectOutputStream aOutputStream) throws IOException
    {
        aOutputStream.writeUTF(title);
        aOutputStream.writeUTF(brand);
        aOutputStream.writeUTF(group);
        aOutputStream.writeUTF(model);
        aOutputStream.writeUTF(lang);
        aOutputStream.writeUTF(description);

        aOutputStream.writeObject(features);
        aOutputStream.writeObject(attributes);
    }*/
}
