package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "MergeRequest")
@Table(name = "merge_request")
public class MergeRequest {

    @Id
    @Column(
            name = "id"
    )
    private Long id;

    @Column(
            name = "author_name",
            nullable = false

    )
    private String authorName;

    @Column(
            name = "title",
            nullable = false

    )
    private String title;

    @Column(
            name = "description",
            nullable = false

    )
    private String description;

    @Column(
            name = "created_at",
            nullable = false

    )
    private String created_at;

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
                    name = "mr_project_id_fk"
            )
    )
    private Project project;

    @ManyToOne
    @JoinColumn(
            name = "user_name",
            nullable = false,
            referencedColumnName = "user_name",
            foreignKey = @ForeignKey(
                    name = "mr_member_user_name_fk"
            )
    )
    private Member member;

    public Long getId() {
        return id;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getCreated_at() {
        return created_at;
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

    public MergeRequest() {
    }

    public MergeRequest(Long id, String authorName, String title, String description, String created_at, String webUrl, Project project, Member member) {
        this.id = id;
        this.authorName = authorName;
        this.title = title;
        this.description = description;
        this.created_at = created_at;
        this.webUrl = webUrl;
        this.project = project;
        this.member = member;
    }

    @Override
    public String toString() {
        return "MergeRequest{" +
                "id=" + id +
                ", authorName='" + authorName + '\'' +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", created_at='" + created_at + '\'' +
                ", webUrl='" + webUrl + '\'' +
                ", project=" + project +
                ", member=" + member +
                '}';
    }
}
