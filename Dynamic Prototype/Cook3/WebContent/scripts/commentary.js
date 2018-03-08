loadExistingComments = () => {

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
	console.log(data);
}

let webSocketConnection = ConnectToWebSocket(processIncomingBroadcast);

sendComment = () => {
    let commentInformation = constructBroadcastPayload();
    let stringifiedInformation = JSON.stringify(commentInformation);
    
    //saveCommentToDatabase(commentInformation);
    broadcastComment(stringifiedInformation);
}

broadcastComment = (stringifiedInformation) =>{
    webSocketConnection.send(stringifiedInformation);
}

constructBroadcastPayload = () => {
    let username = $(".userNameHeader")[0].textContent;
    let message = $(".commentInput")[0].value;
    let currentRecipeId = window.localStorage.getItem("currentRecipeId");
    let currentTime = new Date().getTime();
    
    let payload = {
        "id": currentRecipeId,
        "user": username,
        "comment": message,
        "time": currentTime
    };
    return payload;
}

saveCommentToDatabase = (data) => {
    $.post("rest/commentary", data);
}
