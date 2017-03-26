package Supports;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.List;
import com.opencsv.CSVWriter;

public class Writer {
	private static CSVWriter writer;
	public void writeArrayValuesToFile(List<String[]> values, String filePath){
		try {
			FileOutputStream fileOutputStream = new FileOutputStream(filePath, true);
			writer = new CSVWriter(new OutputStreamWriter(fileOutputStream,"UTF-8"), ',');
			for (String[] value : values) {
				writer.writeNext(value);
			}
			writer.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
