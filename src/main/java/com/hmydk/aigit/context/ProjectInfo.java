package com.hmydk.aigit.context;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.project.ProjectUtil;
import git4idea.repo.GitRepository;
import git4idea.repo.GitRepositoryManager;
import git4idea.config.GitConfigUtil;

/**
 * 项目信息数据结构
 * Linus式设计：简单、直接、无特殊情况
 */
public class ProjectInfo {
    private final String name;
    private final String path;
    private final String branch;
    private final boolean isGitRepository;

    private final String auth;

    public ProjectInfo(String name, String path, String branch, String auth, boolean isGitRepository) {
        this.name = name;
        this.path = path;
        this.branch = branch;
        this.auth = auth;

        this.isGitRepository = isGitRepository;
    }
    
    public static ProjectInfo from(Project project) {
        String name = project.getName();
        String path = project.getBasePath();
        String branch = "unknown";
        String auth = "unknown";

        boolean isGit = false;
        
        try {
            // 获取Git信息
            GitRepositoryManager gitManager = GitRepositoryManager.getInstance(project);
            if (gitManager != null) {
                GitRepository repository = gitManager.getRepositoryForRoot(ProjectUtil.guessProjectDir(project));
                if (repository != null) {
                    isGit = true;
                    if (repository.getCurrentBranch() != null) {
                        branch = repository.getCurrentBranch().getName();
                    }

                    auth = GitConfigUtil.getValue(project, repository.getRoot(), "user.name");
                }


            }
        } catch (Exception e) {
            // 静默处理，使用默认值
        }
        
        return new ProjectInfo(name, path, branch,  auth, isGit);
    }
    
    // Getters
    public String getName() { return name; }
    public String getPath() { return path; }
    public String getBranch() { return branch; }
    public String getAuth() { return auth; }
    public boolean isGitRepository() { return isGitRepository; }
    
    @Override
    public String toString() {
        return String.format("ProjectInfo{name='%s', branch='%s', git=%s}", 
                           name, branch, isGitRepository);
    }
}