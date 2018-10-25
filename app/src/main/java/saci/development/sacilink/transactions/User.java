package saci.development.sacilink.transactions;

public class User {

    private String _id;
    private String email;
    private String senha;

    public User( String email, String senha){
        this.email = email;
        this.senha = senha;
    }

    public User(String _id, String email, String senha) {
        this._id = _id;
        this.email = email;
        this.senha = senha;

    }

    public String get_id() {
        return _id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getSenha() {
        return senha;
    }
}
