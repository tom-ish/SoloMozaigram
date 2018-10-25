package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Part;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import hibernate_entity.User;

public class Tools {

	public static boolean isNullParameter(String parameter) {
		if(parameter == null) return true;
		return false;
	}

	public static boolean isPasswordValid(String password) {
		String acceptable[] = {"a","b","c","d","e","f","g","h","i","j","k","l","m","n","o","p","q","r","s","t","u","v","w","x","y","z",
				"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z",
				"0","1","2","3","4","5","6","7","8","9"};

		if(password.length() < 6) return false;
		else {
			for(int j = 0; j < password.length(); j++) {
				boolean contained = false;
				for(int i = 0; i < acceptable.length; i++) {
					if(Character.toString(password.charAt(j)).equals(acceptable[i])) {
						contained = true;
						break;
					}
				}
				if(!contained) return false;
			}
		}

		return true;
	}





	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

	public static JSONObject readJsonFromUrl(String url) {
		InputStream is = null;
		try {
			is = new URL(url).openStream();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			System.out.println("MalformedURLException on readJsonFromUrl() : " + url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException on readJsonFromUrl() : " + url);
		}
		try {
			BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			JSONObject json = new JSONObject(jsonText);
			return json;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("IOException on readAll from readJsonFromUrl() : " + url);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			System.out.println("JSONException on readJsonFromUrl()");
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return new JSONObject();
	}

	public static final int SIZE_LIMIT = 250000;
	public static ArrayList<String> getURLsfromJSON(JSONObject jsonObject) {
		long startTime = System.currentTimeMillis();
		ArrayList<String> urls = new ArrayList<String>();
		JSONObject data;
		try {
			data = (JSONObject) jsonObject.get("data");
			JSONObject result = (JSONObject) data.get("result");
			JSONArray items = (JSONArray) result.get("items");
			
			System.out.println("Number of items : " + items.length());
			for (int i = 0; i < items.length(); i++) {
				JSONObject item = (JSONObject)items.get(i);
				String media = (String) item.get("media");
				int size = Integer.parseInt((String) (item.get("size")));
				if(size < SIZE_LIMIT)
					urls.add(media);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return urls;
	}
	

	public static boolean verifyImageExtension(String filename) {
		if(filename.endsWith(".jpg")
				||filename.endsWith(".jpeg")
				||filename.endsWith(".png")) {
			return true;
		}
		return false;
	}

	
	
	public static String getFileName(Part part) {
		String contentDisp = part.getHeader("content-disposition");
		System.out.println("content-disposition header= " + contentDisp);
		String[] tokens = contentDisp.split(";");
		for(String token : tokens) {
			if(token.trim().startsWith("filename")) {				
				return token.substring(token.indexOf("=")+2, token.length()-1);
			}
		}
		return "";
	}
	
	public static String getFilenameFromURL(URL url) {
		return url.getFile().substring(url.getFile().lastIndexOf('/')+1);
	}
	
    /**
     * Returns the filename from the content-disposition header of the given part.
     */
    private String getFilename(Part part) {
    	final String CONTENT_DISPOSITION = "content-disposition";
        final String CONTENT_DISPOSITION_FILENAME = "filename";
        for (String cd : part.getHeader(CONTENT_DISPOSITION).split(";")) {
            if (cd.trim().startsWith(CONTENT_DISPOSITION_FILENAME)) {
                return cd.substring(cd.indexOf('=') + 1).trim().replace("\"", "");
            }
        }
        return null;
    }
	
	public static String stringifyUsersSet(Set<User> users) {
		String rslt = "";
		for(User user : users) {
			rslt+= user.getId() + Persist.STRINGIFY_ATTRIBUTE_SEPARATOR
					+ user.getUsername() + Persist.STRINGIFY_ATTRIBUTE_SEPARATOR
					+ user.getEmail() + Persist.STRINGIFY_SEPARATOR;
		}
		return rslt;
	}
	
	public static Set<Map.Entry<Integer, String>> getSecuredUserSet(Set<User> users) {
		Set<Map.Entry<Integer, String>> rslt = new HashSet<Map.Entry<Integer, String>>(users.size());
		for(User user : users)
			rslt.add(new AbstractMap.SimpleEntry<Integer, String>(user.getId(), user.getUsername()));
		return rslt;
	}
	
}
