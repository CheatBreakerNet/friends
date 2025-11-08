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

import lombok.Getter;
import lombok.Setter;

public class FriendTypingData {
    @Getter
    private final String playerId;
    @Setter
    private long startedTypingTime = -1L;

    public FriendTypingData(String playerId, boolean incoming) {
        this.playerId = playerId;
        if (!incoming) {
            this.startedTypingTime = System.currentTimeMillis();
        }
    }

    public boolean hasTypingExpired() {
        return (this.startedTypingTime != -1L && this.startedTypingTime + 2500 < System.currentTimeMillis());
    }
}
