/**
 * The LGPL License
 * <p>
 * Copyright (C) 2019-2025 CheatBreaker.net
 * <p>
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License along with this program.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.cheatbreaker.friends.friend;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import lombok.Getter;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.function.Function;

public class FriendsManager {
    @Getter
    private final Object2ObjectOpenHashMap<String, Friend> friends = new Object2ObjectOpenHashMap<>();
    @Getter
    private final Object2ObjectOpenHashMap<String, FriendRequest> friendRequests = new Object2ObjectOpenHashMap<>();
    @Getter
    private final Object2ObjectOpenHashMap<String, List<String>> readMessages = new Object2ObjectOpenHashMap<>();
    @Getter
    private final Object2ObjectOpenHashMap<String, List<String>> messages = new Object2ObjectOpenHashMap<>();
    @Getter
    private final Object2ObjectOpenHashMap<String, List<String>> unreadMessages = new Object2ObjectOpenHashMap<>();

    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final Function<DateTimeFormatter, String> timeFormatter;
    private final Function<String, String> nameFormatter;

    public FriendsManager(Function<DateTimeFormatter, String> formatTime, Function<String, String> formatName) {
        this.timeFormatter = formatTime;
        this.nameFormatter = formatName;
    }

    public void onWebsocketDisconnect(Object event) {
        this.friends.clear();
        this.friendRequests.clear();
    }

    /**
     * Adds an unread message from a player.
     *
     * @param playerId The ID of the player.
     * @param message  The message.
     */
    public void addUnreadMessage(String playerId, String message) {
        Friend var3 = this.getFriend(playerId);
        if (var3 != null) {
            if (!this.messages.containsKey(playerId)) {
                this.messages.put(playerId, new ObjectArrayList<>());
            }

            String time = this.timeFormatter.apply(this.dateTimeFormatter);
            String name = this.nameFormatter.apply(var3.getName());
            String formattedMessage = time + " " + name + ": " + message;
            this.messages.get(playerId).add(formattedMessage);
        }
    }

    /**
     * Adds a message from a player.
     *
     * @param playerId The ID of the player.
     * @param message  The message.
     */
    public void addMessage(String playerId, String message) {
        Friend friend = this.getFriend(playerId);
        if (friend != null) {
            if (!this.readMessages.containsKey(playerId)) {
                this.readMessages.put(playerId, new ObjectArrayList<>());
            }

            this.readMessages.get(playerId).add(message);
        }
    }

    /**
     * Adds an outgoing message to a player.
     *
     * @param playerId The ID of the player.
     * @param text     The message.
     */
    public void addOutgoingMessage(String playerId, String text, String defaultUsername) {
        Friend friend = this.getFriend(playerId);
        if (friend != null) {
            if (!this.unreadMessages.containsKey(playerId)) {
                this.unreadMessages.put(playerId, new ObjectArrayList<>());
            }

            this.unreadMessages.get(playerId).add(friend.getName() + ": " + text);
            String time = this.timeFormatter.apply(this.dateTimeFormatter);
            String name = this.nameFormatter.apply(defaultUsername);
            String formattedMessage = time + " " + name + ": " + text;
            this.addMessage(playerId, formattedMessage);
        }
    }

    /**
     * Marks all messages from a player as read.
     *
     * @param playerId The ID of the player.
     */
    public void readMessages(String playerId) {
        if (this.messages.containsKey(playerId)) {
            if (!this.readMessages.containsKey(playerId)) {
                this.readMessages.put(playerId, new ObjectArrayList<>());
            }

            this.readMessages.get(playerId).addAll(this.messages.get(playerId));
            this.messages.remove(playerId);
        }
    }

    /**
     * Gets a friend by their player ID.
     *
     * @param playerId The ID of the player.
     * @return The friend, or null if not found.
     */
    public Friend getFriend(String playerId) {
        return this.friends.get(playerId);
    }
}
