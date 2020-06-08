/**
 * 공통
 */

//ajax로 서버에 데이터 요청하여 json으로 받는거 
function ajax_getJson(URL, SEND, FUC, fuc_PAR) {
	var oReq = new XMLHttpRequest();
  
	oReq.onreadystatechange = function(event) {
      if(oReq.readyState == 4 && oReq.status == 200) {//서버처리끝 && 호출성공
    	  //console.log(oReq.responseText);
    	  var pJSON = JSON.parse(oReq.responseText);
       	  if(!pJSON.success) {
       		  console.log("ajax fail!");
       		  return;
       	  }
       	  if(FUC != null) {
       		  FUC(fuc_PAR, pJSON.result);
       		  //console.log(pJSON.result);
       	  }
      	
      	  console.log("ajax complete");
      }
      else {
          console.log("ajax Not Responding...");
      }
      
  	}
  
  oReq.open('POST', URL, true);
  oReq.setRequestHeader('Content-type', 'application/x-www-form-urlencoded');
  oReq.send(SEND);
}

//터치 인식
var tStartEvt = false;
var tMoveEvt = false;
//스위프 인식
var touchstart_x = null;
var swipe = false;
//탭or클릭 중 하나만 되게하기
var alreadyTaped = false;
var alreadyClicked = false;
//탭
function tapEvt(fuc, right, left) {
	document.querySelector("body").addEventListener("touchstart", function (e) {
		tStartEvt = true;
		touchstart_x = e.touches[0].screenX;
	});
	window.addEventListener("touchmove", function (e) {
		tMoveEvt = true;
		if(right == null && right == null) return;
		
		if(!swipe && e.touches[0].screenX - touchstart_x > 100) {
			//console.log("right");
			swipe = true;
			right(e);
		}
		else if(!swipe && touchstart_x - e.touches[0].screenX > 100) {
			//console.log("left");
			swipe = true;
			left(e);
		}
	});
	document.querySelector("body").addEventListener("touchend", function(e) {
		touchstart_x = null;
		//Tap인지 판단
		if(!(tStartEvt && !tMoveEvt)) {
			tStartEvt = false;
			tMoveEvt = false;
			swipe = false;
			
			return;
		}
		
		if(alreadyClicked) return;
		
		alreadyTaped = true;
		console.log("tap");
		fuc(e);
		alreadyClicked = false;
	});
	
	document.querySelector("body").addEventListener("click", function(e) {
		touchstart_x = null;
		if(alreadyTaped) return;

		alreadyClicked = true;
		console.log("click");
		fuc(e);
		alreadyTaped = false;
	});
}

