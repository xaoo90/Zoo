/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entity.view;

import dao.AnimalFeedFacade;
import dao.FeedFacade;
import entity.AnimalFeed;
import entity.Feed;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class FeedView {

    private Integer idFeed;
    private String name;
    private BigDecimal availability;
    private String unit;
    private BigDecimal day;

    public FeedView(Integer idFeed, String name, BigDecimal availability, String unit, BigDecimal day) {
        this.idFeed = idFeed;
        this.name = name;
        this.availability = availability;
        this.unit = unit;
        this.day = day;
    }

    public FeedView() {
    }

    public Integer getIdFeed() {
        return idFeed;
    }

    public void setIdFeed(Integer idFeed) {
        this.idFeed = idFeed;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getAvailability() {
        return availability;
    }

    public void setAvailability(BigDecimal availability) {
        this.availability = availability;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public BigDecimal getDay() {
        return day;
    }

    public void setDay(BigDecimal day) {
        this.day = day;
    }

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("ZooLogicPU");
    FeedFacade feeF = new FeedFacade(emf);
    AnimalFeedFacade afF = new AnimalFeedFacade(emf);

    public BigDecimal addDay(Feed f) {
        BigDecimal bd = new BigDecimal(0);
        for (AnimalFeed af : afF.findAnimalFeedEntities()) {
            if (f.equals(af.getFeed())) {
                bd = bd.add(af.getPortionn());
            }
        }
        return bd;
    }
    
    public ArrayList<FeedView> feed(List<Feed> fe) {
        ArrayList<FeedView> lstFeed = new ArrayList();
        for (Feed f : fe) {
                lstFeed.add(new FeedView(f.getIdFeed(), f.getName(), f.getAvailability(), 
                        f.getUnit(), (f.getAvailability().divide(addDay(f), 0, BigDecimal.ROUND_DOWN))));
        }
        return lstFeed;
    }

}
