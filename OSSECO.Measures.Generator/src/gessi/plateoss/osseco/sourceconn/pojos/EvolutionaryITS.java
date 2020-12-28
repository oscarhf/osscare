package gessi.plateoss.osseco.sourceconn.pojos;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class EvolutionaryITS {
	private List<String> bmitickets = new ArrayList<String>();
	private List<String> changed = new ArrayList<String>();
	private List<String> changers = new ArrayList<String>();
	private List<String> closed = new ArrayList<String>();
	private List<String> closers = new ArrayList<String>();
	private List<String> date = new ArrayList<String>();
	private List<String> id = new ArrayList<String>();
	private List<String> month = new ArrayList<String>();
	private List<String> opened = new ArrayList<String>();
	private List<String> openers = new ArrayList<String>();
	private List<String> trackers = new ArrayList<String>();
	private List<String> unixtime = new ArrayList<String>();
	public boolean initialized = false;

	public List<String> getBmitickets() {
		return bmitickets;
	}

	public void setBmitickets(List<String> bmitickets) {
		this.bmitickets = bmitickets;
	}

	public void setChanged(List<String> changed) {
		this.changed = changed;
	}

	public void setChangers(List<String> changers) {
		this.changers = changers;
	}

	public void setClosed(List<String> closed) {
		this.closed = closed;
	}

	public void setClosers(List<String> closers) {
		this.closers = closers;
	}

	public void setDate(List<String> date) {
		this.date = date;
	}

	public void setId(List<String> id) {
		this.id = id;
	}

	public void setMonth(List<String> month) {
		this.month = month;
	}

	public void setOpened(List<String> opened) {
		this.opened = opened;
	}

	public void setOpeners(List<String> openers) {
		this.openers = openers;
	}

	public void setTrackers(List<String> trackers) {
		this.trackers = trackers;
	}

	public void setUnixtime(List<String> unixtime) {
		this.unixtime = unixtime;
	}

	public List<String> getChanged() {
		return changed;
	}

	public List<String> getChangers() {
		return changers;
	}

	public List<String> getClosed() {
		return closed;
	}

	public List<String> getClosers() {
		return closers;
	}

	public List<String> getDate() {
		return date;
	}

	public List<String> getId() {
		return id;
	}

	public List<String> getMonth() {
		return month;
	}

	public List<String> getOpened() {
		return opened;
	}

	public List<String> getOpeners() {
		return openers;
	}

	public List<String> getTrackers() {
		return trackers;
	}

	public List<String> getUnixtime() {
		return unixtime;
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
	public static EvolutionaryITS getEvolutionaryITS(String JSONFile)
			throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		EvolutionaryITS value = null;
		value = mapper.readValue(new File(JSONFile), EvolutionaryITS.class);
		return value;
	}

	public void addEvolutionaryITS(EvolutionaryITS individualITS) {
		if (!initialized) {
			for (String strValue : individualITS.getOpened()) {
				this.opened.add(strValue);
			}
			initialized = true;
		}
		int intValue1 = 0, intValue2 = 0, limS=this.getOpened().size();
		if(limS > individualITS.getOpened().size())
		{
			limS=individualITS.getOpened().size();
		}
		for (int i = 0; i < limS; i++) {
			intValue1 = Integer.parseInt(this.getOpened().get(i));
			if (individualITS.getOpened() != null) {
				intValue2 = Integer.parseInt(individualITS.getOpened().get(i));
			}
			this.getOpened().set(i, String.valueOf(intValue1 + intValue2));
		}
	}

}
