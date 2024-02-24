package com.example.demo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="album")
public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_album;
    private String albumname;

    @ManyToOne(optional = true)
    private Artist artist;

    public Album() {
        // Default no-argument constructor is required for JPA entities
    }

    public Album(String albumname, Artist artist) {
        this.albumname = albumname;
        this.artist = artist;
    }

    public int getId_album() {
        return id_album;
    }

    public void setId_album(int id_album) {
        this.id_album = id_album;
    }

    public String getAlbumname() {
        return albumname;
    }

    public void setAlbumname(String albumname) {
        this.albumname = albumname;
    }

    public Artist getArtist() {
        return artist;
    }

    public void setArtist(Artist artist) {
        this.artist = artist;
    }

	public String getImageName() {
		// TODO Auto-generated method stub
		return null;
	}
}
