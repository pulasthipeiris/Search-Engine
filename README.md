# I have developed a search engine that will:
  1.  Explore a portion of the web (which I have simulated through a database),
  2.  Build an index of the words contained in each web page,
  3.  Analyze the structure of the web graph to establish which web page should be ranked higher,
  4.  Use this analysis to perform simple searches where a query word is entered by the user and a
     sorted list of relevant web pages available is returned.

Although I have applied these tools using a local database, what I have developed is a
simplified version of the what is used at Google to answer actual web queries.

# How does it work?

A search engine is software, usually accessed on the Internet, designed to carry out searches in a
database of information according to the user’s query. The results provided are meant to best match
what the user is trying to find and they are usually displayed in order of importance. The first search
engine ever developed is considered to be Archie. It was named to resemble the word “archive”,
and it was created in the 1990 by Alan Emtage, Bill Heelan and J. Peter Deutsch, computer science
students at McGill. Today, there are many different search engines available on the Internet, each
with their own abilities and features. The most popular of them all being Google.

# A search engine performs the following tasks:
• Crawling and Indexing

• Ranking

• Searching

# Crawling and Indexing 
Crawling is the process during which a search engine use programs,
generally called spiders or crawlers, to collect information from web pages. By following links from
a web page to another, the spiders find new content. Once a page is crawled, the data contained
in the page is processed and indexed. 

Indexing is the process of storing and organizing the content
found during the crawling. It generally involve associating words and other definable tokens found
on web pages to a list of web pages that contain them. It also includes recording links to other
pages. These associations are all stored in a database which is then used for web search queries.
During this process the webgraph is created/updated. This is a directed graph where each node
represents a page, and an edge represents an hyperlink from one page to another. This is also
when what we refer to as the ‘word index’ is created. You can think of it as a mapping from single
words to a list of urls containing them.

# Ranking 
How useful a search engine is depends on how relevant are the results obtained when we
query it. There are millions of pages containing a specific word, but some pages are generally more
relevant, or authoritative than others. To determine which pages to show in the search results and in
what order, search engines use the data collected to perform a ranking. 

Different search engines use different algorithms to rank pages. Developed by Larry Page and Sergey Brin in 1996, PageRank
is one of the algorithms used at Google to rank webpages based on their importance. Using the
webgraph, each webpage is assigned a numerical value that measure its relative importance. In
simple terms, the rank of a webpage depends on the webpages that link to it. The contribution to
the rank is higher if these linking webpages have a high rank themselves, while it’s lower if these
linking webpage also link to a lot of other webpages.

# Searching 
In this step, search engines receive a query from the user. Using the index created
while crawling and the ranking based on the webgraph, the engine outputs a list of relevant web
pages in order of importance. Do all search engines give the same results? Not necessarily. Search
engines use different spiders to crawl and use different proprietary algorithms to index the data.
Each index is therefore a search engine’s representation of how they see the web. Also the algorithms
to rank and search the data are different, so every search engine has its own approach to finding
what you’re trying to find. 

Finally, personalisation adapts the search to a specific computer/user.
The results may be based on your geographical location, what else you’ve searched for, and what
results were preferred by other users searching for the same thing, for example. Search engines might
use and weigh all these factors in a unique way, which will lead to different search results.

# The code:
• XmlParser

 - This class has two methods: one to read the content of the xml database given a url, and the other to extract
information from it.

• MyWebGraph

 - This class implements the data structure needed to store the data collected
during the crawling phase. It has an inner class called WebVertex which is used to store data
related to a specific web page. I needed a graph data structure
that will allow us to store all the information related to the web pages: the class MyWebGraph is an implementation of a directed graph using adjacency lists. 
I have used this type of data structure to store the information your program collects
in the crawling phase. Each node in the graph will store a String corresponding to the url of
a webpage. 

• SearchEngine

- The method crawlAndIndex within this class takes a String as input representing a url. It will perform a graph traversal
of the web, starting at the given url. For each url visited,
the method updates the web graph by adding the appropriate vertex and edges then updates the word index so that every word in the url just visited appear in the
mapping.

• Sorting

 - This is a utility class containing methods that implement some sorting algorithm. It takes as input a HashMap where the values are Comparable.
The method returns an ArrayList containing all the keys in the map, sorted in descending
order based on the values they map to. 


The method computeRanks is a helper method for assignPageRanks. The purpose of assignPageRanks is to assign a rank to each vertex in
the web graph. It will only be called after crawlAndIndex has been executed. 

# Here are the ideas I have used to assign a rank to a web page:

• Good web pages are cited by many other pages. If we think about it in terms of the
webgraph, this means that we should prefer web pages (i.e. vertices) with a large
in-degree.

• Web pages that link to a large number of other pages are less valuable. In terms of
webgraph, this means that we will value less web pages (i.e. vertices) with a large
out-degree.

• A link from a web page is more valuable if the web page is itself a good one. In
graph terms, higher the rank of a web page (i.e. vertex), more valuable an in-edge
from it would be.



