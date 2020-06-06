package model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

public class MemoDTO {

	private String idx;
	private String user_idx;
	private String title;
	private String content;
	private String hashtag;
	private String colorbar;
	private String wowpoint;
	private String star;
	private String wdate;
	private String mdate;
	private String fold;
	private String box;
	
	private ArrayList<HashtagDTO> hashtaglist = new ArrayList<>();

	public MemoDTO() {}
	
	public MemoDTO(String json) {
		JSONArray arr = new JSONArray(json);
		JSONObject jsonObj = arr.getJSONObject(0);
		
		idx = jsonObj.getString("idx");
		user_idx = jsonObj.getString("user_idx");
		title = jsonObj.getString("title");
		content = jsonObj.getString("content");
		hashtag = jsonObj.getString("hashtag");
		colorbar = jsonObj.getString("colorbar");
		wowpoint = jsonObj.getString("wowpoint");
		star = jsonObj.getString("star");
		wdate = jsonObj.getString("wdate");
		mdate = jsonObj.getString("mdate");
		fold = jsonObj.getString("fold");
		box = jsonObj.getString("box");
		
		JSONArray list = jsonObj.getJSONArray("hashtaglist");
		int count = list.length();
		for(int i=0; i<count; i++) {
			hashtaglist.add(new HashtagDTO((String)list.get(i)));
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
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getHashtag() {
		return hashtag;
	}
	public void setHashtag(String hashtag) {
		this.hashtag = hashtag;
	}
	public String getColorbar() {
		return colorbar;
	}
	public void setColorbar(String colorbar) {
		this.colorbar = colorbar;
	}
	public String getWowpoint() {
		return wowpoint;
	}
	public void setWowpoint(String wowpoint) {
		this.wowpoint = wowpoint;
	}
	public String getStar() {
		return star;
	}
	public void setStar(String star) {
		this.star = star;
	}
	public String getWdate() {
		return wdate;
	}
	public void setWdate(String wdate) {
		this.wdate = wdate;
	}
	public String getMdate() {
		return mdate;
	}
	public void setMdate(String mdate) {
		this.mdate = mdate;
	}
	public String getFold() {
		return fold;
	}
	public void setFold(String fold) {
		this.fold = fold;
	}
	public String getBox() {
		return box;
	}
	public void setBox(String box) {
		this.box = box;
	}
	
	public ArrayList<HashtagDTO> getHashtaglist() {
		return hashtaglist;
	}

	public void setHashtaglist(ArrayList<HashtagDTO> hashtaglist) {
		this.hashtaglist = hashtaglist;
	}

	@Override
	public String toString() {
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("idx", idx);
		jsonObj.put("user_idx", user_idx);
		jsonObj.put("title", title);
		jsonObj.put("content", content);
		jsonObj.put("hashtag", hashtag);
		jsonObj.put("colorbar", colorbar);
		jsonObj.put("wowpoint", wowpoint);
		jsonObj.put("star", star);
		jsonObj.put("wdate", wdate);
		jsonObj.put("mdate", mdate);
		jsonObj.put("fold", fold);
		jsonObj.put("box", box);
		
		JSONArray mhh = new JSONArray();
		for(int i=0; i<hashtaglist.size(); i++) {
			mhh.put(hashtaglist.get(i));
		}
		jsonObj.put("hashtaglist", mhh);
		
		return jsonObj.toString();
	}
	
}
