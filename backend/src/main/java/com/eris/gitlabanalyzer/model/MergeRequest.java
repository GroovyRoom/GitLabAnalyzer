package com.eris.gitlabanalyzer.model;

import javax.persistence.*;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.SEQUENCE;

@Entity(name = "MergeRequest")
@Table(name = "merge_request")
public class MergeRequest {

    @Id
    @SequenceGenerator(
            name = "merge_request_sequence",
            sequenceName = "merge_request_sequence",
            allocationSize = 1
    )
    @GeneratedValue(
            strategy = SEQUENCE,
            generator = "merge_request_sequence"
    )
    @Column(
            name = "merge_request_id"
    )
    private Long id;

    @Column(
            name = "merge_request_iid"
    )
    private Long iid;

    @Column(
            name = "author_username",
            nullable = false
    )
    private String authorUsername;

    @Column(
            name = "title",
            nullable = false
    )
    private String title;

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

    @OneToMany(
            mappedBy = "mergeRequest",
            orphanRemoval = false,
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY
    )
    private List<Commit> commits = new ArrayList<>();

    @OneToMany(
            mappedBy = "mergeRequest",
            orphanRemoval = false,
            cascade = {CascadeType.PERSIST},
            fetch = FetchType.LAZY
    )
    private List<MergeRequestComment> mergeRequestComments = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public Long getIid() {
        return iid;
    }

    public String getAuthorUsername() {
        return authorUsername;
    }

    public String getTitle() {
        return title;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public String getWebUrl() {
        return webUrl;
    }

    public Project getProject() {
        return project;
    }

    public GitManagementUser getGitManagementUser() {
        return gitManagementUser;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setGitManagementUser(GitManagementUser gitManagementUser) {
        this.gitManagementUser = gitManagementUser;
    }

    public MergeRequest() {
    }

    public MergeRequest(Long iid, String authorUsername, String title, OffsetDateTime createdAt, String webUrl, Project project, GitManagementUser gitManagementUser) {
        this.iid = iid;
        this.authorUsername = authorUsername;
        this.title = title;
        this.createdAt = createdAt;
        this.webUrl = webUrl;
        this.project = project;
        this.gitManagementUser = gitManagementUser;
    }

    public void addCommit(Commit commit) {
        if (!this.commits.contains(commit)) {
            this.commits.add(commit);
            commit.setMergeRequest(this);
        }
    }

    public void addMergeRequestComment(MergeRequestComment mergeRequestComment) {
        if (!this.mergeRequestComments.contains(mergeRequestComment)) {
            this.mergeRequestComments.add(mergeRequestComment);
            mergeRequestComment.setMergeRequest(this);
        }
    }

    @Override
    public String toString() {
        return "MergeRequest{" +
                "id=" + id +
                ", iid=" + iid +
                ", authorUsername='" + authorUsername + '\'' +
                ", title='" + title + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", webUrl='" + webUrl + '\'' +
                '}';
    }
}
