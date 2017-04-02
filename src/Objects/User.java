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

	public void updateListenedSongsFromEvents(List<UserEvent> userEvents) {
		// update listened songs from user events
		Map<String, Integer> songsScore = new HashMap<String, Integer>();
		for (UserEvent userEvent : userEvents) {
			// if it is current user id
			if (this.UserID.equals(userEvent.getUserId())) {
				songsScore.put(userEvent.getSongId(), userEvent.getRate());
			}
		}
		this.setListenedSongs(songsScore);
	}

	public double calculateSimilarity(User other) {
		// same function as multiply matrix
		long score = 0;
		Map<String, Integer> otherUserMap = other.getListenedSongs();
		for (String songId : this.listenedSongs.keySet()) {
			int difference = 0;
			if (otherUserMap.containsKey(songId)) {
				difference = this.listenedSongs.get(songId) - otherUserMap.get(songId);
			}
			// if other map doesnt contains that song
			else {
				difference = this.listenedSongs.get(songId);
			}
			score += (difference * difference);
		}
		double percentage = 1.0 / (1 + score);
		return percentage;
	}

	public Map<String, Double> listUsersWithSimilarityScore(List<User> users) {
		Map<String, Double> list = new HashMap<>();
		for (User user : users) {
			if (this.getUserID() != user.getUserID()) {
				double userScore = this.calculateSimilarity(user);
				list.put(user.getUserID(), userScore);
			}
		}
		return list;
	}

	// 24 hours to millisecond
	private final long DAY_TO_MILIS = 24 * 3600 * 1000;

	public List<String[]> CreateUserEvent(List<Song> songs, int songPerDay, int daysDuration) {
		// generate user history (listened from now, in songs list/ how many
		// songs
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

	// must be an user set in parameter
	public Set<String> getRecommendedSongIds(int numberOfSongs, List<User> sortedByScoreUsers) {
		Set<String> recommendedSongIds = new HashSet();
		for (User user : sortedByScoreUsers) {
			Set<String> exceptSongIds = this.minusExceptSongsIdWithOther(user);
			for (String string : exceptSongIds) {
				recommendedSongIds.add(string);
				numberOfSongs--;
				// get enough song ids to recommend
				if (numberOfSongs < 1) {
					return recommendedSongIds;
				}
			}
		}
		return recommendedSongIds;
	}

	// get list of song ids that other user has but this user doesnt have
	private Set<String> minusExceptSongsIdWithOther(User other) {
		Set<String> otherUserListenedSong = new HashSet(other.listenedSongs.keySet()); // need
																						// to
																						// clone
		otherUserListenedSong.removeAll(this.listenedSongs.keySet());
		return otherUserListenedSong;

	}

	public static void writeUserEventsToFile(List<String[]> userEvents, String filePath) {
		writer.writeArrayValuesToFile(userEvents, filePath);
	}

	public static List<User> getUserListWithListenedSongs(List<UserEvent> userEvents) {
		List<User> users = new ArrayList<>();
		List<String> userIds = new ArrayList<>();
		for (UserEvent userEvent : userEvents) {
			userIds.add(userEvent.getUserId());
		}
		Set<String> distinctUserIds = new HashSet<String>(userIds);

		for (String id : distinctUserIds) {
			User u = new User(id, "");
			u.updateListenedSongsFromEvents(userEvents);
			users.add(u);
		}
		return users;
	}

	public static User getUserById(String id, List<UserEvent> userEvents) {
		int middle = userEvents.size() / 2;
		// id > middle user's id
		if (id.compareTo(userEvents.get(middle).getUserId()) > 0) {
			List<UserEvent> newList = userEvents.subList(middle, userEvents.size());
			return User.getUserById(id, newList);
		}
		// id > middle user's id
		else if (id.compareTo(userEvents.get(middle).getUserId()) < 0) {
			List<UserEvent> newList = userEvents.subList(0, middle);
			return User.getUserById(id, newList);
		} else {

			List<User> users = User.getUserListWithListenedSongs(userEvents);
			// 1 user in List
			if (users.size() > 0)
				return users.get(0);
			else
				return null;
		}
	}

	public static List<User> getUserListFromUserIds(Set<String> userIds, List<UserEvent> userEvents) {
		List<User> users = new ArrayList<>();
		for (String id : userIds) {
			User u = User.getUserById(id, userEvents);
			users.add(u);
		}
		return users;
	}
}
