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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

import java.awt.*;
import java.util.Objects;

@Data
@Builder
@AllArgsConstructor
public class Friend {
    private final String playerId;
    private String name;
    private int iconColor;
    private String status;
    private String server;
    private boolean online;
    private long offlineSince;
    private Status onlineStatus;

    public static int getStatusColor(Status playerStatus) {
        if (playerStatus == null) {
            return -13158601;
        } else {
            int color;
            switch (playerStatus) {
                case AWAY:
                    color = new Color(-1722581).getRGB();
                    break;
                case BUSY:
                    color = new Color(-1758421).getRGB();
                    break;
                case OFFLINE:
                    color = new Color(-13158601).getRGB();
                    break;
                default:
                    color = -13369549;
                    break;
            }

            return color;
        }
    }

    public int getIconColors() {
        return new Color(iconColor).getRGB();
    }

    public static FriendBuilder builder() {
        return new FriendBuilder();
    }

    public String getStatusString() {
        String playingText;
        if (this.online) {
            if (this.server != null && !Objects.equals(this.server, "")) {
                playingText = "Playing" + this.server;
            } else {
                switch (this.onlineStatus) {
                    case AWAY:
                        playingText = "Away";
                        break;
                    case BUSY:
                        playingText = "Busy";
                        break;
                    case OFFLINE:
                        playingText = "Offline";
                        break;
                    default:
                        playingText = "Online";
                        break;
                }
            }
        } else {
            long timeDiff = System.currentTimeMillis() - this.offlineSince;
            long seconds = 1000L;
            long minutes = seconds * 60L;
            long hours = minutes * 60L;
            long day = hours * 24L;
            long daysTime = timeDiff / day;
            long hoursTime = (timeDiff %= day) / hours;
            long minutesTime = timeDiff % hours / minutes;
            playingText = daysTime > 0L ? "Offline for " + daysTime + (daysTime == 1L ? " day" : " days") : (hoursTime > 0L ? "Offline for " + hoursTime + (hoursTime == 1L ? " hour" : " hours") : "Offline for " + minutesTime + (minutesTime == 1L ? " minute" : " minutes"));
        }

        return playingText;
    }

    @AllArgsConstructor
    @Getter
    public enum Status {
        ONLINE("Online"),
        AWAY("Away"),
        BUSY("Busy"),
        OFFLINE("Offline");

        private final String name;
    }
}
