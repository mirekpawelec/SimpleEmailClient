(function(){
    var     modal = document.querySelector(".modal"),
            contactForm = document.querySelector("#contactForm"),
            fieldsForm = contactForm.querySelectorAll("[data-error]");
        
    const   MODAL_HEADER_TXT_CREATE = 'Dodaj nowy kontakt',
            MODAL_HEADER_TXT_UPDATE = 'Modyfikuj kontakt';
            
    function assignEventsToButtons(){
        var newContactBtn = document.querySelector("#newContact"),
            closeBtn = document.querySelector(".close"),
            alertCloseBtn = document.querySelector(".alert-close"),
            updateButtons = document.querySelectorAll("button[id^='updateBtn']");
        newContactBtn.onclick = function(e){
            e.stopPropagation();
            displayModalWindow();
        };
        closeBtn.onclick = function(e){
            closeModalWindow();
        };
        fieldsForm.forEach(function(inputForm){
            inputForm.oninput = function(e){clearValidationErrorsDisplayed(this);};
        });
        alertCloseBtn.onclick = function(e){
            var div = this.parentElement;
            div.style.opacity = "0";
            setTimeout(function(){ div.style.display = "none"; }, 600);
        }
        updateButtons.forEach(function(button){
            button.onclick = function(e){
                fillForm.bind(this)();
                displayModalWindow();
            };
        });
        window.onclick = function(e){
            if(e.target===modal){
                closeModalWindow();
            }
        };
    }
    
    function fillForm(){
        var id = this.id.replace("updateBtn_",""),
            name = document.querySelector("#name_"+id).innerText,
            emailAddress = document.querySelector("#emailAddress_"+id).innerText,
            status = document.querySelector("#status_"+id).innerText;
        modal.querySelector(".modalBoxContactHeader p").innerHTML = MODAL_HEADER_TXT_UPDATE;
        contactForm.querySelector("#contactId").value = id;
        contactForm.querySelector("#name").value = name;
        contactForm.querySelector("#emailAddress").value = emailAddress;
        contactForm.querySelector("#status").value = status;
    }
    
    contactForm.onsubmit = function(e){
        e.preventDefault();
        if(validateForm()){
            contactForm.submit();
            return;
        }
    };
    
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
                if(field.value.length>100){
                    errors.push(row[1]);
                }
            }else if("email"===row[0]){
                var regEmail = /^(\w+[+.-]{1}){0,}\w+@(\w+[.-]{1}){0,}\w+\.\w{2,}$/;
                if( ! field.value.match(regEmail)){
                    errors.push(row[1]);
                }
            }else if("pattern"===row[0]){
                if(field.value.length && !field.value.match(/^([^<>\[\]@] *)*$/)){
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
    };

    function displayModalWindow(){
        modal.style.display = 'block';
    };
    
    function closeModalWindow(){
        modal.style.display = 'none';
        clearContentForm();
    };
    
    function clearContentForm(){
        var inputId = contactForm.querySelector("input#contactId").value;
        if(inputId){
            modal.querySelector(".modalBoxContactHeader p").innerHTML = MODAL_HEADER_TXT_CREATE;
            inputId.value = "";
            fieldsForm.forEach(function(field){
                clearValidationErrorsDisplayed(field);
                field.value = "";
            });
        }
    };
    
    assignEventsToButtons();
})();
