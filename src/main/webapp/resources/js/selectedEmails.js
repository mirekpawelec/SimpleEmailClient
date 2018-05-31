function SelectedEmails(folderName, emailsId){
    if( ! (this instanceof SelectedEmails))
        return new SelectedEmails(folderName, emailsId);
    if( ! ( typeof arguments[0]==="string" && Array.isArray(arguments[1]) )){
        console.error("Object SelectedEmails: incorrect data type! Required {string} folderName, {array} emailsId");
        return;
    }
    var _count = emailsId.length,
        _folderName = folderName.toUpperCase(),
        _arrEmailsId = emailsId.map(function(id){return {'id':id,'status':'new'};});
    this.getCount = function(){return _count;};
    this.getFolderName = function(){return _folderName;};
    this.setFolderName = function(folderName){_folderName = folderName.toUpperCase();};
    this.getEmailsId = function(){return _arrEmailsId;};
    this.getEmailIdById = function(id){return _arrEmailsId.filter(function(item){return item.id===id;})[0];};
    this.getEmailIdByIndex = function(index){return _arrEmailsId[index];};
    this.addEmailId = function(emailsId){
        if( ! Array.isArray(emailsId)){
            console.error("SelectedEmails{}, method addEmailId(): incorrect data type! Required {array} emailsId");
            return;
        }
        emailsId.forEach(function(id){
            _arrEmailsId.push({'id':id, 'status':'new'});
        });
        _count = _arrEmailsId.length;
    };
    this.removeEmailIdById = function(id){
        if( ! (typeof id==="string")){
            console.error("Object SelectedEmails{} : method removeEmailIdById(id) : incorrect data type! "+
                          "Required {string} id. Passed type is {"+(typeof id)+"}");
            return;
        }
        var i;
        for(i=0; i<_arrEmailsId.length; i++){
            if(_arrEmailsId[i].id===id) break;
        }
        _arrEmailsId.splice(i, 1);
        _count = _arrEmailsId.length;
    };
    this.removeEmailIdByIndex = function(index){
        _arrEmailsId.splice(index, 1);
        _count = _arrEmailsId.length;
    };
    this.removeAllEmailsId = function(){
        _arrEmailsId.splice(0, _arrEmailsId.length);
        _count = _arrEmailsId.length;
    };
    this.changeEmailIdStatusById = function(id){
        if( ! (typeof id==="string")){
            console.error("Object SelectedEmails{} : method changeEmailIdStatusById(id) : incorrect data type! "+
                          "Required {string} id. Passed type is {"+(typeof id)+"}");
            return;
        }
        _arrEmailsId.filter(function(item){return item.id===id;}).map(function(item){return item.status = "processed";});
    };
    this.changeEmailIdStatusByIndex = function(index){
        _arrEmailsId[index].status = 'processed';
    };
}
SelectedEmails.prototype.toString = function(){
    return "SelectedFolderElement{" 
            + " count="+this.getCount()
            +", folderName="+this.getFolderName()
            +", arrEmailsId=["+this.getEmailsId().map(function(item){return " {id:"+item.id+", status:"+item.status+"}";})+"]"
            +" }";
};
