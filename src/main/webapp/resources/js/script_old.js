function getData(type, url, session, fnOk, fnError){
    var xhr = new XMLHttpRequest();
    xhr.open(type, url, true);
    xhr.onreadystatechange = function(e){
        if(this.readyState===4 && this.status>=200 && this.status<400){
            return fnOk(this.response);
        } else if(this.readyState===4 && this.status>=400) {
            return fnError(this)
        }
    }
    xhr.setRequestHeader("Authorization", window.btoa(session));
    xhr.send();
}

function addCookie(name, value, time){
    if(!navigator.cookieEnabled){
        return;
    }
    var e = encodeURIComponent,
        cookie = e(name)+"="+e(value);
    if(typeof time == "number"){
        var date = new Date();
        date.setTime(date.getTime() + time * 1000 * 60 * 60 );
        cookie += "; expires="+date.toGMTString();
    }
    document.cookie = cookie;
}

function getCookie(name){
    if(!document.cookie){
        return null;
    }
    var arrCookies = document.cookie.split(/; */),
        objCookies = {};
    arrCookies.forEach(function(cookie){
        cookie = cookie.split("=");
        objCookies[cookie[0]] = decodeURIComponent(cookie[1]);
    })
    return objCookies[name] || null; 
}

console.log(getCookie("sessionId"));
//getData("GET", "http://localhost:8080/TomcatAndJavaEECDI/rest/products", getCookie("sessionId"), 
//function(e){
//    console.log("OK! " + e);
//}, 
//function(e){
//    console.log("ERROR! Status: " + e.status + ", Msg: " + e.response);
//});