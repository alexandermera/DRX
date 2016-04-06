package shell;

public class Topic {
	public String id, title ;
	public float weight = 0 ;
	public int nTimes = 0;
	public Topic(){
		
	}
	 @Override
	public String toString( ){
		return  "id: " + id + " title: " + title + " weight: " + weight ;
	}
}
