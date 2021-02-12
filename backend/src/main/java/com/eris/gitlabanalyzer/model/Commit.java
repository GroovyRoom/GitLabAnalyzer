package com.eris.gitlabanalyzer.model;


import javax.persistence.*;

@Entity(name = "Commit")
@Table(name = "commit")
public class Commit {
    @Id
    @Column(
            name = "id"
    )
    private String id;

    @Column(
            name = "title",
            nullable = false

    )
    private String title;

    @Column(
            name = "author_name",
            nullable = false

    )
    private String authorName;

    @Column(
            name = "committer_name",
            nullable = false

    )
    private String committerName;

    @Column(
            name = "comomitted_date",
            nullable = false

    )
    private String committedDate;

    @Column(
            name = "created_at",
            nullable = false

    )
    private String createdAt;

    @Column(
            name = "web_url",
            nullable = false

    )
    private String webUrl;

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            nullable = false,
            referencedColumnName = "id",
            foreignKey = @ForeignKey(
                    name = "commit_project_fk"
            )
    )
    private Project project;

    @ManyToOne
    @JoinColumn(
            name = "user_name",
            nullable = false,
            referencedColumnName = "user_name",
            foreignKey = @ForeignKey(
                    name = "commit_member_fk"
            )
    )
    private Member member;

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getCommitterName() {
        return committerName;
    }

    public String getCommittedDate() {
        return committedDate;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public Project getProject() {
        return project;
    }

    public Member getMember() {
        return member;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public Commit() {
    }

    public Commit(String id, String title, String authorName, String committerName, String committedDate, String createdAt, String webUrl, Project project, Member member) {
        this.id = id;
        this.title = title;
        this.authorName = authorName;
        this.committerName = committerName;
        this.committedDate = committedDate;
        this.createdAt = createdAt;
        this.webUrl = webUrl;
        this.project = project;
        this.member = member;
    }

    @Override
    public String toString() {
        return "Commit{" +
                "id='" + id + '\'' +
                ", title='" + title + '\'' +
                ", authorName='" + authorName + '\'' +
                ", committerName='" + committerName + '\'' +
                ", committedDate='" + committedDate + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", project=" + project +
                ", member=" + member +
                '}';
    }
}
