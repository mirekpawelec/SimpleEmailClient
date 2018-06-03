(function(){
    var     myDataForm          = document.querySelector("#myDataForm"),
            fieldsForm          = myDataForm.querySelectorAll("[data-error]"),
            correctPassword     = false;
    
    const   MY_DATA_HTML_BLOCK_ID = 'myDataPanel',
            FILTERS_HTML_BLOCK_ID = 'filtersPanel';
            
    function assignEventsToButtons(){
        document.querySelector("#myDataBtn").onclick = function(){
            displaySector(MY_DATA_HTML_BLOCK_ID);
        };
        document.querySelector("#filtersBtn").onclick = function(){
            displaySector(FILTERS_HTML_BLOCK_ID);
        };
        document.querySelector(".alert-close").onclick = function(e){
            var div = this.parentElement;
            div.style.opacity = "0";
            setTimeout(function(){ div.style.display = "none"; }, 600);
        };
        fieldsForm.forEach(function(inputForm){
            inputForm.oninput = function(e){clearValidationErrorsDisplayed(this);};
        });
        myDataForm.querySelector("#password").addEventListener("keyup", function(e){verifyPassword(this);},false);
    }
    
    function displaySector(panelName){
        var panelName = panelName || MY_DATA_HTML_BLOCK_ID,
            sectors = document.querySelectorAll("div[id$='Panel']");
        sectors.forEach(function(sector){
            sector.style.display = "none";
        });
        sectors.forEach(function(sector){
            if(sector.id===panelName){
               sector.style.display = "block";
               return;
            }
        });
    }
    
    myDataForm.onsubmit = function(e){
        e.preventDefault();
        if(validateForm()){
            this.submit();
            return;
        }
    }
    
    function validateForm(){
        var errors 		= [], 
            infoValidateErrors 	= {}, 
            validateErrors	= {};

        fieldsForm.forEach(function(field){
            infoValidateErrors[field.id] = getDataErrorInfo(field);
            errors = checkField(field, infoValidateErrors[field.id]);
            if(errors.length>0){
                validateErrors[field.id] = errors;
                errors = [];
            }
        });
        if(Object.keys(validateErrors).length){
            displayValidateError(validateErrors);
            return false;
        }else{
            fieldsForm.forEach(function(field){
                clearValidationErrorsDisplayed(field);
            });
            return true;
        }
    }

    function getDataErrorInfo(elem){
            var arr                 = [], 
                arrValidateErrors   = [];
            arr = elem.dataset.error.split(/; */);
            arr.forEach(function(row){
                    arrValidateErrors.push(row.split("="));
            });
            return arrValidateErrors;
    }

    function checkField(field, arrValidateErrors){
        var 	errors 		= [];
        const 	FIRST_TAB 	= 0, 
                LAST_TAB 	= 2;
        arrValidateErrors.forEach(function(row){
            if("empty"===row[0]){
                if(field.value===""){
                    errors.push(row[1]);
                }
            }else if("length"===row[0]){
                if(field.value.length>150){
                    errors.push(row[1]);
                }
            }else if("pattern"===row[0]){
                if( ! correctPassword && field.id==="password" && field.value.length>=1){
                    errors.push(row[1]);
                }
            }else if("password"===row[0]){
                if( ! (field.value===myDataForm.querySelector("#password").value)){
                    errors.push(row[1]);
                }
            }
        });
        return errors;
    }

    function clearValidationErrorsDisplayed(elementHtml){
        elementHtml.classList.remove("invalid");
        elementHtml.parentNode.querySelector("#form-error-"+elementHtml.id).innerHTML = "";
    }

    function displayValidateError(objErrors){	
        fieldsForm.forEach(function(field){
            clearValidationErrorsDisplayed(field);
            if(field.id in objErrors){
                field.classList.add("invalid");
                objErrors[field.id].forEach(function(errMsg){
                    var elemSpan = document.createElement("span");
                    elemSpan.classList.add("invalid");
                    elemSpan.appendChild(document.createTextNode(errMsg));
                    field.parentNode.querySelector("#form-error-"+field.id).appendChild(elemSpan);
                });
            }
        });
    }
    
    function verifyPassword(passwdInputField){
        var 	infoPasswd		= document.querySelector("#infoPasswd"),
                lengthPasswd		= infoPasswd.querySelector("#lengthPasswd"),
                lowercaseLetterPasswd	= infoPasswd.querySelector("#lowercaseLetterPasswd"),
                digitPasswd 		= infoPasswd.querySelector("#digitPasswd"),
                uppercaseLetterPasswd	= infoPasswd.querySelector("#uppercaseLetterPasswd"),
                regExpLowercase         = /[a-z]/g,
                regExpUppercase		= /[A-Z]/g,
                regExpDigit 		= /[0-9]/g;
        const 	CORRECT_CLASS 		= "correct-value",
                INCORRECT_CLASS         = "incorrect-value",
                LENGTH_PASSWD		= 8;

        if(passwdInputField.value.match(regExpLowercase)){
            lowercaseLetterPasswd.classList.remove(INCORRECT_CLASS);
            lowercaseLetterPasswd.classList.add(CORRECT_CLASS);
        }else{
            lowercaseLetterPasswd.classList.remove(CORRECT_CLASS);
            lowercaseLetterPasswd.classList.add(INCORRECT_CLASS);
        }
        if(passwdInputField.value.match(regExpUppercase)){
            uppercaseLetterPasswd.classList.remove(INCORRECT_CLASS);
            uppercaseLetterPasswd.classList.add(CORRECT_CLASS);
        }else{
            uppercaseLetterPasswd.classList.remove(CORRECT_CLASS);
            uppercaseLetterPasswd.classList.add(INCORRECT_CLASS);
        }
        if(passwdInputField.value.match(regExpDigit)){
            digitPasswd.classList.remove(INCORRECT_CLASS);
            digitPasswd.classList.add(CORRECT_CLASS);
        }else{
            digitPasswd.classList.remove(CORRECT_CLASS);
            digitPasswd.classList.add(INCORRECT_CLASS);
        }
        if(passwdInputField.value.length>=LENGTH_PASSWD){
            lengthPasswd.classList.remove(INCORRECT_CLASS);
            lengthPasswd.classList.add(CORRECT_CLASS);
        }else{
            lengthPasswd.classList.remove(CORRECT_CLASS);
            lengthPasswd.classList.add(INCORRECT_CLASS);
        }
        if(infoPasswd.querySelectorAll(".correct-value").length===4){
            infoPasswd.classList.add(CORRECT_CLASS);
            correctPassword = true;
        }else{
            infoPasswd.classList.remove(CORRECT_CLASS);
            correctPassword = false;
        }
    }
    
    assignEventsToButtons();
    displaySector();
})();
