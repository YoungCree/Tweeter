# Tweeter
An android app clone of Twitter with an AWS backend

Technologies used: Android Studio, Java, AWS: lambda, S3, DynamoDB, API Gateway, SQS
 - API Gateway interacted with lambda's to run backend tasks (following users, fetching followers, etc...)
 - DynamoDB had the following tables: users, feed, story, authToken, follows
 - S3 was used to store user images
 - SQS was used in tambdem with lambdas to post a tweet and update the feed of 10,000+ users within 2 minutes
 
### Screenshots

The Login Screen

<img src="https://github.com/YoungCree/Tweeter/blob/master/TweeterLogin.jpg" width="300">

Feed Page

<img src="https://github.com/YoungCree/Tweeter/blob/master/FeedPage.jpg" width="300">

User Page

<img src="https://github.com/YoungCree/Tweeter/blob/master/UserPage.jpg" width="300">

Following Page

<img src="https://github.com/YoungCree/Tweeter/blob/master/FollowingPage.jpg" width="300">

Post a Tweet Page

<img src="https://github.com/YoungCree/Tweeter/blob/master/TweetPage.jpg" width="300">
