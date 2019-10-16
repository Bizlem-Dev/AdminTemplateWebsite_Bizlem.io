var listjsondata="";
var onetempinfo="";
var url_string = window.location.href ;
var url = new URL(url_string);

$( document ).ready(function() {
	
	
	
	var list = url.searchParams.get("list");
	console.log(list);
	
	if(list!="1"){
	
		console.log("in 0");


	var listvalue="";

	$.ajax({

url:'/portal/servlet/service/TemplateAdminListData?adminEmailId=templateadmin_gmail.com',
method:'GET',

success: function(data){
//alert(data);
listjsondata=data;
var arr = JSON.parse(data);
var i=0;
for (var key in arr) {
    i++;
var ikey = "'" + key + "'";

if(i==0){
	listvalue=listvalue+'<li class="nav-item" ><a style="" class="nav-link active data-domainname" id="pills-all-tab" onclick="clickfunction('+ikey+');" data-toggle="pill" href="#pills-all" role="tab" aria-controls="pills-all" aria-selected="true">'+key+'</a></li>'
}else{
	listvalue=listvalue+'<li class="nav-item" ><a style="" class="nav-link data-domainname" id="pills-all-tab" onclick="clickfunction('+ikey+');" data-toggle="pill" href="#pills-all" role="tab" aria-controls="pills-business-development" aria-selected="false">'+key+'</a></li>'
}
	
	}
document.getElementById("pills-tab").innerHTML = document.getElementById("pills-tab").innerHTML + listvalue;
allfunction();
},
complete: function(){
//	var clientHeight = document.getElementById('hideDiv').clientHeight;
//	console.log("clientHeight: "+clientHeight);
	var body = document.body,
    html = document.documentElement;

var height = Math.max( body.scrollHeight, body.offsetHeight, 
                       html.clientHeight, html.scrollHeight, html.offsetHeight );
console.log("complete clientHeight: "+height);
window.parent.postMessage(height, '*');
}

// all 
});
	}else if(list==1){
		console.log("in list 1");
	var TemplateName	= url.searchParams.get("TemplateName");
	if(TemplateName!=null){
	showViewPage(TemplateName);
	}
	}	
	
	
	
	
	
});

function clickfunction(clickedvalue) {
var divcopy=""


var jsonData = JSON.parse(listjsondata);

for (var i = 0; i < jsonData[clickedvalue].length; i++) {
	
    var name = jsonData[clickedvalue][i];
	var arrName = "'" + clickedvalue + "'";
	var tempname=name.templatename;
	var tempnameLearnMore=name.templatename;
	tempnameLearnMore = "'" + tempnameLearnMore + "'";
//	divcopy=divcopy+'<div class="marketing filter-block" id="namelist"><div class="filter-block-image"><a target="_blank" href="#" title="User Persona Template"><img width="1024" height="569" src="/portal/apps/AdminWebSiteCSSAndJs/images/user-persona.jpg" alt=""></a></div> <div class="filter-block-description"><h2 class="title"><a href="#" id="username">'+name.templatename+'</a></h2><p class="">SubCategory - '+name.subdomainname+'</p> <div class="counter-container"><p class="counterCreated"> Used 343409 times</p> </div><div class="hover-buttons-wrapper"> <a class="btn btn-primary" href="#" target="_blank">Learn More</a><button class="btn btn-default" onclick=getGlobalTemplateData('+arrName+','+i+');>Use Template</button> </div></div></div>'
	divcopy=divcopy+'<div class="marketing filter-block" id="namelist"><div class="filter-block-image"><a target="_blank" href="#" title="User Persona Template"><img width="1024" height="569" src="/portal/apps/AdminWebSiteCSSAndJs/images/user-persona.jpg" alt=""></a></div> <div class="filter-block-description"><h2 class="title"><a href="#" id="username">'+name.templatename+'</a></h2><p class="">'+name.subdomainname+'</p> <div class="counter-container"><p class="counterCreated">343409</p> </div><div class="hover-buttons-wrapper"> <a class="btn btn-primary" href="https://uk.bluealgo.com:8083/portal/servlet/service/newsPaperContainerWordpress?list=1&TemplateName='+tempname+'&name='+arrName+'&pos='+i+'">Learn More</a><button class="btn btn-default" onclick=getGlobalTemplateData('+arrName+','+i+');><i class="fa fa-check" aria-hidden="true"></i></button> </div></div></div>'
}
document.getElementById("pills-all").innerHTML=divcopy;
}


function allfunction() {

var divcopy=""
var arr = JSON.parse(listjsondata);
for (var key in arr) {
    
	for (var i = 0; i < arr[key].length; i++) {
	
    var name = arr[key][i];
	var tempname=name.templatename;
    var tempnameLearnMore=name.templatename;
	tempnameLearnMore = "'" + tempnameLearnMore + "'";
	var arrName ="'"+key+"'";

   // divcopy=divcopy+'<div class="marketing filter-block" id="namelist"><div class="filter-block-image"><a target="_blank" href="#" title="User Persona Template"><img width="1024" height="569" src="/portal/apps/AdminWebSiteCSSAndJs/images/user-persona.jpg" alt=""></a></div> <div class="filter-block-description"><h2 class="title"><a href="#" id="username">'+name.templatename+'</a></h2><p class="">'+name.subdomainname+'</p> <div class="counter-container"><p class="counterCreated">343409</p> </div><div class="hover-buttons-wrapper"> <a class="btn btn-primary" href="#" target="_blank">Learn More</a><a class="btn btn-default" href="#" target="_blank"><i class="fa fa-check" aria-hidden="true"></i></a><button class="btn btn-default" onclick=getGlobalTemplateData('+arrName+','+i+');>Use Template</button> </div></div></div>'
	divcopy=divcopy+'<div class="marketing filter-block" id="namelist"><div class="filter-block-image"><a target="_blank" href="#" title="User Persona Template"><img width="1024" height="569" src="/portal/apps/AdminWebSiteCSSAndJs/images/user-persona.jpg" alt=""></a></div> <div class="filter-block-description"><h2 class="title"><a href="#" id="username">'+name.templatename+'</a></h2><p class="">'+name.subdomainname+'</p> <div class="counter-container"><p class="counterCreated">343409</p> </div><div class="hover-buttons-wrapper"> <a class="btn btn-primary" href="https://uk.bluealgo.com:8083/portal/servlet/service/newsPaperContainerWordpress?list=1&TemplateName='+tempname+'&name='+arrName+'&pos='+i+'"  >Learn More</a><button class="btn btn-default" onclick=getGlobalTemplateData('+arrName+','+i+');><i class="fa fa-check" aria-hidden="true"></i></button> </div></div></div>'
}
	
	}

document.getElementById("pills-all").innerHTML=divcopy;
}

function getGlobalTemplateData(name,pos){
	console.log(name + pos);
	var arr = JSON.parse(listjsondata);
	var clickJson = arr[name][pos];
	console.log(JSON.stringify(clickJson));
			$.ajax({
		        type: "POST",
		        url: "/portal/servlet/service/useglobaltemplateNew",
		        async: false,
		        data:JSON.stringify(clickJson),
		        contentType: "application/json",
		        success: function (data) {
					//alert(data);
		        	console.log(data);
		        	var jsonData = JSON.parse(data);
		        	console.log(jsonData);
		        	if ( jsonData.hasOwnProperty("message")) {
						 var message=jsonData.message;
						 
						 if( message=="Please Login" ){
							 
							 window.open('http://uk.bluealgo.com/portal/login', '_blank');
							 
						 }else if( message=="Template unavailable" ){
							 alert("Please Purchase the Service.");
						 }else if( message=="Template added to local Library" ){
							 alert("Template added to local Library");
						 }
						 
		        	}// message close
				}
				
				
				
				});
	}
function showViewPage(templatename){
	try{
	$.ajax({
        type: "GET",
        url: "/portal/servlet/service/getLearnMoredata?templatename="+templatename,
        async: false,
        success: function (data) {
			//alert(data);
        	console.log(data);
    		onetempinfo=data;
        	var jsonData = JSON.parse(data);
        	console.log(jsonData);
    		var innerli="";
        	if(jsonData.hasOwnProperty("imagelinkarr")){
        		var imagelinkArray=jsonData.imagelinkarr;
        		
        		for(var i=0; i<imagelinkArray.length; i++){
        			innerli=innerli+'<li data-thumb="'+imagelinkArray[0]+'"><img src="'+imagelinkArray[i]+'" /> </li>';
        		}
        		console.log("innerli  "+innerli);
        		}
        	document.getElementById("settempimagesli").innerHTML=innerli;
        	document.getElementById("templatenameid").innerHTML=templatename;
        	$('.flexslider').flexslider({
        		animation: "slide",
        		controlNav: "thumbnails",
        		start: function(slider){
        		$('body').removeClass('loading');
        		}
        		});
        		$(".flex-control-thumbs li img").hover(function(){
        		$(this).click();
        		});
        	}
		});
	     $("#learn-more-wrapper-id").show();
	     $(".newspaper-filter").hide();

	     
}catch( err){
	console.log(err);

	
}
}


$("#downloadinmylibraty").click(function (){
	var onetempinfonew = JSON.parse(onetempinfo);
	console.log("onetempinfonew "+JSON.stringify(onetempinfonew));
var tempname="";
var type="";
var nodepath="";
tempname=document.getElementById(templatenameid);

if(onetempinfonew.hasOwnProperty("type")){
	type=onetempinfonew.type;
}
if(onetempinfonew.hasOwnProperty("nodepath")){
	nodepath=onetempinfonew.nodepath;
}

var newjson={};
newjson["type"]=type;
newjson["nodepath"]=nodepath;
newjson["templatename"]=tempname;
console.log(JSON.stringify(newjson));
$.ajax({
    type: "POST",
    url: "/portal/servlet/service/useglobaltemplateNew",
    async: false,
    data:JSON.stringify(newjson),
    contentType: "application/json",
    success: function (data) {
		//alert(data);
    	console.log(data);
    	var jsonData = JSON.parse(data);
    	console.log(jsonData);
    	if ( jsonData.hasOwnProperty("message")) {
			 var message=jsonData.message;
			 
			 if( message=="Please Login" ){
				 
				 window.open('http://uk.bluealgo.com/portal/login', '_blank');
				 
			 }else if( message=="Template unavailable" ){
				 alert("Please Purchase the Service.");
			 }else if( message=="Template added to local Library" ){
				 alert("Template added to local Library");
			 }
			 
    	}// message close
	}
	
	
	
	});
	
});
