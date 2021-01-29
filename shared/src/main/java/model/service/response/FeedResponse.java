package model.service.response;

//import androidx.annotation.Nullable;

import java.util.Objects;

import model.domain.Feed;

public class FeedResponse extends PagedResponse {

    private Feed feed;

    public FeedResponse(String message) { super(false, message, false); }

    public FeedResponse(Feed feed, boolean hasMorePages) {
        super(true, hasMorePages);
        this.feed = feed;
    }

    public Feed getFeed() { return feed; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        FeedResponse that = (FeedResponse) obj;

        return (Objects.equals(feed, that.feed) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }
}
