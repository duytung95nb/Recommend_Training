package Objects;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.spark.mllib.linalg.Vector;

import Supports.ReadValues;

public class Song {
	private String songID;
	private String songString;
	private String artistString;
	private String authorString;
	private String albumString;
	private String genreString;
	
	private static List<Song> allSongsInList;
	public Song(String songID, String songString, String artistString, String authorString, String albumString, String genreString) {
		this.songID = songID;
		this.songString = songString;
		this.artistString = artistString;
		this.authorString = authorString;
		this.albumString = albumString;
		this.genreString = genreString;
	}
	
	public String getSongID() {
		return songID;
	}

	public void setSongID(String songID) {
		this.songID = songID;
	}


	public String getSongString() {
		return songString;
	}
	public void setSongString(String songString) {
		this.songString = songString;
	}
	public String getArtistString() {
		return artistString;
	}
	public void setArtistString(String artistString) {
		this.artistString = artistString;
	}
	public String getAuthorString() {
		return authorString;
	}
	public void setAuthorString(String authorString) {
		this.authorString = authorString;
	}
	public String getAlbumString() {
		return albumString;
	}
	public void setAlbumString(String albumString) {
		this.albumString = albumString;
	}
	public String getGenreString() {
		return genreString;
	}
	public void setGenreString(String genreString) {
		this.genreString = genreString;
	}
	
	public static List<Song> getAllSongsInList() {
		return allSongsInList;
	}

	public static void setAllSongsInList(List<Song> allSongsInList) {
		Song.allSongsInList = allSongsInList;
	}

	public static List<Song> getAllSongFromFile(String filePath){
		
		List<Song> songs = new ArrayList<>();
		ReadValues reader = new ReadValues();
		// start to read
		List<String> lines = reader.getListOfLines(filePath);
		for (String line : lines) {
			List<String> songInfo = reader.readValuesInQuotes(line);
			songs.add(new Song(songInfo.get(0).toString(), 
					songInfo.get(1).toString(), 
					songInfo.get(2).toString(),
					songInfo.get(3).toString(),
					songInfo.get(4).toString(),
					songInfo.get(5).toString()));
		}
		
		return songs;
	}
	
	public static List<String> getAllGenresInList(List<Song> songs){
		List<String> genres = new ArrayList<>();
		for (Song song : songs) {
			String[] songGenres = Song.getAllGenresInString(song.getGenreString());
			for(int i=0;i<songGenres.length;i++){
				if(!genres.contains(songGenres[i]))
					genres.add(songGenres[i]);
			}
		}
		return genres;
	}
	public static List<Song> getSongsFromSetOfIds(Set<String> ids, List<Song> allSongs){
		List<Song> songs = new ArrayList<>();
		for (String id : ids) {
			Song s = Song.getSongById(id, allSongs);
			songs.add(s);
		}
		return songs;
	}
	
	// need to fix
	public static Song getSongById(String id, List<Song> allSongs) {
		for (Song song : allSongs) {
			if (song.getSongID().equals(id)) {
				return song;
			}
		}
		return null;
	}
	// processing
	private static Song quickFindSongById(String id, List<Song> allSongs){
		
		for(int i=0;i<allSongs.size();i++){
			int middleOfList = allSongs.size()/2;
			if(id.compareTo(allSongs.get(middleOfList).getSongID())>0){
				
			}
		}
		return null;
	}
	private static String[] getAllGenresInString(String genreString){
		String[] genres = genreString.split("/|,");
		for(int i=0;i<genres.length;i++){
			genres[i] = genres[i].replaceAll("^\\s+|\\s+$", "");
		}
		return genres;
	}

}