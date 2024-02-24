package com.example.demo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.List;

@Entity
@Table(name="artist")
public class Artist {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id_artist;
    private String name;
    private String des;
    private Integer id_album;
    

    @Column(name = "image_name")
    private String imageName;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "artist") // Use "artist" as mappedBy
    private List<Album> albums;

    public Artist() {
    }
    	
	public Artist(int id_artist, String name, String des, int id_album, String imageName, List<Album> albums) {
		super();
		this.id_artist = id_artist;
		this.name = name;
		this.des = des;
		this.id_album = id_album;
		this.imageName = imageName;
		this.albums = albums;
	}

	public int getId_artist() {
		return id_artist;
	}

	public void setId_artist(int id_artist) {
		this.id_artist = id_artist;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDes() {
		return des;
	}

	public void setDes(String des) {
		this.des = des;
	}

	public int getId_album() {
		return id_album;
	}

	public void setId_album(int id_album) {
		this.id_album = id_album;
	}

	public String getImageName() {
		return imageName;
	}

	public void setImageName(String imageName) {
		this.imageName = imageName;
	}

	public List<Album> getAlbums() {
		return albums;
	}

	public void setAlbums(List<Album> albums) {
		this.albums = albums;
	}

   
}
