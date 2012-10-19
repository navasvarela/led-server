package org.led.http.protocol;

public enum HttpStatus {
    HTTP_OK("200 OK"), HTTP_PARTIALCONTENT("206 Partial Content"), HTTP_RANGE_NOT_SATISFIABLE(
            "416 Requested Range Not Satisfiable"), HTTP_REDIRECT(
            "301 Moved Permanently"), HTTP_NOTMODIFIED("304 Not Modified"), HTTP_FORBIDDEN(
            "403 Forbidden"), HTTP_NOTFOUND("404 Not Found"), HTTP_BADREQUEST(
            "400 Bad Request"), HTTP_INTERNALERROR("500 Internal Server Error"), HTTP_NOTIMPLEMENTED(
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
