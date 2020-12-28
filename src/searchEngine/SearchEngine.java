package searchEngine;

import java.util.HashMap;
import java.util.ArrayList;

public class SearchEngine {
	public HashMap<String, ArrayList<String> > wordIndex;   // this will contain a set of pairs (String, LinkedList of Strings)	
	public MyWebGraph internet;
	public XmlParser parser;

	public SearchEngine(String filename) throws Exception{
		this.wordIndex = new HashMap<String, ArrayList<String>>(); //database : word -> urls containing them 
		this.internet = new MyWebGraph(); //internet graph, each node -> url, each edge -> hyperlink that connects the urls
		this.parser = new XmlParser(filename);
	}
	
	/* 
	 * This does a graph traversal of the web, starting at the given url.
	 * For each new page seen, it updates the wordIndex, the web graph,
	 * and the set of visited vertices.
	 * 
	 * 	This method will fit in about 30-50 lines (or less)
	 */
	public void crawlAndIndex(String url) throws Exception {
		// TODO : RECURSIVE DFS
		
		//UPDATING WEB GRAPH
		
		internet.addVertex(url); //adding url to graph
		internet.setVisited(url, true); //visited = true
		
		/*
		 * to update the word index, the url provided must be parsed for the words 
		 * -> XmlParser 
		 * --> getContent(String url) - returns an ArrayList of Strings corresponding to the set of words in the web page located at the given url.
		 * --> 
 		 * 
		 * then add each word into the word index (hashmap) and the url associated.
		 * in the hashmap: key - word, value - url
		 * -> search through hashmap for word
		 * --> if word already exists, add url to word (add url as value to that key)
		 * --->check 
		 * 
		 * --> if word doesn't exist, add word and url (add key and value)
		 */
		
		//UPDATING WORD INDEX
		
		ArrayList<String> content = parser.getContent(url);
		
		for(int i = 0; i < content.size(); i ++) {
			
			if(wordIndex.containsKey(content.get(i).toLowerCase())) {
				ArrayList<String> urlList = wordIndex.get(content.get(i).toLowerCase());
				
				if(!urlList.contains(url)) { //check if url already exists in urlList
					urlList.add(url);
					wordIndex.put(content.get(i).toLowerCase(), urlList);
				}
			} 
			else {
				ArrayList<String> urlList = new ArrayList<String>();
				urlList.add(url);
				wordIndex.put(content.get(i).toLowerCase(), urlList);
			}
		}
		
		//WORD INDEX UPDATED	
		
		/* Iterate through all the edges going out of a given node (url)
		 * traverse graph such that node != null or node has a next node
		 * if the next node has not been visited, then carry out crawlAndIndex on the next node
		 * if it has been visited already, then add an edge to it (hyperlink between nodes)
		 */
		
		ArrayList<String> links = parser.getLinks(url); //returns an ArrayList of Strings containing all the hyperlinks 
																			   //going out of a given url		
		for(int i = 0; i < links.size(); i ++) {
				
			String hyperlink = links.get(i);
			internet.addVertex(hyperlink);
			internet.addEdge(url, hyperlink);
			
			if(!internet.getVisited(hyperlink)) {
				crawlAndIndex(hyperlink);
			}
		}	
		//WEB GRAPH UPDATED		
	}
	
	/* 
	 * This computes the pageRanks for every vertex in the web graph.
	 * It will only be called after the graph has been constructed using
	 * crawlAndIndex(). 
	 * To implement this method, refer to the algorithm described in the 
	 * assignment pdf. 
	 * 
	 * This method will probably fit in about 30 lines.
	 */
	
	
	public void assignPageRanks(double epsilon) {
		// TODO : Add code here
		
		/*
		 * Good web pages have: 
		 * a large in-degree
		 * less out-degree
		 * higher pageRank, better in-edge
		 */
		
		//initialize
		ArrayList<String> vertices = internet.getVertices(); //arraylist of all urls in the graph
		
		for(int i = 0; i < vertices.size(); i ++) {
			internet.setPageRank(vertices.get(i), 1.0); //initializing pageRank to 1 for all 0<=i<=N
		}
		
		
		boolean prDiff = false;
		
		while(prDiff == false) {
		
		ArrayList<Double> prePageRanks = new ArrayList<Double>();	
			
		for(int i = 0; i < vertices.size(); i ++) {
			prePageRanks.add(internet.getPageRank(vertices.get(i)));
		}
		
		ArrayList<Double> pageRanks = computeRanks(vertices);
		
		for(int j = 0; j < pageRanks.size(); j ++) {
			internet.setPageRank(vertices.get(j), pageRanks.get(j));;
		}
		
		double diffValue = 0;
		boolean temp = true;
		
		for(int k = 0; k < prePageRanks.size(); k ++) {
			
			diffValue = Math.abs(prePageRanks.get(k) - pageRanks.get(k));
			
			if(diffValue > epsilon) {
				temp = false;
				break;
			}
		}
		if (temp == false) {
			continue;
		} else {
			break;
		}		
	}
}

	/*
	 * The method takes as input an ArrayList<String> representing the urls in the web graph 
	 * and returns an ArrayList<double> representing the newly computed ranks for those urls. 
	 * Note that the double in the output list is matched to the url in the input list using 
	 * their position in the list.
	 */
		
	public ArrayList<Double> computeRanks(ArrayList<String> vertices) {
		// TODO : Add code here
		
		ArrayList<Double> pageRanks = new ArrayList<Double>();
		
		for(int i = 0; i < vertices.size(); i ++) {
			
			ArrayList<String> edgesInto = internet.getEdgesInto(vertices.get(i)); //vertices in the graph that have an out edge going into this vertex
			double pageRank = 0.0;
			
			for(int j = 0; j < edgesInto.size(); j ++) {
				//System.out.println(edgesInto.get(j));
				double jPageRank = internet.getPageRank(edgesInto.get(j)); //pr(w)
				double jOutDegree = internet.getOutDegree(edgesInto.get(j)); //out(w)
				pageRank = pageRank + (jPageRank/jOutDegree); 
				//System.out.println(jPageRank);
				//System.out.println(jOutDegree);
			}
			
			pageRank = 0.5 + 0.5*(pageRank);
			pageRanks.add(pageRank);
			}
		
		return pageRanks;
	}
		
	
	/* Returns a list of urls containing the query, ordered by rank
	 * Returns an empty list if no web site contains the query.
	 * 
	 * This method should take about 25 lines of code.
	 */
	public ArrayList<String> getResults(String query) {
		// TODO: Add code here
		
		/*
		 * input is a single word query
		 * returns ArrayList of urls, based on pageRanks
		 * use fastSort to implement
		 * 
		 * Search through wordIndex for the query(word)
		 * If word doesn't exist, return null
		 * If word exists:
		 * -> get ArrayList of URLs associated with the word
		 * --> computeRanks(ArrayList of URLs)
		 * ---> create new hashmap with key-url, value-pageRank
		 * ----> fastSort based on pageRanks
		 */
		
		if(!wordIndex.containsKey(query.toLowerCase())) {
			return new ArrayList<String>();			
		}
		
		else {
			ArrayList<String> vertices = wordIndex.get(query.toLowerCase());
			ArrayList<Double> verticeRanks = computeRanks(vertices);
			
			HashMap<String, Double> prMap = new HashMap<String, Double>();
			
			for(int i = 0; i < vertices.size(); i ++) {
				prMap.put(vertices.get(i), verticeRanks.get(i));
			}
			
			ArrayList<String> sorted = Sorting.fastSort(prMap);
			
			return sorted;
		}
		
	}
}
