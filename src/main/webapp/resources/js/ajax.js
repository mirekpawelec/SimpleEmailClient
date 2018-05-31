/*
AJAX({
    type: "POST",
    url: "odbierz.php",
    data: {
        firstName: "Piotr",
        lastName: "Kowalski"
    },
	options: {
        async : true,
        timeout : 0,
        username : null,
        password : null
    },
    headers: {
        "X-My-Header": "123#asdf"
    },
    success: function(response, xhr) {
        console.log("Udało się! Status: " + xhr.status);
    },
    failure: function(xhr) {
        console.log("Wystąpił błąd. Status: " + xhr.status);
    }
});
 */

function AJAX(config){
    if(!(this instanceof AJAX)){
        return new AJAX(config);
    }
    this._xhr = new XMLHttpRequest();
    this._config = this._extendOptions(config);
    this._assignEvents();
    this._beforeSend();
}

AJAX.prototype._assignEvents = function(){
    this._xhr.addEventListener("readystatechange", this._handleResponse.bind(this), false); /* metodzie klasy AJAX przekazujemy instancję klasy AJAX */
    this._xhr.addEventListener("abort", this._handleError.bind(this), false);
    this._xhr.addEventListener("error", this._handleError.bind(this), false);
    this._xhr.addEventListener("timeout", this._handleError.bind(this), false);
};

AJAX.prototype._handleResponse = function(){
    if(this._xhr.readyState === 4 && this._xhr.status >= 200 && this._xhr.status < 400){
        if(typeof this._config.success === "function"){
            this._config.success(this._xhr.response, this._xhr);
        }else{
            new Error("No function to handle response! Add a function to 'success' field." );
    	}
    } else if(this._xhr.readyState===4 && this._xhr.status >= 400){
        this._handleError();
    }
};

AJAX.prototype._handleError = function(){
    if(typeof this._config.failure === "function"){
        this._config.failure(this._xhr);
    }else{
        new Error("No function to handle error! Add a function to 'failure' field." );
    }
};

AJAX.prototype._open = function(){
    this._xhr.open(this._config.type, 
                   this._config.url, 
                   this._config.options.async, 
                   this._config.options.username, 
                   this._config.options.password);
	
	/* wskazanie dla serwera, że żadanie pochodzi z AJAX'a, nagłówki 
       z 'X' z przodu są to własne nagłówki, nie związane z protokołem HTTP */
    /*this._xhr.setRequestHeader("X-Requested-With", "XMLHttpRequest");*/ 
    
	this._xhr.timeout = this._config.options.timeout;
};

AJAX.prototype._assignUserHeaders = function(){
    if(Object.keys(this._config.headers).length){                           // Object.keys(this._config.headers) zwraca tablice kluczy obiektu

        for(var key in this._config.headers){
            this._xhr.setRequestHeader(key, this._config.headers[key]);
        }

    }
};

AJAX.prototype._send = function(data){
	this._xhr.send(data);
};

AJAX.prototype._beforeSend = function(){
    var isData = (Object.keys(this._config.data).length > 0) || (this._config.data instanceof FormData),
        data = null; 

    if(isData){
        if(this._config.type.toUpperCase() === "POST"){    

            if(String(this._config.headers['Content-Type']).indexOf("application/x-www-form-urlencoded") > -1){
                data = this._serializeData(this._config.data);
            } else if(String(this._config.headers['Content-Type']).indexOf("application/json") > -1){
                data = JSON.stringify(this._config.data);
            } else{
                data = this._serializeFormData(this._config.data);
            }

        } else if(this._config.type.toUpperCase() === "GET"){
            this._config.url += "?" + this._serializeData(this._config.data);
        }
    }

    this._open();
    this._assignUserHeaders();
    this._send(data);
};

AJAX.prototype._serializeFormData = function(data){
    var serialized = new FormData();
    console.log("isFormData: " + (data instanceof FormData) );
    if(data instanceof FormData){
        console.log("przekazuje dane...");
        serialized = data;
    }else{
        for(var key in data){
            serialized.append(key, data[key]);
        }
    }
    return serialized;
};

AJAX.prototype._serializeData = function(data){
    var serialized = "";
    for(var key in data){
        serialized += key + "=" + encodeURIComponent(data[key]) + "&";      // encodeURIComponent(), kodowanie znaków specjalnych, zastępuję np. spację znakami '%20'
    }
    return serialized.slice(0, serialized.length - 1);
};

AJAX.prototype._extendOptions = function(config){
	/* aby nie kopiować referencji do tego samego obiektu przy 
       tworzeniu kolejnych instancji AJAX, konwertujemy go do string, 
       następnie ponownie do obiektu, tworząc tym samym całkowicie 
       nowy obiekt */
    var defaultConfig = JSON.parse(JSON.stringify(this._defaultConfig));
    for(var key in defaultConfig){
        if(key in config){
            continue;
        }
        config[key] = defaultConfig[key];
    }
    return config;
};

AJAX.prototype._defaultConfig = {
    type: "GET",
    url: window.location.href,
    data: {},
    options: {
        async : true,
        timeout : 0,
        username : null,
        password : null
    },
    headers: {}
};
