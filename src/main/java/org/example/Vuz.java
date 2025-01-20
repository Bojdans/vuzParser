package org.example;


import javax.persistence.*;

@Entity
@Table(
        name = "vuziki",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = {"name"})
}
)
public class Vuz {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    @Column(nullable = false,length = 450,unique = true)
    private String name;
    @Column
    private String abbreviation;
    @Column
    private int paid;
    @Column
    private int free;
    @Column
    private int pointsForFree;
    @Column
    private int pointsForPaid;

    public Vuz() {

    }

    public Vuz(String name, String abbreviation, int free, int pointsForFree, int paid, int pointsForPaid) {
        this.name = name;
        this.abbreviation = abbreviation;
        this.paid = paid;
        this.free = free;
        this.pointsForFree = pointsForFree;
        this.pointsForPaid = pointsForPaid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAbbreviation() {
        return abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public int getPaid() {
        return paid;
    }

    public void setPaid(int paid) {
        this.paid = paid;
    }

    public int getFree() {
        return free;
    }

    public void setFree(int free) {
        this.free = free;
    }

    public int getPointsForFree() {
        return pointsForFree;
    }

    public void setPointsForFree(int pointsForFree) {
        this.pointsForFree = pointsForFree;
    }

    public int getPointsForPaid() {
        return pointsForPaid;
    }

    public void setPointsForPaid(int pointsForPaid) {
        this.pointsForPaid = pointsForPaid;
    }

    @Override
    public String toString() {
        return "Vuz{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", abbreviation='" + abbreviation + '\'' +
                ", paid='" + paid + '\'' +
                ", free='" + free + '\'' +
                ", pointsForFree=" + pointsForFree +
                ", pointsForPaid=" + pointsForPaid +
                '}';
    }
}
