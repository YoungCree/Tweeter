package edu.byu.cs.tweeter.view.main.story;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

import edu.byu.cs.tweeter.R;
import model.domain.AuthToken;
import model.domain.Status;
import model.domain.Story;
import model.domain.User;
import model.service.request.StoryRequest;
import model.service.request.UserByAliasRequest;
import model.service.response.StoryResponse;
import model.service.response.UserByAliasResponse;
import edu.byu.cs.tweeter.presenter.StoryPresenter;
import edu.byu.cs.tweeter.view.asyncTasks.GetStoryTask;
import edu.byu.cs.tweeter.view.asyncTasks.GetUserByAliasTask;
import edu.byu.cs.tweeter.view.main.UserActivity;
import edu.byu.cs.tweeter.view.util.ImageUtils;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link StoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class StoryFragment extends Fragment implements StoryPresenter.View {
    private static final String LOG_TAG = "StoryFragment";
    private static final String USER_KEY = "UserKey";
    private static final String AUTH_TOKEN_KEY = "AuthTokenKey";

    private static final int LOADING_DATA_VIEW = 0;
    private static final int ITEM_VIEW = 1;
    private static final int PAGE_SIZE = 6;

    private User user;
    private AuthToken authToken;
    private StoryPresenter presenter;

    private StoryRecyclerViewAdapter storyRecyclerViewAdapter;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param user Parameter 1.
     * @param authToken Parameter 2.
     * @return A new instance of fragment StoryFragment.
     */
    public static StoryFragment newInstance(User user, AuthToken authToken) {
        StoryFragment fragment = new StoryFragment();
        Bundle args = new Bundle(2);
        args.putSerializable(USER_KEY, user);
        args.putSerializable(AUTH_TOKEN_KEY, authToken);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_story, container, false);

        user = (User) getArguments().getSerializable(USER_KEY);
        authToken = (AuthToken) getArguments().getSerializable(AUTH_TOKEN_KEY);

        presenter = new StoryPresenter(this);

        RecyclerView storyRecyclerView = view.findViewById(R.id.storyRecyclerView);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this.getContext());
        storyRecyclerView.setLayoutManager(layoutManager);

        storyRecyclerViewAdapter = new StoryRecyclerViewAdapter();
        storyRecyclerView.setAdapter(storyRecyclerViewAdapter);

        storyRecyclerView.addOnScrollListener(new StoryRecyclerViewPaginationScrollListener(layoutManager));

        return view;
    }

    private class StoryHolder extends RecyclerView.ViewHolder {
        private final ImageView userImage;
        private final TextView userName;
        private final TextView userAlias;
        private final TextView statusTimestamp;
        private final TextView statusMessage;

        StoryHolder(@NonNull View itemView) {
            super(itemView);

            userImage = itemView.findViewById(R.id.statusUserImage);
            userName = itemView.findViewById(R.id.statusUserName);
            userAlias = itemView.findViewById(R.id.statusUserAlias);
            statusTimestamp = itemView.findViewById(R.id.statusTimeStamp);
            statusMessage = itemView.findViewById(R.id.statusStatusText);

            //TODO: Make on click listeners for message dealios??
        }

        void bindStatus(Status status) {
            userImage.setImageDrawable(ImageUtils.drawableFromByteArray(status.getUser().getImageBytes()));
            userName.setText(status.getUser().getName());
            userAlias.setText(status.getUser().getAlias());
            statusTimestamp.setText(status.getTimestamp().format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)));

            String statusMessageText = status.getMessage();
            SpannableString spannableString = new SpannableString(status.getMessage());

            int startPos = -1;
            int endPos;
            int lastStartPos = -2;
            for (int i = 0; i < statusMessageText.length(); i++) {
                if (statusMessageText.charAt(i) == '@') {
                    startPos = i;
                }
                if (statusMessageText.charAt(i) == ' ' && startPos != -1 && startPos > lastStartPos || i == statusMessageText.length() - 1 && startPos < lastStartPos) {
                    endPos = i;
                    if (i == statusMessageText.length() - 1) endPos++;
                    lastStartPos = startPos;
                    if (startPos != -1) {
                        String userAlias = statusMessageText.substring(startPos, endPos);
                        spannableString.setSpan(new myClickableSpan(1, userAlias), startPos, endPos, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                    }
                }
            }

            statusMessage.setText(spannableString);
            statusMessage.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    /**
     * Proprietary ClickableSpan class to support multiple userAliases, and to pass in the userAlias
     */
    //TODO: Pull this out into it's own class so both Feed and Story can use??
    public class myClickableSpan extends ClickableSpan implements GetUserByAliasTask.Observer {

        int position;
        String userAlias;
        public myClickableSpan(int position, String userAlias) {
            this.position = position;
            this.userAlias = userAlias;
        }

        @Override
        public void onClick(@NonNull View widget) {
            //TODO: Start user Activity, put in the userAlias
            Toast.makeText(getContext(), userAlias, Toast.LENGTH_SHORT).show();
            UserByAliasRequest userByAliasRequest = new UserByAliasRequest(userAlias);
            GetUserByAliasTask userByAliasTask = new GetUserByAliasTask(presenter, this);
            userByAliasTask.execute(userByAliasRequest);
        }

        @Override
        public void updateDrawState(TextPaint textPaint) {
            super.updateDrawState(textPaint);
            textPaint.setUnderlineText(false);
        }

        @Override
        public void getUserByAliasSuccessful(UserByAliasResponse userByAliasResponse) {
            Intent intent = new Intent(getActivity(), UserActivity.class);

            intent.putExtra(UserActivity.CURRENT_USER_KEY, user);
            intent.putExtra(UserActivity.AUTH_TOKEN_KEY, authToken);
            intent.putExtra(UserActivity.USER_GIVEN_KEY, userByAliasResponse.getUser());

            startActivity(intent);
        }

        @Override
        public void getUserByAliasUnsuccessful(UserByAliasResponse userByAliasResponse) {
            Toast.makeText(getActivity(), "Couldn't find user. " + userByAliasResponse.getMessage(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage(), exception);
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private class StoryRecyclerViewAdapter extends RecyclerView.Adapter<StoryHolder> implements GetStoryTask.Observer {

        private final Story story = new Story();

        private Status lastStatus;

        private boolean hasMorePages;
        private boolean isLoading = false;

        StoryRecyclerViewAdapter() { loadMoreItems(); }

        void addItems(Story newStory) {
            int startInsertPosition = story.getSize();
            story.getStatusList().addAll(newStory.getStatusList());
            this.notifyItemRangeInserted(startInsertPosition, newStory.getSize());
        }

        void addItem(Status status) {
            story.getStatusList().add(status);
            this.notifyItemInserted(story.getSize() - 1);
        }

        void removeItem(Status status) {
            int position = story.getStatusList().indexOf(status);
            story.getStatusList().remove(position);
            this.notifyItemRemoved(position);
        }

        @NonNull
        @Override
        public StoryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(StoryFragment.this.getContext());
            View view;

            if (viewType == LOADING_DATA_VIEW) {
                view = layoutInflater.inflate(R.layout.loading_row, parent, false);
            } else {
                view = layoutInflater.inflate(R.layout.status_row, parent, false);
            }

            return new StoryHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull StoryHolder holder, int position) {
            if (!isLoading) {
                holder.bindStatus(story.getStatusAt(position));
            }
        }

        @Override
        public int getItemCount() {
            return story.getSize();
        }

        @Override
        public int getItemViewType(int position) {
            return (position == story.getSize() - 1 && isLoading) ? LOADING_DATA_VIEW : ITEM_VIEW;
        }

        void loadMoreItems() {
            isLoading = true;
            addLoadingFooter();

            GetStoryTask getStoryTask = new GetStoryTask(presenter, this);
            StoryRequest request = new StoryRequest(user, PAGE_SIZE, lastStatus);
            getStoryTask.execute(request);
        }

        @Override
        public void storyRetrieved(StoryResponse storyResponse) {
            Story story = storyResponse.getStory();

            lastStatus = (story.getSize() > 0) ? story.getStatusAt(story.getSize() - 1) : null;
            hasMorePages = storyResponse.getHasMorePages();

            isLoading = false;
            removeLoadingFooter();
            storyRecyclerViewAdapter.addItems(story);
        }

        @Override
        public void handleException(Exception exception) {
            Log.e(LOG_TAG, exception.getMessage(), exception);
            removeLoadingFooter();
            Toast.makeText(getContext(), exception.getMessage(), Toast.LENGTH_LONG).show();
        }

        private void addLoadingFooter() { addItem(new Status("Dummy", LocalDateTime.now(), null, null, user)); }

        private void removeLoadingFooter() { removeItem(story.getStatusAt(story.getSize() - 1)); }
    }

    /**
     * A scroll listener that detects when the user has scrolled to the bottom of the currently
     * available data.
     */
    private class StoryRecyclerViewPaginationScrollListener extends RecyclerView.OnScrollListener {

        private final LinearLayoutManager layoutManager;

        /**
         * Creates a new instance.
         *
         * @param layoutManager the layout manager being used by the RecyclerView.
         */
        StoryRecyclerViewPaginationScrollListener(LinearLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
        }

        /**
         * Determines whether the user has scrolled to the bottom of the currently available data
         * in the RecyclerView and asks the adapter to load more data if the last load request
         * indicated that there was more data to load.
         *
         * @param recyclerView the RecyclerView.
         * @param dx the amount of horizontal scroll.
         * @param dy the amount of vertical scroll.
         */
        @Override
        public void onScrolled(@NotNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount = layoutManager.getChildCount();
            int totalItemCount = layoutManager.getItemCount();
            int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

            if (!storyRecyclerViewAdapter.isLoading && storyRecyclerViewAdapter.hasMorePages) {
                if ((visibleItemCount + firstVisibleItemPosition) >=
                        totalItemCount && firstVisibleItemPosition >= 0) {
                    storyRecyclerViewAdapter.loadMoreItems();
                }
            }
        }
    }
}
