function Base64Unicode(){
    if( ! (this instanceof Base64Unicode)){
        return new Base64Unicode();
    };
    this.decode = function(str) {
        try{
            return decodeURIComponent(Array.prototype.map.call(atob(str), function(c) {
                return '%' + ('00' + c.charCodeAt(0).toString(16)).slice(-2);
            }).join(''));
        }catch(err){
            console.error(err);
            return str;
        }
    };
    this.encode = function(str) {
        return btoa(encodeURIComponent(str).replace(/%([0-9A-F]{2})/g, function(match, p1) {
            return String.fromCharCode(parseInt(p1, 16));
        }));
    };
}