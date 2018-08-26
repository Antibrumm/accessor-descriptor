package ch.mfrey.bean.ad;

import java.util.List;
import java.util.Map;

public class A {

    private String aStr;
    private B b;
    private B[] bArray;
    private List<B> bList;
    private Map<String, B> bMap;

    public String getaStr() {
        return aStr;
    }

    public B getB() {
        return b;
    }

    public B[] getbArray() {
        return bArray;
    }

    public List<B> getbList() {
        return bList;
    }

    public Map<String, B> getbMap() {
        return bMap;
    }

    public void setaStr(final String aStr) {
        this.aStr = aStr;
    }

    public void setB(final B b) {
        this.b = b;
    }

    public void setbArray(final B[] bArray) {
        this.bArray = bArray;
    }

    public void setbList(final List<B> bList) {
        this.bList = bList;
    }

    public void setbMap(final Map<String, B> bMap) {
        this.bMap = bMap;
    }
}
