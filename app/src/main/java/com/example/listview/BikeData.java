package com.example.listview;

public class BikeData {

    final String Company;
    final String Model;
    final Double Price;
    final String Description;
    final String Location;
    final String Date;
    final String Picture;
    final String Link;

    /*
     * Takes the info held within BikeData and formats it using StringBuilder
     * @return: String, The string created with BikeData info
     */
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Company").append(":").append(this.Company).append("\n");
        builder.append("Model").append(":").append(this.Model).append("\n");
        builder.append("Price").append(":").append(this.Price).append("\n");
        builder.append("Location").append(":").append(this.Location).append("\n");
        builder.append("Date Listed").append(":").append(this.Date).append("\n");
        builder.append("Description").append(":").append(this.Description).append("\n");
        builder.append("Link").append(":").append(this.Link);
        return builder.toString();
    }

    private BikeData(Builder b) {
        this.Company = b.Company;
        this.Model = b.Model;
        this.Price = b.Price;
        this.Description = b.Description;
        this.Location = b.Location;
        this.Date = b.Date;
        this.Picture = b.Picture;
        this.Link = b.Link;
    }

    /**
     * @author lynn builder pattern, see page 11 Effective Java UserData mydata
     *         = new
     *         UserData.Builder(first,last).addProject(proj1).addProject(proj2
     *         ).build()
     */
    public static class Builder {
        final String Company;
        final String Model;
        final Double Price;
        String Description;
        String Location;
        String Date;
        String Picture;
        String Link;

        // Model and price required
        Builder(String Company, String Model, Double Price) {
            this.Company = Company;
            this.Model = Model;
            this.Price = Price;
        }

        // the following are setters
        // notice it returns this bulder
        // makes it suitable for chaining
        Builder setDescription(String Description) {
            this.Description = Description;
            return this;
        }

        Builder setLocation(String Location) {
            this.Location = Location;
            return this;
        }

        Builder setDate(String Date) {
            this.Date = Date;
            return this;
        }

        Builder setPicture(String Picture) {
            this.Picture = Picture;
            return this;
        }

        Builder setLink(String Link) {
            this.Link = Link;
            return this;
        }

        // use this to actually construct Bikedata
        // without fear of partial construction
        public BikeData build() {
            return new BikeData(this);
        }
    }
}
