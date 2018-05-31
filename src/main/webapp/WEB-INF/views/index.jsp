<%-- 
    Document   : index
    Created on : 2018-04-11, 14:43:33
    Author     : mirek
--%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Poczta</title>
    <link rel="icon" type="image/ico" href="<c:url value='/resources/favicon/envelope.ico'/>">
    <link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Raleway">
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/layout.css'/>">
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/header.css'/>">
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/footer.css'/>">
    <link rel="stylesheet" type="text/css" href="<c:url value='/resources/css/index.css'/>">
    <link href="https://use.fontawesome.com/releases/v5.0.8/css/all.css" rel="stylesheet">
<body>
    <jsp:include page="header.jsp"/>
    
    <div id="mainPanel" class="container-variable-width">
            <div class="row"> 
                    <div id="leftPanel" class="col-12xs col-2xm">
                        <ul id="sidebarLeft" class="list-group">
                            <li id="sidebarLeftReceivedBtn"
                                class="list-item drop-down-item-0"
                                folder="RECEIVED">
                                    otrzymane
                            </li>
                            <li id="sidebarLeftSentBtn" 
                                class="list-item drop-down-item-1"
                                folder="SENT">
                                    wysłane
                            </li>
                            <li id="sidebarLeftDraftBtn" 
                                class="list-item drop-down-item-2"
                                folder="DRAFT">
                                    robocze
                            </li>
                            <li id="sidebarLeftBinBtn" 
                                class="list-item drop-down-item-3"
                                folder="BIN">
                                    kosz
                            </li>
                            <li id="sidebarLeftSpamBtn" 
                                class="list-item drop-down-item-4"
                                folder="SPAM">
                                    spam
                            </li>
                            <li id="sidebarLeftOpenCloseMenuBtn" class="icon">
                                <div class="btn-icon">
                                    <div class="line-icon rotate1"></div>
                                    <div class="line-icon rotate2"></div>
                                    <div class="line-icon rotate3"></div>
                                </div>
                                <span class="text-icon">
                                    foldery
                                </span>
                            </li>
                        </ul>
                    </div>
                    <div id="rightPanel" class="col-12xs col-10xm">
                            <div class="col-12xs">
                                <div class="empty-space"></div>
                            </div>
                            <div class="col-12xs">
                                <ul id="topActionBar" 
                                    class="action-bar">
                                    <li id="btn_get_emails" 
                                            class="list-item-mini">
                                            odbierz &nbsp;
                                            <i class="fa fa-spinner fa-spin"></i>
                                    </li>
                                    <li id="btn_write_email" 
                                            class="list-item-mini">
                                            napisz
                                    </li>
                                    <li id="btn_delete_email" 
                                            class="list-item-mini">
                                            usuń
                                    </li>
                                    <li id="btn_raport_spam" 
                                            class="list-item-mini">
                                            zgłoś spam
                                    </li>
                                </ul>
                            </div>
                            <div class="col-12xs">
                                    <div class="empty-space"></div>
                            </div>
                            <div class="col-12xs">
                                    <div id="sector_send_email">
                                        <form id="send_email_form" 
                                                  action="send" 
                                                  method="POST">
                                            <div>
                                                <input id="id" 
                                                       type="hidden" 
                                                       name="id">
                                            </div>
                                            <div class="row">
                                                <div class="col-12xs col-2s col-1l">
                                                    <label for="sender" 
                                                           class="form-label">
                                                        Od:
                                                    </label>
                                                </div>
                                                <div class="col-12xs col-10s col-11l">
                                                    <input id="sender"
                                                           class="form-input"
                                                           type="text" 
                                                           name="sender"
                                                           value="${sessionScope.user.emailAccount.emailName}"
                                                           readonly>
                                                    <div id="senderErrorBlock" 
                                                         class="form-error">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-12xs col-2s col-1l">
                                                    <label for="recipient" 
                                                           class="form-label">
                                                        Do:
                                                    </label>
                                                </div>
                                                <div class="col-12xs col-10s col-11l">
                                                    <input id="recipient"
                                                           class="form-input"
                                                           type="text" 
                                                           name="recipient">
                                                    <div id="recipientErrorBlock" 
                                                         class="form-error">
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="row">
                                                <div class="col-12xs col-2s col-1l">
                                                    <label for="additionalRecipient" 
                                                           class="form-label">
                                                        Dw:
                                                    </label>
                                                </div>
                                                <div class="col-12xs col-10s col-11l">
                                                    <input id="additionalRecipient"
                                                           class="form-input"
                                                           type="text" 
                                                           name="additionalRecipient">
                                                    <div id="additionalRecipientErrorBlock"
                                                         class="form-error">
                                                    </div>
                                                </div>		
                                            </div>
                                            <div class="row">
                                                <div class="col-12xs col-2s col-1l">
                                                    <label for="title" 
                                                           class="form-label">
                                                        Temat:
                                                    </label>
                                                </div>
                                                <div class="col-12xs col-10s col-11l">
                                                    <input id="title"
                                                           class="form-input"
                                                           type="text" 
                                                           name="title">
                                                    <div id="titleErrorBlock"
                                                         class="form-error">
                                                    </div>
                                                </div>		
                                            </div>                                            
                                            <div class="row">
                                                <div class="col-12xs form-input-iframe">
                                                    <iframe id="message"
                                                            src="<c:url value='/resources/other/message.html'/>"
                                                            frameborder="0"> 
                                                    </iframe>
                                                    <div id="messageErrorBlock"
                                                         class="form-error">
                                                    </div>
                                                </div>		
                                            </div>
                                            <div class="row">
                                                <div class="col-12xs">
                                                    <label id="attachmentsBtn"
                                                           for="attachments" 
                                                           class="form-input-file">
                                                        Wybierz i załącz pliki.                                                
                                                    </label>
                                                    <input id="attachments"
                                                           type="file" 
                                                           name="attachments"
                                                           style="display: none;"
                                                           multiple>
                                                    <div id="attachmentsErrorBlock"
                                                         class="form-error">
                                                    </div>
                                                </div>		
                                            </div>
                                            <div>
                                                <input id="attachmentName" 
                                                       type="hidden" 
                                                       name="attachmentName">
                                            </div>
                                            <div class="row">
                                                <div id="newFile" class="col-12xs attachment-files-list"></div>
                                            </div>
                                            <div class="row">
                                                <div id="previousFile" class="col-12xs attachment-files-list"></div>
                                            </div>                                            
                                            <div class="row">
                                                <div class="col-12xs">
                                                    <button class="form-btn" type="submit">
                                                        wyślij
                                                    </button>
                                                </div>
                                            </div>
                                        </form>
                                    </div>
                                    <div id="sector_show_message">
                                        <div class="row">
                                            <div class="col-12xs message-action-bar">
                                                <div class="col-12xs col-9s message-action-bar-left">
                                                    <button id="messageBackToListBtn" 
                                                            class="col-4xs message-button">
                                                        wróć do
                                                    </button>
                                                    <button id="messageReplayBtn" 
                                                            class="col-4xs message-button">
                                                        odpowiedz
                                                    </button>
                                                    <button id="messageForwardBtn" 
                                                            class="col-4xs message-button">
                                                        prześlij dalej
                                                    </button>
                                                </div>
                                                <div class="col-12xs col-3s message-action-bar-right">
                                                    <a id="previousMessageBtn" class="col-6xs message-button">
                                                        <i class="fas fa-chevron-left"></i>
                                                    </a>
                                                    <a id="nextMessageBtn" class="col-6xs message-button">
                                                        <i class="fas fa-chevron-right"></i>
                                                    </a>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div class="message-header">
                                                <div class="row">
                                                    <span class="message-title">
                                                            Od:
                                                    </span>
                                                    <span id="message_from" 
                                                              class="message-data">
                                                    </span>
                                                </div>
                                                <div class="row">
                                                    <span class="message-title">
                                                            Do:
                                                    </span>
                                                    <span id="message_recipient" class="message-data"></span>
                                                </div>
                                                <div class="row">
                                                    <span class="message-title">
                                                            Dw:
                                                    </span>
                                                    <span id="message_addictional_recipient" class="message-data"></span>
                                                </div>
                                                <div class="row">
                                                    <span class="message-title">
                                                            Tytuł:
                                                    </span>
                                                    <span id="message_title" class="message-data"></span>
                                                </div>
                                                <div class="row">
                                                    <span class="message-title">
                                                            Otrzymano:
                                                    </span>
                                                    <span id="message_date" class="message-data"></span>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="row">
                                            <div id="message_text" class="message-content"></div>
                                        </div>
                                        <div class="row">
                                            <div id="message_attachment" class="col-12xs">
                                                <div class="attachment-content">
                                                    <div id="attachmentInfo" class="col-4xs col-3s col2-xm"></div>
                                                    <div id="attachmentFiles" class="col-8xs col-9s col-10xm"></div>
                                                </div>
                                            </div>
                                        </div>
                                        <div class="col-12xs">
                                            <div class="empty-space"></div>
                                        </div>
                                    </div>				
                                    <div id="sector_emails_table">
                                        <div id="tableName"></div>
                                        <div id="table_header" class="set-display-flex">
                                            <div class="table-section-checkbox">
                                                <div id="tableHeaderCheckbox" class="column-checkbox">
                                                        <i class="fa fa-check"></i>
                                                </div>
                                                <div class="column-flag">
                                                        <i class="fa fa-flag"></i>
                                                </div>
                                                <div class="column-attachment">
                                                        <i class="fa fa-paperclip"></i>
                                                </div>
                                            </div>
                                            <div class="table-section-text">
                                                <div class="column-from">
                                                    <span>od:</span>
                                                </div>
                                                <div class="column-title">
                                                    <span>temat:</span>
                                                </div>
                                                <div class="column-date">
                                                    <span>odebrano:</span>
                                                </div>
                                            </div>
                                        </div>
                                        <div id="tableData"></div>
                                        <div class="empty-space"></div>
                                        <div id="bottomActionBar" class="action-bar">
                                            <div class="col-4xs bottom-action-bar-left">
                                                <div class="dropdown-move-to">
                                                    <div class="dropdown-header">Przenieś do</div>
                                                    <div class="dropdown">
                                                        <div class="dropdown-btn">
                                                            <i class="far fa-caret-square-down"></i>
                                                        </div>
                                                        <div id="moveEmailToFolderBtn" class="dropdown-content">
                                                            <p> Wybierz folder : </p>
                                                            <a folder="received"><i class="fas fa-folder"></i> OTRZYMANE</a>
                                                            <a folder="bin"><i class="fas fa-folder"></i> KOSZ</a>
                                                            <a folder="spam"><i class="fas fa-folder"></i> SPAM</a>
                                                        </div>
                                                    </div>
                                                </div>
                                            </div>
                                            <div class="col-5xs bottom-action-bar-center pagination-info">
                                                <div class="col-12xs col-6s info-part-1"></div>
                                                <div class="col-12xs col-6s info-part-2"></div>
                                            </div>
                                            <div class="col-3xs bottom-action-bar-right">
                                                <a id="previousPageBtn" class="col-6xs pagination-btn"><i class="fas fa-chevron-left"></i></a>
                                                <a id="nextPageBtn" class="col-6xs pagination-btn"><i class="fas fa-chevron-right"></i></a>
                                            </div>
                                        </div>
                                        <div class="empty-space"></div> 
                                    </div>
                            </div>
                    </div>
            </div>
    </div>

    <div id="snackbar"></div>

    <div id="confirmDeletionFile" class="modal">
        <div class="modal-content">
            <div class="modal-header">
                <span class="close">&times;</span>
                <p>Uwaga!</p>
            </div>
            <div class="modal-body">
                <p>Czy chcesz usunąć z Kosza zaznaczone wiadomości ( <span class="counter"></span> )?</p>
                <!--<p>Ta operacja jest <span>nieodwracalna</span>.</p>-->
            </div>
            <div class="modal-footer">
                <div class="col-12xs col-4s btn-left">
                    <div class="modal-checkbox">
                        <label for="permanentDelete"> 
                            Usuń na serwerze &nbsp;
                            <input id="permanentDelete" type="checkbox" name="permanentDelete">
                        </label>
                    </div>
                </div>
                <div col-12xs col-8s>
                    <div class="modal-button btn-right">
                        <button id="cancelDeletionFileBtn">Anuluj</button>
                        <button id="agreeDeletionFileBtn">Tak, usuń <span class="counter"></span> wiadomoci</button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    
    <jsp:include page="footer.jsp"/>
      
    <script type="text/javascript" src="<c:url value='/resources/js/ajax.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/email.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/folder.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/selectedEmails.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/base64Unicode.js'/>"></script>
    <script type="text/javascript" src="<c:url value='/resources/js/index.js'/>"></script>
</body>
</html>
