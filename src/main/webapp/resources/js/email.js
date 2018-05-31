function Email(id, user, sender, recipient, additionalRecipient, title, message, attachmentName, folder, flag, readingConfirmation, status, createDate){ 
    if( ! (this instanceof Email))
        return new Email(id, user, sender, recipient, additionalRecipient, title, message, attachmentName. folder, flag, readingConfirmation, status, createDate);
    var isOK = true;
    for(var index=0; index<arguments.length; index++){
        var incorrectArg = false;
        if(index===0){
            if( ! Number.isInteger(arguments[index])) incorrectArg=true;
        }else if(index===1){
            if( ! (typeof arguments[index]==="object" && this.isJSON(arguments[index]))) incorrectArg=true;
        }else if(index===9 || index===10){
            if( ! (typeof arguments[index]==="string" && this.isYESOrNO(arguments[index]))) incorrectArg=true;
        }else if(index===11){
            if( ! (typeof arguments[index]==="string" && this.isNEWOrOLD(arguments[index]))) incorrectArg=true;
        }else if(index===12){
            if( ! (arguments[index]!=null && arguments[index].constructor.name==="Date")) incorrectArg=true;
        }else{
            if( ! (typeof arguments[index]==="string")) incorrectArg=true;
        }
        if(incorrectArg){
            console.error("Object Email: incorrect data type [arg. "+index+", value="+arguments[index]+"]! Required {number} id, {string} user, {string} sender, {string} recipient," 
                       + " {string} additionalRecipient, {string} title, {string} message, {string} attachmentName, {string} folder, {string} flag (values only 'YES', 'NO')," 
                       + " {string} readingConfirmation (values only 'YES', 'NO'), {string} status (values only 'NEW', 'OLD'), {date} createDate");
            isOK = false;
        }
    };
    if(!isOK) return;
    var _id = id,
        _user = user,
        _sender = sender,
        _recipient = recipient,
        _additionalRecipient = additionalRecipient,
        _title = title,
        _message = message,
        _attachmentName = attachmentName,
        _folder = folder,
        _flag = flag,
        _readingConfirmation = readingConfirmation,
        _status = status,
        _createDate = createDate;
    this.getId = function(){return _id;};
    this.setId = function(id){
        if( ! Number.isInteger(id)){
            this.displayError("setId","number","id",""); 
            return;
        }
        _id=id;
    };
    this.getUser = function(){return _user;};
    this.setUser = function(user){
        if( ! (typeof recipient==="object" && this.isJSON(user))){
            this.displayError("setUser","object","user","");
            return;
        }
        _user=user;
    };
    this.getSender = function(){return _sender;};
    this.setSender = function(sender){
        if( ! (typeof sender==="string")){
            this.displayError("setSender","string","sender","");
            return;
        }
        _sender=sender;
    };
    this.getRecipient = function(){return _recipient;};
    this.setRecipient = function(recipient){
        if( ! (typeof recipient==="string")){
            this.displayError("setRecipient","string","recipient","");
            return;
        }
        _recipient=recipient;
    };
    this.getAdditionalRecipient = function(){return _additionalRecipient;};
    this.setAdditionalRecipient = function(additionalRecipient){
        if( ! (typeof additionalRecipient==="string")){
            this.displayError("setAdditionalRecipient","string","additionalRecipient","");
            return;
        }
        _additionalRecipient=additionalRecipient;
    };
    this.getTitle = function(){return _title;};
    this.setTitle = function(title){
        if( ! (typeof title==="string")){
            this.displayError("setTitle","string","title","");
            return;
        }
        _title=title;
    };
    this.getMessage = function(){return _message;};
    this.setMessage = function(message){
        if( ! (typeof message==="string")){
            this.displayError("setMessage","string","message","");
            return;
        }
        _message=message;
    };
    this.getAttachmentName = function(){return _attachmentName;};
    this.setAttachmentName = function(attachmentName){
        if( ! (typeof attachmentName==="string")){
            this.displayError("setAttachmentName","string","attachmentName","");
            return;
        }
        _attachmentName=attachmentName;
    };
    this.getFolder = function(){return _folder;};
    this.setFolder = function(folder){
        if( ! (typeof folder==="string")){
            this.displayError("setFolder","string","folder","");
            return;
        }
        _folder=folder;
    };
    this.getFlag = function(){return _flag;};
    this.setFlag = function(flag){
        if( ! (typeof flag==="string" && this.isYESOrNO(flag))){
            this.displayError("setFlag","string","flag","Values only 'YES' or 'NO'!");
            return;
        }
        _flag=flag;
    };
    this.getReadingConfirmation = function(){return _readingConfirmation;};
    this.setReadingConfirmation = function(readingConfirmation){
        if( ! (typeof readingConfirmation==="string" && this.isYESOrNO(readingConfirmation))){
            this.displayError("setReadingConfirmation","string","readingConfirmation","Values only 'YES' or 'NO'!");
            return;
        }
        _readingConfirmation = readingConfirmation;
    };
    this.getStatus = function(){return _status;};
    this.setStatus = function(status){
        if( ! (typeof status==="string" && this.isNEWOrOLD(status))){
            this.displayError("setStatus","string","status","Values only 'NEW' or 'OLD'!");
            return;
        }
        _status = status;
    }; 
    this.getCreateDate = function(){return _createDate;};
    this.setCreateDate = function(createDate){
        if( ! (createDate.constructor.name==="Date")){
            console.error("Email{}, method setCreateDate(): incorrect data type! Required {date} createDate");
            return;
        }
        _createDate = createDate;
    };
}
Email.prototype.toString = function(){
    return "Email{ id="+this.getId()
               +", user="+this.getUser()
               +", recipient="+this.getRecipient()
               +", additionalRecipient="+this.getAdditionalRecipient()
               +", title="+this.getTitle()
               +", message="+this.getMessage()
               +", attachmentName="+this.getAttachmentName()
               +", folder="+this.getFolder()
               +", flag="+this.getFlag()
               +", readingConfirmatio="+this.getReadingConfirmation()
               +", status="+this.getStatus()
               +", createDate="+this.getCreateDate().toLocaleString()
               +" }";};
Email.prototype.displayError = function(method, typeData, variable, additionalInfo){
    console.error("Email{}, method "+method+"(): incorrect data type! Required {"+typeData+"} '"+variable+"'! "+additionalInfo);
};
Email.prototype.isYESOrNO = function(value){
    if( ! (value==="YES" || value==="NO")){
        return false;
    }
    return true;
};
Email.prototype.isNEWOrOLD = function(value){
    if( ! (value==="NEW" || value==="OLD")){
        return false;
    }
    return true;
};
Email.prototype.isJSON = function(value){
    var isJson = false;
    if(arguments[0].constructor==={}.constructor){
        var value = JSON.stringify(arguments[0]);
        try{
            JSON.parse(value);
            isJson = true;
        }catch(err){
            isJson = false;
        }
    }
    return isJson;
};
