package Objects;

import java.io.Console;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.opencsv.CSVWriter;

import Supports.Writer;

public class User {
	private String UserID;
	private String Username;
	private Map<String, Integer> listenedSongs;

	private static Writer writer = new Writer();
	
	// constructors area
	public User(String userID, String username) {
		UserID = userID;
		Username = username;
	}


	
	public String getUserID() {
		return UserID;
	}

	public void setUserID(String userID) {
		UserID = userID;
	}

	public String getUsername() {
		return Username;
	}

	public void setUsername(String username) {
		Username = username;
	}



	public User(String userID, Map<String, Integer> listenedSongs) {
		UserID = userID;
		this.listenedSongs = listenedSongs;
	}

	// get/set/update area
	public Map<String, Integer> getListenedSongs() {
		return listenedSongs;
	}
	
	public void setListenedSongs(Map<String, Integer> listenedSongs) {
		this.listenedSongs = listenedSongs;
	}

	
	public void updateListenedSongsFromEvents(List<UserEvent> userEvents){
		// update listened songs from user events
		Map<String, Integer> songsScore = new HashMap<String, Integer>();
		for (UserEvent userEvent : userEvents) {
			// if it is current user id
			if(this.UserID.equals(userEvent.getUserId())){
				songsScore.put(userEvent.getSongId(), userEvent.getRate());
			}
		}
		this.setListenedSongs(songsScore);
	}
	
	public long calculateSimilarity(User other){
		// same function as multiply matrix
		long score = 0;
		Map<String, Integer> otherUserMap = other.getListenedSongs();
		for (String songId : this.listenedSongs.keySet()) {
			if(otherUserMap.containsKey(songId)){
				score+= (this.listenedSongs.get(songId)*otherUserMap.get(songId));
			}
		}
		return score;
	}
	
	public Map<String,Long> listUsersWithSimilarityScore(List<User> users){
		Map<String, Long> list = new HashMap<>();
		for (User user : users) {
			if(this.getUserID()!=user.getUserID()){
				long userScore = this.calculateSimilarity(user);
				list.put(user.getUserID(), userScore);
			}
		}
		return list;
	}
	
	// 24 hours to millisecond
	private final long DAY_TO_MILIS = 24 * 3600 * 1000;

	
	public List<String[]> CreateUserEvent(List<Song> songs, int songPerDay, int daysDuration) {
		// generate user history (listened from now, in songs list/ how many songs
		// per day/ to how many days later?)
		List<String[]> userEvents = new ArrayList<>();
		long currentTime = new Date().getTime();
		int randomSong = 0;
		long randomTime = 0;
		int rate = 0; // rate from 0 to 5, allow user rate freely
		Date randomDate = new Date();
		for (int i = 0; i < daysDuration; i++) {
			for (int j = 0; j < songPerDay; j++) {
				randomSong = (int) (Math.random() * songs.size());
				Song chosen = songs.get(randomSong);
				randomTime = currentTime + (long) (Math.random() * DAY_TO_MILIS);
				randomDate = new Date(randomTime);
				rate = (int) (Math.random() * 5);
				String[] s = new String[] { this.UserID, chosen.getSongID(), chosen.getGenreString(),
						randomDate.toString(), Integer.toString(rate) };
				userEvents.add(s);
			}
			// add 1 day more
			currentTime += DAY_TO_MILIS;
		}
		return userEvents;
	}
	
	public static void writeUserEventsToFile(List<String[]> userEvents, String filePath) {
		writer.writeArrayValuesToFile(userEvents, filePath);
	}
	
	public static List<User> getUserListWithListenedSongs(List<UserEvent> userEvents){
		List<User> users = new ArrayList<>();
		List<String> userIds = new ArrayList<>();
		for (UserEvent userEvent : userEvents) {
			userIds.add(userEvent.getUserId());
		}
		Set<String> distinctUserIds = new HashSet<String>(userIds);
		
		for (String id : distinctUserIds) {
			User u = new User(id,"");
			u.updateListenedSongsFromEvents(userEvents);
			users.add(u);
		}
		return users;
	}

	
}
