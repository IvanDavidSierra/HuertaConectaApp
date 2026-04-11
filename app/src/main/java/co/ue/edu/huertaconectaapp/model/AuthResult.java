package co.ue.edu.huertaconectaapp.model;

public class AuthResult {
    private String message;
    private String token;
    private UserResponse user;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public UserResponse getUser() { return user; }
    public void setUser(UserResponse user) { this.user = user; }
}
