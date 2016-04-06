package metadata;


import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class Dataset implements Serializable
{
	public HashMap<String, String> 	relations = new HashMap< String, String>( ) ;
	public Set<String>				topics 	 = new HashSet< String > ( ) ;
	public Set<Resource>			resources = new HashSet< Resource > ( ) ;
	public String idDataHub ;
	public String idNet ;
	public String name ;
	public String URL ;
	public boolean LinkedDataCrawl2014 = false ;
	public String sparqlEndpoint ;
	public boolean online = false ;
	
	public Set<String> getTopics() {
		return topics;
	}
	public void setTopics(Set<String> topics) {
		this.topics = topics;
	}
	public String getSparqlEndpoint() {
		return sparqlEndpoint;
	}
	public void setSparqlEndpoint(String sparqlEndpoint) {
		this.sparqlEndpoint = sparqlEndpoint;
	}
	public boolean isOnline() {
		return online;
	}
	public void setOnline(boolean online) {
		this.online = online;
	}
	public boolean isLinkedDataCrawl2014() 
	{
		return LinkedDataCrawl2014;
	}
	public void setLinkedDataCrawl2014(boolean linkedDataCrawl2014) 
	{
		LinkedDataCrawl2014 = linkedDataCrawl2014;
	}
	public String getURL() 
	{
		return URL;
	}
	public void setURL(String uRL) {
		URL = uRL;
	}
	public HashMap<String, String> getRelations( ) 
	{
		return relations;
	}
	public void setRelations(HashMap<String, String> relations) 
	{
		this.relations = relations;
	}
	public String getIdDataHub() 
	{
		return idDataHub;
	}
	public void setIdDataHub(String idDataHub) 
	{
		this.idDataHub = idDataHub;
	}
	
	//id in the data graph file
	public String getIdNet() 
	{
		return idNet;
	}
	public void setIdNet(String id) 
	{
		this.idNet = id;
		this.idNet = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	/**
	 * @return the resources
	 */
	public Set<Resource> getResources() {
		return resources;
	}
	/**
	 * @param resources the resources to set
	 */
	public void setResources(Set<Resource> resources) {
		this.resources = resources;
	}
}
