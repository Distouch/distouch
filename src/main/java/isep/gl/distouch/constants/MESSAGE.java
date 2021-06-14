package isep.gl.distouch.constants;

public enum MESSAGE {
    REGISTRATION_SUCCESS("Successfully registered", MESSAGE_TYPES.SUCCESS),
    LOGIN_FAIL("Invalid username or password", MESSAGE_TYPES.ERROR),
    LOGOUT_SUCCESS("Successfully logged out", MESSAGE_TYPES.SUCCESS),
    PROFILE_UPDATE_SUCCESS("Successfully updated profile", MESSAGE_TYPES.SUCCESS),
    EVENT_CREATION_SUCCESS("Successfully created event", MESSAGE_TYPES.SUCCESS),
    ;

    public String content;
    public MESSAGE_TYPES type;

    MESSAGE(String content, MESSAGE_TYPES type) {
        this.content = content;
        this.type = type;
    }

    public enum MESSAGE_TYPES {
        ERROR, SUCCESS, WARNING, INFO
    }
}
