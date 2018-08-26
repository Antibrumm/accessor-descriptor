package ch.mfrey.bean.ad;

import java.util.List;

public class LeftRightNode {

    private List<LeftRightNode> leftChilds;

    private List<LeftRightNode> rightChilds;

    private Long id;

    public List<LeftRightNode> getLeftChilds() {
        return leftChilds;
    }

    public void setLeftChilds(List<LeftRightNode> leftChilds) {
        this.leftChilds = leftChilds;
    }

    public List<LeftRightNode> getRightChilds() {
        return rightChilds;
    }

    public void setRightChilds(List<LeftRightNode> rightChilds) {
        this.rightChilds = rightChilds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
