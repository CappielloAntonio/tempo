package com.cappielloantonio.play.subsonic.models;

import java.util.ArrayList;
import java.util.List;

public class ChatMessages {
    protected List<ChatMessage> chatMessages;

    /**
     * Gets the value of the chatMessages property.
     *
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the chatMessages property.
     *
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getChatMessages().add(newItem);
     * </pre>
     *
     *
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link ChatMessage }
     */
    public List<ChatMessage> getChatMessages() {
        if (chatMessages == null) {
            chatMessages = new ArrayList<ChatMessage>();
        }
        return this.chatMessages;
    }
}
