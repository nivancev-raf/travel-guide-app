<template>
    <div class="article-details">
        <h1>{{ article.title }}</h1>
        <p>By {{ authorName }} on {{ new Date(article.creationTime).toLocaleDateString() }}</p>
        <p>{{ article.text }}</p>
        <p>Activities:</p>
        <ul>
        <li v-for="activity in activities" :key="activity.id">
            <router-link :to="{ name: 'ArticlesByActivity', params: { activityId: activity.id } }">
            {{ activity.name }}
            </router-link>
        </li>
        </ul>
        <h3>Comments:</h3>
        <ul class="comments-list">
            <li v-for="comment in comments" :key="comment.id">
                <div class="comment-author">
                    <strong>{{ comment.author }}</strong> on {{ new Date(comment.creationTime).toLocaleDateString() }}:
                </div>
                <div class="comment-text">
                    {{ comment.text }}
                </div>
            </li>
        </ul>
        <h3>Add a Comment</h3>
        <form @submit.prevent="addComment">
            <div class="form-group">
                <label for="commentAuthor">Name</label>
                <input type="text" v-model="newComment.authorName" class="form-control" id="commentAuthor" required>
            </div>
            <div class="form-group">
                <label for="commentText">Comment</label>
                <textarea v-model="newComment.text" class="form-control" id="commentText" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Add Comment</button>
        </form>
    </div>
</template>
  
  <script>
  export default {
    data() {
      return {
        article: {},
        authorName: '',
        activities: [],
        comments: [],
        newComment: {
          authorName: '',
          text: ''
        }
      };
    },
    methods: {
      fetchArticleDetails() {
        const articleId = this.$route.params.id;
        this.$axios.get(`/api/articles/${articleId}`).then((response) => {
          this.article = response.data;
          this.incrementVisitCount();
          this.fetchAuthorName(this.article.authorId);
          this.fetchActivities(articleId);
          this.fetchComments(articleId);
        }).catch(error => {
          console.error("There was an error fetching the article details:", error);
        });
      },
      fetchAuthorName(authorId) {
        this.$axios.get(`/api/users/${authorId}`).then((response) => {
          this.authorName = response.data.firstName;
        }).catch(error => {
          console.error("There was an error fetching the author name:", error);
        });
      },
      fetchActivities(articleId) {
        this.$axios.get(`/api/activities/article/${articleId}`).then((response) => {
          this.activities = response.data;
        }).catch(error => {
          console.error("There was an error fetching the activities:", error);
        });
      },
      fetchComments(articleId) {
        this.$axios.get(`/api/comments/article/${articleId}`).then((response) => {
          this.comments = response.data;
        }).catch(error => {
          console.error("There was an error fetching the comments:", error);
        });
      },
      addComment() {
        const articleId = this.article.id;
        const commentData = {
          articleId: articleId,
          author: this.newComment.authorName,
          text: this.newComment.text,
          creationTime: new Date().toISOString()
        };
        console.log("Adding comment:", commentData);
        this.$axios.post('/api/comments', commentData).then(() => {
          this.newComment.authorName = '';
          this.newComment.text = '';
          this.fetchComments(articleId);
        }).catch(error => {
          console.error("There was an error adding the comment:", error);
        });
      },
    incrementVisitCount() {
      this.article.visitCount++;
      this.$axios.put(`/api/articles/${this.article.id}`, this.article).then(() => {
      }).catch(error => {
        console.error("There was an error incrementing the visit count:", error);
      });
    },
    },
    created() {
    console.log("ArticleDetails created");
      this.fetchArticleDetails();
    }
  };
  </script>
  
  <style scoped>
  .article-details {
    max-width: 800px;
    margin: 0 auto;
  }
  
  .comments-list {
    list-style-type: none;
    padding: 0;
  }
  
  .comments-list li {
    border-bottom: 1px solid #ddd;
    padding: 10px 0;
  }
  
  .comment-author {
    font-weight: bold;
  }
  
  .comment-text {
    margin-top: 5px;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  .form-group label {
    display: block;
    font-weight: bold;
    margin-bottom: 5px;
  }
  
  .form-group input,
  .form-group textarea {
    width: 100%;
    padding: 8px;
    box-sizing: border-box;
  }
  
  .btn-primary {
    background-color: #007bff;
    border-color: #007bff;
    color: #fff;
  }
  
  .btn-primary:hover {
    background-color: #0056b3;
    border-color: #004085;
  }
  
  .btn-primary:focus {
    box-shadow: 0 0 0 0.2rem rgba(0, 123, 255, 0.5);
  }
  </style>
  