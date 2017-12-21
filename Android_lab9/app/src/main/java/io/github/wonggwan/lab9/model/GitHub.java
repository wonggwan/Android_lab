package io.github.wonggwan.lab9.model;

/**
 * Created by wonggwan on 2017/12/18.
 */

public class GitHub {
    private String login;
    private String blog;
    private String id;

    public GitHub(String id, String blog, String login){
        this.id=id;
        this.blog=blog;
        this.login=login;
    }

    public String getTitle() { return login; }
    public String getSub1() { return id; }
    public String getSub2() { return blog; }

}

