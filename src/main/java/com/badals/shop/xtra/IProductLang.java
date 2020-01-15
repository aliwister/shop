package com.badals.shop.xtra;

import java.util.List;

public interface IProductLang {
    public void setTitle(String title);

    public void setDescription(String description);

    public void setModel(String model);

    public void setFeatures(List<String> features);

    public void setLang(String lang);
}
