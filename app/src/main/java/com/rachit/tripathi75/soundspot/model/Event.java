package com.rachit.tripathi75.soundspot.model;

// Event.java

import java.util.Date;

public class Event {
    private String id;
    private String title;
    private String artistId;
    private String artistName;
    private Date eventDate;
    private String venue;
    private String location;
    private String ticketUrl;
    private boolean isSoldOut;

    public Event(String id, String title, String artistId, String artistName,
                 Date eventDate, String venue, String location,
                 String ticketUrl, boolean isSoldOut) {
        this.id = id;
        this.title = title;
        this.artistId = artistId;
        this.artistName = artistName;
        this.eventDate = eventDate;
        this.venue = venue;
        this.location = location;
        this.ticketUrl = ticketUrl;
        this.isSoldOut = isSoldOut;
    }

    // Getters and setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtistId() {
        return artistId;
    }

    public void setArtistId(String artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }

    public Date getEventDate() {
        return eventDate;
    }

    public void setEventDate(Date eventDate) {
        this.eventDate = eventDate;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTicketUrl() {
        return ticketUrl;
    }

    public void setTicketUrl(String ticketUrl) {
        this.ticketUrl = ticketUrl;
    }

    public boolean isSoldOut() {
        return isSoldOut;
    }

    public void setSoldOut(boolean soldOut) {
        isSoldOut = soldOut;
    }

    // Helper methods
    public String getFormattedEventDate() {
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("MMM d, yyyy");
        return sdf.format(eventDate);
    }

    public String getFullLocation() {
        return venue + ", " + location;
    }

    public boolean isUpcoming() {
        return eventDate.after(new Date());
    }
}