package uk.co.littlemike.gishgraph.ddag.git

import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevWalk
import uk.co.littlemike.gishgraph.extensions.use

class BranchDiffer(private val repository: Repository) {
    fun diff(myBranch: String, remoteBranch: String): List<RevCommit> {
        return RevWalk(repository).use { walk ->
            walk.markStart(walk.lookupCommit(repository.resolve(remoteBranch)))
            walk.markUninteresting(walk.lookupCommit(repository.resolve(myBranch)))
            walk.reversed()
        }
    }

}
