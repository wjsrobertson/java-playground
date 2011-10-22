package net.xylophones.jobfinder.jobboard;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;

@Component("jobBoardDateParser")
public class JobBoardDateParser {
	
	private final static String format = "dd MMM yyyy";
	
	private ThreadLocal<SimpleDateFormat> dateFormat = new ThreadLocal<SimpleDateFormat>() {
		@Override
		protected SimpleDateFormat initialValue() {
			return new SimpleDateFormat(format);
		}
	};

	public Date parse(String dateString) {
		dateString = dateString.replaceAll("^\\w{3}, (\\d{2} \\w{3} \\d{4}).*", "$1");
		try {
			return dateFormat.get().parse(dateString);
		} catch (ParseException e) {
			return null;
		}
	}
}
