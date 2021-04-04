import React, {useEffect} from "react";
import AuthView from "../../../../components/AuthView";
import MenuLayout from "../../../../components/layout/menu/MenuLayout";
import {AuthContext} from "../../../../components/AuthContext";
import axios, {AxiosResponse} from "axios";
import {useRouter} from "next/router";
import {MergeRequest, OrphanCommitMergeRequest} from "../../../../interfaces/GitLabMergeRequest";
import {useSnackbar} from "notistack";
import DiffViewer from "../../../../components/diff/DiffViewer";
import {FileChange} from "../../../../interfaces/GitLabFileChange";
import MergeRequestList from "../../../../components/diff/MergeRequestList";
import CommitList from "../../../../components/diff/CommitList";
import {Commit} from "../../../../interfaces/GitLabCommit";
import {Grid} from "@material-ui/core";

const index = () => {
    const router = useRouter();
    const {enqueueSnackbar} = useSnackbar();
    const {getAxiosAuthConfig} = React.useContext(AuthContext);
    const [mergeRequests, setMergeRequests] = React.useState<MergeRequest[]>([]);
    const [commits, setCommits] = React.useState<Commit[]>([]);
    const [orphanCommits, setOrphanCommits] = React.useState<Commit[]>([]);
    const [isOrphanCommitsSelected, setIsOrphanCommitsSelected] = React.useState<boolean>(false);
    const [fileChanges, setFileChanges] = React.useState<FileChange[]>([]);
    const [linkToFileChanges, setLinkToFileChanges] = React.useState<string>('');
    const {projectId, startDateTime, endDateTime} = router.query;

    useEffect(() => {
        if (router.isReady) {
            fetchMergeData();
        }
    }, [projectId]);

    useEffect(() => {
        // New file changes have come in, ensure orphan commits are not marked as selected
        setIsOrphanCommitsSelected(false);
    }, [fileChanges]);

    const fetchMergeData = async () => {
        try {
            // Orphan commits
            const orphanCommitResp = await axios.get(`${process.env.NEXT_PUBLIC_API_URL}/${projectId}/commits/orphan/0?startDateTime=${startDateTime}&endDateTime=${endDateTime}`, getAxiosAuthConfig())
            const hasOrphanCommits = orphanCommitResp.data.length > 0;
            setOrphanCommits(orphanCommitResp.data);

            // Merge requests
            // If orphan commits exist, add the OrphanCommitMergeRequest to the merge request array
            const mrResp = await axios.get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}/merge_requests?startDateTime=${startDateTime}&endDateTime=${endDateTime}`, getAxiosAuthConfig())
            let mergeRequests: MergeRequest[] = mrResp.data;
            mergeRequests = hasOrphanCommits ? [...mergeRequests, OrphanCommitMergeRequest] : mergeRequests;
            setMergeRequests(mergeRequests);
        } catch (e) {
            enqueueSnackbar("Failed to load data", {variant: 'error'});
        }
    }

    const fetchCommitData = (mergeRequest: MergeRequest) => {
        axios
            .get(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}/merge_request/${mergeRequest.iid}/commits`, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                setCommits(resp.data);
            }).catch(() => {
            enqueueSnackbar("Failed to load data", {variant: 'error'});
        });
    }

    const fetchDiffDataFromUrl = (url: string) => {
        axios
            .get(url, getAxiosAuthConfig())
            .then((resp: AxiosResponse) => {
                setFileChanges(resp.data);
            }).catch(() => {
            enqueueSnackbar("Failed to load data", {variant: 'error'});
        });
    };

    const handleSelectMergeRequest = (mergeRequest: MergeRequest) => {
        if (mergeRequest.id == OrphanCommitMergeRequest.id) {
            handleSelectOrphanCommits();
        } else {
            fetchCommitData(mergeRequest);
            setLinkToFileChanges(mergeRequest.web_url);
            fetchDiffDataFromUrl(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}/merge_request/${mergeRequest.iid}/diff`);
        }
    };

    const handleSelectOrphanCommits = () => {
        setIsOrphanCommitsSelected(true);
        setCommits(orphanCommits);
        setLinkToFileChanges('');
    }

    const handleSelectCommit = (commit: Commit) => {
        setLinkToFileChanges(commit.web_url);
        // Gitlab retrieved data will have sha in id, but our own backend will place in sha field
        fetchDiffDataFromUrl(`${process.env.NEXT_PUBLIC_API_URL}/gitlab/projects/${projectId}/commit/${commit.sha || commit.id}/diff`);
    };

    return (
        <AuthView>
            <MenuLayout tabSelected={1}>
                <Grid container spacing={3}>
                    <Grid item xs={3}>
                        <MergeRequestList
                            mergeRequests={mergeRequests}
                            handleSelectMergeRequest={handleSelectMergeRequest}
                        />
                        <CommitList commits={commits} handleSelectCommit={handleSelectCommit}/>
                    </Grid>

                    <Grid item xs={9}>
                        <DiffViewer
                            fileChanges={fileChanges}
                            linkToFileChanges={linkToFileChanges}
                            isOrphanCommitsSelected={isOrphanCommitsSelected}
                        />
                    </Grid>
                </Grid>
            </MenuLayout>
        </AuthView>
    );
};

export default index;
