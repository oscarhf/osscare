package gessi.plateoss.osseco.sourceconn;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import gessi.plateoss.osseco.sourceconn.pojos.EvolutionaryITS;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class OssecoJSONFilesAdapter {

	
	/**
	 *  
	 * @return
	 * @throws IOException
	 */
	/*public static EvolutionaryITS getEvolutionaryITSSummary()
			throws IOException {
		EvolutionaryITS summaryITS = new EvolutionaryITS();
		EvolutionaryITS individualITS = new EvolutionaryITS();
		List<String> listOfFiles = filterFiles("json/",
				"^((?<!ABC|XYZ).*?)?-its((?<!ABC|XYZ).*?)?evolutionary.json$");
		
		int i=0;
		for (String strFile : listOfFiles) {
			individualITS = getEvolutionaryITS(strFile);
						
		    summaryITS.addEvolutionaryITS(individualITS);
			
		}
		
		return summaryITS;
	}*/

	
}
