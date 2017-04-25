import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.Map.Entry;

import Objects.Song;
import Objects.User;
import Objects.UserEvent;

// manage actions about song and user, user event,....
public class Action {
	private List<UserEvent> userEvents;
	private List<User> users;

	public Action() {

	}

	public Action(List<UserEvent> userEvents, List<User> users) {
		this.userEvents = userEvents;
		this.users = users;
	}

	public List<UserEvent> getUserEvents() {
		return userEvents;
	}

	public void setUserEvents(List<UserEvent> userEvents) {
		this.userEvents = userEvents;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public void CreateUserEvent(List<Song> songs) {
		// create user events
		Scanner s = new Scanner(System.in);
		int songPerDay;
		int daysDuration;
		String username;
		int numberOfUser;

		System.out.println("Input number of songs user should listen every day: ");
		songPerDay = s.nextInt();
		System.out.println("Input time duration (day):");
		daysDuration = s.nextInt();
		System.out.println("Input user name:");
		username = s.next();
		System.out.println("Input number of users:");
		numberOfUser = s.nextInt();

		// create list of users with username
		List<User> users = new ArrayList<>();
		for (int i = 1; i <= numberOfUser; i++) {
			User u = new User("us" + i, username + i);
			users.add(u);
		}
		System.out.println("Got list of users");
		// create user events and write to file
		List<String[]> allUserEvents = new ArrayList<>();
		for (User user : users) {
			List<String[]> currentUserEvents = user.CreateUserEvent(songs, songPerDay, daysDuration);
			allUserEvents.addAll(currentUserEvents);
		}
		User.writeUserEventsToFile(allUserEvents, "UserEvent.csv");
		System.out.println("Finished writing user events to UserEvent.csv");
	}

	public void recommend(){
		Scanner s = new Scanner(System.in);
		String userId;
		int numberOfRecommendations;
		User chosenUser = null;
		System.out.println("Nhap vao user id:");
		userId = s.next();
		System.out.println("Nhap vao so bai hat can goi y:");
		numberOfRecommendations = s.nextInt();
			chosenUser = User.getUserById(userId,this.getUserEvents());
			// sort
			Map<String,Double> listWithScore = chosenUser.listUsersWithSimilarityScore(this.getUsers());
			listWithScore = this.sortDescByValues(listWithScore);
		System.out.println(listWithScore.toString());
		List<User> sortedUsersByScore = User.getUserListFromUserIds(listWithScore.keySet(), this.getUserEvents());
		
		Set<String> recommendSongIds = chosenUser.getRecommendedSongIds(numberOfRecommendations, sortedUsersByScore);
		List<Song> recommendSongs = Song.getSongsFromSetOfIds(recommendSongIds, Song.getAllSongsInList());
		System.out.println(recommendSongIds);
		
		System.out.println("Do ban da nghe:");
		List<Song> listenedSongs = Song.getSongsFromSetOfIds(chosenUser.getListenedSongs().keySet(), Song.getAllSongsInList());
		for (Song song : listenedSongs) {
			System.out.print(song.getSongString()+"|\t");
		}
		
		// start to recommend and calculate time
		System.out.println();
		long startTime = new Date().getTime();
		System.out.println("Recommend cho ban:");
		for (Song song : recommendSongs) {
			System.out.print(song.getSongString()+"|\t");
		}

		long endTime = new Date().getTime();
		System.out.println();
		System.out.println("Thoi gian thuc thi goi y (milisecond):"+((endTime-startTime)));
		s.close();
	}
	
	public Map<String, Double> sortDescByValues(Map<String, Double> input) {
		// sort by values
		List<Map.Entry<String, Double>> inputList = new LinkedList<>(input.entrySet());
		Collections.sort(inputList, new Comparator<Map.Entry<String, Double>>() {

			@Override
			public int compare(Entry<String, Double> arg0, Entry<String, Double> arg1) {
				// TODO Auto-generated method stub
				return arg1.getValue().compareTo(arg0.getValue());
			}

		});
		Map<String, Double> sortedByValue = new LinkedHashMap<String, Double>();
		for (Entry<String, Double> entry : inputList) {
			sortedByValue.put(entry.getKey(), entry.getValue());
		}
		return sortedByValue;
	}
	
	public List<String[]> sortAscByFirstElement(List<String[]> input) {
		// sort by values
		List<String[]> inputList = new LinkedList<>(input);
		Collections.sort(inputList, new Comparator<String[]>() {

			@Override
			public int compare(String[] arg0, String[] arg1) {
				// TODO Auto-generated method stub
				return arg0[0].compareTo(arg1[0]);
			}

		});
		List<String[]> sortedByFirstEle = new LinkedList<>();
		for (String[] entry : inputList) {
			sortedByFirstEle.add(entry);
		}
		return sortedByFirstEle;
	}

}