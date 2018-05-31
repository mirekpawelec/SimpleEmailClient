function Folder(name, etag, content, metadata){
    if( ! (this instanceof Folder)){
            return new Folder(name, etag, content, metadata);
    }
    if( ! ( typeof arguments[0]==="string" && typeof arguments[1]==="string" && Array.isArray(arguments[2]) && typeof arguments[3]==="object")){
        console.error("Object Folder: incorrect data type! Required {string} name, {string} etag, {array} content, {object} metadata.");
        return;
    }
    var _name = name,
        _etag = etag, 
        _content = content,
        _metadata = metadata;
    this.getName = function(){return _name;};
    this.setName = function(name){
        if( ! (typeof name==="string")){
            console.error("Folder{}, method setName(): incorrect data type! Required {string} name.");
            return;
        }
        _name=name;};
    this.getETag = function(){return _etag;};
    this.setETag = function(etag){
        if( ! (typeof etag==="string")){
            console.error("Folder{}, method setETag(): incorrect data type! Required {string} etag.");
            return;
        }
        _etag=etag;};
    this.getContent = function(){return _content;};
    this.setContent = function(content){
        if( ! Array.isArray(content)){
            console.error("Folder{}, method setContent(): incorrect data type! Required {array} content.");
            return;
        }
        _content=content;};
    this.getMetadata = function(){return _metadata;};
    this.setMetadata = function(metadata){
        if( ! typeof metadata==="object"){
            console.error("Folder{}, method setMetadata(): incorrect data type! Required {object} content.");
            return;
        }
        _metadata=metadata;};
}
Folder.prototype.toString = function(){
        return "Folder{ name="+this.getName()+", etag="+this.getETag()+", contents="+this.getContent()+", metadata="+this.getMetadata()+" }";
};
