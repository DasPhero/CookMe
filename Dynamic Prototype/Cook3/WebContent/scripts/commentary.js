loadExistingComments = () => {

}

ConnectToWebSocket = () => {
	let domain = window.location.href;
	let startOfResource = domain.lastIndexOf('/');
	let domainWithoutResource = domain.substring(0, startOfResource); 
	let pageIsIndexPage = domain.substring(startOfResource + 1, startOfResource + 6) == "index";
	
	if(pageIsIndexPage){
		let socket = new WebSocketClient();
        socket.connect();
        setTimeout(() => socket.send("boi"), 500); 
	}
		
}