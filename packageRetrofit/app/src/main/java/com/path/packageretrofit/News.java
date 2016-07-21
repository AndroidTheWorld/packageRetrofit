package com.path.packageretrofit;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by win10 on 2016/7/21.
 */
public class News {
    private int errCode;
    private String errDesc;
    private boolean hasNext;
    private List<Details> imageViewList=new ArrayList<>();

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }

    public boolean isHasNext() {
        return hasNext;
    }

    public void setHasNext(boolean hasNext) {
        this.hasNext = hasNext;
    }

    public List<Details> getImageViewList() {
        return imageViewList;
    }

    public void setImageViewList(List<Details> imageViewList) {
        this.imageViewList = imageViewList;
    }

    public class Details{
        private String id;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
}
