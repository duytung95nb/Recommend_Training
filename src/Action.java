import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
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

		for (User user : users) {
			List<String[]> currentUserEvents = user.CreateUserEvent(songs, songPerDay, daysDuration);
			User.writeUserEventsToFile(currentUserEvents, "UserEvent.csv");
		}
		System.out.println("Finished writing user events to UserEvent.csv");
	}

	public User getUserById(String id) {
		List<User> users = User.getUserListWithListenedSongs(this.userEvents);
		for (User user : users) {
			System.out.println(user.getUserID());
			if (user.getUserID().equals(id)) {
				return user;
			}
		}
		return null;
	}

	public Map<String, Long> sortDescByValues(Map<String, Long> input) {
		// sort by values
		List<Map.Entry<String, Long>> inputList = new LinkedList<>(input.entrySet());
		Collections.sort(inputList, new Comparator<Map.Entry<String, Long>>() {

			@Override
			public int compare(Entry<String, Long> arg0, Entry<String, Long> arg1) {
				// TODO Auto-generated method stub
				return arg1.getValue().compareTo(arg0.getValue());
			}

		});
		Map<String, Long> sortedByValue = new LinkedHashMap<String, Long>();
		for (Entry<String, Long> entry : inputList) {
			sortedByValue.put(entry.getKey(), entry.getValue());
		}
		return sortedByValue;
	}

}