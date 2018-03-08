class WebSocketClient {
    
    constructor(callback) {
        this.webSocket = null;
        this.externalFunction = callback;
    }
    
    getServerUrl() {
    	let domain = window.location.href;
    	let startOfResource = domain.lastIndexOf('/');
    	let domainWithoutResource = domain.substring(0, startOfResource); 
        
        return domainWithoutResource.replace("http", "ws") + "/endpoint";
    }
    
    connect() {
        try {
            this.webSocket = new WebSocket(this.getServerUrl());
        
            this.webSocket.onopen = function(event) {
                console.log('onopen::' + JSON.stringify(event, null, 4));
            }
            
            this.webSocket.onmessage = (event) => { console.log("booooi"); this.externalFunction(event.data); };

            this.webSocket.onclose = function(event) {
                console.log('onclose::' + JSON.stringify(event, null, 4));                
            }
            this.webSocket.onerror = function(event) {
                console.log('onerror::' + JSON.stringify(event, null, 4));
            }
            
        } catch (exception) {
            console.error(exception);
        }
    }
    
    getStatus() {
        return this.webSocket.readyState;
    }
    send(message) {
        
        if (this.webSocket.readyState == WebSocket.OPEN) {
            this.webSocket.send(message);
            
        } else {
            console.error('webSocket is not open. readyState=' + this.webSocket.readyState);
        }
    }
    disconnect() {
        if (this.webSocket.readyState == WebSocket.OPEN) {
            this.webSocket.close();
            
        } else {
            console.error('webSocket is not open. readyState=' + this.webSocket.readyState);
        }
    }
}