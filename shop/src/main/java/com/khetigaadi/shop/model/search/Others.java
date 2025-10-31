package com.khetigaadi.shop.model.search;
import org.jetbrains.annotations.NotNull;
public class Others {
    @NotNull
    public String getUsedTractorImagePath() {
        return usedTractorImagePath;
    }
    public void setUsedTractorImagePath(@NotNull String usedTractorImagePath) {
        this.usedTractorImagePath = usedTractorImagePath;
    }
    @NotNull
    public String getHarvesterImagePath() {
        return harvesterImagePath;
    }
    public void setHarvesterImagePath(@NotNull String harvesterImagePath) {
        this.harvesterImagePath = harvesterImagePath;
    }
    @NotNull
    public String getTractorImagePath() {
        return tractorImagePath;
    }
    public void setTractorImagePath(@NotNull String tractorImagePath) {
        this.tractorImagePath = tractorImagePath;
    }
    @NotNull
    public String usedTractorImagePath;
    @NotNull
    public String harvesterImagePath;
    @NotNull
    public String tractorImagePath;
    @NotNull
    public String getImplementImagePath() {
        return implementImagePath;
    }
    public void setImplementImagePath(@NotNull String implementImagePath) {
        this.implementImagePath = implementImagePath;
    }
    @NotNull
    public  String implementImagePath;
    public String getShopCategoryImagePath() {
        return shopCategoryImagePath;
    }
    public void setShopCategoryImagePath(String shopCategoryImagePath) {
        this.shopCategoryImagePath = shopCategoryImagePath;
    }
    private String shopCategoryImagePath;
    public String getShopProductImagePath() {
        return shopProductImagePath;
    }
    public void setShopProductImagePath(String shopProductImagePath) {
        this.shopProductImagePath = shopProductImagePath;
    }
    private String shopProductImagePath;
}
