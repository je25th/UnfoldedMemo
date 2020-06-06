package model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class HashtagDTO {
	
	private String idx;
	private String user_idx;
	private String hashtag;
	private int count;
	private ArrayList<String> memo_has_hashtag = new ArrayList<>();
	
	public HashtagDTO() {}
	
	public HashtagDTO(String json) {
		JSONObject jsonObj = new JSONObject();
		if(json.charAt(0) == '{') {
			jsonObj = new JSONObject(json);
		}
		else {
			JSONArray arr = new JSONArray(json);
			jsonObj = arr.getJSONObject(0);
		}
		
		if(!jsonObj.isNull("idx"))
			idx = jsonObj.getString("idx");
		if(!jsonObj.isNull("user_idx"))
			user_idx = jsonObj.getString("user_idx");
		if(!jsonObj.isNull("hashtag"))
			hashtag = jsonObj.getString("hashtag");
		if(!jsonObj.isNull("count"))
			count = jsonObj.getInt("count");
		
		if(!jsonObj.isNull("memo_has_hashtag")) {
			JSONArray mhh = jsonObj.getJSONArray("memo_has_hashtag");
			int count = mhh.length();
			for(int i=0; i<count; i++) {
				memo_has_hashtag.add((String)mhh.get(i));
			}
		}
	}
	
	public String getIdx() {
		return idx;
	}
	public void setIdx(String idx) {
		this.idx = idx;
	}
	public String getUser_idx() {
		return user_idx;
	}
	public void setUser_idx(String user_idx) {
		this.user_idx = user_idx;
	}
	public String getHashtag() {
		return hashtag;
	}
	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}
	public int getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = Integer.parseInt(count);
	}
	public void setCount(int count) {
		this.count = count;
	}	
	public ArrayList<String> getMemo_has_hashtag() {
		return memo_has_hashtag;
	}
	public void setMemo_has_hashtag(ArrayList<String> memo_has_hashtag) {
		this.memo_has_hashtag = memo_has_hashtag;
	}
	
	public String toString() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("idx", idx);
		jsonObj.put("user_idx", user_idx);
		jsonObj.put("hashtag", hashtag);
		jsonObj.put("count", count);
		jsonObj.put("memo_has_hashtag", memo_has_hashtag);
		
		return jsonObj.toString();
	}
}
