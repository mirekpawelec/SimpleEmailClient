(function(){	
    var     currentTab          = 0,
            tabs 		= document.querySelectorAll(".tab"),
            steps 		= document.querySelectorAll(".step"),
            modal 		= document.querySelector('.modal'),
            close 		= document.querySelectorAll('.close'),
            userLoginExists     = false,
            correctPassword     = false;
    const 	HOST		= "http://localHOST:8080/",
            APP_NAME		= "SimpleEmailClient";

    document.querySelector("#prevBtn").addEventListener("click",function(){nextStep(-1);}, false);
    document.querySelector("#nextBtn").addEventListener("click",function(){nextStep(1);}, false);
    document.querySelectorAll(".form-control").forEach(function(elem){
            elem.addEventListener("input", function(event){clearValidationErrorsDisplayed(event.target);}, false);
    });
    document.querySelector("#login").addEventListener("keyup", function(e){checkUserLogin(e.target);},false);
    document.querySelector("#password").addEventListener("keyup", function(e){verifyPassword(e.target);},false);
    document.querySelector("#showModalBtn").addEventListener("click", function(){displayRegisterWindowModal();},false);

    function showTab(tabNo){
        tabs[tabNo].style.display = "block";
        if(tabNo===0){
           document.querySelector("#prevBtn").style.display = "none";
        } else{
           document.querySelector("#prevBtn").style.display = "inline";
        }
        if(tabNo===(tabs.length-1)){
           document.querySelector("#nextBtn").innerHTML = "Utwórz";
        }else{
           document.querySelector("#nextBtn").innerHTML = "Dalej";
        }
        fixStepIndicator();
    }

    function fixStepIndicator(){
        for(var x=0; x<steps.length; x++){
            steps[x].className = steps[x].className.replace(" active","");
        }
        steps[currentTab].classList.add("active");
    }

    function nextStep(n){
        if(n===1 && !tabValidateForm()){
            return;
        }
        tabs[currentTab].style.display = "none";
        currentTab = currentTab + n;
        if(currentTab>=tabs.length){
            document.querySelector("#registerForm").submit();
            return;
        }
        showTab(currentTab);
    }

    function clearValidationErrorsDisplayed(elementHtml){
        elementHtml.classList.remove("invalid");
        elementHtml.parentNode.querySelectorAll(".invalid").forEach(function(singleElement){
            elementHtml.parentNode.removeChild(singleElement);
        });
    }

    function tabValidateForm(){
        var tabFormFields	= tabs[currentTab].querySelectorAll("[data-error]"),
            errors 		= [], 
            infoValidateErrors 	= {}, 
            validateErrors	= {};

        tabFormFields.forEach(function(field){
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
            tabFormFields.forEach(function(field){clearValidationErrorsDisplayed(field);});
            steps[currentTab].classList.add("finish");
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
            }else if("email"===row[0]){
                var regEmail = /^((\w+[+-.]\w+)|(\w+))@(\w+[-.]){0,2}\w+\.\w{2,}$/;
                if( ! field.value.match(regEmail)){
                    errors.push(row[1]);
                }
            }else if("exists"===row[0]){
                if(userLoginExists){ 
                    errors.push(row[1]);
                }
            }else if("pattern"===row[0]){
                if( ! correctPassword && field.id==="password" && field.value.length>=1){
                    errors.push(row[1]);
                }else if( ! field.value.match(/^\w+$/) && field.id==="login" && field.value.length>=1){
                    errors.push(row[1]); 
                }
            }else if("password"===row[0]){
                if(currentTab===FIRST_TAB){
                    if( ! (field.value===tabs[currentTab].querySelector("#emailPassword").value)){
                        errors.push(row[1]);
                    }
                }else if(currentTab===LAST_TAB){
                    if( ! (field.value===tabs[currentTab].querySelector("#password").value)){
                        errors.push(row[1]);
                    }
                }
            }
        });
        return errors;
    }

    function displayValidateError(objErrors, singleField){
        var tabFormFields = singleField || tabs[currentTab].querySelectorAll("[data-error]");	
        steps[currentTab].classList.remove("finish");
        tabFormFields.forEach(function(field){
            clearValidationErrorsDisplayed(field);
            if(field.id in objErrors){
                field.classList.add("invalid");
                objErrors[field.id].forEach(function(errMsg){
                    var elemSpan = document.createElement("span");
                    elemSpan.classList.add("col-12xs", "invalid");
                    elemSpan.appendChild(document.createTextNode(errMsg));
                    field.parentNode.appendChild(elemSpan);
                });
            }
        });
    }

    function checkUserLogin(loginInputField){
        var login   = window.btoa(encodeURIComponent(loginInputField.value) || "null"),
            path    = HOST + APP_NAME + "/rest/logins/" + login + "/exists";
        AJAX({
            type: "GET",
            url: path,
            success: function(response, xhr) {
                userLoginExists = response==="true"?true:false;
                if(userLoginExists){
                    var errors = checkField(loginInputField, getDataErrorInfo(loginInputField));
                    if(errors.length) displayValidateError({[loginInputField.id]:errors}, [loginInputField]);
                }
            },
            failure: function(xhr) {
                throw Error("It hasn't failed to check the username.\n"+xhr.response);
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
                INCORRECT_CLASS             = "incorrect-value",
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

    function displayRegisterWindowModal(){
        modal.style.display = 'block';
    }

    function closeRegisterWindowModal(){
        modal.style.display = 'none';
    }

    close.forEach(function(span){
        span.addEventListener("click", closeRegisterWindowModal, false);
    });

    window.onclick = function(e){
        if(e.target===modal)
            closeRegisterWindowModal();
    };

    showTab(currentTab);
})();

window.onload = function(){
    var boxSuccess = document.querySelector("#boxSuccess"),
        boxError = document.querySelector("#boxError");
//    box.classList.add("show");
    if(boxSuccess.classList.contains("show")){
        setTimeout(function(){
            boxSuccess.classList.add("reduce-size");
            setTimeout(function(){
                boxSuccess.className = "";
            },1000);
        }, 10000);
    }
    boxSuccess.querySelector("#verticalLineRight").onclick = function(){
        boxSuccess.classList.add("reduce-size");
    };
    boxError.querySelector(".btn-close").onclick = function(){
        this.parentNode.classList.remove("show");
    };
};





















