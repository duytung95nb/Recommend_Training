import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.mllib.linalg.Vectors;

import Objects.Song;
import Objects.User;
import Objects.UserEvent;

public class Main {
	public static void main(String[] args) {
		// get list of songs
		List<Song> songs = null;
		songs = Song.getAllSongFromFile("database.csv");
		Song.setAllSongsInList(songs);
		Scanner s = new Scanner(System.in);
		int choice = 0;
		System.out.println("Nhap vao lua chon \n" + "1- Tao user event\n" 
						    + "2 - Tao bang user\n"
		                    + "3 - Recommend bang user id\n");
		choice = s.nextInt();
		
		Action action = new Action();
		List<UserEvent> userEvents = new ArrayList<>();
		userEvents = UserEvent.getUserEventsFromFile("UserEvent.csv");
		action.setUserEvents(userEvents);
		List<User> usersList = User.getUserListWithListenedSongs(userEvents);
		action.setUsers(usersList);
		
		switch (choice) {
		case 1:
			action.CreateUserEvent(songs);
			break;

		case 2:
			long startTime = new Date().getTime();
			action.recommend();
			long endTime = new Date().getTime();
			System.out.println();
			System.out.println("Thoi gian thuc thi goi y (second):"+((endTime-startTime)/1000));
			
			break;
		case 3:
			break;
		default:
			System.out.println("Nhap sai!");
		}
		s.close();
	}
}
