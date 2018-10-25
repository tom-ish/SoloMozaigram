package mozaik_process;
import java.util.ArrayList;
import java.util.concurrent.Callable;

import utils.Tools;

public class URLsSaver implements Callable<ArrayList<String>>{
	
	String userId;
	String url;
	
	public URLsSaver(String userId, String url) {
		this.userId = userId;
		this.url = url;
	}

	@Override
	public ArrayList<String> call() throws Exception {
		// TODO Auto-generated method stub
		ArrayList<String> rslt = Tools.getURLsfromJSON(Tools.readJsonFromUrl(url));
		return rslt;
	}

}
