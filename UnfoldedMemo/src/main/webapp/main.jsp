<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0, user-scalable=no, target-densitydpi=medium-dpi">
    
    <link rel="stylesheet" type="text/css" href="https://cdn.jsdelivr.net/gh/moonspam/NanumSquare@1.0/nanumsquare.css">
    <link rel="stylesheet" type="text/css" href="style.css">
    <link rel="stylesheet" href="iconset/MFGLabs/mfglabs_iconset.css">
    <link href="iconset/fontawesome-free-5.13.0-web/css/all.css" rel="stylesheet">
    
	<title>unfolded memo</title>
</head>
<body>
<div class="container">
    <div id="momo-container" class="momo-container">
    </div>
    
    <!-- 팝업메뉴 시작 -->
    <div id='search-popup' class='popup-wrap hide'>
	    <div class="popup-mask"></div>
	    <div id="popup-content" class="popup-content">
	    	<!-- 키워드 모드 -->
	    	<div id="keyword_search_mode">
		        <div class="search-order">
		        	정렬순서 <i class="icon-star memo-staroff"></i> ! !! !!!
		        </div>
		        <div></div>
	    	</div>
	    	<!-- 해쉬태그 모드 -->
	        <div id="hashtag_search_mode" class="hashtaglist hide">
	            <ul id="hashtag-list"></ul>
	        </div>
	    </div>
    </div>
    <!-- 팝업메뉴 끝 -->

	<nav id='btn-home' class='btn-home hide'><span class='btn btn-icon'><i class="icon-home"></i></span></nav>
    <nav class="btn-wrap">
        <ul>
            <li id='btn-menu' class="btn">
                <span class='btn-icon'><i class="icon-reorder"></i></span>
            </li>
            <li id='btn-check' class='btn hide'>
                <span class='btn-icon'><i class="icon-check"></i></span>
            </li>
            <li class='btn-textbox hide'>
            	<span class='btn btn-icon'><i id='search-mode' class='icon-hashtag mode-keyword'></i></span>
                <div class="menu-textbox">
                <form id="menu_textbox">
                    <input type='text' class='btn-textbox' id='search_textbox'>
                </form>
                </div>
            </li>
            <li id='btn-search' class='btn hide'>
                <span class='btn-icon'><i class="icon-magnifying"></i></span>
            </li>
            <li id='btn-star' class='btn hide'>
                <span class='btn-icon'><i class="icon-star"></i></span>
            </li>
            <li id='btn-box' class='btn hide'>
                <span class='btn-icon'><i class='icon-file_open'></i></span>
            </li>
            <li id='btn-logout' class='btn hide'> 
                <span class='btn-icon'><i class="icon-signout"></i></span>	
            </li>
            <li id='btn-write' class='btn hide'>
                <span class='btn-icon'><i class="icon-plus"></i></span>	
            </li>
        </ul>
    </nav>
    
    <!-- 삭제확인 팝업 시작 -->
    <div id='delete-popup' class='popup-wrap hide'>
	    <div class='popup-mask'></div>
	    <div class='popup-delete'>
	    	<div class='popup-delete-info'>
	    		삭제할래?
	    	</div>
	    	<div class='popup-delete-btn'>
	    		<button id='popup-btn-cancle' class='popup-delete-btn-cancle'>취소</button>
	    		<button id='popup-btn-delete' class='popup-delete-btn-delete'>삭제</button>
	    	</div>
	    </div>
    </div>
    <!-- 삭제확인 팝업 끝 -->
    
</div>

<script type="text/Javascript" charset="UTF-8" src="common.js"></script>
<script src="mainjs.js"></script>

</body>
</html>