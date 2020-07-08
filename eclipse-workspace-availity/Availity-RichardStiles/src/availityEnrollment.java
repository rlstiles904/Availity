import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class availityEnrollment {
	
	static String pathToFile = System.getProperty("user.dir");

	public static void main(String[] args) {
		availityEnrollment ae = new availityEnrollment();
		boolean result = false;
//		result = ae.buildRepList("enrollment.csv");
		result = ae.importCSV("enrollment.csv");
		System.out.println("The enrollment import was successful (true/false) = " + result);
	}
	
	public boolean importCSV(String fileName) {
		pathToFile = pathToFile +"\\"+ fileName;
		boolean success = false;
		List<ArrayList<String>> records = new ArrayList<>();
		List<ArrayList<String>> records2 = new ArrayList<>();
		List<ArrayList<String>> recordsZ = new ArrayList<>();
		File csvFile = new File(pathToFile);  
		if (csvFile.isFile()) {
			try (BufferedReader br = new BufferedReader(new FileReader(pathToFile))) {
				String line;
				String line2;
				while ((line = br.readLine()) != null) {
					String[] values = line.split(",");
					String[] values2;
					if(values[0].indexOf("User Id") == 0) {
						records2.add(new ArrayList<String>(Arrays.asList(values)));
					} else {
//						availityEnrollment ae = new availityEnrollment();
//						values = ae.dedup(values);
						line2 = line + "," + values[0] + values[2] + values[1] + values[3];
						values2 = line2.split(",");
						records.add(new ArrayList<String>(Arrays.asList(values2)));
						success = true;
					}
				}
				availityEnrollment ae = new availityEnrollment();
				records = ae.sort(records);
				records = ae.dedup(records);
				recordsZ = ae.getCompanyList(records);

			} catch (FileNotFoundException e) {success=false;e.printStackTrace();} catch (IOException e) {success=false;e.printStackTrace();}
		
			for(List<String> recordDataZ : recordsZ) {
				FileWriter csvWriter;
//				pathToFile = pathToFile.substring(0, pathToFile.length()-4) + recordDataZ.get(0) + ".csv"
				try {csvWriter = new FileWriter(pathToFile.substring(0, pathToFile.length()-4) + recordDataZ.get(0) + ".csv");				  
					success = true;
					for (List<String> recordData : records2) {  
						csvWriter.append(String.join(",", recordData));
						csvWriter.append("\n");
						}
					for (List<String> recordData : records) {  
						if (recordData.get(4).indexOf(recordDataZ.get(0)) >= 0) {
						csvWriter.append(String.join(",", recordData));
						csvWriter.append("\n");
						}
					}
					csvWriter.flush();  
					csvWriter.close();  
				} catch (IOException e1) {success=false;e1.printStackTrace();}		
			}
		}

		return success;
	}
	
	public static boolean buildRepList(String fileName) {
		pathToFile = pathToFile +"\\"+ fileName;
		boolean status = false;
		
		List<List<String>> rows = Arrays.asList(  
			    Arrays.asList("rstiles", "Richard", "Stiles", "1", "Atnea"),
			    Arrays.asList("trey", "Tom", "Rey", "1", "Atnea"),
			    Arrays.asList("rstiles", "Richard", "Stiles", "2", "Prudential"),
			    Arrays.asList("pallen", "Paul", "Allen", "1", "Atnea"),
			    Arrays.asList("trey", "Tom", "Rey", "2", "Prudential"),
			    Arrays.asList("jbride", "Jim", "Bride", "1", "Prudential"),
			    Arrays.asList("trey", "Tom", "Rey", "3", "Blue")
			);

			FileWriter csvWriter;
			try {csvWriter = new FileWriter(pathToFile);				  
				csvWriter.append("User Id");  
				csvWriter.append(",");  
				csvWriter.append("First Name");  
				csvWriter.append(",");  
				csvWriter.append("Last Name");  
				csvWriter.append(",");  
				csvWriter.append("Version");  
				csvWriter.append(",");  
				csvWriter.append("Insurance Company");  
				csvWriter.append("\n");
				status = true;
				for (List<String> rowData : rows) {  
					csvWriter.append(String.join(",", rowData));
					csvWriter.append("\n");
				}
				csvWriter.flush();  
				csvWriter.close();  
			} catch (IOException e1) {status=false;e1.printStackTrace();}
		
		return status;
	}
	
	public List<ArrayList<String>> sort(List<ArrayList<String>> repData) {
		
		Comparator<ArrayList<String>> comp = new Comparator<ArrayList<String>>() {
		    public int compare(ArrayList<String> csvLine1, ArrayList<String> csvLine2) {
		        return String.valueOf(csvLine1.get(5)).compareTo(String.valueOf(csvLine2.get(5)));
		    }
		};
		Collections.sort(repData, comp);
		
		return repData;
	}
	
	public List<ArrayList<String>> dedup(List<ArrayList<String>> repData) {
		
		String line3="", loUserId="", loFirstName="", loLastName="", loVersion="", loInsuranceCompany="";
		int count = 0;
		List<ArrayList<String>> records3 = new ArrayList<>();

		for (List<String> recordData : repData) {  
			if(count > 0 && recordData.get(1).indexOf(loFirstName) == -1 && recordData.get(2).indexOf(loLastName) == -1) {
				line3 = loUserId+","+loFirstName+","+loLastName+","+loVersion+","+loInsuranceCompany;
				String[] values3 = line3.split(",");
				records3.add(new ArrayList<String>(Arrays.asList(values3)));
			}
			loUserId = recordData.get(0);
			loFirstName = recordData.get(1);
			loLastName = recordData.get(2);
			loVersion = recordData.get(3);
			loInsuranceCompany = recordData.get(4);
			count++;				
		}
		if(count > 0) {
			line3 = loUserId+","+loFirstName+","+loLastName+","+loVersion+","+loInsuranceCompany;
			String[] values3 = line3.split(",");
			records3.add(new ArrayList<String>(Arrays.asList(values3)));
		}
	
		return records3;
	}
	
	public List<ArrayList<String>> getCompanyList(List<ArrayList<String>> repData) {
		
		String line3="";
		int count = 0;
		boolean found=false;
		List<ArrayList<String>> records3 = new ArrayList<>();

		for (List<String> recordData : repData) {
			if (count==0) {				
				line3 = recordData.get(4);
				String[] values3 = line3.split(",");
				records3.add(new ArrayList<String>(Arrays.asList(values3)));
			} else {
				for (List<String> recordData3 : records3) {
					if(recordData.get(4).indexOf(recordData3.get(0)) >= 0) {
						found = true;
					}
				}
				if(!found) {
					line3 = recordData.get(4);
					String[] values3 = line3.split(",");
					records3.add(new ArrayList<String>(Arrays.asList(values3)));
					found = false;
				}
			}
			count++;				
		}
	
		return records3;
	}
}
