package gessi.plateoss.osseco.sourceconn.pojos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EvolutionaryPRJ {
	private List<String> added_lines = new ArrayList<String>();
	private List<String> authors = new ArrayList<String>();
	private List<String> commits = new ArrayList<String>();
	private List<String> committers = new ArrayList<String>();
	private List<String> date = new ArrayList<String>();
	private List<String> id = new ArrayList<String>();
	private List<String> month = new ArrayList<String>();
	private List<String> name = new ArrayList<String>();
	private List<String> removed_lines = new ArrayList<String>();
	private List<String> repositories = new ArrayList<String>();
	private List<String> unixtime = new ArrayList<String>();
	public List<String> getAdded_lines() {
		return added_lines;
	}
	public void setAdded_lines(List<String> added_lines) {
		this.added_lines = added_lines;
	}
	public List<String> getAuthors() {
		return authors;
	}
	public void setAuthors(List<String> authors) {
		this.authors = authors;
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
	public List<String> getDate() {
		return date;
	}
	public void setDate(List<String> date) {
		this.date = date;
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
	public List<String> getName() {
		return name;
	}
	public void setName(List<String> name) {
		this.name = name;
	}
	public List<String> getRemoved_lines() {
		return removed_lines;
	}
	public void setRemoved_lines(List<String> removed_lines) {
		this.removed_lines = removed_lines;
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
	
	/**
	 * Return the serialized instance of object of EvolutionaryPRJ from a JSON
	 * file
	 * This file contains the summary information of measures
	 * *IMPORTANT* this .JSON file was CHANGED 
	 * @param JSONFile
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static EvolutionaryPRJ getEvolutionaryPRJ(String JSONFile)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		EvolutionaryPRJ value = null;
		value = mapper.readValue(new File(JSONFile), EvolutionaryPRJ.class);
		return value;
	}

	

}
