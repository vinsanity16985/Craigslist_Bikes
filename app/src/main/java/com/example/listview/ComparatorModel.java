package com.example.listview;

import java.util.Comparator;

/**
 * Created by vinsa_000 on 3/30/2016.
 */
public class ComparatorModel implements Comparator<BikeData>{

    private int sortBy;

    public ComparatorModel(int sortBy){
        this.sortBy = sortBy;
    }

    /*
    Compares a Collection depending on the sortBy
    sortBy corresponds to one of 4 options: Company, Model, Price, Location
    @param: BikeData data1 & data2, BikeData Objects to be compared
    @return: int, 0 means the two are equal, int>0 means data1 is before data2, and int<0 means data2 is before data1
     */
    @Override
    public int compare(BikeData data1, BikeData data2) {
        switch(sortBy){
            case 0:
                //Sort By Company
                return (data1.Company.compareTo(data2.Company));
            case 1:
                //Sort By Model
                return (data1.Model.compareTo(data2.Model));
            case 2:
                //Sort By Price
                return (data1.Price.compareTo(data2.Price));
            case 3:
                //Sort By Location
                return (data1.Location.compareTo(data2.Location));
        }
        return 0;
    }
}
