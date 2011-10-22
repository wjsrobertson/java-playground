package net.xylophones.jobfinder.jobboard;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import net.xylophones.jobfinder.model.Job;

@Component("descriptionParser")
public class DescriptionParser {

	public void parseDescription(String rssDescription, Job job) {
		Pattern myPattern = Pattern.compile("Job Description : (.*?)<br/>\\s*Advertiser : (.*?)<br/>\\s*Location : (.*?)<br/>\\s*Salary : (.*)");
		Matcher matcher = myPattern.matcher(rssDescription);

		if (matcher.find()) {
			job.setDescription(matcher.group(1));
			job.setAdvertiser(matcher.group(2));
			
			String salary = matcher.group(4);
			parseSalary(salary, job);
		}
	}
	
	private void parseSalary(String salary, Job job) {
		salary = normaliseSalary(salary);
		
		String salaryRange = "£(\\d{3,4}) to £(\\d{3,4}).*";
		String maxValue = "up to £(\\d{3,4}).*";
		String singleValue = "£?(\\d{3,4}) per day";
		String minValue = "from £?(\\d{3,4}) per day";
		
		if (salary.matches(salaryRange)) {
			processSalaryRange(salary, job, salaryRange);
		} else if (salary.matches(maxValue)) {
			processMaxValue(salary, job, maxValue);
		} else if (salary.matches(singleValue)) {
			processSingleValue(salary, job, singleValue);
		} else if (salary.matches(minValue)) {
			processMinValue(salary, job, singleValue);
		} else {
			job.setRateDescription(salary);
		}
	}

	private void processSingleValue(String salary, Job job, String singleValue) {
		Pattern myPattern = Pattern.compile(singleValue);
		Matcher matcher = myPattern.matcher(salary);
		
		if (matcher.find()) {
			String max = matcher.group(1);
			job.setMaxRate( Integer.valueOf(max) );
			job.setMinRate( Integer.valueOf(max) );
		}
	}

	private void processMaxValue(String salary, Job job, String maxValue) {
		Pattern myPattern = Pattern.compile(maxValue);
		Matcher matcher = myPattern.matcher(salary);
		
		if (matcher.find()) {
			String max = matcher.group(1);
			job.setMaxRate( Integer.valueOf(max) );
		}
	}

	private void processSalaryRange(String salary, Job job, String salaryRange) {
		Pattern myPattern = Pattern.compile(salaryRange);
		Matcher matcher = myPattern.matcher(salary);
		
		if (matcher.find()) {
			String min = matcher.group(1);
			String max = matcher.group(2);
			
			int minSalary = Integer.valueOf(min);
			int maxSalary = Integer.valueOf(max);
			
			job.setMinRate(minSalary);
			job.setMaxRate(maxSalary);
		}
	}
	
	private void processMinValue(String salary, Job job, String minValue) {
		Pattern myPattern = Pattern.compile(minValue);
		Matcher matcher = myPattern.matcher(salary);
		
		if (matcher.find()) {
			String min = matcher.group(1);
			job.setMinRate( Integer.valueOf(min) );
		}
	}
	
	private String normaliseSalary(String salary) {
		String original = salary;
		salary = salary.toLowerCase();
		
		salary = salary.replaceAll(".*market .*rate.*", "market rates");
		salary = salary.replaceAll(".*competitive.*", "competitive");
		salary = salary.replaceAll(".*negotiable.*", "negotiable");
		salary = salary.replaceAll("^\\s*neg\\s*$", "negotiable");
		salary = salary.replaceAll(".*euros.*", "wrong currency");
		
		salary = salary.replaceAll(".*per annum *\\+ *", "");		
		salary = salary.replaceAll(".*per month \\+", "");
		salary = salary.replaceAll("rate -", " ");
		
		salary = salary.replaceAll("p/d", " per day");
		salary = salary.replaceAll("a day", " per day");
		salary = salary.replaceAll("/day", " per day");
		salary = salary.replaceAll("per day.*", "per day");
		salary = salary.replaceAll("(\\d{3,4}) pd", "$1 per day");
		salary = salary.replaceAll("/per day", " per day");
		salary = salary.replaceAll("- per day", " per day");
						
		salary = salary.replaceAll("\\.00", " ");
		salary = salary.replaceAll("ukp *", "£");
		salary = salary.replaceAll("&pound;", "£");

		salary = salary.replaceAll("£* (\\d{3,4})", " £$1");
		salary = salary.replaceAll("^ *to", "up to");
		salary = salary.replaceAll("^ *upto", "up to");
		salary = salary.replaceAll("£?(\\d{3,4}) *(to|-|/) *£?(\\d{3,4})", "£$1 to £$3");
		salary = salary.replaceAll(" +", " ");
		salary = salary.replaceAll("^ +", "");
		salary = salary.replaceAll("^£+(\\d{3,4})\\+", "from £$1");
		
		salary = salary.replaceAll("^\\s*(.*)\\s$", "$1");
		
		salary = salary.replaceAll("^\\s*$", "unspecified");

		//System.out.println(original+"@"+salary);

		return salary;
	}

}
