package com.juanmrivero.googleapi;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.CapabilityApi;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.juanmrivero.utils.logging.Log;

import java.util.Locale;
import java.util.Set;

public class WearNodeConnection extends GoogleApiConnection {

    private static final String TAG = WearNodeConnection.class.getName();

    @NonNull private String capability;
    @Nullable private String nodeId;

    public WearNodeConnection(@NonNull Context context,
                              @NonNull String capability) {
        super(context);
        this.capability = capability;
    }

    @Nullable
    public String getNodeId() {
        return nodeId;
    }

    @Override
    protected void configure(GoogleApiClient.Builder builder) {
        builder.addApi(Wearable.API);
    }

    @Override
    protected void onConnected(GoogleApiClient googleApiClient) {
        determineNode(googleApiClient);
    }

    private void determineNode(GoogleApiClient googleApiClient) {
        PendingResult<CapabilityApi.GetCapabilityResult> capabilityPendingResult =
                Wearable.CapabilityApi.getCapability(
                        googleApiClient,
                        capability,
                        CapabilityApi.FILTER_REACHABLE);

        capabilityPendingResult.setResultCallback(new CapabilityListener());
    }

    private void selectNode(Set<Node> nodes) {
        Node node = pickNearby(nodes);

        if (node != null) {
            Log.d(TAG, String.format(Locale.ENGLISH, "Node found %s", node));
            nodeId = node.getId();
            onNodeFound(node);
        } else {
            Log.d(TAG, "no node found");
        }
    }

    @Nullable
    private Node pickNearby(Set<Node> nodes) {
        Node selectedNode = null;

        for (Node node : nodes) {
            if (node.isNearby()) {
                selectedNode = node;
                break;
            }
            selectedNode = node;
        }

        return selectedNode;
    }

    protected void onNodeFound(@NonNull Node node) {
        //Do nothing by default
    }

    private class CapabilityListener implements ResultCallback<CapabilityApi.GetCapabilityResult> {

        @Override
        public void onResult(@NonNull CapabilityApi.GetCapabilityResult capabilityResult) {

            if (capabilityResult.getStatus().isSuccess()) {
                Set<Node> nodes = capabilityResult.getCapability().getNodes();
                selectNode(nodes);

            } else {
                Log.d(TAG, "error looking for capabilities");
            }
        }
    }

}
