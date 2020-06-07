<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.json.JSONObject"%>
<%@ page import="model.MemoDTO"%>
<%@ page import="model.HashtagDTO"%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, target-densitydpi=medium-dpi">
    
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/gh/moonspam/NanumSquare@1.0/nanumsquare.css">
    <link rel="stylesheet" href="style.css">
    <link rel="stylesheet" href="iconset/MFGLabs/mfglabs_iconset.css">
    <link href="iconset/fontawesome-free-5.13.0-web/css/all.css" rel="stylesheet">
    
	<title>write</title>
</head>
<body>

<% 
MemoDTO memoData = null;
if(request.getAttribute("memodata") != null) {
	String data = (String)request.getAttribute("memodata");
	//System.out.println(data);
    memoData = new MemoDTO(data);
    System.out.println(memoData.getStar().equals("0"));
}
%>

<div class="container">
    <header class="write-top">
        <button id="cancel">취소</button>
        <button id="finish">수정</button>
        <button id="delete">삭제</button>
    </header>
    <div class="write-content">
        <div class="write-content-title">
            <textarea id="title" name="title" class="textarea-title" style="height: 25px;" readonly><%=(memoData!=null)?memoData.getTitle():""%></textarea>
        </div>
        <div class="write-content-textarea">
            <textarea id="content" name="content" class="textarea-content" style="height: 300px;" readonly><%=(memoData!=null)?memoData.getContent():""%></textarea>
        </div>
        <div class="write-content-add">
            <div id="hashtag-textbox" class="add-hashtage hide">
                <i class="icon-hashtag write-tagicon"></i> 
                <input type="text" id="search-hashtag" class="write-search-hashtag">
                <button id="add-hashtag" class='hide'>추가</button>
            </div>
            <ul id="selected-hashtag-list">
	            <%
		        if(memoData != null) {
		        %>
		        	<li class='inline wicon'><span id="star"><i class='icon-star memo-star<%=(memoData.getStar().equals("0"))?" offcolor":""%>'></i></span></li>
		        <%
		        	int wowcount = Integer.parseInt(memoData.getWowpoint());
		        	String wowhtml = "";
		        	for(int i=0; i<wowcount; i++) {
		        		wowhtml += "<i class='fas fa-exclamation'></i>";
		        	}
		        	if(wowcount > 0) {
		        %>
		        	<li class="inline wicon"><span id="wowpoint" class="memo-wowpoint"><%=wowhtml%></span></li>
		        <%
		        	}
		        	
		        	int size = memoData.getHashtaglist().size();
		            for(int i=0; i<size; i++) {
		        %>
	            	<li id='selected_id<%=memoData.getHashtaglist().get(i).getIdx()%>' class='hashtag-search inline'>#<%=memoData.getHashtaglist().get(i).getHashtag()%></li>
	            <%
		            }
	            }
	            %>
            </ul>
            <ul id="hashtag-list"></ul>
            <ul id="hashtag-search-list" class="hide"></ul>
        </div>
    </div>
</div>

<script type="text/Javascript" charset="UTF-8" src="common.js"></script>
<script>
/**
 * write
 */
//전역변수
var hide_class = "hide";
var hashtagId = "hashtag_id";
var selectedId = "selected_id";
//이벤트리스너 등록
addEvtInit();
//뷰모드 수정모드의 경우
autoBoxsizing(document.getElementById("title"), "25px");
autoBoxsizing(document.getElementById("content"), "300px");

function autoBoxsizing(textarea, defult) {
	//console.log(textarea);
	//console.log("scrollTop:" + textarea.scrollTop);
	if(textarea.scrollTop == 0)
		textarea.style.height = defult;
	textarea.style.height = textarea.scrollHeight + "px";
	//console.log(textarea.style.height);
}

//해쉬태그 선택  <li id="hashtaglist_id5" class="hashtag-search inline">#와하<span class="hashtag_count">| 0</span></li>
function addHashtag(id, name) {
	var ul = document.getElementById("selected-hashtag-list");
	ul.innerHTML += "<li id='" + id + "' class='hashtag-search inline'>" + name + "</li>";
	//console.log(id.replace("hashtaglist",""));
}

//완료버튼 온오프
function submitBtnOnOff(e) {
	var btn = document.getElementById("finish");
	if(btn.innerHTML == "수정") return;
	
	if(e.srcElement.textLength === 0) 
		btn.classList.add(hide_class);
	else
		btn.classList.remove(hide_class);
}

function getHashtag() {
	var ul = document.querySelector("#hashtag-list");
	ajax_getJson('./hashtag.view', '', displayHashtag, ul);
}

//해쉬태그리스트 화면에 뿌리기
function displayHashtag (ul, parsedJSON) {
	var hashtagList = parsedJSON;
	var h;
	hashtagList.forEach(function(data) {
		if(document.getElementById(selectedId + data.idx) != null)
			h = " " + hide_class;
		else
			h = ""
		ul.innerHTML += "<li id='" + hashtagId + data.idx + "' class='hashtag-unselected inline" + h + "'>#" + data.hashtag + 
						"<span class='hashtag_count'>| " + data.count + "</span></li>";
	});

	//태그온오프
	var li = ul.childNodes;
	li.forEach(function(item) {
		if(item.id != null && item.id.indexOf(selectedId) >= 0)
			document.getElementById(item.id.replace(selectedId, hashtagId)).classList.add(hide_class);
	});
}

//쓰기모드
function writeMode() {
	getHashtag();//전체태그가져옴
	document.getElementById("hashtag-textbox").classList.remove(hide_class);//태그검색창 오픈
	
	//textarea 리드온리 풀기
	document.getElementById("title").removeAttribute("readonly");
	document.getElementById("content").removeAttribute("readonly");

	var ul = document.getElementById("selected-hashtag-list");
	//별 버튼 remove
	var star = document.getElementById("star");
	if(star != null)
		ul.removeChild(star.parentElement);
	
	//느낌표버튼
	if(document.getElementById("wowpoint") == null) {
		ul.innerHTML = "<li class='inline wicon'><span id='wowpoint' class='memo-wowpoint'><i class='fas fa-exclamation offcolor'></i></span></li>" + ul.innerHTML;
	}
	
	//완료버튼
	var btn = document.getElementById("finish");
	btn.innerHTML = "완료";
	
	//삭제버튼 숨김
	document.getElementById("delete").classList.add(hide_class);
}

//이벤트 등록(한번 등록하면 끝)
function addEvtInit() {

	//수정모드
	if(location.pathname.search("modify") >= 0) {
		writeMode();
	}
	//쓰기모드
	else if(location.search.length == 0) {
		writeMode();
	}
	
	//토글버튼
	tapEvt(function(e) {
		//var arr = e.path;
		var target = e.target;//.parentElement;
		//해쉬태그
		if(target.id != null && target.id.indexOf(hashtagId) >= 0) {
			if(document.getElementById("finish").innerHTML != "완료") return;
			
			addHashtag(target.id.replace(hashtagId, selectedId), target.firstChild.textContent);
			target.classList.add(hide_class);
			return;
		}
		else if(target.id != null && target.id.indexOf("selected_id") >= 0) {
			if(document.getElementById("finish").innerHTML != "완료") return;
			
			document.getElementById("selected-hashtag-list").removeChild(document.getElementById(target.id));
			document.getElementById(target.id.replace(selectedId, hashtagId)).classList.remove(hide_class);
			return;
		}
		else if(target.id == "none") {
			if(document.getElementById("finish").innerHTML != "완료") return;
			
			document.getElementById("selected-hashtag-list").removeChild(document.getElementById(target.id));
			return;
		}
		
		//느낌표
		target = e.target.parentElement;
		if(target.id == "wowpoint") {
			if(document.getElementById("finish").innerHTML != "완료") return;
			
			if(!target.firstElementChild.classList.contains("offcolor")) {
				if(target.childNodes.length < 3) {
					target.innerHTML += "<i class='fas fa-exclamation'>"
				}
				else {
					target.innerHTML = "<i class='fas fa-exclamation offcolor'>";
				}
			}
			else {
				target.firstElementChild.classList.remove("offcolor");
			}
			return;
		}
		
		//해쉬태그 재검사
		if(target.id != null && target.id.indexOf(hashtagId) >= 0) {
			if(document.getElementById("finish").innerHTML != "완료") return;
			
			addHashtag(target.id.replace(hashtagId, selectedId), target.firstChild.textContent);
			target.classList.add(hide_class);
			return;
		}
		else if(target.id != null && target.id.indexOf("selected_id") >= 0) {
			if(document.getElementById("finish").innerHTML != "완료") return;
			
			document.getElementById("selected-hashtag-list").removeChild(document.getElementById(target.id));
			document.getElementById(target.id.replace(selectedId, hashtagId)).classList.remove(hide_class);
			return;
		}
		else if(target.id == "none") {
			if(document.getElementById("finish").innerHTML != "완료") return;
			
			document.getElementById("selected-hashtag-list").removeChild(document.getElementById(target.id));
			return;
		}
		
		//console.log(target);
		
		/* //별 토글
		if(arr[i].id == "star") {
			var staricon = arr[i].firstElementChild;
			var starvalue = "1";//현재상태 off
			if(staricon.classList.toggle("offcolor"))
				starvalue = "0";
			ajax_getJson("./star.receive", ('idx=' + arr[i].offsetParent.id +'&star=' + starvalue));
			break;
		}  */
	});
	
	//태그 서치 텍스트박스 이벤트
	document.getElementById("search-hashtag").addEventListener("keyup", function(e) {
		var str = e.srcElement.value;
		if(str.length === 0) {
			document.getElementById("hashtag-search-list").classList.add(hide_class);
			document.getElementById("hashtag-list").classList.remove(hide_class);
			
			document.getElementById("add-hashtag").classList.add(hide_class);
		}
		else {
			document.getElementById("hashtag-search-list").classList.remove(hide_class);
			document.getElementById("hashtag-list").classList.add(hide_class);
			
			document.getElementById("add-hashtag").classList.remove(hide_class);
		}
	});
	//태그 추가 버튼
	document.getElementById("add-hashtag").addEventListener("click", function(e) {
		var textbox = document.getElementById("search-hashtag");
		addHashtag("none", "#"+textbox.value);
		textbox.value = "";
		
		document.getElementById("hashtag-search-list").classList.add(hide_class);
		document.getElementById("hashtag-list").classList.remove(hide_class);
		
		document.getElementById("add-hashtag").classList.add(hide_class);
	});
	
	//삭제 버튼 이벤트
	document.getElementById("delete").addEventListener("click", function (e) {
		if(location.search.length > 0) {
			var idx = location.search.replace("?idx=", "");
			ajax_getJson('./deletememo.receive', 'idx='+idx, function(){window.location.href = "./main";});
		}
	});
	
	//취소 버튼 이벤트
	document.getElementById("cancel").addEventListener("click", function(e) {
		console.log("취소버튼");
	});
	
	//완료 버튼 이벤트
	document.getElementById("finish").addEventListener("click", function(e) {
		if(e.srcElement.innerHTML == "수정") {
			console.log("수정버튼");
			
			window.location.href = "./modify" + location.search;
			/* document.getElementById("hashtag-textbox").classList.remove(hide_class);//태그검색창 오픈
			//textarea 리드온리 풀기
			document.getElementById("title").removeAttribute("readonly");
			document.getElementById("content").removeAttribute("readonly");
			
			e.srcElement.innerHTML = "완료"; */
			return;
		}

		console.log("완료버튼");
		//memo
		var idx = "";
		if(location.search.length > 0) {
			idx = location.search.replace("?idx=", "");
		}
		var user_idx = "1";
		var title = document.getElementById("title").value;
		var content = document.getElementById("content").value;
		var hashtag = "";//밑에서 추가함
		var colorbar = "";
		var wowpoint = "0";
			var span = document.getElementById("wowpoint");
			if(!span.firstElementChild.classList.contains("offcolor")) {
				wowpoint = span.childNodes.length;
			}
		var fold = "0";
		
		//hashtag
		var exist_hashtag = "";
		var new_hashtag = "";
		var ul_child = document.getElementById("selected-hashtag-list").children;
		for(var i=0; i<ul_child.length; i++) {
			var li_id = ul_child[i].id;
			if(li_id.indexOf(selectedId) >= 0) {
				hashtag += ul_child[i].firstChild.textContent;
				exist_hashtag += ul_child[i].id.replace(selectedId, "") + ",";
			}
			else if(li_id == "none") {
				hashtag += ul_child[i].firstChild.textContent;
				new_hashtag += ul_child[i].firstChild.textContent;
			}
		}
		
		var par = 'idx=' + idx + 
					'&user_idx=' + user_idx +
					'&title=' + title +
					'&content=' + content +
					'&hashtag=' + hashtag +
					'&colorbar=' + colorbar +
					'&wowpoint=' + wowpoint +
					'&fold=' + fold;
		par += '&exist_hashtag=' + exist_hashtag +
				'&new_hashtag=' + new_hashtag;
		
		ajax_getJson('./memo.receive', par, function(){window.location.href = "./main";});
	});
	
	//제목 텍스트박스
	document.getElementById("title").addEventListener("keydown", function(e) {
	    
	    //글자수 제한
	    //console.log(e.srcElement.textLength);
	    if(e.srcElement.textLength >= 20) {
	    	var str = e.srcElement.value
	    	e.srcElement.value = str.substring(0, 19);
	    }
	});
	document.getElementById("title").addEventListener("keyup", function(e) {
	    //console.log(this);
	    //console.log(e);
	    
	    //박스 사이즈 자동 조절
	    autoBoxsizing(e.srcElement, "25px");
	});
	
	//내용 텍스트박스
	document.getElementById("content").addEventListener("keyup", function(e) {
	    //console.log(this);
	    //console.log(e.srcElement.textLength);
	    
	    //박스 사이즈 자동 조절
	    autoBoxsizing(e.srcElement, "300px");
	    
	    //완료버튼 온오프
	    submitBtnOnOff(e);
	});
}
</script>


</body>
</html>