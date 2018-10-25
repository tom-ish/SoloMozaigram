package algorithm;

import utils.Persist;
import database.DBAuthentification;
import java.util.ArrayList;

public class SearchComponent {
		
	public static ArrayList<String> search(ArrayList<String> results, String searchword) {
		ArrayList<String> list= DBAuthentification.getUserNames();
		
		for (String user:list){
			if (user.contains(searchword)){
				results.add(user);
			}
		}

		return results;
	}


}
