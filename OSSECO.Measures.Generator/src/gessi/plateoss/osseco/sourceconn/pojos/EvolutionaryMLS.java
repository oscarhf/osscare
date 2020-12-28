package gessi.plateoss.osseco.sourceconn.pojos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EvolutionaryMLS {
	private List<String> date = new ArrayList<String>();
	private List<String> id = new ArrayList<String>();
	private List<String> month = new ArrayList<String>();
	private List<String> organizations = new ArrayList<String>();
	private List<String> repositories = new ArrayList<String>();
	private List<String> senders = new ArrayList<String>();
	private List<String> senders_init = new ArrayList<String>();
	private List<String> senders_response = new ArrayList<String>();
	private List<String> sent = new ArrayList<String>();
	private List<String> sent_response = new ArrayList<String>();
	private List<String> threads = new ArrayList<String>();
	private List<String> unixtime = new ArrayList<String>();
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
	public List<String> getOrganizations() {
		return organizations;
	}
	public void setOrganizations(List<String> organizations) {
		this.organizations = organizations;
	}
	public List<String> getRepositories() {
		return repositories;
	}
	public void setRepositories(List<String> repositories) {
		this.repositories = repositories;
	}
	public List<String> getSenders() {
		return senders;
	}
	public void setSenders(List<String> senders) {
		this.senders = senders;
	}
	public List<String> getSenders_init() {
		return senders_init;
	}
	public void setSenders_init(List<String> senders_init) {
		this.senders_init = senders_init;
	}
	public List<String> getSenders_response() {
		return senders_response;
	}
	public void setSenders_response(List<String> senders_response) {
		this.senders_response = senders_response;
	}
	public List<String> getSent() {
		return sent;
	}
	public void setSent(List<String> sent) {
		this.sent = sent;
	}
	public List<String> getSent_response() {
		return sent_response;
	}
	public void setSent_response(List<String> sent_response) {
		this.sent_response = sent_response;
	}
	public List<String> getThreads() {
		return threads;
	}
	public void setThreads(List<String> threads) {
		this.threads = threads;
	}
	public List<String> getUnixtime() {
		return unixtime;
	}
	public void setUnixtime(List<String> unixtime) {
		this.unixtime = unixtime;
	}
	
	/**
	 * Return the serialized instance of object of EvolutionaryMLS from a JSON
	 * file
	 * This file contains the summary information of measures
	 * @param JSONFile
	 * @return
	 * @throws JsonParseException
	 * @throws JsonMappingException
	 * @throws IOException
	 */
	public static EvolutionaryMLS getEvolutionaryMLS(String JSONFile)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		EvolutionaryMLS value = null;
		value = mapper.readValue(new File(JSONFile), EvolutionaryMLS.class);
		return value;
	}
}
