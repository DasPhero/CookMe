loadExistingComments = () => {
    let recipeId = window.localStorage.getItem("currentRecipeId");
    $(".comments").html("");
    $.get("rest/commentary/comments/" + recipeId,
    (commentList) => {
    	let commentArray = JSON.parse(commentList);
    	commentArray.forEach(comment => {
            createComment(comment.author, comment.comment);
        });
    })
}
    
createComment = (username, commentText) => {
    let commentCode = $(".comments").html();
    let newComment = 
     `<div class='comment'>\
        <h4>${username}</h4>\
        <p>${commentText}</p>\
    </div>`;

    commentCode = newComment + commentCode;
    $(".comments").html(commentCode);
    $(".commentInput")[0].value = "";
}

ConnectToWebSocket = (callback) => {
    let domain = window.location.href;
	let startOfResource = domain.lastIndexOf('/');
	let domainWithoutResource = domain.substring(0, startOfResource); 
	let pageIsIndexPage = domain.substring(startOfResource + 1, startOfResource + 6) == "index";
	
	if(pageIsIndexPage){
        let socket = new WebSocketClient(callback);
        socket.connect();
        return socket;
    }
    return false;
}

processIncomingBroadcast = (data) => {
    let currentRecipeId = window.localStorage.getItem("currentRecipeId");
    data = JSON.parse(data);

    if(data.id == currentRecipeId){
        let commentCode = $(".comments").html();
        let newComment = 
         `<div class='comment'>\
            <h4>${data.user}</h4>\
            <p>${data.comment}</p>\
        </div>`;
    
        commentCode = newComment + commentCode;
        $(".comments").html(commentCode);
        $(".commentInput")[0].value = "";
    }
}

let webSocketConnection = ConnectToWebSocket(processIncomingBroadcast);

sendComment = () => {
	if($(".commentInput")[0].value == "" || $(".userNameHeader").text() == ""){
		return;
    }
    
    let commentInformation = constructBroadcastPayload();
    let stringifiedInformation = JSON.stringify(commentInformation);
    saveCommentToDatabase(commentInformation);
    broadcastComment(stringifiedInformation);
}

broadcastComment = (stringifiedInformation) =>{
    webSocketConnection.send(stringifiedInformation);
}

constructBroadcastPayload = () => {
    let username = $(".userNameHeader")[0].textContent;
    let message = $(".commentInput")[0].value;
    let escapedComment = escapeHtml(message);

    let currentRecipeId = window.localStorage.getItem("currentRecipeId");
    let currentTime = new Date().getTime();

    let payload = {
        "id": currentRecipeId,
        "user": username,
        "comment": escapedComment,
        "time": currentTime
    };
    return payload;
}

let entityMap = {
    '&': '&amp;',
    '<': '&lt;',
    '>': '&gt;',
    '"': '&quot;',
    "'": '&#39;',
    '/': '&#x2F;',
    '`': '&#x60;',
    '=': '&#x3D;'
  };
  
  function escapeHtml (string) {
    return String(string).replace(/[&<>"'`=\/]/g, function (s) {
      return entityMap[s];
    });
  }

saveCommentToDatabase = (data) => {

    $.get("rest/commentary/usernametoid/" + data.user.toLowerCase(),
        (userId) => {
            data.user = userId;
            $.ajax({
                url: "rest/commentary/",
                type: "POST",
                data: data,
                dataType: "json",
                success: ( () => {} ),
                error: ( () => {} )
              });
            });
}
