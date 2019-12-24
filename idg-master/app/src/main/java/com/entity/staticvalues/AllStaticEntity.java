package com.entity.staticvalues;

import com.entity.crm.CusLevelEntity;
import com.entity.crm.CusTypeEntity;

import java.util.List;

/**
 * 静态数据list类CusLevel
 * Created by cx on 2016/5/19.
 */
public class AllStaticEntity {
    private List<StaticValues> datas;//静态数据
    private String status;
    private List<CusTypeEntity> CusType;//客户类型
    private List<CusLevelEntity> CusLevel;//客户级别
    private List<ProductsBrandsEntity> ProductsBrands;//商品品牌
    private List<ProductsTypeEntity> ProductsType;//商品类别


    public List<CusLevelEntity> getCusLevel() {
        return CusLevel;
    }

    public void setCusLevel(List<CusLevelEntity> cusLevel) {
        CusLevel = cusLevel;
    }

    public List<ProductsBrandsEntity> getProductsBrands() {
        return ProductsBrands;
    }

    public void setProductsBrands(List<ProductsBrandsEntity> productsBrands) {
        ProductsBrands = productsBrands;
    }

    public List<ProductsTypeEntity> getProductsType() {
        return ProductsType;
    }

    public void setProductsType(List<ProductsTypeEntity> productsType) {
        ProductsType = productsType;
    }

    public List<StaticValues> getDatas() {
        return datas;
    }

    public void setDatas(List<StaticValues> datas) {
        this.datas = datas;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<CusTypeEntity> getCusType() {
        return CusType;
    }

    public void setCusType(List<CusTypeEntity> cusType) {
        CusType = cusType;
    }

}
