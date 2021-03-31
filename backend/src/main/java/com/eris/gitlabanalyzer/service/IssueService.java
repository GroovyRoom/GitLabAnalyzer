package com.eris.gitlabanalyzer.service;

import com.eris.gitlabanalyzer.model.*;
import com.eris.gitlabanalyzer.repository.GitManagementUserRepository;
import com.eris.gitlabanalyzer.repository.IssueCommentRepository;
import com.eris.gitlabanalyzer.repository.IssueRepository;
import com.eris.gitlabanalyzer.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.Optional;

@Service
public class IssueService {
    IssueRepository issueRepository;
    IssueCommentRepository issueCommentRepository;
    ProjectRepository projectRepository;
    GitManagementUserRepository gitManagementUserRepository;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.SERVER_URL}")
    String serverUrl;

    // TODO Remove after server info is correctly retrieved based on internal projectId
    @Value("${gitlab.ACCESS_TOKEN}")
    String accessToken;

    public IssueService(IssueRepository issueRepository, IssueCommentRepository issueCommentRepository, ProjectRepository projectRepository, GitManagementUserRepository gitManagementUserRepository) {
        this.issueRepository = issueRepository;
        this.issueCommentRepository = issueCommentRepository;
        this.projectRepository = projectRepository;
        this.gitManagementUserRepository = gitManagementUserRepository;
    }

    public void saveIssueInfo(Project project, OffsetDateTime startDateTime, OffsetDateTime endDateTime) {
        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);
        var gitLabIssues = gitLabService.getIssues(project.getGitLabProjectId(), startDateTime, endDateTime);
        var gitLabIssueList = gitLabIssues.collectList().block();

        Objects.requireNonNull(gitLabIssueList).forEach(gitLabIssue -> {
                    GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerUrl(gitLabIssue.getAuthor().getId(), serverUrl);
                    Issue issue = issueRepository.findByIidAndProjectId(gitLabIssue.getIid(), project.getId());
                    if (issue == null) {
                        issue = new Issue(
                                gitLabIssue.getIid(),
                                gitLabIssue.getTitle(),
                                gitLabIssue.getAuthor().getName(),
                                gitLabIssue.getCreatedAt(),
                                gitLabIssue.getWebUrl(),
                                project,
                                gitManagementUser
                        );
                    }
                    issue = issueRepository.save(issue);
                    saveIssueComments(project, issue);
                }
        );
    }

    public void saveIssueComments(Project project, Issue issue) {
        // TODO use an internal projectId to find the correct server
        var gitLabService = new GitLabService(serverUrl, accessToken);
        var gitLabIssueComments = gitLabService.getIssueNotes(project.getGitLabProjectId(), issue.getIid());
        var gitLabIssueCommentList = gitLabIssueComments.collectList().block();

        Objects.requireNonNull(gitLabIssueCommentList).parallelStream().forEach(gitLabNote -> {
            GitManagementUser gitManagementUser = gitManagementUserRepository.findByGitLabUserIdAndServerUrl(gitLabNote.getAuthor().getId(), serverUrl);
            Optional<Note> note = issueCommentRepository.findByGitLabNoteIdAndProjectId(gitLabNote.getId(), issue.getIid());
            if (note.isEmpty() && !gitLabNote.isSystem()) {
                boolean isOwn = gitLabNote.getAuthor().getId().equals(issue.getGitManagementUser().getGitLabUserId());
                issueCommentRepository.save(new Note(
                        gitLabNote.getId(),
                        gitLabNote.getBody(),
                        gitManagementUser,
                        gitLabNote.getCreatedAt(),
                        project.getId(),
                        isOwn,
                        issue.getIid(),
                        issue.getWebUrl(),
                        gitLabNote.getNoteableType()
                ));
            }
        });
    }


}
