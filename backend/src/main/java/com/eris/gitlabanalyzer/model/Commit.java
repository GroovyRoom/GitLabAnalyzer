package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "Commit")
@Table(name = "commit")
public class Commit {
    @Id
    @SequenceGenerator(
            name = "commit_sequence",
            sequenceName = "commit_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "commit_sequence"
    )
    @Column(
            name = "commit_id"
    )
    private Long id;

    @Column(
            name = "sha",
            nullable = false
    )
    private String sha;

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
            name = "author_email",
            nullable = false

    )
    private String authorEmail;

    @Column(
            name = "committer_name",
            nullable = false

    )
    private String committerName;

    @Column(
            name = "committer_email",
            nullable = false

    )
    private String committerEmail;

    @Column(
            name = "created_at",
            nullable = false

    )
    private OffsetDateTime createdAt;

    @Column(
            name = "web_url",
            nullable = false

    )
    private String webUrl;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(
            name = "commit_id",
            nullable = true,
            referencedColumnName = "commit_id",
            foreignKey = @ForeignKey(
                    name = "commit_mapping_commit_id_fk"
            )
    )
    private CommitMapping commitMapping;

    @OneToMany(
            mappedBy = "commit",
            orphanRemoval = true,
            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
            fetch = FetchType.LAZY
    )
    private List<CommitComment> commitComments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(
            name = "project_id",
            nullable = false,
            referencedColumnName = "project_id")
    private Project project;

    @ManyToOne
    @JoinColumn(
            name = "git_management_user_id",
            nullable = false,
            referencedColumnName = "git_management_user_id")
    private GitManagementUser gitManagementUser;

    @ManyToOne
    @JoinColumn(
            name = "merge_request_id",
            nullable = true,
            referencedColumnName = "merge_request_id")
    private MergeRequest mergeRequest;

    public Commit() {
    }

    public Commit(String sha, String title, String authorName, String authorEmail, String committerName,
                  String committerEmail, OffsetDateTime createdAt, String webUrl, Project project, GitManagementUser gitManagementUser) {
        this.sha = sha;
        this.title = title;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.committerName = committerName;
        this.committerEmail = committerEmail;
        this.createdAt = createdAt;
        this.webUrl = webUrl;
        this.project = project;
        this.gitManagementUser = gitManagementUser;
    }

    public CommitMapping getCommitMapping() {
        return commitMapping;
    }

    public void setCommitMapping(CommitMapping commitMapping) {
        this.commitMapping = commitMapping;
    }

    public Long getId() {
        return id;
    }

    public String getSha() {
        return sha;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public String getCommitterName() {
        return committerName;
    }

    public String getCommitterEmail() {
        return committerEmail;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setGitManagementUser(GitManagementUser gitManagementUser) {
        this.gitManagementUser = gitManagementUser;
    }

    public void setMergeRequest(MergeRequest mergeRequest) {
        this.mergeRequest = mergeRequest;
    }

    public void addCommitComment(CommitComment commitComment) {
        if (!this.commitComments.contains(commitComment)) {
            this.commitComments.add(commitComment);
            commitComment.setCommit(this);
        }
    }

    @Override
    public String toString() {
        return "Commit{" +
                "id=" + id +
                ", sha='" + sha + '\'' +
                ", title='" + title + '\'' +
                ", authorName='" + authorName + '\'' +
                ", authorEmail='" + authorEmail + '\'' +
                ", committerName='" + committerName + '\'' +
                ", committerEmail='" + committerEmail + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }
}
