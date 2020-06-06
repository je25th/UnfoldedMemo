/**
 * main
 */
var menu_wrap = document.querySelector(".btn-wrap");
var menu = menu_wrap.firstElementChild; 
var ori_wid = "50px";
var wid = "90%";

var hashtagId = "hashtag_id";
var hide_btn_class = "hide";

var isDisplayHashtag = 0;//해쉬태그 불러왔는지 확인용
var lastScrollTop = 0;//메뉴닫기할때 필요

var page = 1;
var scrollpage = 1;

addMenuEvt();
loadmemo();

function loadmemo() {
	if(location.pathname.search("main") >= 0) {//.search("modify")
		ajax_getJson('./moemo.view', 'page='+page, displayMemolist, '');
	}
	else if(location.pathname.search("search") >= 0) {
		//뒤로가기 버튼(main으로)
		var home = document.getElementById("btn-home");
		home.addEventListener("click", function() {
			window.location.href = "./main";
		});
		//뒤로가기 버튼 켬
		home.classList.remove(hide_btn_class);
		
		ajax_getJson('./moemo.view', location.search.substr(1) + '&page='+page, displayMemolist, '');
	}
	else if(location.pathname.search("star") >= 0) {
		//뒤로가기 버튼(main으로)
		var home = document.getElementById("btn-home");
		home.addEventListener("click", function() {
			window.location.href = "./main";
		});
		//뒤로가기 버튼 켬
		home.classList.remove(hide_btn_class);
		
		ajax_getJson('./star.view', 'page='+page, displayMemolist, '');
	}
}

function addMenuEvt() {
	//각종 클릭 이벤트 등록
	tapEvt(function(e) {
		var target = e.target;
		
		//해쉬태그 선택 -> 검색창
		if(target.id.indexOf(hashtagId) >= 0) {
			var hashtag = "hashtag=" + target.id.replace(hashtagId, "");
	    	window.location.href = "./search?" + hashtag;
		}
		
		//별 토글
		target = target.parentElement;
		if(target == null) return;
		
		if(target.className == "starwrap") {
			var staricon = target.firstElementChild;
			var starvalue = "1";//현재상태 off
			if(staricon.classList.toggle("offcolor"))
				starvalue = "0";
			ajax_getJson("./star.receive", ('idx=' + target.offsetParent.id +'&star=' + starvalue));
			
			return;
		}
		//상세페이지로
		target = target.parentElement.parentElement;
		//console.log(target);
		if(target.localName == "article") {
			var memo_idx = target.getAttribute("id");
		    window.location.href = "./viewer?idx="+ +memo_idx;
		}
		
	}, function(e) {//right
		//console.log(e);//parentElement
		var p = e.target;
		while(p.parentElement != null) {
			p = p.parentElement;
			if(p.localName == "article") {
				if(p.firstElementChild.childNodes[2].style.transform != "none") {
					p.firstElementChild.childNodes[2].style.transform = "none";
					p.firstElementChild.childNodes[0].style.width = "0";
					p.firstElementChild.childNodes[1].style.width = "0";
					
					return;
				}
				
				//memo-inner
				p.firstElementChild.childNodes[2].style.transform = "translate3d(+30%, 0px, 0px)";
				//swipe-box
				p.firstElementChild.childNodes[0].style.width = "30%";
				p.firstElementChild.childNodes[0].addEventListener("click", function(e) {
					console.log(p.id + " box");
					//p.firstElementChild.firstElementChild.style.transform = "translate3d(-100%, 0px, 0px)";
					//p.parentElement.removeChild(p);
				});
				
				break;
			}
		}
		
	}, function(e) {//left
		//console.log(e);
		var p = e.target;
		while(p.parentElement != null) {
			p = p.parentElement;
			if(p.localName == "article") {
				if(p.firstElementChild.childNodes[2].style.transform != "none") {
					p.firstElementChild.childNodes[2].style.transform = "none";
					p.firstElementChild.childNodes[0].style.width = "0";
					p.firstElementChild.childNodes[1].style.width = "0";
					
					return;
				}
				//ajax_getJson('./deletememo.receive', 'idx='+p.id, function(){p.parentElement.removeChild(p);});
				//memo-inner
				p.firstElementChild.childNodes[2].style.transform = "translate3d(-30%, 0px, 0px)";
				//swipe-delete
				p.firstElementChild.childNodes[1].style.width = "30%";
				p.firstElementChild.childNodes[1].addEventListener("click", function(e) {
					console.log(p.id + " delete");
					//p.firstElementChild.firstElementChild.style.transform = "translate3d(-100%, 0px, 0px)";
					//p.parentElement.removeChild(p);
				});
				console.log(p.id + " swipe");
				
				break;
			}
		}
	});
	
	//스크롤
	window.addEventListener("scroll", function(e) {
		var scrollHeight = e.target.scrollingElement.scrollHeight;
		var nowscroll = e.target.scrollingElement.scrollTop + e.target.scrollingElement.clientHeight;
		if(scrollHeight - nowscroll < 50 && scrollpage+1 == page) {
			console.log("scroll end");
			scrollpage++;
			loadmemo();
		}
		
//		console.log("---------------------------------");
//		console.log("clientHeight:" + e.target.scrollingElement.clientHeight);
//		console.log("scrollHeight:" + e.target.scrollingElement.scrollHeight);
//		console.log("scrollTop:" + e.target.scrollingElement.scrollTop);
//		console.log("clientHeight + scrollTop:" + (e.target.scrollingElement.scrollTop + e.target.scrollingElement.clientHeight));
	})
	
	
	//태그 서치 텍스트박스 이벤트
	document.getElementById("search_textbox").addEventListener("keyup", function(e) {
		var str = e.srcElement.value;
		
		//모드체인지
		//해쉬태그 모드
		if(str == "#"){
			console.log("hashtag_search_mode");
			e.srcElement.value = "";
			document.getElementById("search-mode").classList.remove("mode-keyword");
			document.getElementById("keyword_search_mode").classList.add(hide_btn_class);
			document.getElementById("hashtag_search_mode").classList.remove(hide_btn_class);
			
			return;
		}
		//키워드 모드
		else if(str == "k" || str == "K") {
			e.srcElement.value = "";
			document.getElementById("search-mode").classList.add("mode-keyword");
			document.getElementById("keyword_search_mode").classList.remove(hide_btn_class);
			document.getElementById("hashtag_search_mode").classList.add(hide_btn_class);
			
			return;
		}
		
		//태그 검색
		if(!document.getElementById("hashtag_search_mode").classList.contains(hide_btn_class)) {
			var par = "";
			if(str.length > 0)
				par = "hashtag=" + str;
			var ul = document.getElementById("hashtag-list");
		    ajax_getJson('./hashtag.view', par, displayHashtag, ul);
			
			return;
		}
	});
	
	//메뉴 클릭 이벤트
    var li = menu.children;
    li[0].addEventListener("click", function() {
        menu_open();

        lastScrollTop = window.pageYOffset || document.documentElement.scrollTop;
    });
    li[1].addEventListener("click", function() {
        menu_check();
    });
    li[3].addEventListener("click", function() {
        menu_search();
    });
    li[4].addEventListener("click", function() {
        menu_star();
    });
    li[5].addEventListener("click", function() {
        menu_box();
    });
    li[6].addEventListener("click", function() {
        menu_logout();
    });
    li[7].addEventListener("click", function() {
        menu_write();
    });

    //메뉴 닫기 
    window.addEventListener("scroll", function() {
        //console.log(this);
        var scrollTop = window.pageYOffset || document.documentElement.scrollTop;
        if(scrollTop > lastScrollTop) return;

        lastScrollTop = scrollTop;
        menu_close();
    });

    var popup = document.getElementById("mask");
    popup.addEventListener("click", function() {    
        menu_close();
    });
}

function select_menu(n) {
	var li_num;
	switch(n) {
		case "menu" :
			li_num = 0;
			break;
		case "check" :
			li_num = 1;
			break;
		case "searchbox" :
			li_num = 2;
			break;
		case "search" :
			li_num = 3;
			break;
		case "star" :
			li_num = 4;
			break;
		case "box" :
			li_num = 5;
			break;
		case "logout" :
			li_num = 6;
			break;
		case "write" :
			li_num = 7;
			break;
	}
	
	return li_num;
}

function hide_menu(n) {
	var li_num = select_menu(n);
	var li_num2 = select_menu("searchbox");
	
	var li = menu.children;
    for(var i=0; i<li.length; i++)
    {
    	if(li_num == i || li_num2 == i) {
    		li[i].classList.add(hide_btn_class);
    		continue;
		}
    	
        li[i].classList.remove(hide_btn_class);
    }
}

function show_menu(n, n2) {
	var li_num = select_menu(n);
	var li_num2 = select_menu(n2);
	
	var li = menu.children;
    for(var i=0; i<li.length; i++)
    {
    	if(li_num == i || li_num2 == i) {
    		li[i].classList.remove(hide_btn_class);
    		continue;
		}
    	
        li[i].classList.add(hide_btn_class);
    }
}

function menu_open() {
    if(menu_wrap.style.width === wid) return;

    menu_wrap.style.width = wid;

    hide_menu("menu");
}

function menu_close() {
    if(menu_wrap.style.width === ori_wid) return;
    //팝업
    popup_close();

    menu_wrap.style.width = ori_wid;

    show_menu("menu");
}

function popup_open(popupwindow) {
    //마스크 켬
    var popup = document.getElementById("mask");
    popup.classList.add("popup-show");
    //팝업창 켬
    popup = document.getElementById(popupwindow);
    popup.classList.add("popup-show");
}

function popup_close() {
    //마스크
    var popup = document.getElementById("mask");
    popup.classList.remove("popup-show");
    //팝업창
    popup = document.getElementById("popup-content");
    popup.classList.remove("popup-show");
}

function menu_search() {
    if(menu_wrap.style.width === ori_wid) return;

    if(!document.getElementById("popup-content").classList.contains("popup-show")) {
	    //팝업 열기
	    popup_open("popup-content");
	    //검색창 열기
	    show_menu("search", "searchbox");
    }
    else {
    	//검색
    	if(document.getElementById("search_textbox").value.length <= 0) return;
    	
    	//키워드 모드
    	//console.log(document.getElementById("search_textbox").value);
	    if(document.getElementById("hashtag_search_mode").classList.contains(hide_btn_class)) {
	    	var keyword = "keyword=" + document.getElementById("search_textbox").value;
	    	window.location.href = "./search?" + keyword;
    	}
    	//해쉬태그 모드
	    else {
	    	//var hashtag = "hashtag=" + document.getElementById("search_textbox").value;
	    	//window.location.href = "./search?" + hashtag;
	    }
    }
}

function menu_star() {
    if(menu_wrap.style.width === ori_wid) return;

    window.location.href = "./star";
}

function menu_check() {
    if(menu_wrap.style.width === ori_wid) return;

}

function menu_logout() {
    if(menu_wrap.style.width !== wid) return;

    window.location.href = "./login";
}

function menu_box() {
    if(menu_wrap.style.width !== wid) return;
	
}

function menu_write() {
    if(menu_wrap.style.widthh === ori_wid) return;
    
    //console.log("write");
    window.location.href = "./write";
    //window.location.replace("./write");//이건 뒤로가기 누르면 이상한것같음
}

function addEvt_star(id, func) {
	var icon = document.getElementById(id);//.querySelector(".memo-bottom");//starwrap");
	icon.addEventListener("click", func);
}

//*********************
//여기부터 화면에 뿌리는 함수들 모음
//*********************
//해쉬태그
function displayHashtag (ul, parsedJSON) {
	var hashtagList = parsedJSON;
	var h;
	ul.innerHTML = "";
	hashtagList.forEach(function(data) {
//		if(document.getElementById(selectedId + data.idx) != null)
//			h = " " + hide_class;
//		else
//			h = ""
		ul.innerHTML += "<li id='" + hashtagId + data.idx + "' class='hashtag-unselected inline'>#" + data.hashtag + 
						"<span class='hashtag_count'>| " + data.count + "</span></li>";
	});
}

//메모 화면에 뿌리기
function displayMemolist (dom, parsedJSON) {
	//var memojson = document.getAttribute("memolist");
	//console.log(parsedJSON);
    var memo = parsedJSON;
    var condiv = document.getElementById("momo-container");
//    //일단 다지움
//    condiv.innerHTML = "";
    //page 카운트 업
    if(memo.length > 0)
    	page++;
    
    //console.log(condiv);
    memo.forEach(function(data) {
    	//star
        var staroff = "offcolor";
        if(data.star === "1")
            staroff = "";
        
        //wowpoint
        var hashtagli = "";
        if(data.wowpoint === "1")
        	hashtagli = "<li class='memo-hashtag memo-wowpoint'><i class='fas fa-exclamation'></i></li>";
        else if(data.wowpoint === "2")
        	hashtagli = "<li class='memo-hashtag memo-wowpoint'><i class='fas fa-exclamation'></i><i class='fas fa-exclamation'></i></li>";
        else if(data.wowpoint === "3")
        	hashtagli = "<li class='memo-hashtag memo-wowpoint'><i class='fas fa-exclamation'></i><i class='fas fa-exclamation'></i><i class='fas fa-exclamation'></i></li>";
        
        //hashtag
        var contentli = data.hashtag.split('#')
        contentli.forEach(function(tag) {
            if(tag !== "")
                hashtagli += '<li class="memo-hashtag">#' + tag + '</li>';
        });
        //뿌림
		condiv.innerHTML += '<article id="' + data.idx + '" class="memo">' +
							'<div class="memo-swipe-wrap">' +
							'<div class="swipe-box" style="width: 0"></div><div class="swipe-delete" style="width: 0"></div>' +
	                            '<div class="momo-inner" style="transform: none;">' +
	                                '<div class="memo-top">' +
	                                    '<div class="memo-imp">'+'</div>' +
	                                    '<div class="slider">' + 
	                                        '<ul class="memo-hashtag-slider">' +
	                                            hashtagli +
	                                        '</ul>' +
	                                    '</div>' +
	                                '</div>' +
	                                '<div class="memo-title">' +
	                                    '<span>' + data.title + '</span>' +
	                                '</div>' +
	                                '<div class="memo-content">' +
	                                    '<span>' + data.content + '</span>' +
	                                '</div>' +
	                                '<div class="memo-bottom">' +
	                                    '<span class="memo-date">' + data.mdate.slice(0, 10) + '</span><span class="space">|</span>' + 
	                                    '<span class="starwrap"><i class="icon-star memo-star ' + staroff +'"></i></span>' +
	                                '</div>' +
	                            '</div>' +
	                        '</div>' +
	                        '<div class="memo-border"></div>'
	                        '</article>';
    });
}