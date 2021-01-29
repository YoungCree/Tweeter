package model.service.response;

//import androidx.annotation.Nullable;

import org.jetbrains.annotations.Nullable;

import java.util.Objects;

import model.domain.Story;

public class StoryResponse extends PagedResponse {

    private Story story;

    public StoryResponse(String message) {super(false, message, false); }

    public StoryResponse(Story story, boolean hasMorePages) {
        super(true, hasMorePages);
        this.story = story;
    }

    public Story getStory() { return story; }

    @Override
    public boolean equals(@Nullable Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        StoryResponse that = (StoryResponse) obj;

        return (Objects.equals(story, that.story) &&
                Objects.equals(this.getMessage(), that.getMessage()) &&
                this.isSuccess() == that.isSuccess());
    }
}
