package org.led.http.protocol;

public enum HttpStatus {
    OK("200 OK"), PARTIALCONTENT("206 Partial Content"), RANGE_NOT_SATISFIABLE(
            "416 Requested Range Not Satisfiable"), REDIRECT(
            "301 Moved Permanently"), NOTMODIFIED("304 Not Modified"), FORBIDDEN(
            "403 Forbidden"), NOTFOUND("404 Not Found"), BADREQUEST(
            "400 Bad Request"), INTERNALERROR("500 Internal Server Error"), NOTIMPLEMENTED(
            "501 Not Implemented");

    private String message;

    private HttpStatus(String msg) {
        message = msg;
    }

    @Override
    public String toString() {
        return message;
    }

}
