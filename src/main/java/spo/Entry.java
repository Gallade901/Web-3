package spo;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "entry")
public class Entry implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "jpaSequence")
    @SequenceGenerator(name = "jpaSequence", sequenceName = "entry_id_seq", allocationSize = 1)
    private int id;
    @Column
    private Double xValue;
    @Column
    private Double yValue;
    @Column
    private Double rValue;
    @Column
    private String hitResult;

    public Entry() { }

    private boolean checkTriangle() {
        return xValue >=0 && yValue >=0 && (yValue <= -2 * xValue + rValue );
    }

    private boolean checkRectangle() {
        return xValue >= 0 && yValue <= 0 && xValue <= rValue && yValue >= -rValue/2;
    }

    private boolean checkCircle() {
        return xValue <= 0 && yValue >= 0 && xValue*xValue + yValue*yValue <= rValue * rValue;
    }

    public void checkHit() {
        hitResult = checkTriangle() || checkRectangle() || checkCircle() ? "Попадание" : "Промах";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getxValue() {
        return xValue;
    }

    public void setxValue(Double xValue) {
        this.xValue = xValue;
    }

    public Double getyValue() {
        return yValue;
    }

    public void setyValue(Double yValue) {
        this.yValue = yValue;
    }

    public Double getrValue() {
        return rValue;
    }

    public void setrValue(Double rValue) {
        this.rValue = rValue;
    }

    public String getHitResult() {
        return hitResult;
    }

    public void setHitResult(String hitResult) {
        this.hitResult = hitResult;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "xValye=" + xValue +
                ", yValue=" + yValue +
                ", rValue=" + rValue +
                ", hitResult=" + hitResult +
                '}';
    }

    @Override
    public int hashCode() {
        return xValue.hashCode() + yValue.hashCode() +
                rValue.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj instanceof Entry) {
            Entry entryObj = (Entry) obj;
            return xValue.equals(entryObj.getxValue()) &&
                    yValue.equals(entryObj.getyValue()) &&
                    rValue.equals(entryObj.getrValue());
        }
        return false;
    }
}