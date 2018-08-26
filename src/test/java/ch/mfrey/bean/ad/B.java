package ch.mfrey.bean.ad;

import java.util.List;
import java.util.Map;

public class B {

    private A a;
    private String bStr;
    private C c;
    private C[] cArray;
    private List<C> cList;
    private Map<String, C> cMap;

    public A getA() {
        return a;
    }

    public String getbStr() {
        return bStr;
    }

    public C getC() {
        return c;
    }

    public C[] getcArray() {
        return cArray;
    }

    public List<C> getcList() {
        return cList;
    }

    public Map<String, C> getcMap() {
        return cMap;
    }

    public void setA(final A a) {
        this.a = a;
    }

    public void setbStr(final String bStr) {
        this.bStr = bStr;
    }

    public void setC(final C c) {
        this.c = c;
    }

    public void setcArray(final C[] cArray) {
        this.cArray = cArray;
    }

    public void setcList(final List<C> cList) {
        this.cList = cList;
    }

    public void setcMap(final Map<String, C> cMap) {
        this.cMap = cMap;
    }
}
