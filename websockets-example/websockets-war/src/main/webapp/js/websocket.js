function openSocket() {
    var ws = new WebSocket("ws://" + window.location.hostname + ":8080/logger-war/console");
    ws.onopen = function () {
    };
    ws.onmessage = function (evt) {
        insertNewLog(evt.data);
    };
    ws.onclose = function () {
        alert("Connection is closed...");
    };
}
function insertNewLog(log) {
    var logLine = document.createElement("div");
    logLine.innerHTML = log;
    if (log.indexOf("INFO") > -1) {
        logLine.className = "info"
    }
    if (log.indexOf("ERROR") > -1) {
        logLine.className = "error"
    }
    if (log.indexOf("DEBUG") > -1) {
        logLine.className = "debug"
    }
    if (log.indexOf("WARNING") > -1) {
        logLine.className = "warning"
    }
    if (log.indexOf("TRACE") > -1) {
        logLine.className = "trace"
    }
    document.body.appendChild(logLine)
}
