(function(){
/*****************************************************************************
Declaration of a variables, constants ****************************************
*****************************************************************************/	
 
    var     divLeftPanel                            = document.querySelector("#leftPanel"),
            divRightPanel                           = document.querySelector("#rightPanel"),
            snackbar                                = document.querySelector("#snackbar"),
            sidebarLeft                             = divLeftPanel.querySelector("#sidebarLeft"),
            sidebarLeftReceivedBtn                  = divLeftPanel.querySelector("#sidebarLeftReceivedBtn"),
            sidebarLeftSentBtn                      = divLeftPanel.querySelector("#sidebarLeftSentBtn"),
            sidebarLeftDraftBtn                     = divLeftPanel.querySelector("#sidebarLeftDraftBtn"),
            sidebarLeftBinBtn                       = divLeftPanel.querySelector("#sidebarLeftBinBtn"),
            sidebarLeftSpamBtn                      = divLeftPanel.querySelector("#sidebarLeftSpamBtn"),
            sidebarLeftOpenCloseMenuBtn             = divLeftPanel.querySelector("#sidebarLeftOpenCloseMenuBtn"),
            btnGetEmails                            = divRightPanel.querySelector("#btn_get_emails"),
            btnWriteEmail                           = divRightPanel.querySelector("#btn_write_email"),
            btnDeleteEmail                          = divRightPanel.querySelector("#btn_delete_email"),
            btnRaportSpam                           = divRightPanel.querySelector("#btn_raport_spam"),
            btnPreviousPage                         = divRightPanel.querySelector("#previousPageBtn"),
            btnNextPage                             = divRightPanel.querySelector("#nextPageBtn"),
            dropdownMoveToButtons                   = divRightPanel.querySelectorAll("#moveEmailToFolderBtn a"),
            divEmailsTable                          = divRightPanel.querySelector("#sector_emails_table"),
            divSendEmail                            = divRightPanel.querySelector("#sector_send_email"),
            divShowMessage                          = divRightPanel.querySelector("#sector_show_message"),
            btnMessageBackToList                    = divShowMessage.querySelector("#messageBackToListBtn"),
            btnMessageReplay                        = divShowMessage.querySelector("#messageReplayBtn"),
            btnMessageForward                       = divShowMessage.querySelector("#messageForwardBtn"),
            btnPreviousMessage                      = divShowMessage.querySelector("#previousMessageBtn"),
            btnNextMessage                          = divShowMessage.querySelector("#nextMessageBtn"),
            formSendEmail                           = divSendEmail.querySelector("#send_email_form"),
            inputFieldsForm                         = formSendEmail.querySelectorAll('input[type="text"], input[type="hidden"], iframe#message'),
            selectedTableRows                       = [],
            selectedEmails                          = new SelectedEmails("",[]),
            newMessages                             = [],
            lastMessageDate                         = 0,
            newMessageCounter                       = 0,
            currentFolder                           = "",
            foldersTable                            = [],
            formData                                = new FormData(),
            modalWindowConfirmDeletionFile          = document.querySelector("#confirmDeletionFile"),
            lastDisplayedEmail                      = {},
            currentDisplayEmail,
            blockDiv;

    const   HOST_APP_PATH                           = window.location.href,
            INFO_CONNECT_ERROR                      = "Nie można nawiązać połączenia z serwerem!",
            ANY_TABLE_ROWS_NO_SELECTED              = "Nie wybrano żadnych elementów.",
            RESPONSIVE_NAV_CSS_CLASS_NAME           = "responsive",
            MAKE_X_CSS_CLASS_NAME                   = "make-x-sign",
            FORM_ERROR_CSS_CLASS_NAME               = "form-error",
            SUCCESS_CSS_CLASS_NAME                  = "success",
            ERROR_CSS_CLASS_NAME                    = "error",
            SHOW_CSS_CLASS_NAME                     = "show",
            AUTO_SHOW_HIDE_CSS_CLASS_NAME           = "auto-show-hide",
            MANUAL_SHOW_CSS_CLASS_NAME              = "manual-show",
            MANUAL_HIDE_CSS_CLASS_NAME              = "manual-hide",
            JSON_FIELD_NAME_VALIDATION_ERRORS       = "validationErrors",
            JSON_FIELD_NAME_SUCCESS                 = "success",
            JSON_FIELD_NAME_ERROR                   = "error",
            NO_DATA_FIELD_NAME                      = "no_data_found",
            HTML_BLOCK_ID_OF_EMAILS_TABLE           = "sector_emails_table",
            HTML_BLOCK_ID_OF_EMAIL_SEND_FORM        = "sector_send_email",
            HTML_BLOCK_ID_OF_MESSAGE_SECTOR         = "sector_show_message",
            RECEIVED_FOLDER_NAME                    = "RECEIVED",
            SENT_FOLDER_NAME                        = "SENT",
            DRAFT_FOLDER_NAME                       = "DRAFT",
            BIN_FOLDER_NAME                         = "BIN",
            SPAM_FOLDER_NAME                        = "SPAM",
            JSON_FIELD_ID                           = "id",
            JSON_FIELD_USER                         = "user", 
            JSON_FIELD_USER_ID                      = "id", 
            JSON_FIELD_USER_EMAIL                   = "email",
            JSON_FIELD_USER_FULL_NAME               = "fullName",
            JSON_FIELD_SENDER                       = "sender",
            JSON_FIELD_RECIPIENT                    = "recipient",
            JSON_FIELD_ADDITIONAL_RECIPIENT         = "additionalRecipient",
            JSON_FIELD_TITLE                        = "title",
            JSON_FIELD_MESSAGE                      = "message",
            JSON_FIELD_ATTACHMENT_NAME              = "attachmentName",
            JSON_FIELD_FOLDER                       = "folder",
            JSON_FIELD_FLAG                         = "flag",
            JSON_FIELD_READING_CONFIRMATION         = "readingConfirmation",
            JSON_FIELD_STATUS                       = "status", 
            JSON_FIELD_CREATE_DATE                  = "createDate",
            TABLE_ROW_IDENTIFIER_HTML               = "row_checkbox",
            FLAG_IDENTIFIER_HTML                    = "flag_checkbox",
            ATTACHMENT_IDENTIFIER_HTML              = "attachment_checkbox",
            QUERY_PARAM_NAME_CHECK_SERVER           = "checkServer",
            COOKIE_NAME_SESSION_ID                  = "sessionId",
            IGNORE_FILE_NAME_OF_ATTACHMENT          = "text.html";
    
/*****************************************************************************
Set max height the #rightPanel **********************************************
*****************************************************************************/
	
//    document.querySelector("#mainPanel").style.height = 
//        window.innerHeight-(document.querySelector("header").offsetHeight+document.querySelector("footer").offsetHeight)+"px";
//    divRightPanel.style.maxHeight = (document.querySelector("footer").offsetTop - document.querySelector("header").offsetHeight - 60) +"px";
//    console.log(divRightPanel.offsetHeight - 60);
//    document.querySelector("#tableData").style.height = (divRightPanel.offsetHeight - 60) +"px";
/*****************************************************************************
Hide/show list of folders (the resolution less than 720px) *******************
*****************************************************************************/
	
    sidebarLeftOpenCloseMenuBtn.addEventListener("click", function(){
        sidebarLeft.classList.toggle(RESPONSIVE_NAV_CSS_CLASS_NAME);
        sidebarLeftOpenCloseMenuBtn.querySelector(".btn-icon").classList.toggle(MAKE_X_CSS_CLASS_NAME);
    }, false);
	
/*****************************************************************************
Display the html box indicated ***********************************************
*****************************************************************************/
    /**
     * The function hide all the main html sectors on the page, then it look for on base 
     * of the received parameter a html sectors to display.  
     * @param {string} name , default value 'sector_emails_table'
     * @returns none;
     */
    function displaySector(name){
        var nameSectors = name || HTML_BLOCK_ID_OF_EMAILS_TABLE,
            sectors = divRightPanel.querySelectorAll("div[id^='sector_']");
        sectors.forEach(function(sector){
            sector.style.display = "none";
        });
        sectors.forEach(function(sector){
            if(sector.id===nameSectors){
               sector.style.display = "block";
               return;
            }
        });
    }

/*****************************************************************************
Assigning events to top acction bar ******************************************
*****************************************************************************/
	
    btnGetEmails.addEventListener("click", function(e){
        showAnimationButtonGetEmails();
        getEmails(this, RECEIVED_FOLDER_NAME, true, true, true, true, true); 
    }, false);
    btnWriteEmail.addEventListener("click", function(e){
        displaySector(HTML_BLOCK_ID_OF_EMAIL_SEND_FORM);
        resetSelectedEmails();
    }, false);
    btnDeleteEmail.addEventListener("click", function(){
        removeSelectedRowTable(BIN_FOLDER_NAME);
    }, false);
    btnRaportSpam.addEventListener("click", function(){
        removeSelectedRowTable(SPAM_FOLDER_NAME);
    }, false);

/*****************************************************************************
Assigning events to bottom acction bar ***************************************
*****************************************************************************/

    function assignEventsToPaginationButton(links){
        btnPreviousPage.href = links[0].previous;
        btnNextPage.href = links[1].next;
        btnPreviousPage.onclick = function(e){
            e.preventDefault();
            getEmails(this, "", false, false, false, true, false); 
        };
        btnNextPage.onclick = function(e){
            e.preventDefault();
            getEmails(this, "", false, false, false, true, false); 
        };
    }
    
    dropdownMoveToButtons.forEach(function(singleBtn){
        singleBtn.onclick = function(e){
            console.log(this.getAttribute("folder"));
            removeSelectedRowTable(this.getAttribute("folder"));
        };
    });
    
/*****************************************************************************
Assigning events to all buttons of sector_show_message ***********************
*****************************************************************************/
	
    btnMessageBackToList.addEventListener("click", function(e){
        getEmails("", currentFolder);
    }, false); 
    btnMessageReplay.addEventListener("click", function(e){
        forwardOrReplyToEmail("reply");
        displaySector(HTML_BLOCK_ID_OF_EMAIL_SEND_FORM);
        resetSelectedEmails(); 
    }, false); 
    btnMessageForward.addEventListener("click", function(e){
        forwardOrReplyToEmail("forward");
        displaySector(HTML_BLOCK_ID_OF_EMAIL_SEND_FORM);
        resetSelectedEmails(); 
    }, false); 
    btnPreviousMessage.addEventListener("click", function(e){
        foldersTable.forEach(function(folder){
            if(folder.getName()===lastDisplayedEmail.folder){
                if(Object.keys(lastDisplayedEmail).length && lastDisplayedEmail.index<folder.getContent().length-1){
                    showMessage(folder.getContent()[lastDisplayedEmail.index+1]);
                    lastDisplayedEmail["index"] = lastDisplayedEmail.index+1;
                }
            }
        });
    }, false);
    btnNextMessage.addEventListener("click", function(e){
        if(Object.keys(lastDisplayedEmail).length && lastDisplayedEmail.index>0){
            foldersTable.forEach(function(folder){
                if(folder.getName()===lastDisplayedEmail.folder){
                    showMessage(folder.getContent()[lastDisplayedEmail.index-1]);
                    lastDisplayedEmail["index"] = lastDisplayedEmail.index-1;
                }
            });
        }
    }, false);
    
/*****************************************************************************
Showing info of process or errors in the message box *************************
*****************************************************************************/	
	
    function showInfo(cssClass, df){
        var list = snackbar.querySelector("ul");
            snackbar.className = cssClass;
        if(list){
            snackbar.removeChild(snackbar.querySelector("ul"));
        }
        snackbar.appendChild(df);
    }
	          
/*****************************************************************************
Assigning events to buttons of main menu  ************************************
*****************************************************************************/	          
                    
    [].forEach.call(sidebarLeft.children, function(button){
        if(button.hasAttribute("folder")){
            var folderName = button.getAttribute("folder");
            foldersTable.push(new Folder(folderName, "", [], {}));
            button.addEventListener("click", function(e){
                getEmails(this, "", false, false, false, true, false);
                rememberWhereIAm(this.getAttribute("folder"));
                addFolderNameToTableHeader(this);
                resetSelectedEmails();
                clearFormFields();
                activateButtonsOfDropdown();
            }, false);
        }
    });
	
/*****************************************************************************
Getting the emails list from database ****************************************
*****************************************************************************/	

    function getEmails(htmlElement, folderName, showInfoAboutNewMessageInSnackbar, noDisplayResult, 
                       addMoreItemToExistingTable, fetchDataFromDatabase, checkContentOfServer){
        var folderName = folderName || "",
            showInfoAboutNewMessageInSnackbar = showInfoAboutNewMessageInSnackbar || false,
            noDisplayResult = noDisplayResult || false,
            addMoreItemToExistingTable = addMoreItemToExistingTable || false,
            fetchDataFromDatabase = fetchDataFromDatabase || false,
            checkContentOfServer = checkContentOfServer || false,
            path;
        if(typeof arguments[0]==="object" && htmlElement.hasAttribute("folder")){
                folderName = htmlElement.getAttribute("folder");
        }
        path = window.location.href+"rest/emails/folder/"+folderName+"?"
               +QUERY_PARAM_NAME_CHECK_SERVER+"="+checkContentOfServer;
        if(htmlElement===btnPreviousPage){
            folderName = getFolderNameFromPath(btnPreviousPage.href);
            path = btnPreviousPage.href;
        }else if(htmlElement===btnNextPage){
            folderName = getFolderNameFromPath(btnNextPage.href);
            path = btnNextPage.href;
        }
        foldersTable.forEach(function(folderObj){
            if(folderObj.getName().toUpperCase()===folderName.toUpperCase()){	
                if(fetchDataFromDatabase){
                    AJAX({
                        type: "GET",
                        url: path,
                        headers: {
                            "If-none-match": folderObj.getETag(),
                            "Authorization": window.btoa(getCookie(COOKIE_NAME_SESSION_ID))
                        },
                        success: function(response, xhr){
                            if(xhr.status !== 304){
                                var arrEmailObjects = [],
                                    arrEmailsJson = JSON.parse(response, function(key, value){
                                                        if(key==="emails"){
                                                            var jsonTable = [];
                                                            value.forEach(function(email){
                                                                jsonTable.push(
                                                                    JSON.parse(email, function(k, v){
                                                                        if(k===JSON_FIELD_ID){
                                                                            return Number(v);
                                                                        };
                                                                        if(k===JSON_FIELD_CREATE_DATE){
                                                                            return new Date(v);
                                                                        };
                                                                        return v;
                                                                    })
                                                                );
                                                            })
                                                            return jsonTable;
                                                        }
                                                        return value;
                                                    });
                                if(NO_DATA_FIELD_NAME in arrEmailsJson.emails[0]){
                                    clearTableContents(divEmailsTable.querySelector("#tableData"));
                                    folderObj.setETag(xhr.getResponseHeader('etag'));
                                    folderObj.setContent([]);
                                    folderObj.setMetadata(arrEmailsJson.metadata);
                                }else{
                                    arrEmailsJson.emails.forEach(function(emailJson){
                                        arrEmailObjects.push(
                                            new Email(emailJson[JSON_FIELD_ID],
                                                      emailJson[JSON_FIELD_USER],
                                                      emailJson[JSON_FIELD_SENDER],
                                                      emailJson[JSON_FIELD_RECIPIENT],
                                                      emailJson[JSON_FIELD_ADDITIONAL_RECIPIENT],
                                                      emailJson[JSON_FIELD_TITLE],
                                                      Base64Unicode().decode(emailJson[JSON_FIELD_MESSAGE]),
                                                      emailJson[JSON_FIELD_ATTACHMENT_NAME],
                                                      emailJson[JSON_FIELD_FOLDER],
                                                      emailJson[JSON_FIELD_FLAG],
                                                      emailJson[JSON_FIELD_READING_CONFIRMATION],
                                                      emailJson[JSON_FIELD_STATUS],
                                                      emailJson[JSON_FIELD_CREATE_DATE]));
                                    })
                                    folderObj.setETag(xhr.getResponseHeader('etag'));
                                    folderObj.setContent(arrEmailObjects);
                                    folderObj.setMetadata(arrEmailsJson.metadata);
                                }
                            }
                            assignEventsToPaginationButton(folderObj.getMetadata().links);
                            displayContentOfFolder(folderObj, noDisplayResult, addMoreItemToExistingTable, showInfoAboutNewMessageInSnackbar);
                            displayInfoInBottomActionBar(folderObj.getMetadata());
                            if(htmlElement===btnGetEmails){
                                hidenAnimationButtonGetEmails();
                            }
                        },
                        failure: function(xhr){
                            hidenAnimationButtonGetEmails();
                            if(Object.keys(xhr.response).length > 0){
                                showInfoAboutProcess("Info: "+xhr.response[JSON_FIELD_NAME_ERROR]+" (code: "+xhr.status+")", true);
                                throw new Error(xhr.response[JSON_FIELD_NAME_ERROR]+" RESPONSE: "+xhr.response+", STATUS: "+xhr.status);
                            }else{
                                showInfoAboutProcess(INFO_CONNECT_ERROR, true); 
                                throw new Error(INFO_CONNECT_ERROR+" RESPONSE: "+xhr.response+", STATUS: "+xhr.status);
                            }
                        }
                    });
                }else{
                    displayContentOfFolder(folderObj, noDisplayResult, addMoreItemToExistingTable, showInfoAboutNewMessageInSnackbar);
                }
            }
        });
    }

    function getFolderNameFromPath(link){
        var name = "";
        for(var folderObj of foldersTable){
            if(link.includes(folderObj.getName().toUpperCase()) 
                    || link.includes(folderObj.getName().toLowerCase())){ 
                name = folderObj.getName();
                break;
            }
        };
        return name;
    }

    function displayContentOfFolder(folderObj, noDisplayResult, addMoreItemToExistingTable, showInfoAboutNewMessageInSnackbar){
        if(!noDisplayResult || currentFolder===RECEIVED_FOLDER_NAME){
            if(addMoreItemToExistingTable){
                var emailsToBeAdd = [];
                folderObj.getContent().forEach(function(emailObject){
                    if(emailObject.getStatus()==="NEW"){
                        emailsToBeAdd.push(emailObject);
                    }
                });
                addItemsToTable(emailsToBeAdd, false);
            }else{
                addItemsToTable(folderObj.getContent(), true);
            }
            displaySector(HTML_BLOCK_ID_OF_EMAILS_TABLE);
        }
        var arrEmailsIdToChangedStatus = [];
        folderObj.getContent().forEach(function(emailObject){
            if(emailObject.getStatus()==="NEW"){
                incrementNewMessageCounter();
                emailObject.setStatus("OLD");
                arrEmailsIdToChangedStatus.push(emailObject.getId());
            }           
        });
        if(showInfoAboutNewMessageInSnackbar){
            if(newMessageCounter>0){
                showInfoAboutProcess("Odebrano: "+newMessageCounter+" "+(newMessageCounter===1?"wiadomość.":"wiadomości."));
            }else{
                showInfoAboutProcess("Nie masz nowych wiadomości.");
            }
            if(arrEmailsIdToChangedStatus.length){
                emailChangeStatus(arrEmailsIdToChangedStatus);
            }
        }
        resetNewMessageCounter();
    }

    function displayInfoInBottomActionBar(metadata){
        var pagesText = "strona: "+metadata.currentPage+"/"+metadata.allPages,
            counterText = "wiadomości: "+metadata.totalCount,
            infoBlock = divRightPanel.querySelector(".pagination-info");
        infoBlock.querySelector(".info-part-1").innerHTML = counterText;
        infoBlock.querySelector(".info-part-2").innerHTML = pagesText;
    }

    function activateButtonsOfDropdown(){
        dropdownMoveToButtons.forEach(function(button){
            if(button.getAttribute("folder").toUpperCase()===currentFolder){
                button.style.display = "none";
            }else{
                button.style.display = "block";
            }
        });
    }

    function deleteEmail(flagPermanentDelete){
        var emailsTable = divEmailsTable.querySelector("#tableData"),
            folderName = selectedEmails.getFolderName(),
            result = [],
            processedItems = 0,
            ids = "",
            flagPermanentDelete = flagPermanentDelete || false;
        selectedEmails.getEmailsId().forEach(function(item){
            ids+=item.id+",";
        });
        AJAX({
            type: "PUT",
            url: window.location.href+"rest/emails/delete/group;ids="+ids.slice(0, ids.length-1)+";permanent="+flagPermanentDelete,
            headers: {
                'Authorization': window.btoa(getCookie(COOKIE_NAME_SESSION_ID))  
            },
            success: function(response, xhr){
                result = JSON.parse(response, function(key, value){
                            if(key==="deleted")
                                return value==="true";
                            return value;
                         });
                result.forEach(function(item){
                    if(item.deleted===true){
                        selectedEmails.changeEmailIdStatusById(item.id);
                        emailsTable.removeChild(emailsTable.querySelector("div[id='"+folderName+"_"+item.id+"']"));
                    }
                });      
                selectedEmails.getEmailsId().forEach(function(item){
                    if(item.status!=="new") processedItems++;
                });
                if((selectedEmails.getCount()-processedItems)===0){
                    showInfoAboutProcess("Usunięto: "+processedItems+" "+(processedItems===1?"wiadomość.":"wiadomości."));
                    resetSelectedEmails();
                }else{
                    showInfoAboutProcess("Usunięto: "+processedItems+" "
                            +((selectedEmails.getCount()-processedItems)===1?"wiadomość.":"wiadomości.")
                            +" Nie udało się usunąć "+(selectedEmails.getCount()-processedItems)+" "
                            +((selectedEmails.getCount()-processedItems)===1?"wiadomość.":"wiadomości."));
                }
            },
            failure: function(xhr){
                console.log("Usunąłem! Błąd");
                var jsonResponseError = JSON.parse(xhr.response);
                showInfoAboutProcess(jsonResponseError[JSON_FIELD_NAME_ERROR]+" (code: "+xhr.status+")", true);
                throw new Error(jsonResponseError[JSON_FIELD_NAME_ERROR]+" RESPONSE: "+xhr.response+", STATUS: "+xhr.status);
            }
        });
    }
	
    function addItemsToTable(emailObjects, createNewTable){
        var tableData = divEmailsTable.querySelector("#tableData"),
            documentFragment = document.createDocumentFragment(),
            newRowTable, lastRowTable;
        emailObjects.forEach(function(emailObject, index){
            newRowTable = createTableRow(emailObject);
            addEventsToNewRowTable(newRowTable, emailObject.getFolder());
            index===0? documentFragment.appendChild(newRowTable): documentFragment.insertBefore(newRowTable, lastRowTable);		
            lastRowTable = newRowTable;
        });
        if(createNewTable){ 
            clearTableContents(tableData);
            tableData.appendChild(documentFragment);
        }else{
            tableData.insertBefore(documentFragment, tableData.firstElementChild);
        }
    }
    
    function emailChangeStatus(ids){
        if( ! (Array.isArray(arguments[0]))){
            console.error("Method emailChangeStatus(): invalid type of parameter. Required {array} ids!");
            return;
        }
        var groupIds = "";
        ids.forEach(function(id){
            groupIds += id+",";
        })
        AJAX({
            type: "PUT",
            url: window.location.href+"rest/emails/status;ids="+groupIds.slice(0, groupIds.length-1),
            headers: {
                'Authorization': window.btoa(getCookie(COOKIE_NAME_SESSION_ID))
            },
            success: function(response, xhr){
                var result = JSON.parse(response);
                result.forEach(function(item){
                    if(item.changed==="false"){
                        console.error("Dla id=" + item.id + " zmiana statusu nie powiodła się.");
                    }
                });
                
            },
            failure: function(xhr){
//              var jsonResponseError = JSON.parse(xhr.response);
//		showInfoAboutProcess(jsonResponseError[JSON_FIELD_NAME_ERROR]+" (code: "+xhr.status+")", true);
//		throw new Error(jsonResponseError[JSON_FIELD_NAME_ERROR]+" RESPONSE: "+xhr.response+", STATUS: "+xhr.status);
            }
        });
    }
    
    function clearTableContents(table){
	table.innerHTML = "";
    }
	
    function createTableRow(emailObject){
        blockDiv = document.createElement("div"); 
        blockDiv.setAttribute("id", emailObject.getFolder()+"_"+emailObject.getId());
        blockDiv.setAttribute("class", "table-row set-display-flex");  
        if(emailObject.getReadingConfirmation()==="NO") blockDiv.classList.add("row-bold");
        blockDiv.innerHTML = "<div class='table-section-checkbox'>"
                            +	"<label class='cell-checkbox-email column-checkbox'>"
                            +		"<input id='"+TABLE_ROW_IDENTIFIER_HTML+"_"+emailObject.getId()+"' type='checkbox'>"
                            +		"<span class='checkmark'></span>"
                            +	"</label>"
                            +	"<label class='cell-flag-email column-flag'>"
                            +		"<input id='"+FLAG_IDENTIFIER_HTML+"_"+emailObject.getId()+"' type='checkbox' "+(emailObject.getFlag()==="YES"?"checked":"")+">"
                            +		"<i class='fa fa-flag'></i>"
                            +		"<i class='far fa-flag'></i>"
                            +	"</label>"
                            +	"<div class='cell-attachment-email column-attachment'>"
                            +		"<input id='"+ATTACHMENT_IDENTIFIER_HTML+"_"+emailObject.getId()+"' type='checkbox' "+(emailObject.getAttachmentName()!=""?"checked":"")+" disabled>"
                            +		"<i class='fa fa-paperclip'></i>"
                            +	"</div>"
                            +"</div>"
                            +"<div id='"+emailObject.getId()+"' class='table-section-text'>"
                            +	"<div class='cell-from-email column-from'>"
                            +		(emailObject.getFolder()===SENT_FOLDER_NAME?emailObject.getRecipient():emailObject.getSender())
                            +	"</div>"
                            +	"<div class='cell-title-email column-title'>"
                            +		emailObject.getTitle()
                            +	"</div>"
                            +	"<div class='cell-date-email column-date'>"
                            +		emailObject.getCreateDate().toLocaleString()
                            +	"</div>"
                            +"</div>";
        return blockDiv;
    };
	
    function addEventsToNewRowTable(div, folderName){
        div.childNodes[1].addEventListener("click", findMessage, false); /*element div.table-section-text*/
        div.firstElementChild.children[0].firstElementChild.onclick = function(e){ collectSelectedEmails(this, folderName); }; /*element input#row_checkbox_*/
        div.firstElementChild.children[1].firstElementChild.onclick = function(e){ changeFlag(e.target); }; /*element input#flag_checkbox_*/
    }
    
    function findMessage(){
        var     parent = this.parentNode,
                rowNumberId = parseInt(this.id);
        const   START_NAME_BTN = "wróć do "
        foldersTable.forEach(function(folder){
            if(folder.getName().toUpperCase()===parent.id.substr(0, parent.id.indexOf("_")).toUpperCase()){
                btnMessageBackToList.innerHTML = START_NAME_BTN + divEmailsTable.querySelector("#tableName").innerText;
                folder.getContent().forEach(function(emailObject, index){
                    if(emailObject.getId()===rowNumberId){
                        lastDisplayedEmail["index"] = index;
                        lastDisplayedEmail["folder"] = folder.getName();
                        showMessage(emailObject);
                        return;
                    }	
                });
            }
        });
    }

    function showMessage(emailObject){
        var df = document.createDocumentFragment(),
            spanInfo = document.createElement("span"),
            attachment =  divShowMessage.querySelector("#message_attachment"),
            info = attachment.querySelector("#attachmentInfo"),
            files = attachment.querySelector("#attachmentFiles"),
            nameFiles = [], 
            divSingleFile, aLinkShow, aLinkSave;
        currentDisplayEmail = emailObject;
	divShowMessage.querySelector("#message_from").textContent = emailObject.getSender();
        divShowMessage.querySelector("#message_recipient").textContent = emailObject.getRecipient();
        divShowMessage.querySelector("#message_addictional_recipient").textContent = emailObject.getAdditionalRecipient();
        divShowMessage.querySelector("#message_title").textContent = emailObject.getTitle();
        divShowMessage.querySelector("#message_date").textContent = emailObject.getCreateDate().toLocaleString();
        divShowMessage.querySelector("#message_text").innerHTML = emailObject.getMessage();
        attachment.classList.remove("display");
        if(emailObject.getAttachmentName().length){
            nameFiles = emailObject.getAttachmentName().split(/; ?/);
            var size = nameFiles.length;
            if(size === 1){
                spanInfo.innerHTML = size+" załącznik";
            }else if(size > 1 && size < 5){
                spanInfo.innerHTML = size+" załączniki";
            }else{
                spanInfo.innerHTML = size+" załączników";
            }
            info.innerHTML = "";
            info.appendChild(spanInfo);
            files.innerHTML = "";
            nameFiles.forEach(function(name){
                divSingleFile = document.createElement("div");
                divSingleFile.classList.add("single-file");
                aLinkShow = document.createElement("a");
                aLinkShow.id = "show";
                aLinkShow.href = "app/files?userId="+emailObject.getUser()[JSON_FIELD_USER_ID]+"&messageId="+emailObject.getId()+"&fileName="+name;
                aLinkShow.target = "_blank";
                aLinkShow.title = "Pokaż";
                aLinkShow.innerHTML = name;
                divSingleFile.appendChild(aLinkShow);
                aLinkSave = document.createElement("a");
                aLinkSave.id = "save";
                aLinkSave.href = "app/files?userId="+emailObject.getUser()[JSON_FIELD_USER_ID]+"&messageId="+emailObject.getId()+"&fileName="+name+"&save=true";
                aLinkSave.title = "Zapisz";
                aLinkSave.innerHTML = "<i class='fas fa-download'></i>";
                divSingleFile.appendChild(aLinkSave);
                df.appendChild(divSingleFile);
            });
            files.appendChild(df);
            attachment.classList.add("display");
        }
        displaySector(HTML_BLOCK_ID_OF_MESSAGE_SECTOR);
        changeReadingConfirmationStatus(emailObject.getId());
        resetSelectedEmails();
    }
	
    function changeReadingConfirmationStatus(id, status){
        var statusValue = status || "YES", 
            path = window.location.href+"rest/emails/"+id+"/read";
        AJAX({
            type: "POST",
            url: path,
            data: {
                status: statusValue
            },
            headers: {
                'Content-Type' : 'application/x-www-form-urlencoded',
                'Authorization': window.btoa(getCookie(COOKIE_NAME_SESSION_ID))
            },
            success: function(response, xhr){},
            failure: function(xhr){
//              var jsonResponseError = JSON.parse(xhr.response);
//		showInfoAboutProcess(jsonResponseError[JSON_FIELD_NAME_ERROR]+" (code: "+xhr.status+")", true);
//		throw new Error(jsonResponseError[JSON_FIELD_NAME_ERROR]+" RESPONSE: "+xhr.response+", STATUS: "+xhr.status);
            }
        });
    }
	
    function resetSelectedEmails(){
        selectedEmails.setFolderName("");
        selectedEmails.removeAllEmailsId();
    }
	
    function changeFlag(target){
        var id = target.id.substr(14, target.id.length),
            value = target.checked?"YES":"NO",
            path = window.location.href+"rest/emails/"+id+"/flag";
        AJAX({
            type: "PUT",
            url: path,
            headers: {
                "Authorization": window.btoa(getCookie(COOKIE_NAME_SESSION_ID))
            },
            success: function(response, xhr){
                if(!(response===value)){
                    target.checked = response==="YES"?true:false;
                }
            },
            failure: function(xhr){
                var jsonResponseError = JSON.parse(xhr.response);
                showInfoAboutProcess(jsonResponseError[JSON_FIELD_NAME_ERROR]+" (code: "+xhr.status+")", true);
                throw new Error(jsonResponseError[JSON_FIELD_NAME_ERROR]+" RESPONSE: "+xhr.response+", STATUS: "+xhr.status);
            }
        });
    }
	
    function collectSelectedEmails(inputField, folderName){
        var inputFieldId = inputField.id.substr(13,inputField.id.length);
        if(inputField.checked){
            selectedEmails.setFolderName(folderName);
            selectedEmails.addEmailId([inputFieldId]);
        }else{
            selectedEmails.removeEmailIdById(inputFieldId);
        }
        console.log(selectedEmails.toString());
    }
	
    function removeSelectedRowTable(destinationFolder){
        if(selectedEmails.getCount()===0){
            showInfoAboutProcess(ANY_TABLE_ROWS_NO_SELECTED);
        }else{
            if(selectedEmails.getFolderName()===BIN_FOLDER_NAME && destinationFolder===BIN_FOLDER_NAME){
                displayModalWindowConfirmDeletionFile();
            }else{
                moveToFolder(destinationFolder);
            }
        }
    }
    
    divEmailsTable.querySelector("#tableHeaderCheckbox").onclick = function(){
        divEmailsTable.querySelectorAll("input[id^='row_checkbox_']").forEach(function(input){
            input.click();
        });
    }
    
    function displayModalWindowConfirmDeletionFile(){
        modalWindowConfirmDeletionFile.querySelectorAll(".counter").forEach(function(span){
            span.innerHTML = "";
            span.innerHTML = selectedEmails.getCount();
        });
        modalWindowConfirmDeletionFile.style.display = "block";
    }
    
    function hideModalWindowConfirmDeletionFile(){
        modalWindowConfirmDeletionFile.style.display = "none";
    }
    
    function assignEventsToElementsInModalWindow(){
        modalWindowConfirmDeletionFile.querySelector(".close").onclick = function(){
            hideModalWindowConfirmDeletionFile();
        };
        modalWindowConfirmDeletionFile.querySelector("#cancelDeletionFileBtn").onclick = function(){
            hideModalWindowConfirmDeletionFile();
        };
        modalWindowConfirmDeletionFile.querySelector("#agreeDeletionFileBtn").onclick = function(){
            var permanentDelete = modalWindowConfirmDeletionFile.querySelector("#permanentDelete").checked;
            deleteEmail(permanentDelete);
            hideModalWindowConfirmDeletionFile();
        };
        window.onclick = function(e){
            if(event.target === modalWindowConfirmDeletionFile){
                hideModalWindowConfirmDeletionFile();
            }
        };
    }

    function moveToFolder(destinationFolder){
        var emailsTable = divEmailsTable.querySelector("#tableData"),
            folderName = selectedEmails.getFolderName(),
            result = [],
            processedItems = 0,
            ids = "";
        if(destinationFolder===folderName) return;
        selectedEmails.getEmailsId().forEach(function(item){
            ids+=item.id+","
        });
        AJAX({
            type: "PUT",
            url: window.location.href+"rest/emails/move/"+destinationFolder+";ids="+ids.slice(0, ids.length-1),
            headers:{
                "Authorization": window.btoa(getCookie(COOKIE_NAME_SESSION_ID))
            },
            success: function(response){
                result = JSON.parse(response, function(key, value){
                            if(key==="moved")
                                return value==="true";
                            return value;
                         });
                result.forEach(function(item){
                    if(item.moved===true){
                        selectedEmails.changeEmailIdStatusById(item.id);
                        emailsTable.removeChild(emailsTable.querySelector("div[id='"+folderName+"_"+item.id+"']"));
                    }
                });      
                selectedEmails.getEmailsId().forEach(function(item){
                    if(item.status!=="new") processedItems++;
                });
                var msgDestinationFolderName = sidebarLeft.querySelector("[folder='"+destinationFolder.toUpperCase()+"']").innerText.toUpperCase();//destinationFolder===BIN_FOLDER_NAME? "Kosza": "Spam";
                if((selectedEmails.getCount()-processedItems)===0){
                    showInfoAboutProcess("Przeniesiono do "+msgDestinationFolderName+": "+processedItems+" "+(processedItems===1?"wiadomość.":"wiadomości."));
                    resetSelectedEmails();
                }else{
                    showInfoAboutProcess("Przeniesiono do "+msgDestinationFolderName+": "+processedItems+" "
                            +((selectedEmails.getCount()-processedItems)===1?"wiadomość.":"wiadomości.")
                            +" Nie udało się przenieść "+(selectedEmails.getCount()-processedItems)+" "
                            +((selectedEmails.getCount()-processedItems)===1?"wiadomość.":"wiadomości."));
                }
            },
            failure: function(xhr){
                var jsonResponseError = "";
                if(Object.keys(xhr.response).length){
                    console.dir(xhr.response);
                    jsonResponseError = JSON.parse(xhr.response);
                    showInfoAboutProcess(jsonResponseError[JSON_FIELD_NAME_ERROR]+" (code: "+xhr.status+")", true);
                }else{
                    jsonResponseError = xhr.response;
                    showInfoAboutProcess(jsonResponseError+" (code: "+xhr.status+")", true);
                }
                throw new Error(jsonResponseError[JSON_FIELD_NAME_ERROR]+" RESPONSE: "+xhr.response+", STATUS: "+xhr.status);
            }
        });
    }
	
    function incrementNewMessageCounter(){
        newMessageCounter++;
    }
	
    function resetNewMessageCounter(){
	newMessageCounter = 0;
    }
    
    /**
     * It's setting global variable 'currentFolder'.
     * @param {string} folderName , the name of the folder that is going to be assigned to the global variable.
     * @returns none
     */
    function rememberWhereIAm(folderName){
        currentFolder = folderName.toUpperCase();
    }

    /**
     * It's setting the inner text of html element about id "#tableName".
     * If a passed value to 'sidebarElement' is not a element html, it will be used the value of second parameter.
     * In case of no content both of parameters it'll be used default value 'UNKNOWN'.
     * @param {object} sidebarElement , one of elements html list about id 'sidebarLeft' 
     * @param {string} folderName , the name of folder 
     * @returns {undefined}
     */
    function addFolderNameToTableHeader(sidebarElement, folderName){
        var name = "";
        if(typeof sidebarElement==="object"){
            name = sidebarElement.innerText;
        }else{
            if(folderName.length) name = folderName;
        }
        divEmailsTable.querySelector("#tableName").innerHTML = name || 'UNKNOWN';
    }
    
    function showAnimationButtonGetEmails(){
        document.querySelector("#btn_get_emails i").style.visibility = "visible";
    }
    
    function hidenAnimationButtonGetEmails(){
        document.querySelector("#btn_get_emails i").style.visibility = "hidden";
    }
    
    function showInformationAboutSendingEmail(){
        
    }
/*****************************************************************************
Sending a email **************************************************************
*****************************************************************************/
	
    formSendEmail.addEventListener("submit", function(e){
        e.preventDefault();
        var path = window.location.href+"rest/emails/",
            count = 0,
            id;
        inputFieldsForm.forEach(function(field){
            console.dir(field);
            if(field.id === "message"){
                formData.append(field.id, field.contentDocument.querySelector("body").innerHTML);
            }else{
                formData.append(field.name, field.value);
            }
        });
        showInfoAboutProcess("Wysyłam...", false, true, "show");
        AJAX({
            type: this.getAttribute("method"),
            url: path+this.getAttribute("action"),
            data: formData,
            headers: {
                "Authorization": window.btoa(getCookie(COOKIE_NAME_SESSION_ID))
            },
            success: function(response, xhr){
                showInfoAboutProcess("", false, true, "hide");
                var jsonResponseSuccess = JSON.parse(response);
                getEmails("", "sent", false, false, false, true, false);
                addFolderNameToTableHeader(sidebarLeft.children[1]);
                clearFormFields();
            },
            failure: function(xhr){
                if(Object.keys(xhr.response).length > 0){
                    var jsonResponseError = JSON.parse(xhr.response);
                    if(JSON_FIELD_NAME_VALIDATION_ERRORS in jsonResponseError){
                        displayValidationErrors(inputFieldsForm, jsonResponseError[JSON_FIELD_NAME_VALIDATION_ERRORS]);
                    }else if(JSON_FIELD_NAME_ERROR in jsonResponseError){
                        showInfoAboutProcess(jsonResponseError[JSON_FIELD_NAME_ERROR]+" (code: "+xhr.status+")", true);
                        throw new Error(jsonResponseError[JSON_FIELD_NAME_ERROR]+" RESPONSE: "+xhr.response+", STATUS: "+xhr.status);
                    }
                    }else{
                        showInfoAboutProcess(INFO_CONNECT_ERROR+" (code: "+xhr.status+")", true);
                        throw new Error(INFO_CONNECT_ERROR+" RESPONSE: "+xhr.response+", STATUS: "+xhr.status);
                    }
                }
        });
    }, false);
    
    function clearFormFields(){
	inputFieldsForm.forEach(function(field){
            if(field.id!=="sender"){
		field.value = "";   
                if(field.tagName==="IFRAME") field.contentDocument.querySelector("body").innerHTML = "";
            }
	});
    }	

    function displayValidationErrors(inputFields, jsonErrors){
	var     formError;
        const   KEY_NAME = "ErrorBlock";
        inputFields.forEach(function(field){
            removeValidationErrorClassAndFieldContent(field, ERROR_CSS_CLASS_NAME);
            for(var key in jsonErrors){
                if(field.name === key){
                    field.classList.add(ERROR_CSS_CLASS_NAME);
                    try{
                        formError = field.parentElement
                                .querySelector("#"+field.id+KEY_NAME+"."+FORM_ERROR_CSS_CLASS_NAME);
                        formError.classList.add(ERROR_CSS_CLASS_NAME);
                        formError.innerHTML = jsonErrors[key];
                    }catch(err){}
                }
            }
	});
    }
	
    function removeValidationErrorClassAndFieldContent(field, removingClass){
        var     formError;
        const   KEY_NAME = "ErrorBlock";
        field.classList.remove(removingClass);
        try{
            formError = field.parentElement
                    .querySelector("#"+field.id+KEY_NAME+"."+FORM_ERROR_CSS_CLASS_NAME);
            formError.classList.remove(removingClass);
            formError.innerHTML = "";
        }catch(err){}
    }
    
    formSendEmail.querySelector("iframe#message").onload = function(){
        var iframeElement = this;
        iframeElement.contentDocument.querySelector("body.scrollHide").oninput = function(e){
            if(iframeElement.offsetHeight!==this.offsetHeight){
                iframeElement.style.height = this.offsetHeight+"px";
            }
        };
    };
    
    formSendEmail.querySelector("#attachments").onchange = function(e){
        var formFieldIdNewFile = formSendEmail.querySelector("#newFile"),
            count = 0, tabKeys = [], inputId = this.id;
        if(this.files.length){
            formFieldIdNewFile.innerHTML = "";
            clearContentOfFormData();
            [].forEach.call(this.files, function(file){
                var key = inputId + count++;
                formFieldIdNewFile.innerHTML += createHtmlElementAttachmentFileCard(key, file.type, file.name, file.size, file.lastModifiedDate);
                formData.append(key, file);
            });
        }
        addMouseEventToAllFilesCard();
        showInfoAboutCountOfFiles();
    };
    
    function addCardsAttachments(fileNames){
        var formFieldIdPreviousFile = formSendEmail.querySelector("#previousFile"),
            count = 0;
        formFieldIdPreviousFile.innerHTML = "";
        fileNames.split(/; ?/).forEach(function(fileName){
            if(fileName.length>0){
                var key = "previousFile" + count++;
                formFieldIdPreviousFile.innerHTML += createHtmlElementAttachmentFileCard(key, "", fileName);
            }
        });
        addMouseEventToAllFilesCard();
        showInfoAboutCountOfFiles();
    }
    
    function createHtmlElementAttachmentFileCard(elementId, type, fileName, size, modifyDate){
        return  "<div id='"+elementId+"'class='col-12xs col-6s col-4l col-3lg card'>"
               +"  <div class='file'>"
               +"    <div class='close'>&times;</div>"
               +"    <div class='file-header'>"
               +"       <p class='file-type'><i class='fas fa-file'></i>"+(type!==""?type.split(/\//)[1]:"plik")+"</p>"
               +"    </div>"
               +"    <div class='file-body'>"                       
               +"      <p class='file-name'>"+fileName+"</p>"
               +"      <p class='file-size'>"+(isFinite(size)?Math.round(size/1024)+" KB":"brak dnych")+"</p>"
               +"      <p class='file-date'>"+(modifyDate?new Date(modifyDate).toLocaleString():new Date().toLocaleString())+"</p>"
               +"    </div>"
               +"  </div>"
               +"</div>"
    }
    
    function addMouseEventToAllFilesCard(){
        var cards = formSendEmail.querySelectorAll(".attachment-files-list .card"),
            attachmentName = formSendEmail.querySelector("#attachmentName");
        cards.forEach(function(card){
            card.querySelector(".close").onclick = function(e){
                var elementId = this.id,
                    fileName = this.querySelector(".file-name").textContent;
                this.classList.add("hide");
                setTimeout(function(){
                    this.style.display = "none";
                    this.parentNode.removeChild(this.parentNode.querySelector("#"+elementId));
                }.bind(this),700);
                if(formData.has(elementId)){ 
                    formData.delete(elementId); 
                    showInfoAboutCountOfFiles();
                }
                if(attachmentName.value.indexOf(fileName)!==-1){
                    var stringNames = attachmentName.value.replace(fileName+"; ", "");
                    stringNames = stringNames.replace(fileName, "");
                    console.log(stringNames);
                    attachmentName.value = stringNames;
                    showInfoAboutCountOfFiles();
                }
            }.bind(card);
        });
    }
    
    function showInfoAboutCountOfFiles(){
        var attachmentsBtn = formSendEmail.querySelector("#attachmentsBtn"),
            attachmentName = formSendEmail.querySelector("#attachmentName"),    
            count = 0, info = "";
        for(var key of formData.keys()){
            count++;
        };
        if(attachmentName.value.length){
            attachmentName.value.split(/; ?/).forEach(function(fileName){
                if(fileName.length>0){count++;}
            })
        }
        if(count===0){
            info = "Wybierz i załącz pliki.";
        }else if(count===1){
            info = "Dołączono do wiadomości "+count+" plik.";
        }else if(count>1 && count<5){
            info = "Dołączono do wiadomości "+count+" pliki.";
        }else if(count>4){
            info = "Dołączono do wiadomości "+count+" plików.";
        }
        attachmentsBtn.textContent = info;
    }
    
    function clearContentOfFormData(){
        var tabKeys = [];
        for(var key of formData.keys()){
            tabKeys.push(key);
        };
        tabKeys.forEach(function(key){
            formData.delete(key);
        });
    }
/*****************************************************************************
Forwarding or replying an email **********************************************
*****************************************************************************/
    
    function forwardOrReplyToEmail(type){
        const REPLY_START_TEXT_TITLE = "Odp: ",
              FORWARD_START_TEXT_TITLE = "Pd: ";
        divSendEmail.querySelector("#id").value = currentDisplayEmail.getId();
        divSendEmail.querySelector("#sender").value = currentDisplayEmail.getRecipient();
        if(type==="reply"){
            divSendEmail.querySelector("#recipient").value = currentDisplayEmail.getSender();
            divSendEmail.querySelector("#additionalRecipient").value = currentDisplayEmail.getAdditionalRecipient();
            divSendEmail.querySelector("#title").value = REPLY_START_TEXT_TITLE+currentDisplayEmail.getTitle();
        }else if(type==="forward"){
            divSendEmail.querySelector("#title").value = FORWARD_START_TEXT_TITLE+currentDisplayEmail.getTitle();
        }
        divSendEmail.querySelector("#message").contentDocument.querySelector("body").innerHTML = addHeaderMessage(currentDisplayEmail.getMessage());
        var stringAttachmentsNames = currentDisplayEmail.getAttachmentName().replace(IGNORE_FILE_NAME_OF_ATTACHMENT+"; ","");
        stringAttachmentsNames = stringAttachmentsNames.replace(IGNORE_FILE_NAME_OF_ATTACHMENT,"");;
        divSendEmail.querySelector("#attachmentName").value = stringAttachmentsNames;
        addCardsAttachments(stringAttachmentsNames);
    }
    
    function addHeaderMessage(insideText){
        return  "<br> <br>"
              + "<div style='border-left: 2px black solid; padding-left: 10px; width: 100%; overflow: auto;'>"
              + " <p>Dnia "+currentDisplayEmail.getCreateDate().toLocaleString()+" "+currentDisplayEmail.getSender()+" napisał(a): </p>"
              + " <div style='width: 100%;'>"+insideText+"</div>"
              + "</div>";
    }
    
/*****************************************************************************
Handling a info-bar **********************************************************
*****************************************************************************/
    /**
     * The function displays a message on the bottom part of page in the snackbar.
     * @param {String}  message , the text message to display
     * @param {Boolean} isError , it indicates if a message is an error (true) or a information (false). 
     *                  Default value is false.
     * @param {Boolean} manualShowHideSnackbar , does the snackbar manual show and hide.
     *                  Default value is false.
     * @param {String}  workingMode , if you set 'manualShowHideSnackbar' on 'true', you have to set this parameter. 
     *                  You are using the 'show' value, if you want to show the snackbar, or the 'hide' value, 
     *                  if you want to hide the one. 
     * @returns none
     */
    function showInfoAboutProcess(message, isError, manualShowHideSnackbar, workingMode){
	var     error                   = isError || false,
                manualShowHideSnackbar  = manualShowHideSnackbar || false;
        const   ERROR_TXT_COLOR = "red",
                INFO_TXT_COLOR  = "white";
	if(manualShowHideSnackbar && workingMode.length){
            if(workingMode==="show"){
                snackbar.innerHTML = message;
                snackbar.style.color = error?ERROR_TXT_COLOR: INFO_TXT_COLOR;
                snackbar.classList.add(MANUAL_SHOW_CSS_CLASS_NAME);
            }else if(workingMode==="hide"){
                snackbar.classList.remove(MANUAL_SHOW_CSS_CLASS_NAME);
                snackbar.classList.add(MANUAL_HIDE_CSS_CLASS_NAME);
                snackbar.style.color = INFO_TXT_COLOR;
                snackbar.innerHTML = "";
                setTimeout(function(){
                    snackbar.className = "";
                    snackbar.style.color = INFO_TXT_COLOR;
                    snackbar.innerHTML = "";
                }, 500);
            }
        }else{
            snackbar.innerHTML = message;
            snackbar.style.color = error?ERROR_TXT_COLOR: INFO_TXT_COLOR;
            snackbar.classList.add(AUTO_SHOW_HIDE_CSS_CLASS_NAME);
            setTimeout(function(){
		snackbar.className = "";
		snackbar.style.color = INFO_TXT_COLOR;
		snackbar.innerHTML = "";
            }, 3000);
        }
    }

/*****************************************************************************
Handling a Cookies ***********************************************************
*****************************************************************************/

    function getCookie(name){
        if(!document.cookie){
            return null;
        }
        var arrCookies = document.cookie.split(/; */),
            objCookies = {};
        arrCookies.forEach(function(cookie){
            cookie = cookie.split("=");
            objCookies[cookie[0]] = decodeURIComponent(cookie[1]);
        });
        return objCookies[name] || null; 
    }

/*****************************************************************************
Others ***********************************************************************
*****************************************************************************/

    function addEventToButtons(fields, event, fn, cssClass){
	fields.forEach(function(field){
            field.addEventListener(event, function(e){
		fn(e.target, cssClass);
            });
	});
    }

/*****************************************************************************
Run functions at start *******************************************************
*****************************************************************************/

    getEmails("", RECEIVED_FOLDER_NAME, false, false, false, true, false);
    rememberWhereIAm(RECEIVED_FOLDER_NAME);
    addFolderNameToTableHeader(sidebarLeft.children[0]);
    displaySector();
    addEventToButtons(inputFieldsForm, "focus", removeValidationErrorClassAndFieldContent, ERROR_CSS_CLASS_NAME);	
    assignEventsToElementsInModalWindow();
    activateButtonsOfDropdown();
})();
