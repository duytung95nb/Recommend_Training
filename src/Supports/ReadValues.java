package Supports;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReadValues {
	public List<String> readValuesInQuotes(String inputString) {
		List<String> values = new ArrayList<>();
		Matcher m = Pattern.compile("\"([^\"]*)\"").matcher(inputString);
		while (m.find()){
			String founded = m.group(1).trim();
			if(founded!="")
				values.add(founded);
		}
		return values;
	}

	public List<String> getListOfLines(String filePath) {
		List<String> lines = new ArrayList<>();
		File f = new File(filePath);

		try {
			FileInputStream fileInputStream = new FileInputStream(f);
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream, "UTF-8"));
				String line;
				try {
					while ((line = bufferedReader.readLine()) != null) {
						lines.add(line);
					}
					// System.out.println(songs.get(51745).getGenreString());
				}

				catch (IOException ex) {
					// TODO: handle exception
				}
				
				try {
					bufferedReader.close();
					fileInputStream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (UnsupportedEncodingException e) {

			}
		} catch (FileNotFoundException e) {
			// TODO: handle exception
		} finally {
		}
		return lines;
	}
}
