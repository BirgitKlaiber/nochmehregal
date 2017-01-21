package de.bklaiber.Utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.kbParser.log4KRReader.Log4KRReader;
import edu.cs.ai.log4KR.relational.probabilisticConditionalLogic.syntax.RelationalConditional;

/*
 * this class is used to read from a textfile to initialize a knowledgebase and the queris.
 */
public class QueryReader {
	public static final String QUERY_PREFIX = "query";

	public static Collection<RelationalConditional> readQueries(File file) {

		Log4KRReader reader = new Log4KRReader();
		reader.read(file);

		Map<String, Collection<RelationalConditional>> allKnowledgeBases = reader.getKnowledgeBases();
		Set<Entry<String, Collection<RelationalConditional>>> knowledgebases = allKnowledgeBases.entrySet();

		Collection<RelationalConditional> queries = new ArrayList<RelationalConditional>();
		Iterator<Entry<String, Collection<RelationalConditional>>> it = knowledgebases.iterator();
		while (it.hasNext()) {

			Entry<String, Collection<RelationalConditional>> entry = it.next();
			if (entry.getKey().startsWith(QUERY_PREFIX)) {
				Collection<RelationalConditional> queryCond = reader.getKnowledgeBase(entry.getKey());

				queries.add(queryCond.iterator().next());
			}

		}
		return queries;
	}

}
