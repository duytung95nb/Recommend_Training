package Objects;
import java.util.ArrayList;
import java.util.List;

import Supports.ReadValues;

public class UserEvent {
	private String userId;
	private String songId;
	private String genresString;
	private String eventDate;
	private int rate;
	
	public UserEvent(String userId, String songId, String genresString, String eventDate, int rate) {
		this.userId = userId;
		this.songId = songId;
		this.genresString = genresString;
		this.eventDate = eventDate;
		this.rate = rate;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getSongId() {
		return songId;
	}
	public void setSongId(String songId) {
		this.songId = songId;
	}
	public String getGenresString() {
		return genresString;
	}
	public void setGenresString(String genresString) {
		this.genresString = genresString;
	}
	public String getEventDate() {
		return eventDate;
	}
	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}
	
	public static List<UserEvent> getUserEventsFromFile(String filePath){
		List<UserEvent> events = new ArrayList<>();
		ReadValues reader = new ReadValues();
		
		List<String> lines = reader.getListOfLines(filePath);
		
		for (String line : lines) {
			List<String> eventProperties = reader.readValuesInQuotes(line);
			
			// get rate from user event
			String userId = eventProperties.get(0);
			String songId = eventProperties.get(1);
			String genresString = eventProperties.get(2);
			String eventDate = eventProperties.get(3);
			int rate = 0;
			try{
				rate = Integer.parseInt(eventProperties.get(4));
			}catch(NumberFormatException e){
				e.printStackTrace();
			}
			
			UserEvent e = new UserEvent(userId,songId,genresString,eventDate,rate);
			events.add(e);
		}
		
		return events;
	}

}
