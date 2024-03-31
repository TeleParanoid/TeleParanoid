package org.teleparanoid.utils;

import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.teleparanoid.TeleParanoidConfig;

public class TeleParanoidUtils {
    public static Long getDialogId(TLRPC.InputPeer peer) {
        long dialogId;
        if (peer.chat_id != 0) {
            dialogId = -peer.chat_id;
        } else if (peer.channel_id != 0) {
            dialogId = -peer.channel_id;
        } else {
            dialogId = peer.user_id;
        }

        return dialogId;
    }

    public static void markReadOnServer(int accountId, int messageId, TLRPC.InputPeer peer) {
        ConnectionsManager connectionsManager = ConnectionsManager.getInstance(accountId);

        TLObject req;
        if (peer instanceof TLRPC.TL_inputPeerChannel) {
            TLRPC.TL_channels_readHistory request = new TLRPC.TL_channels_readHistory();
            request.channel = MessagesController.getInputChannel(peer);
            request.max_id = messageId;
            req = request;
        } else {
            TLRPC.TL_messages_readHistory request = new TLRPC.TL_messages_readHistory();
            request.peer = peer;
            request.max_id = messageId;
            req = request;
        }

        TeleParanoidConfig tpc = TeleParanoidConfig.getInstance(accountId);
        tpc.setAllowReadPacket(true, 1);
        connectionsManager.sendRequest(req, (response, error) -> {
            if (error == null) {
                if (response instanceof TLRPC.TL_messages_affectedMessages) {
                    TLRPC.TL_messages_affectedMessages res = (TLRPC.TL_messages_affectedMessages) response;
                    MessagesController.getInstance(accountId).processNewDifferenceParams(-1, res.pts, -1, res.pts_count);
                }
            }
        });
    }
}
