package org.jacoco.core.diff;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
import org.eclipse.jgit.diff.DiffFormatter;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectReader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.treewalk.CanonicalTreeParser;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class GitDiff {

    public Git git;
    public Repository repository;
    //项目地址
    public String gitProjectDir="F:/java/idea_workspace/jacoco/.github";

    public void  diffMethod(String oldBranch , String newBranch){

        try {
            git = Git.open(new File(gitProjectDir));
        } catch (IOException e) {
            e.printStackTrace();
        }

        repository = git.getRepository();
        ObjectReader objectReader = repository.newObjectReader();
        CanonicalTreeParser oldTreeIter = new CanonicalTreeParser();
        CanonicalTreeParser newTreeIter = new CanonicalTreeParser();
        try {
            ObjectId oldB = repository.resolve(oldBranch + "^{tree}");
            ObjectId newB = repository.resolve(newBranch + "^{tree}");
            oldTreeIter.reset(objectReader,oldB);
            newTreeIter.reset(objectReader,newB);
            List<DiffEntry> diffs = git.diff().setNewTree(newTreeIter).setOldTree(oldTreeIter).call();

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DiffFormatter df = new DiffFormatter(out);
            df.setRepository(git.getRepository());

            for (DiffEntry diffEntry : diffs) {
                df.format(diffEntry);
                String diffText = out.toString("UTF-8");
                System.out.println(diffText);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GitAPIException e) {
            e.printStackTrace();
        }

    }
}
