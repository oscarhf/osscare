package gessi.plateoss.osseco.sourceconn.pojos;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EvolutionarySCM {
	private List<String> authors= new ArrayList<String>();
	private List<String> branches= new ArrayList<String>();
	private List<String> commits= new ArrayList<String>();
	private List<String> committers= new ArrayList<String>();
	private List<String> date= new ArrayList<String>();
	private List<String> files= new ArrayList<String>();
	private List<String> id= new ArrayList<String>();
	private List<String> month= new ArrayList<String>();
	private List<String> repositories= new ArrayList<String>();
	private List<String> unixtime= new ArrayList<String>();
	public boolean initialized = false;
	
	/**
	 * GETTERS AND SETTERS
	 */
	
	public List<String> getAuthors() {
		return authors;
	}
	public void setAuthors(List<String> authors) {
		this.authors = authors;
	}
	public List<String> getBranches() {
		return branches;
	}
	public void setBranches(List<String> branches) {
		this.branches = branches;
	}
	public List<String> getCommits() {
		return commits;
	}
	public void setCommits(List<String> commits) {
		this.commits = commits;
	}
	public List<String> getCommitters() {
		return committers;
	}
	public void setCommitters(List<String> committers) {
		this.committers = committers;
	}
	public List<String> getFiles() {
		return files;
	}
	public void setFiles(List<String> files) {
		this.files = files;
	}
	public List<String> getId() {
		return id;
	}
	public void setId(List<String> id) {
		this.id = id;
	}
	public List<String> getMonth() {
		return month;
	}
	public void setMonth(List<String> month) {
		this.month = month;
	}
	public List<String> getRepositories() {
		return repositories;
	}
	public void setRepositories(List<String> repositories) {
		this.repositories = repositories;
	}
	public List<String> getUnixtime() {
		return unixtime;
	}
	public void setUnixtime(List<String> unixtime) {
		this.unixtime = unixtime;
	}
	
	public void addEvolutionarySCM(EvolutionarySCM individualSCM) {
		if (!initialized) {
			for (String strValue : individualSCM.getFiles()) {
				this.files.add(strValue);
			}
			for (String strValue : individualSCM.getCommits()) {
				this.commits.add(strValue);
			}
			
			initialized = true;
		}
		int intValue1 = 0, intValue2 = 0, limS=this.getFiles().size();
		if(limS > individualSCM.getFiles().size())
		{
			limS=individualSCM.getFiles().size();
		}
		for (int i = 0; i < limS; i++) {
			intValue1 = Integer.parseInt(this.getFiles().get(i));
			if (individualSCM.getFiles() != null) {
				intValue2 = Integer.parseInt(individualSCM.getFiles().get(i));
			}
			this.getFiles().set(i, String.valueOf(intValue1 + intValue2));
			intValue1 = Integer.parseInt(this.getCommits().get(i));
			if (individualSCM.getCommits() != null) {
				intValue2 = Integer.parseInt(individualSCM.getCommits().get(i));
			}
			this.getCommits().set(i, String.valueOf(intValue1 + intValue2));
			
		}
	}
	public List<String> getDate() {
		return date;
	}
	public void setDate(List<String> date) {
		this.date = date;
	}
	
	
	/**
	 * Return the serialized instance of object of EvolutionaryITS from a JSON
	 * file
	 * This file contains the summary information of measures
	 * @param JSONFile
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static EvolutionarySCM getEvolutionarySCM(String JSONFile)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		EvolutionarySCM value = null;
		value = mapper.readValue(new File(JSONFile), EvolutionarySCM.class);
		return value;
	}

/**
 * 
 * @return
 * @throws IOException
 */
	
	public static EvolutionarySCM getEvolutionarySCM()
			throws IOException {
		EvolutionarySCM summarySCM = new EvolutionarySCM();
		EvolutionarySCM individualSCM = new EvolutionarySCM();
		List<String> listOfFiles = filterFiles("data/json/",
				"^((?<!ABC|XYZ).*?)?scm-dom((?<!ABC|XYZ).*?)?evolutionary.json$");
		
		int i=0;
		for (String strFile : listOfFiles) {
		
			individualSCM = getEvolutionarySCM(strFile);	
		    summarySCM.addEvolutionarySCM(individualSCM);
			
		}
		for(String strDate: individualSCM.getDate())
		{
			summarySCM.getDate().add(strDate);
		}
		return summarySCM;
		
		

	}
	
	/**
	 * Return the list of filter files from a path
	 * 
	 * @param strFilter
	 * @return
	 * @throws IOException
	 */
	private static List<String> filterFiles(String strPath, String strFilter)
			throws IOException {
		List<String> listFiles = new ArrayList<String>();
		 System.out.println("Working Directory = " +
	              System.getProperty("user.dir"));
		try (Stream<Path> stream = Files.list(Paths.get(strPath))) {
			listFiles = stream.map(String::valueOf)
					.filter(path -> path.matches(strFilter))
					.collect(Collectors.toList());

		} catch (IOException e) {
			throw e;
		}

		return listFiles;
	}


}
